package primitives;

public class Vector extends Point
{
    public Vector(double x, double y,double z)
    {
        super(x,y,z);
        if(xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero vector is illegal");

    }


    public Vector(Double3 double3)
    {
        super(double3);
        if(double3.equals(Double3.ZERO))
            throw new IllegalArgumentException("Zero vector is illegal");
    }

public  Vector scale(double d1)
{
    return new Vector(this.xyz.scale(d1));
}

public Vector add(Vector v1)
{
       return new Vector(this.xyz.add(v1.xyz));
}
public double dotProduct(Vector v1)
{
    return this.xyz.d1 * v1.xyz.d1 + this.xyz.d2 * v1.xyz.d2 + this.xyz.d3 * v1.xyz.d3;
}

public Vector crossProduct(Vector v1)
{
   return new Vector((this.xyz.d2 * v1.xyz.d3)-(this.xyz.d3 * v1.xyz.d2),(this.xyz.d3 * v1.xyz.d1)-(this.xyz.d1 * v1.xyz.d3),(this.xyz.d1 * v1.xyz.d2)-(this.xyz.d2 * v1.xyz.d1));
}

public double lengthSquared()
{
    return dotProduct(this);
}

public double length()
{
    return Math.sqrt(lengthSquared());
}

public Vector normalize()
{
    double length = this.length();
    return new Vector(this.xyz.d1/length,this.xyz.d2/length,this.xyz.d3/length);
}

@Override
public boolean equals(Object obj)
{
       if(this == obj)
           return true;
       if(!super.equals(obj))
           return false;
       return (obj instanceof Point other)
               && this.xyz.equals(other.xyz);
}

}
