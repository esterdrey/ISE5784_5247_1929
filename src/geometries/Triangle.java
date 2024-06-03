package geometries;
import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * class Triangle is a basic class representing a triangle
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Triangle  extends Polygon
{

    public Triangle(Point p1, Point p2, Point p3)
    {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray)
    {
        if(this.plane.findIntersections(ray)==null)
            return null;
        Vector v1 = this.vertices.get(0).subtract(ray.getPoint());
        Vector v2 =this.vertices.get(1).subtract(ray.getPoint());
        Vector v3 = this.vertices.get(2).subtract(ray.getPoint());

        Vector n1 = (v1.crossProduct(v2)).normalize();
        Vector n2 = (v2.crossProduct(v3)).normalize();
        Vector n3 = (v3.crossProduct(v1)).normalize();

        double t1=n1.dotProduct(ray.getDir());
        double t2=n2.dotProduct(ray.getDir());
        double t3=n3.dotProduct(ray.getDir());

        if((t1 > 0 && t2 > 0 && t3 > 0) ||(t1 < 0 && t2 < 0 && t3 < 0))
        {
            return this.plane.findIntersections(ray);
        }
        return null;
    }
}
