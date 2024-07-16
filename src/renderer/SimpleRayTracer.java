package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.awt.Color.BLACK;
import static primitives.Util.alignZero;


/**
 * class implements rayTracer abstract class
 *
 * @author Ester Drey Avigail Bash
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;


    /**
     * Parameter constructor
     *
     * @param scene The scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * find the closest intersection point between ray and geometries in scene
     *
     * @param ray ray constructed from camera to scene
     * @return closest intersection Point
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        // check if ray constructed through the pixel intersects any of geometries
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);

        // return closest point if list is not empty
        return intersections == null ? null : ray.findClosestGeoPoint(intersections);

    }

    /**
     * calculate the color of a pixel
     *
     * @param geoPoint the {@link GeoPoint} viewed through the pixel to calculate color of
     * @param ray      ray of camera through pixel in view plane where the point is located
     * @return color of the pixel
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }

    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * Calculates the local effects (diffuse and specular) of a given geometric point on a ray.
     *
     * @param gp  The geometric point at which to calculate the local effects.
     * @param ray The ray being traced.
     * @return The color resulting from the local effects.
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {

        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        int nShininess = gp.geometry.getMaterial().nShininess;
        Double3 kd = gp.geometry.getMaterial().KD;
        Double3 ks = gp.geometry.getMaterial().KS;
        Color color = new Color(BLACK).add(gp.geometry.getEmission());
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if ((nl * nv > 0) && !transparency(gp, lightSource, l, n).product(INITIAL_K).lowerThan(MIN_CALC_COLOR_K)) {
                Color intensity = lightSource.getIntensity(gp.point).scale(transparency(gp, lightSource, l, n));
                color = color.add(calcDiffusive(kd, l, n, intensity), calcSpecular(ks, l, n, v, nShininess, intensity));
            }
        }
        return color;


    }


    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {

        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK; // Return no contribution if the combined coefficient is too small

        GeoPoint gp = findClosestIntersection(ray);
        return gp == null ? scene.background.scale(kx) // If no intersection found, return background color
                // Recursively calculate color with scaled coefficient
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }


    private Ray constructReflectedRay(GeoPoint gp, Vector v, Vector n) {
        double nv = n.dotProduct(v);
        if (nv == 0) return null;

        Vector vec = v.subtract(n.scale(2 * nv));
        return new Ray(gp.point, vec, n);
    }

    /**
     * Constructs a refracted ray based on the intersection point and incoming ray.
     * The refraction ray is determined by Snell's law, considering the refractive indices of the materials involved.
     *
     * @param gp The geometric point of intersection.
     * @param v  The direction vector of the incoming ray.
     * @param n  The normal vector at the intersection point.
     * @return The refracted ray originating from the intersection point.
     */
    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v, n);
    }

    /**
     * Calculates the combined global effects (such as reflection and refraction) at a given geometric point using recursive ray tracing.
     *
     * @param gp    The geometric point at which to calculate global effects.
     * @param ray   The view direction vector.
     * @param level The current recursion level for handling transparency or reflection effects.
     * @param k     The accumulated coefficient (e.g., reflection coefficient kR or transparency coefficient kT).
     * @return The calculated color representing combined global effects at the geometric point.
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        return calcGlobalEffect(constructRefractedRay(gp, v, n), material.KT, level, k)
                .add(calcGlobalEffect(constructReflectedRay(gp, v, n), material.KR, level, k));
    }


    /**
     * Calculates the specular reflection component for a given material.
     *
     * @param ks             The specular reflection coefficient of the material.
     * @param l              The direction of the light source.
     * @param n              The surface normal at the point of reflection.
     * @param v              The direction from the point of reflection towards the viewer.
     * @param nShininess     The shininess factor of the material.
     * @param lightIntensity The intensity of the light source.
     * @return The color resulting from the specular reflection.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(2 * (l.dotProduct(n))));
        double vrMinus = Math.max(0, v.scale(-1).dotProduct(r));
        double vrn = Math.pow(vrMinus, nShininess);
        return lightIntensity.scale(ks.scale(vrn));
    }

    /**
     * Calculates the diffuse reflection component for a given material.
     *
     * @param kd        The diffuse reflection coefficient of the material.
     * @param l         The direction of the light source.
     * @param n         The surface normal at the point of reflection.
     * @param intensity The intensity of the light source.
     * @return The color resulting from the diffuse reflection.
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color intensity) {
        double ln = Math.abs(l.dotProduct(n));
        return intensity.scale(kd.scale(ln));
    }

    /**
     * Checks if the ray sent from the camera is damaging in any way, if that means there is a shadow
     *
     * @param geoPoint
     * @param light
     * @param l
     * @param n
     * @param nl
     * @return
     */
    private boolean unshaded(GeoPoint geoPoint, LightSource light, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point point = geoPoint.point.add(epsVector);
        Ray ray = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections != null)
            for (GeoPoint intersection : intersections) {
                double distanceToIntersection = intersection.point.distance(point);
                double distanceToLight = light.getDistance(intersection.point);
                if (distanceToIntersection < distanceToLight && intersection.geometry.getMaterial().KT.equals(Double3.ZERO)) {
                    return false;
                }
            }
        return true;
    }

    /**
     * Calculates the transparency coefficient for a given geometry point, light
     * source, light direction vector, and normal vector.
     *
     * @param geoPoint
     * @param ls
     * @param l
     * @param n
     * @return
     */
    private Double3 transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n) {
        Vector lDir = l.scale(-1);
        Ray lR = new Ray(geoPoint.point, lDir, n);

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lR);
        if (intersections == null)
            return Double3.ONE;

        Double3 ktr = INITIAL_K;
        double distanceToLight = ls.getDistance(geoPoint.point);
        for (GeoPoint intersectionPoint : intersections) {
            if (alignZero(intersectionPoint.point.distance(geoPoint.point) - distanceToLight) <= 0) {
                ktr = ktr.product(intersectionPoint.geometry.getMaterial().KT);
                if (ktr.equals(Double3.ZERO))
                    break;
            }
        }
        return ktr;
    }
}