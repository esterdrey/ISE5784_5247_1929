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
    private final Vector vector;

    /**
     *  Constructor to initialize Ray based on point and a vector
     * @param p1  starting point of the ray
     * @param v1  direction vector of the ray
     */
    public Ray(Point p1, Vector v1)
    {
        point = p1;
       vector= v1.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.point.equals(other.point)
                && this.vector.equals(other.vector);
    }

    public Point getPoint() {
        return point;
    }

    public Vector getVector() {
        return vector;
    }
}
