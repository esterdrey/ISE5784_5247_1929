package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;


/**
 * Abstract class Intersectable represents a geometry object that can be
 * intersected by a ray.
 *
 * @author Ester Drey and Avigail Bash
 */

public abstract class Intersectable {
    /**
     * Finds intersection points between the intersectable object and a given ray.
     *
     * @param ray
     * @return A list of intersection points between the object and the ray. If no
     * intersections are found, an empty list is returned.
     */
    public List<Point> findIntersections(Ray ray) {
        List<GeoPoint> geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(geoPoint -> geoPoint.point).toList();

    }


    /**
     * Public method findGeoIntersections for finding GeoPoints of intersections
     * between the intersectable object and a given ray.
     *
     * @param ray
     * @return A list of GeoPoints representing intersection points between the object and the ray.
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Protected method findGeoIntersectionsHelper for finding GeoPoints of
     * intersections between the intersectable object and a given ray. This method
     * should be implemented in subclasses.
     *
     * @param ray
     * @return A list of GeoPoints representing intersection points between the object and the ray.
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    /**
     * Inner static class GeoPoint representing a geometric intersection point with
     * associated geometry.
     */
    public static class GeoPoint {

        /**
         * The geometry object of this intersection point.
         */
        public Geometry geometry;

        /**
         * The actual point of intersection.
         */
        public Point point;


        /**
         * Constructor for GeoPoint.
         *
         * @param geo
         * @param point
         */
        public GeoPoint(Geometry geo, Point point) {
            geometry = geo;
            this.point = point;
        }


        /**
         * Override equals method
         */

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            return o instanceof GeoPoint other && (this.point == other.point) && (this.geometry == other.geometry);
        }

        /**
         * Override toString method
         *
         * @return the Object
         */
        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }

    }

}
