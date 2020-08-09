package ra.one.two.ray.tracing.scene;

import ra.one.two.ray.tracing.rayhit.HittableList;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.rayhit.Sphere;
import ra.one.two.ray.tracing.materials.Dielectric;
import ra.one.two.ray.tracing.materials.Lambertian;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.materials.Metal;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RenderDriver {

    // Image
    // We take multiple samples for each pixel in an image and average out the mean color.
    // This is useful to remove jaggedness of pixels as the pixels for the object rim
    // take color that is  blend of background and foreground(object) colors and we get a smooth transition
    // This is called antialiasing.
    private static final int SAMPLES_PER_PIXEL = 100;
    // Aspect ratio of the image.
    private static final double ASPECT_RATIO = 16.0 / 9.0;
    // Image width in pixels
    private static final int IMAGE_WIDTH = 384;
    // Image height in pixels
    private static final int IMAGE_HEIGHT = (int)(IMAGE_WIDTH / ASPECT_RATIO);
    // Each ray on interacting with a hittable object depending on the material attached to the object
    // will scatter/reflect/refract/diffract the ray, max depth is used to determine depth (iterations)
    // till which we want to trace the rays before stopping.
    private static final int MAX_DEPTH = 50;
    
    // Camera
    // Camera position
    private static final Vec3 LOOK_FROM = new Vec3(13,2,3);
    // Direction camera is pointing at
    private static final Vec3 LOOK_AT = new Vec3(0,0,0);
    // Up direction for the camera
    private static final Vec3 UP_VECTOR = new Vec3(0,1,0);
    // We will specify the distance we want to focus camera at
    private static final double DISTANCE_TO_FOCUS = 10.0;
    // Aperture size of the camera, cameras have lenses and different aperture sizes.
    private static final double CAMERA_APERTURE = 0.1;

    public static void main(String[] args) throws InterruptedException {

        final long start = System.currentTimeMillis();
        //Camera
        final Camera camera =  new Camera(LOOK_FROM, LOOK_AT, UP_VECTOR, 20, ASPECT_RATIO, CAMERA_APERTURE, DISTANCE_TO_FOCUS);

        // World
        final HittableList world = randomScene();

        System.out.println("P3\n" + IMAGE_WIDTH + ' ' + IMAGE_HEIGHT + "\n255");

        //creating a pool of 6 threads
        final ExecutorService executor = Executors.newFixedThreadPool(6);
        final Vec3[][] renderedImage = new Vec3[IMAGE_HEIGHT][IMAGE_WIDTH];

        // For each pixel in image calculate its color
        for (int pixelRowIndex = IMAGE_HEIGHT -1; pixelRowIndex >= 0; --pixelRowIndex) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < IMAGE_WIDTH; ++pixelColumnIndex) {
                    executor.execute(new PixelRenderer(camera, world, MAX_DEPTH, renderedImage, pixelRowIndex, pixelColumnIndex, SAMPLES_PER_PIXEL));
            }
        }

        // Wait for all tasks to finish
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Finally we will write the color after dividing by numberOfSamples to get the average color.
        for (int pixelRowIndex = IMAGE_HEIGHT -1; pixelRowIndex >= 0; --pixelRowIndex) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < IMAGE_WIDTH; ++pixelColumnIndex) {
                System.out.println(renderedImage[pixelRowIndex][pixelColumnIndex]);
            }
        }

        final long timeToRender = (System.currentTimeMillis() - start) / 1000;
        System.out.println(timeToRender);
        System.out.println(IMAGE_HEIGHT * IMAGE_WIDTH /timeToRender);
    }

    private static HittableList randomScene() {
        final HittableList world = new HittableList();
        // Introducing a seed based random for consistent testing.
        final Random random = new Random(1);
        final Material groundMaterial = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        world.getHittableList().add(new Sphere(new Vec3(0,-1000,0), 1000, groundMaterial));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                final double choose_mat = random.nextDouble();
                final Vec3 center = new Vec3(a + 0.9*random.nextDouble(), 0.2, b + 0.9*random.nextDouble());

                if (Vec3.subtract(center, new Vec3(4, 0.2, 0)).length() > 0.9) {
                    if (choose_mat < 0.8) {
                        // diffuse
                        final Vec3 albedo = Vec3.componentWiseMultiply(Vec3.random(), Vec3.random());
                        world.getHittableList().add(new Sphere(center, 0.2, new Lambertian(albedo)));
                    } else if (choose_mat < 0.95) {
                        // metal
                        final Vec3 albedo = Vec3.random(0.5, 1);
                        final double fuzz = random.nextDouble() * 0.5;
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
