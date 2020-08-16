package ra.one.two.ray.tracing.textures;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;

@AllArgsConstructor
public class SolidColorTexture implements Texture {

    private final Vec3 color;

    @Override
    public Vec3 value(TextureCoordinates textureCoordinates, Vec3 point) {
        return color;
    }
}
