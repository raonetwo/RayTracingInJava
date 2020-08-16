package ra.one.two.ray.tracing.rayhit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;

@AllArgsConstructor
@Getter
public class AxisAlignedBoundingBox {

    private final Vec3 min; // Corner of a 3d parallelepiped with edges aligned with the axis X0
    private final Vec3 max; // Diagonally opposite corner of the same parallelepiped X1

    public static AxisAlignedBoundingBox surroundingBox(AxisAlignedBoundingBox startBox, AxisAlignedBoundingBox endBox) {
        final Vec3 small = new Vec3(Math.min(startBox.getMin().getXComponent(), endBox.getMin().getXComponent()),
                Math.min(startBox.getMin().getYComponent(), endBox.getMin().getYComponent()),
                Math.min(startBox.getMin().getZComponent(), endBox.getMin().getZComponent()));

        final Vec3 big = new Vec3(Math.max(startBox.getMax().getXComponent(), endBox.getMax().getXComponent()),
                Math.max(startBox.getMax().getYComponent(), endBox.getMax().getYComponent()),
                Math.max(startBox.getMax().getZComponent(), endBox.getMax().getZComponent()));
        return new AxisAlignedBoundingBox(small, big);
    }

    public AxisAlignedBoundingBoxHitResult hit(final Ray ray, final double rayExtensionScaleMin, final double rayExtensionScaleMax) {
        // Check if some t exists such that min and max of ((X1 - A)/ B) and ((X0 - A)/ B) for x dimension
        // lies b/w rayExtensionScaleMin and rayExtensionScaleMax where X0 and X1 are two diagonal vertices of our
        // Axis Aligned bounding box parallelepiped and ray is given R = A + tB
        double inverseX = 1.0 / ray.getDirection().getXComponent();
        double rayExtensionMinX = (min.getXComponent() - ray.getOrigin().getXComponent()) * inverseX;
        double rayExtensionMaxX = (max.getXComponent() - ray.getOrigin().getXComponent()) * inverseX;
        if (inverseX < 0.0f) {
            var temp = rayExtensionMinX;
            rayExtensionMinX = rayExtensionMaxX;
            rayExtensionMaxX = temp;
        }
        double maxExtension = rayExtensionMaxX < rayExtensionScaleMax ? rayExtensionMaxX : rayExtensionScaleMax;
        double minExtension = rayExtensionMinX > rayExtensionScaleMin ? rayExtensionMinX : rayExtensionScaleMin;
        if (maxExtension <= minExtension) {
            return null;
        }

        // Check if some t exists such that min and max of ((X1 - A)/ B) and ((X0 - A)/ B) for y dimension
        // lies b/w rayExtensionScaleMin and rayExtensionScaleMax where X0 and X1 are two diagonal vertices of our
        // Axis Aligned bounding box parallelepiped and ray is given R = A + tB
        // check if these min and max value of t lies within the values set for x
        double inverseY = 1.0 / ray.getDirection().getYComponent();
        double rayExtensionMinY = (min.getYComponent() - ray.getOrigin().getYComponent()) * inverseY;
        double rayExtensionMaxY = (max.getYComponent() - ray.getOrigin().getYComponent()) * inverseY;
        if (inverseY < 0.0f) {
            var temp = rayExtensionMinY;
            rayExtensionMinY = rayExtensionMaxY;
            rayExtensionMaxY = temp;
        }
        maxExtension = rayExtensionMaxY < maxExtension ? rayExtensionMaxY : maxExtension;
        minExtension = rayExtensionMinY > minExtension ? rayExtensionMinY : minExtension;
        if (maxExtension <= minExtension) {
            return null;
        }

        // Check if some t exists such that min and max of ((X1 - A)/ B) and ((X0 - A)/ B) for z dimension
        // lies b/w rayExtensionScaleMin and rayExtensionScaleMax where X0 and X1 are two diagonal vertices of our
        // Axis Aligned bounding box parallelepiped and ray is given R = A + tB
        // check if these min and max value of t lies within the values set for y (and x),
        // basically we are trying to find that the range of t exists for all three dimensions.
        double inverseZ = 1.0 / ray.getDirection().getZComponent();
        double rayExtensionMinZ = (min.getZComponent() - ray.getOrigin().getZComponent()) * inverseZ;
        double rayExtensionMaxZ = (max.getZComponent() - ray.getOrigin().getZComponent()) * inverseZ;
        if (inverseZ < 0.0f) {
            var temp = rayExtensionMinZ;
            rayExtensionMinZ = rayExtensionMaxZ;
            rayExtensionMaxZ = temp;
        }
        maxExtension = rayExtensionMaxZ < maxExtension ? rayExtensionMaxZ : maxExtension;
        minExtension = rayExtensionMinZ > minExtension ? rayExtensionMinZ : minExtension;
        if (maxExtension <= minExtension) {
            return null;
        }

        // If t exists for all dimensions then we say that the bounding box hit is possible
        return new AxisAlignedBoundingBoxHitResult(minExtension, maxExtension);
    }
}
