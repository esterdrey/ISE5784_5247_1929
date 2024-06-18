package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {

    private final Color intensity;
    public static AmbientLight NONE=new AmbientLight(Color.BLACK,0);

    public AmbientLight(Color IA, Double3 KA) {
        this.intensity = IA.scale(KA);
    }

    public AmbientLight(Color IA, double KA) {
        this.intensity = IA.scale(KA);
    }

    public Color getIntensity() {
        return intensity;
    }
}
