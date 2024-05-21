package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * class Cylinder is a class representing a cylinder
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */

public class Cylinder extends Tube
{

    /**
     * height of the tube
     */
    private final double height;

    /**
     * Constructor to initialize Cylinder based on given axis ray, radius, and height
     * @param radius
     * @param axis
     * @param height
     */
    public Cylinder(double radius, Ray axis,double height)
    {
        super(radius, axis);
        this.height = height;
    }

    /**
     *
     * @param p
     * @return
     */
    @Override
    public Vector getNormal(Point p)
    {
       return null;
    }
}
