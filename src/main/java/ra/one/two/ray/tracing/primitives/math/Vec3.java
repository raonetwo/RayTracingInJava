package ra.one.two.ray.tracing.primitives.math;

/**
 * This class implements the same 3 dimensional vector in Cartesian space.
 */
public class Vec3 {
    private double xComponent;
    private double yComponent;
    private double zComponent;

    /**
     * Standard 3 Dimensional constructor that takes x y and z values/
     *
     * @param xComponent x coordinate value of the vector
     * @param yComponent y coordinate value of the vector
     * @param zComponent z coordinate value of the vector
     */
    public Vec3(double xComponent, double yComponent, double zComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
        this.zComponent = zComponent;
    }

    /**
     * Default initialised zero vector.
     */
    public Vec3() {
        this(0);
    }

    /**
     * Constructor to set the same value for all 3 coordinates
     *
     * @param commonComponentValue the value to set for the 3 coordinates.
     */
    public Vec3(double commonComponentValue) {
        this(commonComponentValue, commonComponentValue, commonComponentValue);
    }

    /**
     * Getter for x coordinate
     *
     * @return x coordinate value of this vector
     */
    public double getXComponent() {
        return xComponent;
    }

    /**
     * Getter for y coordinate
     *
     * @return y coordinate value of this vector
     */
    public double getYComponent() {
        return yComponent;
    }

    /**
     * Getter for z coordinate
     *
     * @return z coordinate value of this vector
     */
    public double getZComponent() {
        return zComponent;
    }

    /**
     * Add the provided vector to this
     *
     * @param other vector to add to this
     * @return this after the input vector is added to it.
     */
    public Vec3 add(final Vec3 other) {
        this.xComponent = this.xComponent + other.xComponent;
        this.yComponent = this.yComponent + other.yComponent;
        this.zComponent = this.zComponent + other.zComponent;
        return this;
    }

    /**
     * Subtract the provided vector from this
     *
     * @param other vector to subtract from this
     * @return this after the input vector is added to it.
     */
    public Vec3 subtract(final Vec3 other) {
        this.xComponent = this.xComponent - other.xComponent;
        this.yComponent = this.yComponent - other.yComponent;
        this.zComponent = this.zComponent - other.zComponent;
        return this;
    }

    /**
     * Scale this vector up, the current vector length gets changed but direction remains the same
     *
     * @param scale scaling factor we want to multiply with
     * @return this vector scaled up
     */
    public Vec3 scaleUp(final double scale) {
        this.xComponent = this.xComponent * scale;
        this.yComponent = this.yComponent * scale;
        this.zComponent = this.zComponent * scale;
        return this;
    }

    /**
     * Scale this vector down, the current vector length gets changed but direction remains the same
     *
     * @param scale scaling factor we want to divide with
     * @return this vector scaled down
     */
    public Vec3 scaleDown(final double scale) {
        this.xComponent = this.xComponent / scale;
        this.yComponent = this.yComponent / scale;
        this.zComponent = this.zComponent / scale;
        return this;
    }

    /**
     * Raise each component of vector by power
     *
     * @param power power by which each component has to be raised
     * @return this after the power operation has been applied to all components.
     */
    public Vec3 pow(final double power) {
        this.xComponent = Math.pow(this.xComponent, power);
        this.yComponent = Math.pow(this.yComponent, power);
        this.zComponent = Math.pow(this.zComponent, power);
        return this;
    }

    /**
     * Clamp the value of the vector components b/w min and max. This restricts the value b/w min and max.
     *
     * @param min minimum value allowed
     * @param max maximum value allowed
     * @return returns either the input value if it lies b/w min and max, if its lower then min then returns min, max if its higher then max.
     */
    public Vec3 clamp(final double min, final double max) {
        this.xComponent = Math.min(max, Math.max(min, this.xComponent));
        this.yComponent = Math.min(max, Math.max(min, this.yComponent));
        this.zComponent = Math.min(max, Math.max(min, this.zComponent));
        return this;
    }

    public Vec3 inverse() {
        return this.scaleUp(-1);
    }

    /**
     * Length of a vector squared aka dot product with self
     *
     * @return length of the vector squared.
     */
    public double lengthSquared() {
        return this.xComponent * this.xComponent + this.yComponent * this.yComponent + this.zComponent * this.zComponent;
    }

    /**
     * Length of the vector aka norm aka magnitude
     *
     * @return length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Get a vector that is negative of this vector. Same length opposite direction.
     *
     * @return a new vector that is negative of this vector.
     */
    public Vec3 negative() {
        return new Vec3(-this.xComponent, -this.yComponent, -this.zComponent);
    }

    public String toString() {
        return "" + (int) this.xComponent + " " + (int) this.yComponent + " " + (int) this.zComponent;
    }

    /**
     * Add two vectors, for visualisation read the parallelogram law
     *
     * @param first  first vector that we want to add.
     * @param second second vector that we want to add.
     * @return a new vector that is sum of the two input vectors
     */
    public static Vec3 add(final Vec3 first, final Vec3 second) {
        return new Vec3(first.xComponent + second.xComponent, first.yComponent + second.yComponent, first.zComponent + second.zComponent);
    }

    /**
     * Subtract two vectors, for visualisation read the parallelogram law
     *
     * @param first  first vector that we want to subtract from.
     * @param second second vector that we want to subtract.
     * @return a new vector that is subtraction of second from the first.
     */
    public static Vec3 subtract(final Vec3 first, final Vec3 second) {
        return new Vec3(first.xComponent - second.xComponent, first.yComponent - second.yComponent, first.zComponent - second.zComponent);
    }

    /**
     * scalar multiplication to a vector to create a new vector(increases length no change in direction)
     *
     * @param first vector we want to scale
     * @param scale value by which each coordinate of the vector will be multiplied
     * @return a new vector that is scaled up.
     */
    public static Vec3 multiply(final Vec3 first, final double scale) {
        return new Vec3(first.xComponent * scale, first.yComponent * scale, first.zComponent * scale);
    }

    /**
     * scalar division to a vector to create a new vector(increases length no change in direction)
     *
     * @param first vector we want to scale
     * @param scale value by which each coordinate of the vector will be divided
     * @return a new vector that is scaled down.
     */
    public static Vec3 divide(final Vec3 first, final double scale) {
        return new Vec3(first.xComponent / scale, first.yComponent / scale, first.zComponent / scale);
    }

    /**
     * Hadamard product of vectors. see https://en.wikipedia.org/wiki/Hadamard_product_(matrices)
     * Basically x multiplied by x, y by y and z by z of the two vectors
     *
     * @param first  vector to be multiplied
     * @param second vector to be multiplied
     * @return a new vector with component wise product of two vectors
     */
    public static Vec3 componentWiseMultiply(final Vec3 first, final Vec3 second) {
        return new Vec3(first.xComponent * second.xComponent, first.yComponent * second.yComponent, first.zComponent * second.zComponent);
    }

    /**
     * Dot product aka Scalar product of two vectors, basically multiply x by x, y by and z by z and then sum them all up.
     * A ⋅ B = ||A|| ||B|| cos θ.
     *
     * @param first  vector to be multiplied
     * @param second vector to be multiplied
     * @return double value of the scalar product of two vector
     */
    public static double dot(final Vec3 first, final Vec3 second) {
        return first.xComponent * second.xComponent + first.yComponent * second.yComponent + first.zComponent * second.zComponent;
    }

    /**
     * Cross product aka Vector product of the two vectors. see https://en.wikipedia.org/wiki/Cross_product for details.
     * A × B = ||A|| ||B|| sin θ n. n is the unit vector perpendicular to the plane determined by vectors A and B,
     *
     * @param first  vector to be multiplied
     * @param second vector to be multiplied
     * @return new vector which is cross product of input vector
     */
    public static Vec3 cross(final Vec3 first, final Vec3 second) {
        return new Vec3(first.yComponent * second.zComponent - first.zComponent * second.yComponent, first.zComponent * second.xComponent - first.xComponent * second.zComponent, first.xComponent * second.yComponent - first.yComponent * second.xComponent);
    }

    /**
     * Get a unit vector in the direction of a given vector
     *
     * @param v vector for which we want to get a unit vector for
     * @return unit vector in the direction of the input vector
     */
    public static Vec3 unitVector(Vec3 v) {
        return divide(v, v.length());
    }

    /**
     * Create a random vector, note this vector is likely not of unit length.
     *
     * @return a random vector.
     */
    public static Vec3 random() {
        return new Vec3(Math.random(), Math.random(), Math.random());
    }

    /**
     * Create a random vector where values of coordinate lies between provided min and max values.
     *
     * @param min minimum value for any coordinate
     * @param max maximum value for any coordinate
     * @return a new random vector with coordinate values b/w provided limits
     */
    public static Vec3 random(double min, double max) {
        return new Vec3(Math.random() * (max - min) + min, Math.random() * (max - min) + min, Math.random() * (max - min) + min);
    }

    /**
     * Get a random unit vector in a unit sphere by first creating a random vector in unit cube
     * and then checking if it lies in sphere. This method we will call rejection method.
     *
     * @return a random unit vector.
     */
    public static Vec3 randomInUnitSphere() {
        while (true) {
            Vec3 randomVector = random(-1, 1);
            if (randomVector.lengthSquared() >= 1) {
                continue;
            }
            return randomVector;
        }
    }

    /**
     * This is a drop in replacement of randomInUnitSphere. This returns a random unit vector.
     *
     * @return a random unit vector
     */
    public static Vec3 randomUnitVector() {
        // Get a random azimuthal angle in 0 to 2 pi
        // see https://en.wikipedia.org/wiki/Spherical_coordinate_system#Cartesian_coordinates
        final double azimuthalAngle = Math.random() * 2 * Math.PI;
        // Get a random z coordinate b/w -1 and 1, serves as a way to get random polar angle
        // as z = r cos theta where r is radius of sphere and theta is polar angle in this case its 1 so z = cos theta
        final double randomZCoordinate = Math.random() * 2 - 1;
        // Get the radius of circle at the current z coordinate which is r sin theta and since r is 1 its just sin theta
        // which is sqrt of 1 - cos theta * cos theta
        final double radiusOfDiscCutIntoASphereAtChosenZ = Math.sqrt(1 - randomZCoordinate * randomZCoordinate);
        return new Vec3(radiusOfDiscCutIntoASphereAtChosenZ * Math.cos(azimuthalAngle), radiusOfDiscCutIntoASphereAtChosenZ * Math.sin(azimuthalAngle), randomZCoordinate);
    }

    /**
     * Get a vector which is reflection of input incident vector reflected about the input normal vector
     * see https://en.wikipedia.org/wiki/Reflection_(mathematics) and https://en.wikipedia.org/wiki/Specular_reflection
     *
     * @param incidence vector we want to reflect
     * @param normal    vector we want to reflect against
     * @return reflected vector
     */
    public static Vec3 reflect(final Vec3 incidence, final Vec3 normal) {
        return subtract(incidence, multiply(normal, 2 * dot(incidence, normal)));
    }

    /**
     * Get a random vector that lies in a unit disk.
     *
     * @return a random vector that lies in a unit disk.
     */
    public static Vec3 randomInUnitDisk() {
        while (true) {
            Vec3 randomVectorInUnitSquareOnXYAxis = new Vec3(Math.random() * 2 - 1, Math.random() * 2 - 1, 0);
            if (randomVectorInUnitSquareOnXYAxis.lengthSquared() >= 1) continue;
            return randomVectorInUnitSquareOnXYAxis;
        }
    }

    /**
     * Get a vector which is the result of refraction against a normal of the surface of the changed medium.
     * see https://graphics.stanford.edu/courses/cs148-10-summer/docs/2006--degreve--reflection_refraction.pdf for reference
     *
     * @param incident                                                incident unit vector
     * @param normal                                                  normal vector of the surface we want to refract around
     * @param refractiveIndexIncidenceOverRefractiveIndexTransmission relative refractive index of the new medium
     * @return refracted vector
     */
    public static Vec3 refract(final Vec3 incident, final Vec3 normal, final double refractiveIndexIncidenceOverRefractiveIndexTransmission) {
        // Get incidence angle
        double cosTheta = dot(incident.negative(), normal);
        // Get component perpendicular to normal for the refracted ray
        // Since the tangential component of incident and transmitted ray point in same direction. snell's law say that sinθt=η1/η2sinθi
        // which means the tangential component follow the above law as they are sin theta component of the incident and transmitted rays.
        // which means that t tangential = η1/η2[i+cosθin] where i and n are incident and normal vectors respectively.
        Vec3 rOutPerp = add(incident, multiply(normal, cosTheta)).scaleUp(refractiveIndexIncidenceOverRefractiveIndexTransmission);
        // t⊥=−√1−∣∣t‖∣∣^2  n i.e. the component parallel to normal can be retrieved using Pythagoras theorem we know that output ray length is 1.
        Vec3 rOutParallel = multiply(normal, -Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared())));
        return rOutPerp.add(rOutParallel);
    }

}
