package geometries;


import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * class Geometries is a class representing a set of geometric shapes
 * in Cartesian 3-Dimensional coordinate system.
 *
 * @author Ester Drey and Avigail Bash
 */
public abstract class Geometry extends Intersectable {
    /**
     * Field representing the emission color of the geometry.
     */
    protected Color emission = Color.BLACK;

    private Material material = new Material();


    /**
     * Field representing the material of the geometry.
     */
    public abstract Vector getNormal(Point point);


    /**
     * Method that returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }


    /**
     * Method to update the emission color of the geometry.
     *
     * @param emission the new emission color to set
     * @return the updated Geometry object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }


    /**
     * Get the material of this object.
     *
     * @return the material of this object
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return the updated geometry with the new material
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
