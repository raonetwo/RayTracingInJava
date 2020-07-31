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
     * @return HitRecord of hit if possible null otherwise
     */
    public HitRecord hit(final Ray ray, final double tMin, final double tMax);
}
