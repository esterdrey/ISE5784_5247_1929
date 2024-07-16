package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * A class representing a directional light source. Inherits from Light and
 * implements LightSource.
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * Represents a direction vector in three-dimensional space. This vector can be
     * used to denote the direction of light, movement, or any other directional
     * quantity.
     */
    private final Vector direction;


    /**
     * Constructs a directional light with the given intensity and direction.
     *
     * @param intensity The intensity (color) of the light source.
     * @param direction The direction vector of the light source.
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }


    @Override
    public Color getIntensity(Point p) {
        return getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
