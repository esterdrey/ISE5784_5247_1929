package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;


/**
 * SpotLight class represents a light source in the scene.
 */
public class SpotLight extends PointLight {

    private final Vector direction;
    private double narrowBeam = 1;


    /**
     * Constructor of the class
     *
     * @param intensity the intensity color
     * @param position  the position
     * @param direction the direction
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();

    }

    /**
     * Set the attenuation factor for light intensity.
     *
     * @param KL the attenuation factor to set
     * @return
     */
    @Override
    public SpotLight setKL(double KL) {
        return (SpotLight) super.setKL(KL);

    }

    /**
     * Set the quadratic attenuation factor for the point light.
     *
     * @param KQ the new quadratic attenuation factor
     * @return
     */
    @Override
    public SpotLight setKQ(double KQ) {
        return (SpotLight) super.setKQ(KQ);
    }

    /**
     * Sets the constant attenuation factor for the point light.
     *
     * @param KC the constant attenuation factor to set
     * @return
     */
    @Override
    public SpotLight setKC(double KC) {
        return (SpotLight) super.setKC(KC);
    }

    /**
     * Retrieves the vector from the specified point.
     *
     * @param p The point in the scene.
     * @return
     */
    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }


    /**
     * A method to retrieve the intensity color.
     *
     * @param p the point at which to calculate the intensity
     * @return the intensity color
     */
    @Override
    public Color getIntensity(Point p) {
        double dotProduct = alignZero(direction.dotProduct(getL(p)));
        return dotProduct <= 0 ? Color.BLACK : super.getIntensity(p).scale(Math.pow(dotProduct, narrowBeam));

    }


    /**
     * Set the narrow beam value.
     *
     * @param narrowBeam the value to set for the narrow beam
     * @return the updated LightSource object
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }


    @Override
    public Point[] getPoints(Point p, int numOfPoints) {
        return super.getPoints(p.add(direction), numOfPoints);
    }


}
