package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.primitives.ray.Ray;

/**
 * Any class implementing this is hittable by a ray and must define a hit method.
 */
public interface Hittable {
    /**
     * The method checks if a given ray can hit an object implementing this when extended by scale t between tMin and tMax.
     * @param ray ray that we check for hits.
     * @param tMin minimum scale by which the ray must extend before we start checking for collisions.
     * @param tMax maximum scale by which the ray can be extended
     * @param hitRecord object to record a ray hit, stores information about a hit
     *                  like the material of object hit, the normal at hit location, the ray position from origin where it hit the object
     * @return boolean that indicates whether something was hit by the given ray
     */
    boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord);
}
