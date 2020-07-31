package ra.one.two.ray.tracing.primitives.ray;

import ra.one.two.ray.tracing.primitives.math.Vec3;

/**
 * This class is not part of the original code written in article, this is a result of us wanting to avoid input parameters.
 * As we do optimisations we may do away with this class.
 */
public class ScatterResult {
    private final Vec3 colorFromObjectHit;
    private final Ray scatteredRay;

    /**
     * Constriuctor containing result of interaction of ray with materials to the object it hits.
     * @param colorFromObjectHit color that we get from scattering, this may be attenuated albedo, complete white etc. depending in material property
     * @param scatteredRay after the ray hits the object a new new ray is generated from the point of hit as origin and scattered depending on material property.
     */
    public ScatterResult(final Vec3 colorFromObjectHit, final Ray scatteredRay) {
        this.colorFromObjectHit = colorFromObjectHit;
        this.scatteredRay = scatteredRay;
    }

    public Vec3 getColorFromObjectHit() {
        return colorFromObjectHit;
    }

    public Ray getScatteredRay() {
        return scatteredRay;
    }
}


