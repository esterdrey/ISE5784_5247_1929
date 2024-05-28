package geometries;
import primitives.*;

import java.util.List;

/**
 * class Sphere is a class representing a sphere
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author AEster Drey and Avigail Bash
 */
public class Sphere extends RadialGeometry
{
    /**
     * center point of the sphere
     */
    private final Point center;

    /**
     * Constructor to initialize Sphere based on a center point and a radius of the sphere
     * @param center center center of the sphere
     * @param radius  radius radius of the sphere
     */
    public Sphere (Point center, double radius)
    {
        super(radius);
        this.center = center;
    }


    @Override
    public Vector getNormal(Point point)
    {
        Vector v1 = point.subtract(center);
        v1.normalize();
        return v1;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
