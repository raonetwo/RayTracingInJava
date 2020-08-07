package ra.one.two.ray.tracing.scene;

import ra.one.two.ray.tracing.primitives.math.Vec3;
import ra.one.two.ray.tracing.primitives.ray.Ray;

public class Camera {

    // Camera origin
    private final Vec3 cameraOrigin;
    // Lower left corner of the viewport
    private final Vec3 lowerLeftCorner;
    private final Vec3 horizontal;
    private final Vec3 vertical;
    private final double lensRadius;
    private final Vec3 u;
    private final Vec3 v;
    private final Vec3 w;

    /**
     * Constructor for camera.
     * @param lookFrom the location the camera should be present at
     * @param lookAt the location we want camera to look at
     * @param upDirectionUnitVector a unit vector telling camera what up direction is
     * @param vetricalFieldOfViewInDegrees field of View of Camera, basically till what angle can camera see from its viewport.
     * @param aspect_ratio aspect ration of the camera viewport
     * @param aperture diameter of camera lens
     * @param focusDistance distance to focus camera at
     */
    public Camera(
            final Vec3 lookFrom,
            final Vec3 lookAt,
            final Vec3 upDirectionUnitVector,
            final double vetricalFieldOfViewInDegrees,
            final double aspect_ratio,
            final double aperture,
            final double focusDistance) {
        final double cameraFieldOfViewInRadians = Math.toRadians(vetricalFieldOfViewInDegrees);
        // When the camera is at origin viewport midpoint lies on z axis at z = - focusDistance plane.
        // Now that the camera moves, viewport moves with it still separated by distance focusDistance away.
        // Viewport height becomes two times (adjusting for the negative axis) distance allowed by field of view / 2
        // tan(theta/2)= viewportHalfHeight/focusDistance where theta is field of view angle relative to camera origin
        // which makes viewportHeight as
        final double viewportHeight = 2.0 * Math.tan(cameraFieldOfViewInRadians / 2) * focusDistance ;
        final double viewportWidth = aspect_ratio * viewportHeight;

        // Get unit vector in direction to look at from the position to look from.
        // Get unit vectors in the plane orthogonal to this vector which is our camera's plane
        // These vectors will be the same for viewport as well since
        w = Vec3.unitVector(Vec3.subtract(lookFrom, lookAt));
        u = Vec3.unitVector(Vec3.cross(upDirectionUnitVector, w));
        v = Vec3.cross(w, u);

        cameraOrigin = lookFrom;

        // Vectors on viewport (since its a parallel plane to camera plane) in horizontal and vertical direction.
        horizontal = Vec3.multiply(u, viewportWidth);
        vertical = Vec3.multiply(v, viewportHeight);
        // Lower left corner is viewportLocation - horizontal/2 - vertical/2
        // viewportLocation = cameraOrigin - w * focalLength

        lowerLeftCorner = Vec3.subtract(cameraOrigin, Vec3.divide(horizontal, 2)).subtract(Vec3.divide(vertical, 2)).subtract(Vec3.multiply(w, focusDistance));
        lensRadius = aperture / 2;
    }

    /**
     * Get a ray from camera from some random point on its lens towards the viewport.
     * The ray is in a direction of lower left corner of viewport
     * offset by multiplying input scales to viewport horizontal and vertical orthogonal vectors
     * @param horizontalScaleOfViewportOffset scale by which we want to scale viewport's horizontal direction vector by. It should lie b/w 0 and 1
     *          as this scaled horizontal is added to lower left corner of view port to get the exact location where we want
     *          ray to direct at from the camera.
     * @param verticalScaleOfViewportOffset scale by which we want to scale viewport's vertical direction vector by. It should lie b/w 0 and 1
     *          as this scaled horizontal is added to lower left corner of view port to get the exact location where we want
     *          ray to direct at from the camera.
     * @return a ray from camera origin + random offset within lens radius (aperture/2) in the direction of lower left corner of view port that lies focus distance away
     *         with a horizontal and vertical offset on the viewport place as requested in input.
     */
    Ray getRay(final double horizontalScaleOfViewportOffset, final double verticalScaleOfViewportOffset) {
        // Get a random vector that lies inside a unit disc and scale up to lensRadius
        final Vec3 randomDisk = Vec3.randomInUnitDisk().scaleUp(lensRadius);
        // Get a random vector in a disc of radius equal to lensRadius in the camera plane and add camera origin to get the ray origin
        final Vec3 rayOrigin = Vec3.multiply(u, randomDisk.getxComponent()).add(Vec3.multiply(v, randomDisk.getyComponent())).add(cameraOrigin);
        return new Ray(rayOrigin,
                Vec3.add(lowerLeftCorner, Vec3.multiply(horizontal, horizontalScaleOfViewportOffset)).add(Vec3.multiply(vertical, verticalScaleOfViewportOffset)).subtract(rayOrigin));
    }
}
