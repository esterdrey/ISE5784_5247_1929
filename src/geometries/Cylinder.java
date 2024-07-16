package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * class Cylinder is a class representing a cylinder
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */

public class Cylinder extends Tube {

    /**
     * height of the tube
     */
    private final double height;

    /**
     * Constructor to initialize Cylinder based on given axis ray, radius, and height
     *
     * @param radius
     * @param axis
     * @param height
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    /**
     * @param p the point
     * @return the normal of the cylinder
     */
    @Override
    public Vector getNormal(Point p) {
        // Check that surface point is different from head of axisRay to avoid creating
        // a zero vector
        Vector dir = axis.getDirection();
        Point p0 = axis.getPoint();
        if (p.equals(p0))
            return dir.scale(-1);
        // Finding the nearest point to the given point that is on the axis ray
        double t = dir.dotProduct(p.subtract(p0));
        // Finds out if surface point is on a base and returns a normal appropriately
        if (isZero(t))
            return dir.scale(-1);
        if (isZero(t - height))
            return dir;
        // If surface point is on the side of the cylinder, the superclass (Tube) is
        // used to find the normal
        return super.getNormal(p);
    }
}
