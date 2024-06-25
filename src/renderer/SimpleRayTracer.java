package renderer;

import geometries.Intersectable;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;


/**
 *  A simple ray tracer implementation.
 *   @author Ester Drey Avigail Bash
 */

public class SimpleRayTracer extends RayTracerBase {


    /**
     * Constructs a new SimpleRayTracer with the specified scene.
     * @param scene  The scene to be traced.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray in the scene and returns the color of the closest intersection point.
     * @param ray
     * @return he color of the closest intersection point, or the background color if no intersections are found.
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Intersectable.GeoPoint> points = scene.geometries.findGeoIntersections(ray);
        if (points == null)
            return scene.background;
        Intersectable.GeoPoint closestPoint = ray.findClosestGeoPoint(points);
        return calcColor(closestPoint, ray);
    }

    /**
     * this function calculates color of a point
     *
     * @param point the point
     * @return the color
     */
    private Color calcColor(Intersectable.GeoPoint point, Ray ray) {
        return scene.ambientLight.getIntensity().add(point.geometry.getEmission());
    }

}
