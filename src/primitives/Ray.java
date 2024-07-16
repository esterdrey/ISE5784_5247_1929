package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Ray {
    public static final double DELTA = 0.00001;
    /**
     * starting point of the ray
     */
    private final Point point;
    /**
     * direction vector of the ray
     */
    private final Vector direction;


    /**
     * Constructor to initialize Ray based on point and a vector
     *
     * @param p1 starting point of the ray
     * @param v1 direction vector of the ray
     */
    public Ray(Point p1, Vector v1) {
        point = p1;
        direction = v1.normalize();
        p1 = point;
    }


    /**
     * New constructor for ray that also receives a normal vector. It then moves the
     * ray's origin a short distance in the normal's direction.
     *
     * @param p0     the original point
     * @param dir    the direction vector
     * @param normal the normal along which to move the origin point
     */
    public Ray(Point p0, Vector dir, Vector normal) {
        double res = dir.dotProduct(normal);
        this.point = isZero(res) ? p0 : res > 0 ? p0.add(normal.scale(0.00001)) : p0.add(normal.scale(-0.00001));
        this.direction = dir.normalize();
    }

    /**
     * Override equals method
     *
     * @param obj
     * @return object
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.point.equals(other.point)
                && this.direction.equals(other.direction);
    }

    /**
     * get point of the ray
     *
     * @return the point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * return the direction
     *
     * @return vector
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * getter
     *
     * @param t
     * @return point
     */
    public Point getPoint(double t) {
        if (t == 0) return point;
        return point.add(direction.scale(t));
    }

    /**
     * find the closest point to ray's head
     *
     * @param list
     * @return the closet point
     */
    public Point findClosestPoint(List<Point> list) {
        if (list == null)
            return null;

        double distance = Double.POSITIVE_INFINITY;
        Point closest = null;
        for (Point p : list)    //find the closest point
            if (p.distance(point) < distance) {
                distance = p.distance(point);
                closest = p;
            }
        return closest;
    }


    /**
     * Finds the closest GeoPoint to the start of the ray from a collection of
     * GeoPoints.
     *
     * @param intersections The collection of GeoPoints.
     * @return The closest GeoPoint to the start of the ray.
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> intersections) {
        // Initialize variables to store the closest GeoPoint and its distance
        GeoPoint closestGeoPoint = null;
        double closestDistance = Double.POSITIVE_INFINITY;

        // Iterate through the list of GeoPoints
        for (GeoPoint geoPoint : intersections) {
            // Calculate the distance between the origin of the ray and the current GeoPoint
            double distance = point.distance(geoPoint.point);

            // Check if the current GeoPoint is closer than the previous closest GeoPoint
            if (distance < closestDistance) {
                closestGeoPoint = geoPoint;
                closestDistance = distance;
            }
        }

        // Return the closest GeoPoint
        return closestGeoPoint;
    }
}
