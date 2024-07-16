package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * class Tube is a class representing a tube
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Tube extends RadialGeometry {

    /**
     * axis ray of the tube
     */
    protected final Ray axis;

    /**
     * Constructor to initialize Tube based on an axis ray and the radius of the tube
     * *
     *
     * @param radius radius of the tube
     * @param axis   axis ray of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }


    /**
     * find the normal
     *
     * @param point
     * @return the normal
     */
    @Override
    public Vector getNormal(Point point) {
        Vector v = axis.getDirection();//get the direction vector
        Point p0 = axis.getPoint();//get the head point of the cylinder's ray
        double d = v.dotProduct(point.subtract(p0));//calculate the projection of the point on tube's ray
        if (d == 0) {
            Vector normal = point.subtract(p0);
            return normal.normalize();
        }
        //the point is on the body of the tube
        Point O = p0.add(v.scale(d));//calculate the "new" center of the tube that is in front of the given point
        Vector normal = point.subtract(O);//calculate the normal
        return normal.normalize();
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        return List.of();
    }
}
