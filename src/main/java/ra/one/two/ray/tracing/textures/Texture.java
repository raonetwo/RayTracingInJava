package ra.one.two.ray.tracing.textures;

import ra.one.two.ray.tracing.primitives.math.Vec3;

public interface Texture {
    Vec3 value(final TextureCoordinates textureCoordinates, final Vec3 point);
}
