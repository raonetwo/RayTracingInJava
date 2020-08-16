package ra.one.two.ray.tracing.primitives.objects;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.AxisAlignedBoundingBox;
import ra.one.two.ray.tracing.rayhit.HitRecord;

@AllArgsConstructor
public class MovingSphere implements Hittable {
    private final Vec3 centerStart;
    private final Vec3 centerEnd;
    private final double startTime;
    private final double endTime;
    private final double radius;
    private final Material material;

    public Vec3 getCenter(final double time) {
        return Vec3.subtract(centerEnd, centerStart).scaleUp((time - startTime) / (endTime - startTime)).add(centerStart);
    }

    @Override
    // same as stationary sphere hit just that the center is now offset by time
    public boolean hit(final Ray ray, final double tMin, final double tMax, HitRecord hitRecord) {
        final Vec3 center = getCenter(ray.getRayFireTime());
        final Vec3 originToCenter = Vec3.subtract(ray.getOrigin(), center);
        final double a = ray.getDirection().lengthSquared();
        final double halfB = Vec3.dot(originToCenter, ray.getDirection());
        final double c = originToCenter.lengthSquared() - radius * radius;
        final double quarterDiscriminant = halfB * halfB - a * c;

        if (quarterDiscriminant > 0) {
            final double root = Math.sqrt(quarterDiscriminant);
            final double firstRoot = (-halfB - root) / a;
            if (recordHit(ray, tMin, tMax, hitRecord, firstRoot, center)) {
                return true;
            }
            final double secondRoot = (-halfB + root) / a;
            return recordHit(ray, tMin, tMax, hitRecord, secondRoot, center);
        }

        return false;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        final AxisAlignedBoundingBox startBox = new AxisAlignedBoundingBox(new Vec3(-radius).add(getCenter(tStart)), new Vec3(radius).add(getCenter(tStart)));
        final AxisAlignedBoundingBox endBox = new AxisAlignedBoundingBox(new Vec3(-radius).add(getCenter(tEnd)), new Vec3(radius).add(getCenter(tEnd)));
        return AxisAlignedBoundingBox.surroundingBox(startBox, endBox);
    }

    private boolean recordHit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord, final double root, final Vec3 center) {
        if (root < tMax && root > tMin) {
            // Save the details in the hit record
            hitRecord.setRayHitLocationOnHittableObject(ray.at(root));
            hitRecord.setRayExtensionScale(root);
            hitRecord.setMaterialOfObjectHit(material);
            final Vec3 outwardNormal = Vec3.subtract(hitRecord.getRayHitLocationOnHittableObject(), center).scaleDown(radius);
            hitRecord.setFaceNormal(ray, outwardNormal);
            return true;
        }
        return false;
    }
}
