package ra.one.two.ray.tracing.primitives.objects;

import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.AxisAlignedBoundingBox;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.rayhit.HittableList;

public class Box implements Hittable {

    private final HittableList sides = new HittableList();
    private final Vec3 bottomCorner;
    private final Vec3 topCorner;
    private final Material material;

    public Box(final Vec3 bottomCorner, final Vec3 topCorner, final Material material) {
        this.material = material;
        this.bottomCorner = bottomCorner;
        this.topCorner = topCorner;
        sides.getHittableList().add(new XYPlaneRectangle(bottomCorner.getXComponent(), bottomCorner.getYComponent(), topCorner.getXComponent(), topCorner.getYComponent(), bottomCorner.getZComponent(), material));
        sides.getHittableList().add(new XYPlaneRectangle(bottomCorner.getXComponent(), bottomCorner.getYComponent(), topCorner.getXComponent(), topCorner.getYComponent(), topCorner.getZComponent(), material));
        sides.getHittableList().add(new XZPlaneRectangle(bottomCorner.getXComponent(), bottomCorner.getZComponent(), topCorner.getXComponent(), topCorner.getZComponent(), bottomCorner.getYComponent(), material));
        sides.getHittableList().add(new XZPlaneRectangle(bottomCorner.getXComponent(), bottomCorner.getZComponent(), topCorner.getXComponent(), topCorner.getZComponent(), topCorner.getYComponent(), material));
        sides.getHittableList().add(new YZPlaneRectangle(bottomCorner.getZComponent(), bottomCorner.getYComponent(), topCorner.getZComponent(), topCorner.getYComponent(), bottomCorner.getXComponent(), material));
        sides.getHittableList().add(new YZPlaneRectangle(bottomCorner.getZComponent(), bottomCorner.getYComponent(), topCorner.getZComponent(), topCorner.getYComponent(), topCorner.getXComponent(), material));
    }

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord) {
        return sides.hit(ray, tMin, tMax, hitRecord);
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return new AxisAlignedBoundingBox(bottomCorner, topCorner);
    }
}
