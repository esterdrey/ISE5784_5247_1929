package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.awt.Color.BLACK;


/**
 * class implements rayTracer abstract class
 *
 * @author Ester Drey Avigail Bash
 */
public class SimpleRayTracer extends RayTracerBase {

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

        // find the closest intersection point
        GeoPoint closestPoint = findClosestIntersection(ray);
        // if no intersection point was found , return basic background color of scene
        if (closestPoint == null)
            return scene.background;

        // intersection was found, calculate color of the of pixel.
        return calcColor(closestPoint,ray);
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
     * @param gp  the {@link GeoPoint} viewed through the pixel to calculate color of
     * @param ray ray of camera through pixel in view plane where the point is located
     * @return color of the pixel
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return scene.ambientLight.getIntensity().add(calcLocalEffects(gp, ray));
    }

    /**
     Calculates the local effects (diffuse and specular) of a given geometric point on a ray.
     @param gp The geometric point at which to calculate the local effects.
     @param ray The ray being traced.
     @return The color resulting from the local effects.
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = Util.alignZero(n.dotProduct(v));
        int nShininess = gp.geometry.getMaterial().nShininess;
        Double3 kd = gp.geometry.getMaterial().kD;
        Double3 ks = gp.geometry.getMaterial().kS;
        Color color = new Color(BLACK).add(gp.geometry.getEmission());//new Color(BLACK);
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = Util.alignZero(n.dotProduct(l));
            if(nl*nv > 0 ) {
                Color intensity = lightSource.getIntensity(gp.point);
                color = color.add(calcDiffusive(kd,l,n,intensity), calcSpecular(ks,l,n,v,nShininess,intensity));
            }
        }
        return color;
    }


    /**
     Calculates the specular reflection component for a given material.
     @param ks The specular reflection coefficient of the material.
     @param l The direction of the light source.
     @param n The surface normal at the point of reflection.
     @param v The direction from the point of reflection towards the viewer.
     @param nShininess The shininess factor of the material.
     @param lightIntensity The intensity of the light source.
     @return The color resulting from the specular reflection.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity){
        Vector r = l.subtract(n.scale(2*(l.dotProduct(n))));
        double vrMinus = Math.max(0, v.scale(-1).dotProduct(r));
        double vrn =Math.pow(vrMinus,nShininess);
        return lightIntensity.scale(ks.scale(vrn));
    }

    /**
     Calculates the diffuse reflection component for a given material.
     @param kd The diffuse reflection coefficient of the material.
     @param l The direction of the light source.
     @param n The surface normal at the point of reflection.
     @param intensity The intensity of the light source.
     @return The color resulting from the diffuse reflection.
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color intensity){
        double ln = Math.abs(l.dotProduct(n));
        return intensity.scale(kd.scale(ln));
    }
}