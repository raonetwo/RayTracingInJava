package ra.one.two.ray.tracing.materials;

import ra.one.two.ray.tracing.primitives.ray.Ray;
import ra.one.two.ray.tracing.primitives.ray.ScatterResult;
import ra.one.two.ray.tracing.rayhit.HitRecord;

// Different types of material interact with input ray differently and scatter it differently
public interface Material {
    /**
     * This method scatters the input ray based on the material properties
     * @param rayIn input ray
     * @param record record where the ray hit an object
     * @return scattered ray with attenuation (color)
     */
    ScatterResult scatter(final Ray rayIn, final HitRecord record);
}
