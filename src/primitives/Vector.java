package primitives;

/**
 * Class Vector is the basic class representing a vector of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail bash
 */
public class Vector extends Point {
    /**
     * Constructor to initialize Vector based on 3 number values
     *
     * @param x number value for x coordinate
     * @param y number value for y coordinate
     * @param z number value for z coordinate
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero vector is illegal");

    }

    /**
     * Constructor to initialize Vector based on 3 double numbers (Double3) value
     *
     * @param xyz number value for all 3 numbers
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero vector is illegal");
    }

    /**
     * cales a vector into a new vector where each coordinate is multiplied by the scale factor
     *
     * @param d1
     * @return
     */
    public Vector scale(double d1) {
        return new Vector(this.xyz.scale(d1));
    }

    /**
     * Sums two vectors into a new vector where each coordinate is summarized
     *
     * @param v1 right handle side operand for addition
     * @return vector result of addition
     */
    public Vector add(Vector v1) {
        return new Vector(this.xyz.add(v1.xyz));
    }

    /**
     * calculates the dot product of two vectors
     *
     * @param v1 right handle side operand for dot product calculation
     * @return result of dot product
     */
    public double dotProduct(Vector v1) {
        return this.xyz.d1 * v1.xyz.d1 + this.xyz.d2 * v1.xyz.d2 + this.xyz.d3 * v1.xyz.d3;
    }

    /**
     * calculates the cross product of two vectors
     *
     * @param v1 vector right handle side operand for cross product calculation
     * @return result of cross product
     */
    public Vector crossProduct(Vector v1) {
        return new Vector(
                (this.xyz.d2 * v1.xyz.d3) - (this.xyz.d3 * v1.xyz.d2),
                (this.xyz.d3 * v1.xyz.d1) - (this.xyz.d1 * v1.xyz.d3),
                (this.xyz.d1 * v1.xyz.d2) - (this.xyz.d2 * v1.xyz.d1)
        );
    }

    /**
     * calculates length of the vector squared
     *
     * @return length of vector squared
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    /**
     * length of the vector
     *
     * @return length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * normalizes the vector
     *
     * @return a new normalized vector
     */
    public Vector normalize() {
        double length = this.length();
        return new Vector(this.xyz.d1 / length, this.xyz.d2 / length, this.xyz.d3 / length);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }

}
