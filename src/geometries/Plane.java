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
       Point p=this.point;
       Vector v=this.vector;
       Vector dir=ray.getDir();
       Point p0=ray.getPoint();

       if(p.equals(p0))
           return null;
       double v_dir=v.dotProduct(dir);
       if(isZero(v_dir))
           return null;
       double dir_p_p0=alignZero(v.dotProduct(p.subtract(p0)));
       if(isZero(dir_p_p0))
           return null;
       double t=(dir_p_p0)/v_dir;
       Point p1=ray.getPoint();
       if ((t<0))
           return null;
       return List.of(p1);

       }

}
