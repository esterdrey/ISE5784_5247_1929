package geometries;


import primitives.*;

/**
 * class Geometries is a class representing a set of geometric shapes
 * in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public interface Geometry  extends Intersectable
{
    public Vector getNormal(Point point);
}
