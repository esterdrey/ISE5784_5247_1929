package geometries;
import primitives.*;

public class Sphere extends RadialGeometry
{
    private final Point center;

    public Sphere (Point center, double radius)
    {
        super(radius);
        this.center = center;
    }
    @Override
    public Vector getNormal(Point point)
    {
        Vector v1 = center.subtract(point);
        v1.normalize();
        return v1;
    }
}
