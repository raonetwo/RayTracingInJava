package ra.one.two.ray.tracing.scene;

import ra.one.two.ray.tracing.materials.Dielectric;
import ra.one.two.ray.tracing.materials.DiffuseLight;
import ra.one.two.ray.tracing.materials.Lambertian;
import ra.one.two.ray.tracing.materials.Metal;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.objects.Box;
import ra.one.two.ray.tracing.primitives.objects.MovingSphere;
import ra.one.two.ray.tracing.primitives.objects.Sphere;
import ra.one.two.ray.tracing.primitives.objects.XZPlaneRectangle;
import ra.one.two.ray.tracing.rayhit.BoundingVolumeHeirarchyNode;
import ra.one.two.ray.tracing.rayhit.ConstantMedium;
import ra.one.two.ray.tracing.rayhit.HittableList;
import ra.one.two.ray.tracing.rayhit.RotateY;
import ra.one.two.ray.tracing.rayhit.Translate;
import ra.one.two.ray.tracing.textures.ImageTexture;
import ra.one.two.ray.tracing.textures.NoiseTexture;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RenderDriver {

    // Image
    // We take multiple samples for each pixel in an image and average out the mean color.
    // This is useful to remove jaggedness of pixels as the pixels for the object rim
    // take color that is  blend of background and foreground(object) colors and we get a smooth transition
    // This is called antialiasing.
    private static final int SAMPLES_PER_PIXEL = 10000;
    // Aspect ratio of the image.
    private static final double ASPECT_RATIO = 1;
    // Image width in pixels
    private static final int IMAGE_WIDTH = 800;
    // Image height in pixels
    private static final int IMAGE_HEIGHT = (int) (IMAGE_WIDTH / ASPECT_RATIO);
    // Each ray on interacting with a hittable object depending on the material attached to the object
    // will scatter/reflect/refract/diffract the ray, max depth is used to determine depth (iterations)
    // till which we want to trace the rays bef200ore stopping.
    private static final int MAX_DEPTH = 50;
    // background color of the render
    private static final Vec3 BACKGROUND_COLOR = new Vec3();

    // Camera
    // Camera position
    private static final Vec3 LOOK_FROM = new Vec3(478, 278, -600);
    // Direction camera is pointing at
    private static final Vec3 LOOK_AT = new Vec3(278, 278, 0);
    // Up direction for the camera
    private static final Vec3 UP_VECTOR = new Vec3(0, 1, 0);
    // We will specify the distance we want to focus camera at
    private static final double DISTANCE_TO_FOCUS = 10.0;
    // Aperture size of the camera, cameras have lenses and different aperture sizes.
    private static final double CAMERA_APERTURE = 0.0;
    public static final int VERTICAL_FIELD_OF_VIEW_IN_DEGREES = 40;

    public static void main(String[] args) throws InterruptedException, IOException {

        final long start = System.currentTimeMillis();
        //Camera
        final Camera camera = new Camera(LOOK_FROM, LOOK_AT, UP_VECTOR, VERTICAL_FIELD_OF_VIEW_IN_DEGREES, ASPECT_RATIO, CAMERA_APERTURE, DISTANCE_TO_FOCUS, 0, 1.0);

        // World
        final HittableList world = finalScene();

        System.out.println("P3\n" + IMAGE_WIDTH + ' ' + IMAGE_HEIGHT + "\n255");

        //creating a pool of 6 threads
        final ExecutorService executor = Executors.newFixedThreadPool(6);
        final Vec3[][] renderedImage = new Vec3[IMAGE_HEIGHT][IMAGE_WIDTH];

        // For each pixel in image calculate its color
        for (int pixelRowIndex = IMAGE_HEIGHT - 1; pixelRowIndex >= 0; --pixelRowIndex) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < IMAGE_WIDTH; ++pixelColumnIndex) {
                executor.execute(new PixelRenderer(camera, world, MAX_DEPTH, pixelRowIndex, pixelColumnIndex, SAMPLES_PER_PIXEL, renderedImage, BACKGROUND_COLOR));
            }
        }

        // Wait for all tasks to finish
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Finally we will write the color after dividing by numberOfSamples to get the average color.
        for (int pixelRowIndex = IMAGE_HEIGHT - 1; pixelRowIndex >= 0; --pixelRowIndex) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < IMAGE_WIDTH; ++pixelColumnIndex) {
                System.out.println(renderedImage[pixelRowIndex][pixelColumnIndex]);
            }
        }

        final long timeToRender = (System.currentTimeMillis() - start) / 1000;
        System.out.println(timeToRender);
        System.out.println(IMAGE_HEIGHT * IMAGE_WIDTH / timeToRender);
    }

    private static HittableList finalScene() throws IOException {
        final HittableList boxes = new HittableList();
        var ground = new Lambertian(new Vec3(0.48, 0.83, 0.53));
        final int boxesPerSide = 20;
        for (int i = 0; i < boxesPerSide; i++) {
            for (int j = 0; j < boxesPerSide; j++) {
                var w = 100.0;
                var x0 = -1000.0 + i*w;
                var z0 = -1000.0 + j*w;
                var y0 = 0.0;
                var x1 = x0 + w;
                var y1 = Math.random()*100 + 1;
                var z1 = z0 + w;

                boxes.getHittableList().add(new Box(new Vec3(x0,y0,z0), new Vec3(x1,y1,z1), ground));
            }
        }

        HittableList objects = new HittableList();

        objects.getHittableList().add(new BoundingVolumeHeirarchyNode(boxes, 0, 1));

        DiffuseLight light = new DiffuseLight(new Vec3(7, 7, 7));
        objects.getHittableList().add(new XZPlaneRectangle(123, 147,423,  412, 554, light));

        var center1 = new Vec3(400, 400, 200);
        var center2 = new Vec3(30,0,0).add(center1);
        var movingSphereMaterial = new Lambertian(new Vec3(0.7, 0.3, 0.1));
        objects.getHittableList().add(new MovingSphere(center1, center2, 0, 1, 50, movingSphereMaterial));

        objects.getHittableList().add(new Sphere(new Vec3(260, 150, 45), 50, new Dielectric(1.5)));
        objects.getHittableList().add(new Sphere(new Vec3(0, 150, 145), 50, new Metal(new Vec3(0.8, 0.8, 0.9), 10.0)));

        var boundary = new Sphere(new Vec3(360,150,145), 70, new Dielectric(1.5));
        objects.getHittableList().add(boundary);
        objects.getHittableList().add(new ConstantMedium(boundary, 0.2, new Vec3(0.2, 0.4, 0.9)));
        boundary = new Sphere(new Vec3(0, 0, 0), 5000, new Dielectric(1.5));
        objects.getHittableList().add(new ConstantMedium(boundary, .0001, new Vec3(1,1,1)));

        var emat = new Lambertian(new ImageTexture(RenderDriver.class.getResourceAsStream("/earthmap.jpg")));
        objects.getHittableList().add(new Sphere(new Vec3(400,200,400), 100, emat));
        var pertext = new NoiseTexture(0.1);
        objects.getHittableList().add(new Sphere(new Vec3(220,280,300), 80, new Lambertian(pertext)));

        HittableList boxes2 = new HittableList();
        var white = new Lambertian(new Vec3(.73, .73, .73));
        int ns = 1000;
        for (int j = 0; j < ns; j++) {
            boxes2.getHittableList().add(new Sphere(Vec3.random(0,165), 10, white));
        }

        objects.getHittableList().add(new Translate(
                new Vec3(-100,270,395),
                new RotateY(15, new BoundingVolumeHeirarchyNode(boxes2, 0.0, 1.0))
        ));

        return objects;
    }

}
