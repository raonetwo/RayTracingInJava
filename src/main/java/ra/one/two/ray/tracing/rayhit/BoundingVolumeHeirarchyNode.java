package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.primitives.objects.Hittable;
import ra.one.two.ray.tracing.primitives.ray.Ray;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BoundingVolumeHeirarchyNode implements Hittable {

    private final Hittable left;
    private final Hittable right;
    private final AxisAlignedBoundingBox boundingBox;

    public BoundingVolumeHeirarchyNode(final HittableList hittableList, final double startTime, final double endTime) {
        this(hittableList.getHittableList(), startTime, endTime);
    }

    public BoundingVolumeHeirarchyNode(final List<Hittable> hittableList, final double startTime, final double endTime) {
        final int randomAxis = ThreadLocalRandom.current().nextInt(0, 3);
        final Comparator<Hittable> comparator = (randomAxis == 0) ? getBoxXComparator() : (randomAxis == 1) ? getBoxYComparator() : getBoxZComparator();
        Collections.sort(hittableList, comparator);
        if (hittableList.size() == 1) {
            this.left = hittableList.get(0);
            this.right = this.left;
        } else if (hittableList.size() == 2) {
                this.left = hittableList.get(0);
                this.right = hittableList.get(1);
        } else {
            left = new BoundingVolumeHeirarchyNode(hittableList.subList(0, hittableList.size() / 2), startTime, endTime);
            right = new BoundingVolumeHeirarchyNode(hittableList.subList(hittableList.size() / 2, hittableList.size()), startTime, endTime);
        }
        this.boundingBox = AxisAlignedBoundingBox.surroundingBox(this.left.boundingBox(startTime, endTime), this.right.boundingBox(startTime, endTime));
    }

    private Comparator<Hittable> getBoxZComparator() {
        return Comparator.comparingDouble(hittable -> hittable.boundingBox(0, 0).getMin().getZComponent());
    }

    private Comparator<Hittable> getBoxYComparator() {
        return Comparator.comparingDouble(hittable -> hittable.boundingBox(0, 0).getMin().getYComponent());
    }

    private Comparator<Hittable> getBoxXComparator() {
        return Comparator.comparingDouble(hittable -> hittable.boundingBox(0, 0).getMin().getXComponent());
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord) {
        final AxisAlignedBoundingBoxHitResult axisAlignedBoundingBoxHitResult = boundingBox.hit(ray, tMin, tMax);
        if (axisAlignedBoundingBoxHitResult == null) {
            return false;
        }
        final boolean hitLeft = left.hit(ray, axisAlignedBoundingBoxHitResult.getMinRayExtension(), axisAlignedBoundingBoxHitResult.getMaxRayExtension(), hitRecord);
        final boolean hitRight = right.hit(ray, axisAlignedBoundingBoxHitResult.getMinRayExtension(), hitLeft ? hitRecord.getRayExtensionScale() : axisAlignedBoundingBoxHitResult.getMaxRayExtension(), hitRecord);

        return hitLeft || hitRight;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return boundingBox;
    }
}
