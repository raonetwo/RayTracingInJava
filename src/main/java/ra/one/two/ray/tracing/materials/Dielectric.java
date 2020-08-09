package ra.one.two.ray.tracing.materials;


import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.rayhit.HitRecord;
import ra.one.two.ray.tracing.primitives.ray.ScatterResult;

public class Dielectric implements Material {

    private final double refractiveIndex;

    /**
     * Constructor for dielectric material
     * @param refractiveIndex refractive index of the material
     */
    public Dielectric(final double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    @Override
    public ScatterResult scatter(final Ray rayIn, final HitRecord record) {

        final double refractiveIndexIncidenceOverRefractiveIndexTransmission = record.isFrontFace() ? (1.0 / refractiveIndex) : refractiveIndex;
        final Vec3 unitIncidenceRayDirection = Vec3.unitVector(rayIn.getDirection());
        // get cos (angle of incidence)
        final double cosTheta = Math.min(Vec3.dot(unitIncidenceRayDirection.negative(), record.getNormal()), 1.0);
        final double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);
        // Check for total internal reflection
        if (refractiveIndexIncidenceOverRefractiveIndexTransmission * sinTheta > 1.0 ) {
            final Vec3 reflected = Vec3.reflect(unitIncidenceRayDirection, record.getNormal());
            return new ScatterResult(new Vec3(1.0), new Ray(record.getRayHitLocationOnHittableObject(), reflected));
        }
        // There is a probability associated with reflection vs refraction, we can estimate that with schlick approximation
        final double reflectProbability = schlick(cosTheta, refractiveIndexIncidenceOverRefractiveIndexTransmission);
        // Check on random if this ray can be reflected
        if (Math.random() < reflectProbability)
        {
            final Vec3 reflected = Vec3.reflect(unitIncidenceRayDirection, record.getNormal());
            return new ScatterResult(new Vec3(1.0), new Ray(record.getRayHitLocationOnHittableObject(), reflected));
        }
        final Vec3 refracted = Vec3.refract(unitIncidenceRayDirection, record.getNormal(), refractiveIndexIncidenceOverRefractiveIndexTransmission);
        return new ScatterResult(new Vec3(1.0), new Ray(record.getRayHitLocationOnHittableObject(), refracted));
    }

    // Read https://en.wikipedia.org/wiki/Schlick%27s_approximation
    private double schlick(final double cosTheta, final double refractiveIndex) {
        final double r0 = Math.pow((1 - refractiveIndex) / (1 + refractiveIndex), 2);
        return r0 + (1-r0)*Math.pow((1 - cosTheta),5);
    }
}
