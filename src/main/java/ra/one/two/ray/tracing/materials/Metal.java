package ra.one.two.ray.tracing.materials;


import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.ScatterResult;

public class Metal implements Material {

    private final Vec3 albedo;
    private final double fuzz;

    /**
     * This is the metal material which can be attached to any object.
     *
     * @param albedo the basic (diffuse?) color of the material.
     * @param fuzz   factor by which we may want to randomize the reflected scattered ray.
     */
    public Metal(final Vec3 albedo, final double fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    @Override
    public ScatterResult scatter(final Ray rayIn, final HitRecord record) {
        // Get a unit vector in the direction of reflection
        final Vec3 reflected = Vec3.reflect(Vec3.unitVector(rayIn.getDirection()), record.getNormal());
        // Get the reflected ray with direction slightly randomized based upon the fuzz factor
        final Ray scattered = new Ray(record.getRayHitLocationOnHittableObject(), reflected.add(Vec3.randomInUnitSphere().scaleUp(fuzz)), rayIn.getRayFireTime());
        return new ScatterResult(albedo, Vec3.dot(scattered.getDirection(), record.getNormal()) > 0 ? scattered : null);
    }
}
