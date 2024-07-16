package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: constructor acting well
        assertThrows(IllegalArgumentException.class,
                () -> new Triangle(new Point(0, 1, 0),
                        new Point(0, 1, 0), new Point(1, 1, 0)),
                "TC01: Triangle constructor should throw IllegalArgumentException)");
    }

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: simple check
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(1, 0, 0), new Point(1, 1, 0));

        boolean bool = new Vector(0, 0, -1).equals(t.getNormal(new Point(0, 1, 0))) || new Vector(0, 0, 1).equals(t.getNormal(new Point(0, 1, 0)));
        assertTrue(bool, "TC01: getNormal does not work correctly");
    }

    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(-1, 0, 1), new Point(1, 0, 1), new Point(0, 2, 1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: The point of intersection inside the triangle (1 point)
        Point p = new Point(0, 1, 1);
        List<Point> result = triangle.findIntersections(new Ray(new Point(0, 2, 0), new Vector(0, -1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p), result, "Ray crosses triangle");
        // TC02: The point of intersection is outside the triangle opposite a side (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0), new Vector(2, -1, 1)));
        assertNull(result, "Ray's line out of triangle");
        // TC03: The point of intersection is outside the triangle opposite a vertex (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 1), new Vector(0, 1, 1)));
        assertNull(result, "Ray's line out of triangle");

        // =============== Boundary Values Tests ==================
        // TC11: The intersection point is on a side (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0), new Vector(-0.5, -1, 1)));
        assertNull(result, "Wrong number of points");
        // TC12: The intersection point is on a vertex (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0), new Vector(2, -2, 1)));
        assertNull(result, "Wrong number of points");
        // TC13: The intersection point is on the continuation of an edge (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0), new Vector(-1, -2, 1)));
        assertNull(result, "Wrong number of points");
    }
}