package ra.one.two.ray.tracing.primitives.noise;

import ra.one.two.ray.tracing.primitives.math.Vec3;

import java.util.Arrays;
import java.util.Collections;

public class Perlin {
    private static final int POINT_COUNT = 256;
    private final double randomDouble[] = new double[POINT_COUNT];
    private final int permX[] = new int[POINT_COUNT];
    private final int permY[] = new int[POINT_COUNT];
    private final int permZ[] = new int[POINT_COUNT];
    private final Vec3[] randomVector = new Vec3[POINT_COUNT];

    public Perlin() {
        for (int i = 0; i < POINT_COUNT; i++) {
            randomDouble[i] = Math.random();
            permX[i] = i;
            permY[i] = i;
            permZ[i] = i;
            randomVector[i] = Vec3.random(-1, 1);
        }
        Collections.shuffle(Arrays.asList(permX));
        Collections.shuffle(Arrays.asList(permY));
        Collections.shuffle(Arrays.asList(permZ));
    }

    public double noise(final Vec3 point) {
        double u = point.getXComponent() - Math.floor(point.getXComponent());
        double v = point.getYComponent() - Math.floor(point.getYComponent());
        double w = point.getZComponent() - Math.floor(point.getZComponent());

        double uu = u * u * (3 - 2 * u);
        double vv = v * v * (3 - 2 * v);
        double ww = w * w * (3 - 2 * w);

        int i = (int) Math.floor(point.getXComponent());
        int j = (int) Math.floor(point.getYComponent());
        int k = (int) Math.floor(point.getZComponent());

        double accum = 0.0;
        // Perform trilinear interpolation
        for (int di = 0; di < 2; di++) {
            for (int dj = 0; dj < 2; dj++) {
                for (int dk = 0; dk < 2; dk++) {
                    accum += (di * uu + (1 - di) * (1 - uu)) * (dj * vv + (1 - dj) * (1 - vv)) * (dk * ww + (1 - dk) * (1 - ww))
                            * Vec3.dot(randomVector[permX[(i + di) & 255] ^ permY[(j + dj) & 255] ^ permZ[(k + dk) & 255]], new Vec3(u - di, v - dj, w - dk));
                }
            }
        }

        return accum;
//        int i = (int)(4*point.getXComponent()) & 255;
//        int j = (int)(4*point.getYComponent()) & 255;
//        int k = (int)(4*point.getZComponent()) & 255;
//
//        return randomDouble[permX[i] ^ permY[j] ^ permZ[k]];
    }

    public double turbulence(final Vec3 point, final int depth) {
        double accum = 0.0;
        double weight = 1.0;
        Vec3 tempPoint = Vec3.multiply(point, 1);
        for (int i = 0; i < depth; i++) {
            accum += weight * noise(tempPoint);
            weight *= 0.5;
            tempPoint.scaleUp(2);
        }

        return Math.abs(accum);
    }
}
