package ra.one.two.ray.tracing.primitives.objects;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.AxisAlignedBoundingBox;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

@AllArgsConstructor
public class XYPlaneRectangle implements Hittable {

    // Coordinates for bottom vertex
    private final double bottomX;
    private final double bottomY;
    // Coordinate for diagonally opposite top vertex
    private final double topX;
    private final double topY;
    // The value of z at which the rectangle is located on XY plane
    private final double k;
    // Material associated with the object
    private final Material material;

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord) {
        double t = (k - ray.getOrigin().getZComponent()) / ray.getDirection().getZComponent();
        if (t < tMin || t > tMax) {
            return false;
        }
        double x = ray.getOrigin().getXComponent() + t * ray.getDirection().getXComponent();
        double y = ray.getOrigin().getYComponent() + t * ray.getDirection().getYComponent();
        if (x < bottomX || x > topX || y < bottomY || y > topY) {
            return false;
        }

        hitRecord.setTextureCoordinatesAtHitLocation(new TextureCoordinates((x - bottomX) / (topX - bottomX), (y - bottomY) / (topY - bottomY)));
        hitRecord.setRayExtensionScale(t);
        hitRecord.setFaceNormal(ray, new Vec3(0, 0, 1));
        hitRecord.setMaterialOfObjectHit(material);
        hitRecord.setRayHitLocationOnHittableObject(ray.at(t));
        return true;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return new AxisAlignedBoundingBox(new Vec3(bottomX, bottomY, k - 0.0001), new Vec3(topX, topY, k + 0.0001));
    }
}
