package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.primitives.ray.Ray;

import java.util.ArrayList;

/**
 * This class contains a list of hittable objects
 */
public class HittableList implements Hittable {

    private final ArrayList<Hittable> hittableList;

    public ArrayList<Hittable> getHittableList() {
        return hittableList;
    }

    public HittableList() {
        this.hittableList = new ArrayList<>();
    }

    @Override
    /*
     * This method returns the hit record for the closes object in the list that our ray can hit.
     * Our ray on extension by t may hit any number of objects but we only care about the closest object
     * as upon interacting with the closest object it either gets reflected, refracted or scattered. (diffraction out of scope).
     */
    public boolean hit(final Ray ray, final double tMin, final double tMax, final HitRecord hitRecord) {
        double closestSoFar = tMax;
        boolean hasRayHitSomething = false;
        for (final Hittable object : hittableList) {
            boolean hit = object.hit(ray, tMin, closestSoFar, hitRecord);
            if (hit) {
                hasRayHitSomething = true;
                closestSoFar = hitRecord.getRayExtensionScale();
            }
        }

        return hasRayHitSomething;
    }
}
