package primitives;

/**
 * Class Point is the basic class representing a point of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Point {
    /**
     * Represents a constant representing the zero point.
     */
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * 3-dimensional coordinates
     */
    protected final Double3 xyz;

    /**
     * Constructor to initialize Point based object with 3 number values
     *
     * @param x first number value
     * @param y second number value
     * @param z third number value
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructor to initialize Point based object with one 3 double numbers (Double3) value
     *
     * @param xyz 3 double numbers (Double3) value
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * subtracts two points into a new vector from the second
     * point (right handle side) to the first one
     *
     * @param p1
     * @return
     */
    public Vector subtract(Point p1) {
        return new Vector(xyz.subtract(p1.xyz));
    }

    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    /**
     * calculates the distance between two points - squared
     *
     * @param p1 right handle side operand for distance squared calculation
     * @return distance between points - squared
     */
    public double distanceSquared(Point p1) {
        return ((this.xyz.d1 - p1.xyz.d1) * (this.xyz.d1 - p1.xyz.d1)) +
                (this.xyz.d2 - p1.xyz.d2) * (this.xyz.d2 - p1.xyz.d2) +
                (this.xyz.d3 - p1.xyz.d3) * (this.xyz.d3 - p1.xyz.d3);
    }

    /**
     * calculates the distance between two points
     *
     * @param p1 right handle side operand for distance calculation
     * @return distance between points
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    public Double3 getXyz() {
        return xyz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }
}
