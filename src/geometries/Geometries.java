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
        List<Point> intersactions=null;
        for(Intersectable geo : geometries)
        {
            List<Point> geoPoints=geo.findIntersections(ray);
            if(geoPoints!=null)
            {
                if(intersactions==null)
                    intersactions=new LinkedList<>();
                intersactions.addAll(geoPoints);
            }
        }
        return intersactions;
    }
}
