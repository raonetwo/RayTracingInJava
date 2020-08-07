package ra.one.two.ray.tracing.scene;

import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.HittableList;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.rayhit.Sphere;
import ra.one.two.ray.tracing.materials.Dielectric;
import ra.one.two.ray.tracing.materials.Lambertian;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.materials.Metal;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.primitives.ray.ScatterResult;

public class Main {

    public static void main(String[] args) {

        final long start = System.currentTimeMillis();

        // Image

        // We take multiple samples for each pixel in an image and average out the mean color.
        // This is useful to remove jaggedness of pixels as the pixels for the object rim
        // take color that is  blend of background and foreground(object) colors and we get a smooth transition
        // This is called antialiasing.
        final int samplesPerPixel = 100;
        // Aspect ration of the image.
        final double aspectRatio = 16.0 / 9.0;
        // Image width in pixels
        final int imageWidth = 384;
        // Image height in pixels
        final int imageHeight = (int)(imageWidth / aspectRatio);
        // Each ray on interacting with a hittable object depending on the material attached to the object
        // will scatter/reflect/refract/diffract the ray, max depth is used to determine depth (iterations)
        // till which we want to trace the rays before stopping.
        final int maxDepth = 50;


        // World

        final HittableList world = randomScene();


        // Camera

        // Camera position
        final Vec3 lookfrom = new Vec3(13,2,3);
        // Direction camera is pointing at
        final Vec3 lookAt = new Vec3(0,0,0);
        // Up direction for the camera
        final Vec3 upVector = new Vec3(0,1,0);
        // distanceToFocus = Vec3.subtract(lookfrom,lookAt).length();
        // We will specify the distance we want to focus camera at
        final double distanceToFocus = 10.0;
        // Aperture size of the camera, cameras have lenses and different aperture sizes.
        final double aperture = 0.1;

        final Camera camera =  new Camera(lookfrom, lookAt, upVector, 20, aspectRatio, aperture, distanceToFocus);

        // Render
        // Steps involved in rendering are
        // (1) calculate the ray from the eye to the pixel,
        // (2) determine which objects the ray intersects, and
        // (3) compute a color for that intersection point.

        System.out.println("P3\n" + imageWidth + ' ' + imageHeight + "\n255");

        // For each pixel calculate pixel color
        for (int pixelRowIndex = imageHeight-1; pixelRowIndex >= 0; --pixelRowIndex) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < imageWidth; ++pixelColumnIndex) {
                final Vec3 pixelColor = new Vec3(0, 0, 0);
                // For the number of rays that we want to sample,
                // we will get rays from the camera that fire at viewport and trace those rays for intersection with objects
                // and calculate the color for each pixel for that ray and add them all together.
                for (int s = 0; s < samplesPerPixel; ++s) {
                    // offset for the pixel to right direction along the surface of viewport from the lower left corner.
                    // we added a random number for using with multiple samples
                    final double u = (pixelColumnIndex + Math.random()) / (imageWidth - 1);
                    // off to get the pixel to top direction along the surface of viewport from the lower left corner.
                    final double v = (pixelRowIndex + Math.random()) / (imageHeight-1);
                    // Get a ray in the direction of pixel
                    final Ray r = camera.getRay(u, v);
                    // Calculate color at a given pixel
                    pixelColor.add(rayColor(r, world, maxDepth));
                }
                // Finally we will write the color after dividing by numberOfSamples to get the average color.
                Vec3.writeColor(System.out, pixelColor, samplesPerPixel);
            }
        }


        final long timeToRender = (System.currentTimeMillis() - start) / 1000;
        System.out.println(timeToRender);
        System.out.println(imageHeight*imageWidth/timeToRender);
    }

    /**
     * This function is resposible for calculating color for a given ray which was fired earlier for a given pixel in viewport
     * @param ray input ray we want to find the color for
     * @param world list of hittable objects that our ray might interact with
     * @param depth number of interactions with the world after which we stop the calculations
     * @return a vector with color representation for that ray after depth number of iterations.
     */
    public static Vec3 rayColor(final Ray ray, final HittableList world, final int depth) {
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
                return Vec3.componentWiseMultiply(scatterResult.getColorFromObjectHit(), rayColor(scatterResult.getScatteredRay(), world, depth-1));
            }
            return new Vec3();
        }
        // If our ray does not hit anything in the world we want a generic gradient of color blue that blends to white at the bottom. This serves as faux sky for our render.
        // get a unit vector in the direction of the ray
        final Vec3 unit_direction = Vec3.unitVector(ray.getDirection());
        // get the y coord for the unit vector and offset it by 1 since viewport max coordinate can be -1 and we want color to be positive
        // and finally scale by 0.5 since max value can be 1 and we know color value lies b/w 0 and 1
        final double t = 0.5*(unit_direction.getyComponent() + 1.0);
        // mix white with blue at bottom y is small hence t is small
        // standard linear interpolation or lerp
        // blendedValue=(1−t)⋅startValue+t⋅endValue
        return Vec3.add(new Vec3(1.0, 1.0, 1.0).scaleUp((1.0-t)), new Vec3(0.5, 0.7, 1.0).scaleUp(t));
    }

    public static HittableList randomScene() {
        final HittableList world = new HittableList();

        final Material ground_material = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        world.getHittableList().add(new Sphere(new Vec3(0,-1000,0), 1000, ground_material));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                final double choose_mat = Math.random();
                final Vec3 center = new Vec3(a + 0.9*Math.random(), 0.2, b + 0.9*Math.random());

                if (Vec3.subtract(center, new Vec3(4, 0.2, 0)).length() > 0.9) {
                    if (choose_mat < 0.8) {
                        // diffuse
                        final Vec3 albedo = Vec3.componentWiseMultiply(Vec3.random(), Vec3.random());
                        world.getHittableList().add(new Sphere(center, 0.2, new Lambertian(albedo)));
                    } else if (choose_mat < 0.95) {
                        // metal
                        final Vec3 albedo = Vec3.random(0.5, 1);
                        final double fuzz = Math.random() * 0.5;
                        world.getHittableList().add(new Sphere(center, 0.2, new Metal(albedo, fuzz)));
                    } else {
                        // glass
                        world.getHittableList().add(new Sphere(center, 0.2, new Dielectric(1.5)));
                    }
                }
            }
        }

        final Material material1 = new Dielectric(1.5);
        world.getHittableList().add(new Sphere(new Vec3(0, 1, 0), 1.0, material1));

        final Material material2 = new Lambertian(new Vec3(0.4, 0.2, 0.1));
        world.getHittableList().add(new Sphere(new Vec3(-4, 1, 0), 1.0, material2));

        final Material material3 = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
        world.getHittableList().add(new Sphere(new Vec3(4, 1, 0), 1.0, material3));

        return world;
    }

}
