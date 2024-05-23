package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

public class Geometries implements Intersectable {
    List<Intersectable> geometries=new LinkedList<Intersectable>();


    public Geometries() {
    }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    private void add(Intersectable... geometries)
    {
        Collections.addAll(this.geometries, geometries);
    }

    @Override
    public List<Point> findIntersections(Ray ray)
    {

        return null;
    }
}
