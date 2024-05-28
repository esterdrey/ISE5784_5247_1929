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
        Point p0 = ray.getPoint();
        Point o = center;
        Vector u = o.subtract(p0);
        Vector v = ray.getDir();

        double tm = v.dotProduct(u);
        double d = Math.sqrt(u.lengthSquared()-( tm * tm));
        if(d >= radius)
        {
            return null;
        }
        double th = Math.sqrt(radius * radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;

        if(t1 > 0 && t2 > 0)
        {
            Point p1=p0.add(v.scale(t1));
            Point p2=p0.add(v.scale(t2));
            return List.of(p1,p2);
        }
        if(t1 > 0)
        {
            Point p1=p0.add(v.scale(t1));
            return List.of(p1);
        }
        if(t2 > 0)
        {
            Point p2=p0.add(v.scale(t2));
            return List.of(p2);
        }
        return null;
    }
}
