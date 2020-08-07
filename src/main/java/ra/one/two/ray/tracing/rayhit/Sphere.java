package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.ray.Ray;

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
        // Points that lie on sphere are the points P that satisfy (P - C).(P - C) = r^2 where C is the center of the sphere
        // (x - Cx)^2 + (y - Cy)^2 + (z - Cz)^2 = r^2 now a point can be represented as a ray from origin as origin + t * unit_vector_direction
        // in other words origin + t * direction which we had earlier defined in a ray. So a point P(t) can lie on sphere if (P(t)−C)⋅(P(t)−C)=r2
        // (O+tB−C)⋅(O+tB−C)=r^2 or (O-C + tB).(O-C + tB) on expanding  t^2*B⋅B+2tB⋅(O−C)+(O−C)⋅(O−C)−r^2=0
        // here we want to solve for t as we know B for all rays we know O and C and see if solution is possible i.e. some t exists for ray O + t B
        // If such t exists then this means the ray we got as input will be able to intersect the sphere. We will solve for t using the quadratic formula
        // More importantly we want to solve for positive t (or t greater than t_min) as we want the intersection to be in front of the ray.
        // Get a vector from center to origin (O-C)
        final Vec3 originToCenter = Vec3.subtract(ray.getOrigin(), center);
        // dot product of direction of ray if a ray is O + tB we want B.B
        // a x^2 + b x + c = 0 we now have a
        final double a = ray.getDirection().length_squared();
        // (O-C).B
        // a x^2 + b x + c = 0 we now have b/2
        final double halfB = Vec3.dot(originToCenter, ray.getDirection());
        // (O-C).(O-C) - r^2
        // a x^2 + b x + c = 0 we now have c
        final double c = originToCenter.length_squared() - radius * radius;
        // discriminant = b*b - 4*a*c so quarter descriminant becomes
        final double quarterDiscriminant = halfB * halfB - a * c;

        // A real solution for t exists iff the quarterDiscriminant is positive otherwise the ray does not hit/intersect the sphere.
        // Check if a real solution exists.
        if (quarterDiscriminant > 0) {
            final double root = Math.sqrt(quarterDiscriminant);

            // Get the closest intersection point.
            final double firstRoot = (-halfB - root) / a;
            if (firstRoot < tMax && firstRoot > tMin) {
                // Save the details in the hit record
                hitRecord.setRayHitLocationOnHittableObject(ray.at(firstRoot));
                hitRecord.setRayExtensionScale(firstRoot);
                hitRecord.setMaterialOfObjectHit(material);
                final Vec3 outward_normal = Vec3.subtract(hitRecord.getRayHitLocationOnHittableObject(), center).scaleDown(radius);
                hitRecord.setFaceNormal(ray, outward_normal);
                return true;
            }

            // Get the second intersection point
            final double secondRoot = (-halfB + root) / a;
            if (secondRoot < tMax && secondRoot > tMin) {
                // Save the details in the hit record
                hitRecord.setRayHitLocationOnHittableObject(ray.at(secondRoot));
                hitRecord.setRayExtensionScale(secondRoot);
                hitRecord.setMaterialOfObjectHit(material);
                final Vec3 outward_normal = Vec3.subtract(hitRecord.getRayHitLocationOnHittableObject(), center).scaleDown(radius);
                hitRecord.setFaceNormal(ray, outward_normal);
                return true;
            }
        }

        return false;
    }
}
