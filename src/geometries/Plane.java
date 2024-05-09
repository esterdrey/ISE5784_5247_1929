package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry
{

    private Vector vector;
    private Point point;
    public Plane(Point p0, Point p1, Point p2)
    {
        Vector v0 = p0.subtract(p1);
        Vector v1 = p2.subtract(p1);
        Vector v2 = v0.crossProduct(v1);
        v2.normalize();
        this.vector = v2;
        point=v1;
    }

    public Plane(Point v0, Vector v1)
    {
        vector=v1.normalize();
        point=v0;
    }

    public Vector getNormal()
    {
        return vector.normalize();
    }

    @Override
    public Vector getNormal(Point point) {
        return vector.normalize();
    }
}
