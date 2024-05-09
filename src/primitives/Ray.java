package primitives;

public class Ray
{
    private final Point point;
    private final Vector vector;
    public Ray(Point p1, Vector v1)
    {
        point = p1;
       vector= v1.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.point.equals(other.point)
                && this.vector.equals(other.vector);
    }
}
