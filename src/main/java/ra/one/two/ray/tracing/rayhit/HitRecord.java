package ra.one.two.ray.tracing.rayhit;

import lombok.Getter;
import lombok.Setter;
import ra.one.two.ray.tracing.materials.Material;
import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.textures.TextureCoordinates;

public class HitRecord {

    // We may have chosen to just save ray instead and get this location using ray.at(rayExtensionScale), its simpler to keep this as this is required by materials.
    // We could also have chose to save the hit object in this record and use that for normal calculation, but we are going by the book and again its simpler to keep just the normal at the hit location here.
    @Getter
    @Setter
    private Vec3 rayHitLocationOnHittableObject;

    @Getter
    @Setter
    private Material materialOfObjectHit;

    // the parameter t by which a ray P(t) = Origin + t * Direction was able to hit the object this record corresponds to.
    @Getter
    @Setter
    private double rayExtensionScale;

    @Getter
    @Setter
    private TextureCoordinates textureCoordinatesAtHitLocation;

    @Getter
    @Setter
    private Vec3 normal = new Vec3();

    // We could have omitted frontFace and setFaceNormal and directly could have set normal as part of the constructor
    // if we chose to have all our normals outer only.
    @Getter
    @Setter
    private boolean frontFace = false;

    public HitRecord() {
        /*
         * The value will be set at the time when some ray hits some object through setters.
         * Immediately after that we check for scattering, so there is no use of creating new objects for each hit.
         * The same object will be reused for all the future ray hits by scattered rays.
         * This will save us some memory and cpu time, you can compare this with the first commit
         * when we created new object for each hit.
         */
    }

    /**
     * This method is required by every hittable object to be called at the time of collision, after calculating the outward normal.
     * We could have omitted frontFace and setFaceNormal and directly could have set normal as part of the constructor
     * if we chose to have all our normals outer only.
     *
     * @param ray           ray for which we want to save the normal information for which is part of the hit record.
     * @param outwardNormal normal vector pointing outwards from the surface
     */
    public void setFaceNormal(final Ray ray, final Vec3 outwardNormal) {
        frontFace = Vec3.dot(ray.getDirection(), outwardNormal) < 0;
        normal = frontFace ? outwardNormal : outwardNormal.inverse();
    }

}
