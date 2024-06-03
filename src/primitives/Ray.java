package primitives;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Ray
{
    /**
     * starting point of the ray
     */
    private final Point point;
    /**
     * direction vector of the ray
     */
    private final Vector dir;

    /**
     *  Constructor to initialize Ray based on point and a vector
     * @param p1  starting point of the ray
     * @param v1  direction vector of the ray
     */
    public Ray(Point p1, Vector v1)
    {
        point = p1;
       dir = v1.normalize();
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
}
