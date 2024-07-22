package lighting;

import geometries.Plane;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;


/**
 * A class representing a point light source. Inherits from Light and implements
 * LightSource.
 */
public class PointLight extends Light implements LightSource {

    private final Point position;
    private double KC = 1, KL = 0, KQ = 0;


    //*****Parameter for Soft shadow
    /**
     * The size of the light
     */
    private double size = 0;
    /**
     * The array of points
     */
    protected Point[] points;
    /**
     * A random number
     */
    private final Random rand = new Random();


    /**
     * Constructs a point light with the given intensity and position.
     *
     * @param intensity The intensity (color) of the light source.
     * @param position  The position of the light source.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }


    /**
     * A method to retrieve the intensity color.
     *
     * @param p The point in the scene.
     * @return the intensity color
     */
    @Override
    public Color getIntensity(Point p) {

        double d = getDistance(p);
        return getIntensity().scale(1d / (KC + KL * d + KQ * d * d));
    }



    /**
     * Sets the size of the array of points
     * If the given number is a positive number, this will activate SoftShadowing
     *
     * @param size Size of the array
     * @return the Point light with array of point
     */
    public PointLight setSize(double size) {
        this.size = size;
        return this;
    }

    /**
     * Retrieves the vector from the specified point.
     *
     * @param p The point in the scene.
     * @return the vector retrieved from the specified point
     */
    @Override
    public Vector getL(Point p) {
        if (p.equals(position))
            return null;
        return p.subtract(position).normalize();

    }


    /**
     * Sets the constant attenuation factor for the point light.
     *
     * @param KC the constant attenuation factor to set
     * @return the updated PointLight object
     */
    public PointLight setKC(double KC) {
        this.KC = KC;
        return this;
    }


    /**
     * Set the quadratic attenuation factor for the point light.
     *
     * @param KQ the new quadratic attenuation factor
     * @return the updated PointLight object
     */
    public PointLight setKQ(double KQ) {
        this.KQ = KQ;
        return this;
    }


    /**
     * Set the attenuation factor for light intensity.
     *
     * @param KL the attenuation factor to set
     * @return the updated PointLight object
     */
    public PointLight setKL(double KL) {
        this.KL = KL;
        return this;
    }


    /**
     * @param point The point to which the distance is calculated
     * @return the distance
     */
    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

    /**
     * Get the array of points that will cast shadow rays
     *
     * @return The array of point
     */
    public Point[] getPoints(Point p, int numOfPoints) {
        if (size == 0) return null;
        if (this.points != null)
            return this.points;
        Point[] points = new Point[numOfPoints];
        Vector to = p.subtract(position).normalize();
        Vector vX = to.getOrthogonal().normalize();
        Vector vY = vX.crossProduct(to).normalize();
        double x, y, radius;
        for (int i = 0; i < numOfPoints; i += 4) {
            radius = rand.nextDouble(size) + 0.1;
            x = rand.nextDouble(radius) + 0.1;
            y = radius * radius - x * x;//getCircleScale(x, radius);
            for (int j = 0; j < 4; j++) {
                //in this part we mirror the point we got 4 times, to each quarter of the grid
                points[i + j] = position.add(vX.scale(j % 2 == 0 ? x : -x)).add(vY.scale((j <= 1 ? -y : y)));
            }
        }
        this.points = points;
        return points;
    }

}
