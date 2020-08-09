package ra.one.two.ray.tracing.scene;

import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.primitives.ray.ScatterResult;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.HittableList;

/**
 * This class is responsible for calculating the color of a given pixel
 */
public class PixelRenderer implements Runnable {
    private final Camera camera;
    private final HittableList world;
    private final int depth;
    private final int pixelRowIndex;
    private final int pixelColumnIndex;
    private final int samplesPerPixel;
    private final Vec3[][] renderedImage;

    /**
     * Constructor for PixelRenderer
     * @param camera Camera through which we want to render pixel
     * @param world world for which we want to render
     * @param depth number of scatters till which we want to trace the rays
     * @param renderedImage output Vec3 matrix parameter to save the pixel to its row and column in the rendered image
     * @param pixelRowIndex row index in the image of the pixel that we want to render
     * @param pixelColumnIndex column index in the image of the pixel that we want to render
     * @param samplesPerPixel number of samples of incoming rays that we want coming from camera to average over
     */
    public PixelRenderer(final Camera camera, final HittableList world, final int depth, final Vec3[][] renderedImage,
                         final int pixelRowIndex, final int pixelColumnIndex, final int samplesPerPixel) {
        this.camera = camera;
        this.world = world;
        this.depth = depth;
        this.renderedImage = renderedImage;
        this.pixelRowIndex = pixelRowIndex;
        this.pixelColumnIndex = pixelColumnIndex;
        this.samplesPerPixel = samplesPerPixel;
    }

    /**
     * This function is responsible for calculating color for a given ray which was fired earlier for a given pixel in viewport
     *
     * @param ray   input ray we want to find the color for
     * @param world list of hittable objects that our ray might interact with
     * @param depth number of interactions with the world after which we stop the calculations
     * @return a vector with color representation for that ray after depth number of iterations.
     */
    public Vec3 rayColor(final Ray ray, final HittableList world, final int depth) {

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
            // If the ray gets scattered and absorbed.
            if (scatterResult.getScatteredRay() != null) {
                // Calculate the color of the pixel by the color retrieved from original ray scattering
                // and attenuate (Hadamard product) the color with the color values we retrieve from interactions of scattered ray with the world
                return Vec3.componentWiseMultiply(scatterResult.getColorFromObjectHit(), rayColor(scatterResult.getScatteredRay(), world, depth - 1));
            }
            return new Vec3();
        }
        // If our ray does not hit anything in the world we want a generic gradient of color blue that blends to white at the bottom. This serves as faux sky for our render.
        // get a unit vector in the direction of the ray
        final Vec3 unitDirection = Vec3.unitVector(ray.getDirection());
        // get the y coord for the unit vector and offset it by 1 since viewport max coordinate can be -1 and we want color to be positive
        // and finally scale by 0.5 since max value can be 1 and we know color value lies b/w 0 and 1
        final double t = 0.5 * (unitDirection.getYComponent() + 1.0);
        // mix white with blue at bottom y is small hence t is small
        // standard linear interpolation or lerp
        // blendedValue=(1−t)⋅startValue+t⋅endValue
        return new Vec3((1.0 - 0.5 * t), (1.0 - 0.3 * t), 1.0);
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
            // off to get the pixel to top direction along the surface of viewport from the lower left corner.
            final double v = (pixelRowIndex + Math.random()) / (renderedImage.length-1);
            // Get a ray in the direction of pixel
            final Ray ray = camera.getRay(u, v);
            // Calculate color at a given pixel
            pixelColor.add(this.rayColor(ray, world, depth));
        }
        // Get average pixel color and gamma correct by 2 i.e. raise to power 1/2
        pixelColor.scaleDown(samplesPerPixel).pow(0.5).clamp(0, 0.999).scaleUp(255.999);
        renderedImage[pixelRowIndex][pixelColumnIndex] = pixelColor;
    }

}