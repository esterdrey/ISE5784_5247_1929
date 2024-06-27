package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

public class SpotLight extends  PointLight{

   private Vector direction;
    private double narrowBeam = 1;

    public SpotLight(Color intensity,Point position, Vector direction) {
        super(intensity,position);
        this.direction = direction.normalize();

    }

    @Override
    public SpotLight setkL(double kL) {
        return (SpotLight) super.setkL(kL);

    }

    @Override
    public SpotLight setkQ(double kQ) {
        return (SpotLight)super.setkQ(kQ);
    }

    @Override
    public SpotLight setkC(double kC) {
        return (SpotLight)super.setkC(kC);
    }

    @Override
    public Color getIntensity(Point p) {
       return super.getIntensity(p).scale(Math.max(0, direction.dotProduct(getL(p))));
    }

    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }

    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }
}
