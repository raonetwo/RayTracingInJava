package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.objects.Hittable;
import ra.one.two.ray.tracing.primitives.ray.Ray;

public class RotateY implements Hittable {
    private final double angle;
    private final Hittable rotationTarget;
    private final AxisAlignedBoundingBox boundingBoxAfterRotation;
    private final double sinTheta;
    private final double cosTheta;

    public RotateY(double angle, Hittable rotationTarget) {
        this.angle = angle;
        this.rotationTarget = rotationTarget;
        final double radians = Math.toRadians(angle);
        sinTheta = Math.sin(radians);
        cosTheta = Math.cos(radians);
        final AxisAlignedBoundingBox boundingBoxWithoutRotation = rotationTarget.boundingBox(0, 1);

        Vec3 min = new Vec3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Vec3 max = new Vec3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    final double x = i * boundingBoxWithoutRotation.getMax().getXComponent() + (1 - i) * boundingBoxWithoutRotation.getMin().getXComponent();
                    final double y = j * boundingBoxWithoutRotation.getMax().getYComponent() + (1 - j) * boundingBoxWithoutRotation.getMin().getYComponent();
                    final double z = k * boundingBoxWithoutRotation.getMax().getZComponent() + (1 - k) * boundingBoxWithoutRotation.getMin().getZComponent();
                    final double newx = cosTheta * x + sinTheta * z;
                    final double newz = -sinTheta * x + cosTheta * z;
                    min = new Vec3(Math.min(min.getXComponent(), newx), y, Math.min(min.getZComponent(), newz));
                    max = new Vec3(Math.max(max.getXComponent(), newx), y, Math.max(max.getZComponent(), newz));
                }
            }
        }

        boundingBoxAfterRotation = new AxisAlignedBoundingBox(min, max);
    }

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord) {

        double rotatedRayOriginX = cosTheta * ray.getOrigin().getXComponent() - sinTheta * ray.getOrigin().getZComponent();
        double rotatedRayOriginZ = sinTheta * ray.getOrigin().getXComponent() + cosTheta * ray.getOrigin().getZComponent();

        double rotatedRayDirectionX = cosTheta * ray.getDirection().getXComponent() - sinTheta * ray.getDirection().getZComponent();
        double rotatedRayDirectionZ = sinTheta * ray.getDirection().getXComponent() + cosTheta * ray.getDirection().getZComponent();

        final Ray rotatedRay = new Ray(new Vec3(rotatedRayOriginX, ray.getOrigin().getYComponent(), rotatedRayOriginZ),
                new Vec3(rotatedRayDirectionX, ray.getDirection().getYComponent(), rotatedRayDirectionZ), ray.getRayFireTime());

        if (!rotationTarget.hit(rotatedRay, tMin, tMax, hitRecord)) {
            return false;
        }

        double hitLocationRotatedX = cosTheta * hitRecord.getRayHitLocationOnHittableObject().getXComponent() + sinTheta * hitRecord.getRayHitLocationOnHittableObject().getZComponent();
        double hitLocationRotatedZ = -sinTheta * hitRecord.getRayHitLocationOnHittableObject().getXComponent() + cosTheta * hitRecord.getRayHitLocationOnHittableObject().getZComponent();

        double normalRotatedX = cosTheta * hitRecord.getNormal().getXComponent() + sinTheta * hitRecord.getNormal().getZComponent();
        double normalRotatedz = -sinTheta * hitRecord.getNormal().getXComponent() + cosTheta * hitRecord.getNormal().getZComponent();

        hitRecord.setRayHitLocationOnHittableObject(new Vec3(hitLocationRotatedX, hitRecord.getRayHitLocationOnHittableObject().getYComponent(), hitLocationRotatedZ));
        hitRecord.setFaceNormal(rotatedRay, new Vec3(normalRotatedX, hitRecord.getNormal().getYComponent(), normalRotatedz));

        return true;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return boundingBoxAfterRotation;
    }
}
