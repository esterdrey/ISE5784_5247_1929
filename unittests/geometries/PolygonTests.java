package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Polygons
 *
 * @author Ester Drey and Avigail Bash
 */
public class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    @Test
    void testFindIntsersections() {
        Polygon poly = new Polygon(new Point(-1, 0, 1), new Point(1, 0, 1), new Point(0, 2, 1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: The point of intersection inside the polygon (1 point)
        Point p = new Point(0, 1, 1);
        List<Point> result = poly.findIntersections(new Ray(new Point(0, 2, 0), new Vector(0, -1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");


        // TC02: The point of intersection is outside the polygon opposite a side (0 points)
        result = poly.findIntersections(new Ray(new Point(0, 2, 0), new Vector(2, -1, 1)));
        assertNull(result, "Ray's line out of polygon");
        // TC03: The point of intersection is outside the polygon opposite a vertex (0 points)
        result = poly.findIntersections(new Ray(new Point(0, 2, 1), new Vector(0, 1, 1)));
        assertNull(result, "Ray's line out of polygon");

        // =============== Boundary Values Tests ==================
        // TC11: The intersection point is on a side (0 points)
        result = poly.findIntersections(new Ray(new Point(0, 2, 0), new Vector(-0.5, -1, 1)));
        assertNull(result, "Wrong number of points");
        // TC12: The intersection point is on a vertex (0 points)
        result = poly.findIntersections(new Ray(new Point(0, 2, 0), new Vector(2, -2, 1)));
        assertNull(result, "Wrong number of points");
        // TC13: The intersection point is on the continuation of an edge (0 points)
        result = poly.findIntersections(new Ray(new Point(0, 2, 0), new Vector(-1, -2, 1)));
        assertNull(result, "Wrong number of points");
    }
}
