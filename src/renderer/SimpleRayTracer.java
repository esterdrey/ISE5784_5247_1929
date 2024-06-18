package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

public class SimpleRayTracer extends RayTracerBase {


    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        Point point = ray.findClosestPoint(scene.geometries.findIntersections(ray));
        if (point == null)
            return scene.background;
        return calcColor(point);
    }

    /**
     * this function calculates color of a point
     *
     * @param point the point
     * @return the color
     */
    //מחזירה את הצבע של התאורה
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }

}
