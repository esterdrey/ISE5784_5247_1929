package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * class Sphere is a class representing a sphere
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author AEster Drey and Avigail Bash
 */
public class Sphere extends RadialGeometry {
    /**
     * center point of the sphere
     */
    private final Point center;

    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     *
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * find the normal of the polygon
     *
     * @param point
     * @return the normal
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    public Point getCenter() {
        return center;
    }

    /**
     * Finds the intersections of a given ray with the geometry of the object.
     *
     * @param ray the ray to find intersections with
     * @return a list of GeoPoint objects representing the intersections, or null if there are no intersections
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        if (ray.getPoint().equals(center)) {
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        }

        // Check if there is intersection between them
        Vector v = center.subtract(ray.getPoint());

        double tm = alignZero(ray.getDirection().dotProduct(v));

        // Check if the ray is tangent to the sphere
        double d = alignZero(Math.sqrt(v.lengthSquared() - tm * tm));
        if (d >= radius) return null;

        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if (t1 > 0 && t2 > 0) {
            return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
        }
        if (t1 > 0) {
            return List.of(new GeoPoint(this, ray.getPoint(t1)));
        }
        if (t2 > 0) {
            return List.of(new GeoPoint(this, ray.getPoint(t2)));
        }
        return null;
    }
}
