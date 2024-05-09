package primitives;

import static primitives.Util.isZero;

public class Point
{
    public static final Point ZERO = new Point(Double3.ZERO);
    protected final Double3 xyz;
    public Point(double x, double y, double z)
    {
        xyz = new Double3(x,y,z);
    }

    public Point(Double3 xyz)
    {
        this.xyz = xyz;
    }

    public Vector subtract(Point p1)
    {
        return new Vector(xyz.subtract(p1.xyz));
    }

    public Point add(Vector v1)
    {
        return new Point(xyz.add(v1.xyz));
    }

    public double distanceSquared(Point p1)
    {
        return ((this.xyz.d1 - p1.xyz.d1) *  (this.xyz.d1-p1.xyz.d1))+ (this.xyz.d2-p1.xyz.d2)* (this.xyz.d2-p1.xyz.d2) + (this.xyz.d3- p1.xyz.d3)*(this.xyz.d3- p1.xyz.d3);
    }

    public double distance(Point p1)
    {
        return Math.sqrt(distanceSquared(p1));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && this.xyz.equals(other.xyz);
    }
}
