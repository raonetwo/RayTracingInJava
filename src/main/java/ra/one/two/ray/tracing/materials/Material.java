package ra.one.two.ray.tracing.materials;

import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.ScatterResult;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

// Different types of material interact with input ray differently and scatter it differently
public interface Material {
    /**
     * This method scatters the input ray based on the material properties
     *
     * @param rayIn  input ray
     * @param record record where the ray hit an object
     * @return scattered ray with attenuation (color)
     */
    ScatterResult scatter(final Ray rayIn, final HitRecord record);

    default Vec3 emitted(final TextureCoordinates textureCoordinates, final Vec3 point) {
        return new Vec3(0);
    }
}
