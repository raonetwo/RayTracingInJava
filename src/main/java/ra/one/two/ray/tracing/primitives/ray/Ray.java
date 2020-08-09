package ra.one.two.ray.tracing.primitives.ray;

import ra.one.two.ray.tracing.primitives.math.Vec3;

/**
 * This class is used to represents a ray.
 */
public class Ray {
    private final Vec3 origin;
    private final Vec3 direction;

    public Ray() {
        origin = new Vec3();
        direction = new Vec3();
    }

    /**
     * Constructor for ray
     * @param origin vector position where a ray starts
     * @param direction vector direction in which the ray points
     */
    public Ray(final Vec3 origin, final Vec3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * Getter for the origin
     * @return origin of this ray
     */
    public Vec3 getOrigin() {
        return origin;
    }

    /**
     * Getter for direction
     * @return direction in which the ray is moving
     */
    public Vec3 getDirection() {
        return direction;
    }

    /**
     * This method returns position of a ray at a given parameter t
     * All rays can be represented as P(t) = A + tB where P is the Position on the line,
     * A is the origin and B is a vector in the direction in the direction of line,
     * as the ray moves/extended t changes, positive t represents the points in front of ray away from ray origin.
     * @param t parameter value by which we want to extend the ray from origin.
     * @return position vector of ray at a given t
     */
    public Vec3 at(final double t) {
        return Vec3.add(origin, Vec3.multiply(direction, t));
    }
}
