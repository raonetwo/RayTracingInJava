package ra.one.two.ray.tracing.rayhit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AxisAlignedBoundingBoxHitResult {
    private final double minRayExtension;
    private final double maxRayExtension;
}
