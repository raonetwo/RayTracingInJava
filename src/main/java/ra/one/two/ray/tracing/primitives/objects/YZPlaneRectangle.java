package ra.one.two.ray.tracing.primitives.objects;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.AxisAlignedBoundingBox;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

@AllArgsConstructor
public class YZPlaneRectangle implements Hittable{

    // Coordinates for bottom vertex
    private final double bottomZ;
    private final double bottomY;
    // Coordinate for diagonally opposite top vertex
    private final double topZ;
    private final double topY;
    // The value of z at which the rectangle is located on YZ plane
    private final double k;
    // Material associated with the object
    private final Material material;

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord) {
        double t = (k - ray.getOrigin().getXComponent()) / ray.getDirection().getXComponent();
        if (t < tMin || t > tMax) {
            return false;
        }
        double z = ray.getOrigin().getZComponent() + t * ray.getDirection().getZComponent();
        double y = ray.getOrigin().getYComponent() + t * ray.getDirection().getYComponent();
        if (z < bottomZ || z > topZ || y < bottomY || y > topY) {
            return false;
        }

        hitRecord.setTextureCoordinatesAtHitLocation(new TextureCoordinates((z - bottomZ) / (topZ - bottomZ), (y - bottomY) / (topY - bottomY)));
        hitRecord.setRayExtensionScale(t);
        hitRecord.setFaceNormal(ray, new Vec3(1, 0, 0));
        hitRecord.setMaterialOfObjectHit(material);
        hitRecord.setRayHitLocationOnHittableObject(ray.at(t));
        return true;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return new AxisAlignedBoundingBox(new Vec3(k - 0.0001, bottomY, bottomZ), new Vec3(k + 0.0001, topY, topZ));
    }
}
