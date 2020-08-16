package ra.one.two.ray.tracing.materials;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.ScatterResult;
import ra.one.two.ray.tracing.textures.SolidColorTexture;
import ra.one.two.ray.tracing.textures.Texture;

@AllArgsConstructor
public class Isotropic implements Material {

    private final Texture albedo;

    public Isotropic(final Vec3 color) {
        this.albedo = new SolidColorTexture(color);
    }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        final Ray scattered = new Ray(record.getRayHitLocationOnHittableObject(), Vec3.randomInUnitSphere(), rayIn.getRayFireTime());
        final Vec3 attenuation = albedo.value(record.getTextureCoordinatesAtHitLocation(), record.getRayHitLocationOnHittableObject());
        return new ScatterResult(attenuation, scattered);
    }
}
