package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    public Geometries geometries=new Geometries();
    public String name;
    public Color background=new Color(java.awt.Color.black);
    public AmbientLight ambientLight=AmbientLight.NONE;


    public Scene(String sceneName) {
        name=sceneName;
    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    public Scene setBackground(Color back) {
        this.background = back;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }
}
