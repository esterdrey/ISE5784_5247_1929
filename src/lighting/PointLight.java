package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    private final Point position;
    private double kC=1,kL=0,kQ=0;



    public PointLight(Color intensity,Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * get the intensity of point light
     */
    @Override
    public Color getIntensity(Point p) {

        double d = getDistance(p);
        return getIntensity().scale(1d / (kC + kL * d + kQ * d * d));
    }

    @Override
    public Vector getL(Point p) {
        if (p.equals(position))
           return null;
        return p.subtract(position).normalize();

    }

    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}
