package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;


/**
 * A class representing a scene.
 *
 * @author Ester Drey Avigail Bash
 */
public class Scene {

    /**
     * The geometries in the scene
     */
    public Geometries geometries = new Geometries();

    /**
     * The name of the scene
     */
    public String name;

    /**
     * The background color of the scene
     */
    public Color background = new Color(java.awt.Color.black);

    /**
     * The ambient light of the scene
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructor for scene
     *
     * @param sceneName sceneName
     */
    public Scene(String sceneName) {
        name = sceneName;
    }

    /**
     * Updates the geometries in the scene.
     *
     * @param geometries The new geometries.
     * @return This Scene object.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }


    /**
     * Updates the background color of the scene.
     *
     * @param background The new background color.
     * @return This Scene object.
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Updates the ambient light of the scene.
     *
     * @param ambientLight The new list of lights.
     * @return This Scene object.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Set the lights of the lightsource
     *
     * @param lights
     * @return
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    public Color getBackground() {
        return background;
    }

    public List<LightSource> getLights() {
        return lights;
    }
}
