package ra.one.two.ray.tracing.materials;


import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.ScatterResult;
import ra.one.two.ray.tracing.textures.SolidColorTexture;
import ra.one.two.ray.tracing.textures.Texture;

public class Lambertian implements Material {

    private final Texture albedo;

    /**
     * Constructor for basic lambertian material
     *
     * @param albedo basic color of the material
     */
    public Lambertian(final Vec3 albedo) {
        this(new SolidColorTexture(albedo));
    }

    public Lambertian(final Texture texture) {
        this.albedo = texture;
    }

    @Override
    public ScatterResult scatter(final Ray rayIn, final HitRecord record) {
        // Excerpt from the original book
        // "For Lambertian It can either scatter always and attenuate by its reflectance R, or it can scatter with no attenuation but absorb the fraction 1−R
        // Note we could just as well only scatter with some probability p and have attenuation be albedo/p."
        // Note: Something seem off about it as we are not actually attenuating color or absorbing the rays, need to check with the author
        final Vec3 scatterDirection = Vec3.add(record.getNormal(), Vec3.randomUnitVector());
        return new ScatterResult(albedo.value(record.getTextureCoordinatesAtHitLocation(), record.getRayHitLocationOnHittableObject()),
                new Ray(record.getRayHitLocationOnHittableObject(), scatterDirection, rayIn.getRayFireTime()));
    }
}
