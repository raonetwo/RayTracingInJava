package ra.one.two.ray.tracing.scene;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.HittableList;
import ra.one.two.ray.tracing.rayhit.ScatterResult;

/**
 * This class is responsible for calculating the color of a given pixel
 */
@AllArgsConstructor
public class PixelRenderer implements Runnable {
    private final Camera camera;
    private final HittableList world;
    private final int depth;
    private final int pixelRowIndex;
    private final int pixelColumnIndex;
    private final int samplesPerPixel;
    private final Vec3[][] renderedImage;
    private final Vec3 backgroundColor;

    /**
     * This function is responsible for calculating color for a given ray which was fired earlier for a given pixel in viewport
     *
     * @param ray             input ray we want to find the color for
     * @param backgroundColor default backGroundColor to use for the image render
     * @param world           list of hittable objects that our ray might interact with
     * @param depth           number of interactions with the world after which we stop the calculations
     * @return a vector with color representation for that ray after depth number of iterations.
     */
    public Vec3 rayColor(final Ray ray, final Vec3 backgroundColor, final HittableList world, final int depth) {

        // Render
        // Steps involved in rendering are
        // (1) get the ray from the eye to the pixel,
        // (2) determine which objects the ray intersects, and
        // (3) compute a color for that intersection point.

        if (depth <= 0) {
            return new Vec3();
        }

        // Check if our input ray hits something in the world
        final HitRecord hitRecord = new HitRecord();
        final boolean hit = world.hit(ray, 0.001, Double.POSITIVE_INFINITY, hitRecord);
        // If we find that it did indeed hit some objects in the world
        if (hit) {
            // scatter the ray from the objects it hit based on their material types.
            final ScatterResult scatterResult = hitRecord.getMaterialOfObjectHit().scatter(ray, hitRecord);
            final Vec3 emitted = hitRecord.getMaterialOfObjectHit().emitted(hitRecord.getTextureCoordinatesAtHitLocation(), hitRecord.getRayHitLocationOnHittableObject());
            // If the ray gets scattered and absorbed.
            if (scatterResult.getScatteredRay() != null) {
                // Calculate the color of the pixel by the color retrieved from original ray scattering
                // and attenuate (Hadamard product) the color with the color values we retrieve from interactions of scattered ray with the world
                return Vec3.componentWiseMultiply(scatterResult.getColorFromObjectHit(), rayColor(scatterResult.getScatteredRay(), backgroundColor, world, depth - 1)).add(emitted);
            }
            return emitted;
        } else {
            return backgroundColor;
        }
    }

    @Override
    public void run() {
        final Vec3 pixelColor = new Vec3();
        // For the number of rays that we want to sample,
        // we will get rays from the camera that fire at viewport and trace those rays for intersection with objects
        // and calculate the color for each pixel for that ray and add them all together.
        for (int s = 0; s < samplesPerPixel; ++s) {
            // offset for the pixel to right direction along the surface of viewport from the lower left corner.
            // we added a random number for using with multiple samples
            final double u = (pixelColumnIndex + Math.random()) / (renderedImage[0].length - 1);
            // offset to get the pixel to top direction along the surface of viewport from the lower left corner.
            final double v = (pixelRowIndex + Math.random()) / (renderedImage.length - 1);
            // Get a ray in the direction of pixel
            final Ray ray = camera.getRay(u, v);
            // Calculate color at a given pixel
            pixelColor.add(this.rayColor(ray, backgroundColor, world, depth));
        }
        // Get average pixel color and gamma correct by 2 i.e. raise to power 1/2
        pixelColor.scaleDown(samplesPerPixel).pow(0.5).clamp(0, 0.999).scaleUp(255.999);
        renderedImage[pixelRowIndex][pixelColumnIndex] = pixelColor;
    }

}