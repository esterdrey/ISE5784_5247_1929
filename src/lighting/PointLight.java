package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    private final Point position;
    private double KC =1, KL =0, KQ =0;



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
        return getIntensity().scale(1d / (KC + KL * d + KQ * d * d));
    }

    @Override
    public Vector getL(Point p) {
        if (p.equals(position))
           return null;
        return p.subtract(position).normalize();

    }

    public PointLight setKC(double KC) {
        this.KC = KC;
        return this;
    }

    public PointLight setKQ(double KQ) {
        this.KQ = KQ;
        return this;
    }

    public PointLight setKL(double KL) {
        this.KL = KL;
        return this;
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}
