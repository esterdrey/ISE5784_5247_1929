package geometries;

import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The department implements operations for several geometric bodies
 *
 * @author Ester Drey Avigail Bash
 */

public class Geometries extends Intersectable {


    private List<Intersectable> geometries = new LinkedList<>();

    /**
     * a constructor
     */
    public Geometries() {
    }

    /**
     * a constructor that receives a list of geometric objects and adds them to the list
     *
     * @param geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }


    /**
     * add object to list
     *
     * @param geometries objects to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }


    /**
     * A function that receives a foundation and returns all bodies that the foundation
     *
     * @param ray
     * @return
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersactions = null;
        for (Intersectable geo : geometries) {
            List<GeoPoint> geoPoints = geo.findGeoIntersectionsHelper(ray);
            if (geoPoints != null) {
                if (intersactions == null)
                    intersactions = new LinkedList<>();
                intersactions.addAll(geoPoints);
            }
        }
        return intersactions;
    }
}
