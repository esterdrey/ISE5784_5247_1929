package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * A class representing Ambient Light.
 *
 * @author Ester Drey and Avigail Bash
 */

public class AmbientLight extends Light {


    /**
     * black ambient light (no light)
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     * constructor for the intensity
     *
     * @param IA original intensity of the lighting
     * @param KA attenuation coefficient of the lighting
     */
    public AmbientLight(Color IA, Double3 KA) {
        super(IA.scale(KA));
    }


    /**
     * constructor for the intensity
     *
     * @param IA original intensity of the lighting
     * @param KA attenuation coefficient of the lighting
     */
    public AmbientLight(Color IA, double KA) {
        super(IA.scale(KA));
    }


}
