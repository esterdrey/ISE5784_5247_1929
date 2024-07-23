package renderer;

import primitives.Color;
import primitives.*;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * Abstract base class for ray tracing algorithms.
 *
 * @author Ester Drey Avigail Bash
 */
public abstract class RayTracerBase {


    /**
     * The scene being traced.
     */
    protected Scene scene;

    /**
     * Constructs a new RayTracerBase with the specified scene.
     *
     * @param scene The scene to be traced.
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method to trace a ray and compute its color.
     *
     * @param ray The ray to be traced.
     * @return The color computed for the traced ray.5559;oo
     */
    public abstract Color traceRay(Ray ray);

    /**
     * Trace the rays and calculates the color of the point that interact with the geometries of the scene
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    public abstract Color traceRays(List<Ray> rays);

    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * @param centerP center pixl
     * @param Width Length
     * @param Height width
     * @param minWidth min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright Vector right
     * @param Vup vector up
     * @param prePoints pre Points
     * @return Pixel color
     */

    public abstract Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints);
    /**
     * Checks the color of the pixel with the help of Division of the pixel into a network of sub-pixels
     * @param centerP center pixl
     * @param Width Length
     * @param Height width
     * @param minWidth min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright Vector right
     * @param Vup vector up
     * @param prePoints pre Points
     * @return Pixel color
     */
    public abstract Color RegularSuperSampling(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints);


}
