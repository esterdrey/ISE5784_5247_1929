package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;


/**
 * A class representing a point light source. Inherits from Light and implements
 * LightSource.
 */
public class PointLight extends Light implements LightSource {

    private final Point position;
    private double KC = 1, KL = 0, KQ = 0;


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
}
