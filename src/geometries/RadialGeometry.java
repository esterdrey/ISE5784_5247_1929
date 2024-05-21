package geometries;

/**
 * class radialGeometry is a class representing a geometry
 * with a radius of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public abstract class RadialGeometry implements Geometry
{
    /**
     * the geometry radius
     */
    protected double radius;

    /**
     * Constructor to initialize radialGeometry based on a radius
     * @param radius
     */
    public RadialGeometry(double radius)
    {
        this.radius = radius;
    }


}
