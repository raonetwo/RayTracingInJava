package ra.one.two.ray.tracing.primitives.objects;

import lombok.AllArgsConstructor;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.AxisAlignedBoundingBox;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

@AllArgsConstructor
public class XZPlaneRectangle implements Hittable{

    // Coordinates for bottom vertex
    private final double bottomX;
    private final double bottomZ;
    // Coordinate for diagonally opposite top vertex
    private final double topX;
    private final double topZ;
    // The value of u at which the rectangle is located on XZ plane
    private final double k;
    // Material associated with the object
    private final Material material;

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord hitRecord) {
        double t = (k - ray.getOrigin().getYComponent()) / ray.getDirection().getYComponent();
        if (t < tMin || t > tMax) {
            return false;
        }
        double x = ray.getOrigin().getXComponent() + t * ray.getDirection().getXComponent();
        double z = ray.getOrigin().getZComponent() + t * ray.getDirection().getZComponent();
        if (x < bottomX || x > topX || z < bottomZ || z > topZ) {
            return false;
        }

        hitRecord.setTextureCoordinatesAtHitLocation(new TextureCoordinates((x - bottomX) / (topX - bottomX), (z - bottomZ) / (topZ - bottomZ)));
        hitRecord.setRayExtensionScale(t);
        hitRecord.setFaceNormal(ray, new Vec3(0, 1, 0));
        hitRecord.setMaterialOfObjectHit(material);
        hitRecord.setRayHitLocationOnHittableObject(ray.at(t));
        return true;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return new AxisAlignedBoundingBox(new Vec3(bottomX, k - 0.0001, bottomZ ), new Vec3(topX, k + 0.0001, topZ));
    }
}
