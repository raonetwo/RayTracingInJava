package ra.one.two.ray.tracing.materials;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.ScatterResult;
import ra.one.two.ray.tracing.textures.SolidColorTexture;
import ra.one.two.ray.tracing.textures.Texture;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

@AllArgsConstructor
public class DiffuseLight implements Material {

    private final Texture emitTexture;

    public DiffuseLight(final Vec3 color) {
        this.emitTexture = new SolidColorTexture(color);
    }

    @Override
    public Vec3 emitted(final TextureCoordinates textureCoordinates, final Vec3 point) {
        return emitTexture.value(textureCoordinates, point);
    }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        return new ScatterResult(null, null);
    }
}
