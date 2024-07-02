package lighting;

import primitives.Color;

public abstract class Light {

    protected final Color intensity;

    public Light(Color intensity) {
        this.intensity = intensity;
    }

    public Color getIntensity() {
        return intensity;
    }
}
