package primitives;

import java.util.List;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Ray {
    /**
     * starting point of the ray
     */
    private final Point point;
    /**
     * direction vector of the ray
     */
    private final Vector dir;

    /**
     * Constructor to initialize Ray based on point and a vector
     *
     * @param p1 starting point of the ray
     * @param v1 direction vector of the ray
     */
    public Ray(Point p1, Vector v1) {
        point = p1;
        dir = v1.normalize();
        p1 = point;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.point.equals(other.point)
                && this.dir.equals(other.dir);
    }

    public Point getPoint() {
        return point;
    }

    public Vector getDir() {
        return dir;
    }

    public Point getPoint(double t) {
        try {
            return point.add(dir.scale(t));
        } catch (Exception e) {
            return point;
        }
    }

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
}
