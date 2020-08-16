package ra.one.two.ray.tracing.primitives.objects;

import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.AxisAlignedBoundingBox;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

public class Sphere implements Hittable {
    final Vec3 center;
    final double radius;
    final Material material;

    public Sphere(final Vec3 center, final double radius, final Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public boolean hit(final Ray ray, final double tMin, final double tMax, HitRecord hitRecord) {
        /* Points that lie on sphere are the points P that satisfy (P - C).(P - C) = r^2 where C is the center of the sphere
         * (x - Cx)^2 + (y - Cy)^2 + (z - Cz)^2 = r^2 now a point can be represented as a ray from origin as origin + t * unit_vector_direction
         * in other words origin + t * direction which we had earlier defined in a ray. So a point P(t) can lie on sphere if (P(t)−C)⋅(P(t)−C)=r2
         * (O+tB−C)⋅(O+tB−C)=r^2 or (O-C + tB).(O-C + tB) on expanding  t^2*B⋅B+2tB⋅(O−C)+(O−C)⋅(O−C)−r^2=0
         * here we want to solve for t as we know B for all rays we know O and C and see if solution is possible i.e. some t exists for ray O + t B
         * If such t exists then this means the ray we got as input will be able to intersect the sphere. We will solve for t using the quadratic formula
         * More importantly we want to solve for positive t (or t greater than t_min) as we want the intersection to be in front of the ray.
         * Get a vector from center to origin (O-C)
         */
        final Vec3 originToCenter = Vec3.subtract(ray.getOrigin(), center);
        // dot product of direction of ray if a ray is O + tB we want B.B
        // a x^2 + b x + c = 0 we now have a
        final double a = ray.getDirection().lengthSquared();
        // (O-C).B
        // a x^2 + b x + c = 0 we now have b/2
        final double halfB = Vec3.dot(originToCenter, ray.getDirection());
        // (O-C).(O-C) - r^2
        // a x^2 + b x + c = 0 we now have c
        final double c = originToCenter.lengthSquared() - radius * radius;
        // discriminant = b*b - 4*a*c so quarter discriminant becomes
        final double quarterDiscriminant = halfB * halfB - a * c;

        // A real solution for t exists iff the quarterDiscriminant is positive otherwise the ray does not hit/intersect the sphere.
        // Check if a real solution exists.
        if (quarterDiscriminant > 0) {
            final double root = Math.sqrt(quarterDiscriminant);

            // Get the closest intersection point.
            final double firstRoot = (-halfB - root) / a;
            if (recordHit(ray, tMin, tMax, hitRecord, firstRoot)) {
                return true;
            }

            // Get the second intersection point if first not possible
            final double secondRoot = (-halfB + root) / a;
            return recordHit(ray, tMin, tMax, hitRecord, secondRoot);
        }

        return false;
    }

    @Override
    public AxisAlignedBoundingBox boundingBox(double tStart, double tEnd) {
        return new AxisAlignedBoundingBox(new Vec3(-radius).add(center), new Vec3(radius).add(center));
    }

    private boolean recordHit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord, final double root) {
        if (root < tMax && root > tMin) {
            // Save the details in the hit record
            hitRecord.setRayHitLocationOnHittableObject(ray.at(root));
            hitRecord.setRayExtensionScale(root);
            hitRecord.setMaterialOfObjectHit(material);
            final Vec3 outwardNormal = Vec3.subtract(hitRecord.getRayHitLocationOnHittableObject(), center).scaleDown(radius);
            hitRecord.setTextureCoordinatesAtHitLocation(getTextureCoordinates(outwardNormal));
            hitRecord.setFaceNormal(ray, outwardNormal);
            return true;
        }
        return false;
    }

    private final TextureCoordinates getTextureCoordinates(final Vec3 pointOnUnitSphere) {
        final double phi = Math.atan2(pointOnUnitSphere.getZComponent(), pointOnUnitSphere.getXComponent());
        final double theta = Math.asin(pointOnUnitSphere.getYComponent());
        return new TextureCoordinates(1 - (phi + Math.PI) / (2 * Math.PI), (theta + Math.PI / 2) / Math.PI);
    }
}
