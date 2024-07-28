package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
     * The max number of points that are in the array (The grid needs to be large enough to render a sharp shadow)
     */
    private int numberOfPoints = 80;


    /**
     * Parameter constructor
     *
     * @param scene The scene
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }


    /**
     *
     * @param ray The ray to be traced.
     * @return the color
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background : calcColor(closestPoint, ray);
    }

    /**
     * Trace the list of ray and calculates the color of the point that interact with the geometries of the scene
     *
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color traceRays(List<Ray> rays) {
        Color color = new Color(BLACK);
        //over all the rays
        for (Ray ray : rays) {
            GeoPoint clossestGeoPoint = findClosestIntersection(ray);
            //if there is no closest intersection point with the shape
            if (clossestGeoPoint == null)
                color = color.add(scene.getBackground());
                //if there is intersection point
            else color = color.add(calcColor(clossestGeoPoint, ray));
        }
        return color.reduce(rays.size());
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
     * @param ray ray of camera through pixel in view plane where the point is located
     * @return color of the pixel
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }



    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray,INITIAL_K);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * Calculates the local effects (diffuse and specular) of a given geometric point on a ray.
     *
     * @param gp  The geometric point at which to calculate the local effects.
     * @param ray The ray being traced.
     * @return The color resulting from the local effects.
     */

    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;

        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point).normalize();
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Double3 ktr = hitPercentageColor(gp, lightSource, n, l);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color Li = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(
                            Li.scale(calcDiffusive(material, nl)),
                            Li.scale(calcSpecular(material, n, l, nl, v))
                    );
                }
            }
        }
        return color;
    }


    /**
     * Calculates the global effect of a ray (reflection or refraction) by recursively tracing the ray
     * and combining the colors according to the given coefficients.
     *
     * @param ray the ray to trace
     * @param kx the coefficient for the current effect (reflection or refraction)
     * @param level the recursion level
     * @param k the accumulated coefficient from previous levels
     * @return the color contribution from the global effect
     */

    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {

        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK; // Return no contribution if the combined coefficient is too small

        GeoPoint gp = findClosestIntersection(ray);
        return gp == null ? scene.background.scale(kx) // If no intersection found, return background color
                // Recursively calculate color with scaled coefficient
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     * Constructs the reflected ray based on the incident ray, normal vector, and intersection point
     * @param gp the geometry point of intersection
     * @param v the direction vector of the incident ray
     * @param n the normal vector at the intersection point
     * @return the constructed reflected ray or null if the dot product of n and v is zero
     */
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
     * Calculate the specular factor
     *
     * @param mat the material of the geometry
     * @param n   Vector n
     * @param l   vector l
     * @param nl  dotProduct of n and l
     * @param v   Vector v
     * @return the specular factor
     */
    private Double3 calcSpecular(Material mat, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(nl * 2));
        double vr = alignZero(-v.dotProduct(r));
        if (vr <= 0) return Double3.ZERO;
        return mat.getKS().scale(Math.pow(vr, mat.nShininess));
    }



    /**
     * Calculate the Diffusive factor
     *
     * @param m  the material
     * @param nl the dot product between vector n and l
     * @return the diffusive factor
     */
    private Double3 calcDiffusive(Material m, double nl) {
        return m.getKD().scale(nl > 0 ? nl : -nl);
    }

    /**
     * Checks if the ray sent from the camera is damaging in any way, if that means there is a shadow
     *
     * @param geoPoint the geometry point being checked
     * @param light the light source
     * @param l vector from the geometry point towards the light source
     * @param n the normal vector at the geometry point
     * @param nl dot product of the normal vector and the direction vector
     * @return true if there is no shadow, false otherwise
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
     * @param geoPoint the geometry point being checked
     * @param ls the light source
     * @param l the direction vector from the geometry point towards the light source
     * @param n the normal vector at the geometry point
     * @return the transparency coefficient (Double3)
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

    /**
     * Calculates the percentage of rays that hit the object,
     * from all the rays that were created by the points of hte light source.
     *
     * @param ls       the light source.
     * @param geoPoint the intersection point.
     * @return the percentage of rays that are heat by some object.
     */
    private Double3 hitPercentageColor( GeoPoint geoPoint,LightSource ls, Vector n, Vector l) {
        Double3 average = Double3.ZERO;
        Point[] points = ls.getPoints(geoPoint.point, numberOfPoints);
        if (points == null)
            return transparency(geoPoint, ls, l, n);
        for (Point point : points) {
            average = average.add(transparency(geoPoint,ls, geoPoint.point.subtract(point).normalize(), n ).reduce(points.length));
        }
        return average;
    }


    public SimpleRayTracer setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
        return this;
    }

    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * (credit to Rivki&Efrat)
     * @param centerP   center pixel
     * @param Width     Length
     * @param Height    width
     * @param minWidth  min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright    Vector right
     * @param Vup       vector up
     * @param prePoints pre Points
     * @return Pixel color
     */
    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight,
                                          Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {

        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc)));
        }

        //initialize list of following subpixel center points
        List<Point> nextCenterPList = new LinkedList<>();
        //initialize list of corners points
        List<Point> cornersList = new LinkedList<>();
        //initialize list of colors
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;

        //over about all four corners
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                //calculate the corner place
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                //add this corner to list of corners
                cornersList.add(tempCorner);
                //if this point is empty or does not exist
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    //add the corner point and its color
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));
                    colorList.add(traceRay(tempRay));
                }
            }
        }

        //if not all colors are equal
        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }

        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        //over all the point in colorList
        for (primitives.Color color : colorList) {
            //if all the colors almost equals
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        //if all the colors does not equals ant there are some color
        if (isAllEquals && colorList.size() > 1)
            return tempColor;


        tempColor = primitives.Color.BLACK;
        //over about all the corner points
        for (Point center : nextCenterPList) {
            //recursive call to AdaptiveSuperSamplingRec
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width / 2, Height / 2,
                    minWidth, minHeight, cameraLoc, Vright, Vup, cornersList));
        }
        //return the average color
        return tempColor.reduce(nextCenterPList.size());
    }

    public Color RegularSuperSampling(Point centerP, double Width, double Height, double minWidth, double minHeight,
                                      Point cameraLoc, Vector Right, Vector Vup, List<Point> prePoints) {
        //initialize list of colors
        List<Color> colorList = new ArrayList<>();

        //calculate num of sub pixels
        int numSubPixelsX = (int) Math.ceil(Width / minWidth);
        int numSubPixelsY = (int) Math.ceil(Height / minHeight);

        //initialize random number
        Random random = new Random();

        //over all the sub pixels
        for (int i = 0; i < numSubPixelsY; i++) {
            for (int j = 0; j < numSubPixelsX; j++) {
                //calculate the coordinates of the place of this subpixel
                double offsetX = minWidth * j;
                double offsetY = minHeight * i;

                //calculate a random point in this subpixel
                double randomX = offsetX + random.nextDouble() * minWidth;
                double randomY = offsetY + random.nextDouble() * minHeight;

                //calculate the place of this subpixel
                Point subPixelPoint = centerP.add(Right.scale(randomX - Width / 2)).add(Vup.scale(randomY - Height / 2));

                //if this point is empty or does not exist
                if (prePoints == null || !isInList(prePoints, subPixelPoint)) {
                    //build ray from  camera to subpixel
                    Ray ray = new Ray(cameraLoc, subPixelPoint.subtract(cameraLoc));
                    colorList.add(traceRay(ray));
                }
            }
        }

        //if no colors are received
        if (colorList.isEmpty()) {
            //initialize black color
            return primitives.Color.BLACK;
        }

        Color averageColor = Color.BLACK;
        //over about all the colorList
        for (Color color : colorList) {
            //calculate the average color
            averageColor = averageColor.add(color);
        }
        //return the average color
        return averageColor.reduce(colorList.size());
    }

    /**
     * Find a point in the list
     *
     * @param pointsList the list
     * @param point      the point that we look for
     * @return
     */
    private boolean isInList(List<Point> pointsList, Point point) {
        //over about all the list of the points
        for (Point tempPoint : pointsList) {
            //if point == desired point
            if (point.equals(tempPoint))
                return true;
        }
        return false;
    }
}