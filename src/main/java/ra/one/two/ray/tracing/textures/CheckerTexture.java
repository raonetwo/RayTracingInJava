package ra.one.two.ray.tracing.textures;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;

@AllArgsConstructor
public class CheckerTexture implements Texture {
    private final Texture even;
    private final Texture odd;

    public CheckerTexture(final Vec3 even, final Vec3 odd) {
        this(new SolidColorTexture(even), new SolidColorTexture(odd));
    }

    @Override
    public Vec3 value(TextureCoordinates textureCoordinates, Vec3 point) {
        final double sines = Math.sin(10 * point.getXComponent()) * Math.sin(10 * point.getYComponent()) * Math.sin(10 * point.getZComponent());
        if (sines < 0)
            return odd.value(textureCoordinates, point);
        else
            return even.value(textureCoordinates, point);
    }
}
