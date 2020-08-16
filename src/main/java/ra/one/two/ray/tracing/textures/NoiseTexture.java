package ra.one.two.ray.tracing.textures;

import lombok.RequiredArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.noise.Perlin;

@RequiredArgsConstructor
public class NoiseTexture implements Texture {
    private final Perlin perlinNoise = new Perlin();
    private final double scale;

    @Override
    public Vec3 value(TextureCoordinates textureCoordinates, Vec3 point) {
        return new Vec3(1).scaleUp(0.5 * (1 + Math.sin(scale * point.getZComponent() + 10 * perlinNoise.turbulence(point, 7))));
//        return new Vec3(1,1,1).scaleUp(perlinNoise.noise(point));
    }
}
