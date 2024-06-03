package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * class Plane is a class representing a plane
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Plane implements Geometry
{
    /**
     * normal vector to the plane
     */
    private Vector vector;
    /**
     * point in plane
     */
    private Point point;

    /**
     * Constructor to initialize Plane based on three points in plane
     * @param p0 first point in plane
     * @param p1 second point in plane
     * @param p2 third point in plane
     */
    public Plane(Point p0, Point p1, Point p2)
    {
        Vector v0 = p0.subtract(p1);
        Vector v1 = p2.subtract(p1);
        Vector v2 = v0.crossProduct(v1);
        v2.normalize();
        this.vector = v2;
        point = p1;
    }

    /**
     *  Constructor to initialize Plane based on a normal vector and point in plane
     * @param v0  point in plane
     * @param v1  normal vector to plane
     */
    public Plane(Point v0, Vector v1)
    {
        vector=v1.normalize();
        point=v0;
    }

    /**
     *  getter to normal vector to plane normal
     * @return
     */
    public Vector getNormal()
    {
        return vector.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return vector.normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        double numerator=vector.dotProduct(point.subtract(ray.getPoint()));
        double denumerator=vector.dotProduct(ray.getDir());

        if(denumerator==0)
        {
            return null;
        }

        double d=numerator/denumerator;

        if(d<=0)
        {
            return null;
        }

       Point p=ray.getPoint(d);
        return List.of(p);

       }

}
