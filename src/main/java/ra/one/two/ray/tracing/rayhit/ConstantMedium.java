package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.materials.Isotropic;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.objects.Hittable;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.textures.Texture;

public class ConstantMedium implements Hittable {
    private final double negativeInverseDensity;
    private final Hittable mediumBoundary;
    private final Material phaseFunction;

    public ConstantMedium(final Hittable mediumBoundary, final double mediumDensity, final Texture texture) {
        this.mediumBoundary = mediumBoundary;
        negativeInverseDensity = -1.0 / mediumDensity;
        phaseFunction = new Isotropic(texture);
    }

    public ConstantMedium(final Hittable mediumBoundary, final double mediumDensity, final Vec3 color) {
        this.mediumBoundary = mediumBoundary;
        negativeInverseDensity = -1.0 / mediumDensity;
        phaseFunction = new Isotropic(color);
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord) {

        final HitRecord hitRecord1 = new HitRecord();
        final HitRecord hitRecord2 = new HitRecord();

        // Check if ray can hit object anywhere
        if (!mediumBoundary.hit(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, hitRecord1)) {
            return false;
        }

        // Check if ray can hit object again on extension from the point of previous intersection,
        // basically checking if ray is coming from outside and is inside the medium boundary
        if (!mediumBoundary.hit(ray, hitRecord1.getRayExtensionScale() + 0.0001, Double.POSITIVE_INFINITY, hitRecord2)) {
            return false;
        }

        double firstHitRayExtension = hitRecord1.getRayExtensionScale() < tMin ? tMin : hitRecord1.getRayExtensionScale();
        final double secondHitRayExtension = hitRecord2.getRayExtensionScale() > tMax ? tMax : hitRecord2.getRayExtensionScale();

        if (firstHitRayExtension >= secondHitRayExtension) {
            return false;
        }

        if (firstHitRayExtension < 0) {
            firstHitRayExtension = 0;
        }

        final var rayLength = ray.getDirection().length();
        final var distanceInsideBoundary = (secondHitRayExtension - firstHitRayExtension) * rayLength;
        final var hitDistance = negativeInverseDensity * Math.log(Math.random());

        if (hitDistance > distanceInsideBoundary) {
            return false;
        }

        hitRecord.setRayExtensionScale(firstHitRayExtension + hitDistance / rayLength);
        hitRecord.setRayHitLocationOnHittableObject(ray.at(hitRecord.getRayExtensionScale()));
        hitRecord.setNormal(new Vec3(1, 0, 0)); // arbitrary
        hitRecord.setFrontFace(true); // also arbitrary
        hitRecord.setMaterialOfObjectHit(phaseFunction);

        return true;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return mediumBoundary.boundingBox(tStart, tEnd);
    }
}
