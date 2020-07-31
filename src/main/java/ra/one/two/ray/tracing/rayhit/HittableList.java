package ra.one.two.ray.tracing.rayhit;

import ra.one.two.ray.tracing.primitives.ray.Ray;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a list of hittable objects
 */
public class HittableList implements Hittable {

    List<Hittable> hittableList;

    public List<Hittable> getHittableList() {
        return hittableList;
    }

    public HittableList() {
        this.hittableList = new ArrayList<>();
    }

    @Override
    /**
     * This method returns the hit record for the closes object in the list that our ray can hit.
     * Our ray on extension by t may hit any number of objects but we only care about the closest object
     * as upon interacting with the closest object it either gets reflected, refracted or scattered. (diffraction out of scope)
     */
    public HitRecord hit(Ray ray, double tMin, double tMax) {
        double closest_so_far = tMax;

        HitRecord hitRecord = null;

        for (final Hittable object : hittableList) {
            final HitRecord hitRecordObject = object.hit(ray, tMin, closest_so_far);
            if (hitRecordObject != null) {
                hitRecord = hitRecordObject;
                closest_so_far = hitRecordObject.getRayExtensionScale();
            }
        }

        return hitRecord;
    }
}
