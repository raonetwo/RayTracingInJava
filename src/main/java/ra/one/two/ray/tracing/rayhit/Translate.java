package ra.one.two.ray.tracing.rayhit;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.objects.Hittable;
import ra.one.two.ray.tracing.primitives.ray.Ray;

@AllArgsConstructor
public class Translate implements Hittable {
    final Vec3 offset;
    final Hittable translationTarget;

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord) {
        final Ray movedRay = new Ray(Vec3.subtract(ray.getOrigin(), offset), ray.getDirection(), ray.getRayFireTime());
        if (!translationTarget.hit(movedRay, tMin, tMax, hitRecord)) {
            return false;
        }

        hitRecord.getRayHitLocationOnHittableObject().add(offset);
        hitRecord.setFaceNormal(movedRay, hitRecord.getNormal());

        return true;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        final AxisAlignedBoundingBox boundingBoxUntranslate = translationTarget.boundingBox(tStart, tEnd);
        if (boundingBoxUntranslate == null) {
            return null;
        }
        return new AxisAlignedBoundingBox(boundingBoxUntranslate.getMin().add(offset), boundingBoxUntranslate.getMax().add(offset));
    }
}
