package geometries;
import primitives.*;

import java.util.ArrayList;
import java.util.Comparator;
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
        Vector u;
        try {
            u = center.subtract(p0);
        }
        catch (IllegalArgumentException e)
        {
            return List.of(ray.getPoint(radius));
        }
        Vector v = ray.getDir();

        double tm = v.dotProduct(u);
        double d = Math.sqrt(u.lengthSquared()-( tm * tm));
        if(Util.alignZero(d-radius)>=0)
        {
            return null;
        }
        double th = Math.sqrt(radius * radius - d * d);
        double t1 = tm - th;
        double t2 = tm + th;

        if(Util.alignZero(t1)<=0 && Util.alignZero(t2 )<=0)
        {
           return null;
        }
        List<Point> intersections=new ArrayList<>();

        if(Util.alignZero(t1) > 0)
        {
            intersections.add(ray.getPoint(t1));
        }
        if(Util.alignZero(t2) > 0)
        {
            intersections.add(ray.getPoint(t2));
        }
        if (intersections.isEmpty())
        {
            return null;
        }
        intersections.sort(Comparator.comparingDouble(p -> p.distance(ray.getPoint())));

       return List.copyOf(intersections);
    }
}
