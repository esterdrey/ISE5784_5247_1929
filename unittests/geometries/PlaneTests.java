package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {


    private final double DELTA = 0.000001;

    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: tests the correct construction of plane
        try {
            new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct plane");
        }
        // =============== Boundary Values Tests ==================

        // TC11: The first and second points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0, 1)
                        , new Point(0, 0, 1), new Point(0, 1, 0)),
                "Constructed a Plane with the same first and second points ");
        // TC12: The points are on the same line
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(5, 2, 4), new Point(1, 1, 1), new Point(9, 3, 7)),
                "Constructed a plane with the points are on the same line");
    }

    @Test
    void testGetNormal() {
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane p = new Plane(p1, p2, p3);

        // generate the test result
        Vector result = p.getNormal();
        // ensure |result| = 1
        assertEquals(1.0, result.length(), DELTA, "Plane's normal is not a unit vector");
        assertEquals(new Vector(-1, -1, -1).normalize(), p.getNormal(),
                "Plane's normal is not the expected value");
    }

    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        //The Ray must be neither orthogonal nor parallel to the plane
        //TC01: Ray intersects the plane
        Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        Point p1 = new Point(0, 1, 1);
        List<Point> intersaction = plane.findIntersections(new Ray(new Point(2, 0, 0),
                new Vector(-2, 1, 1)));
        assertEquals(1, intersaction.size(), "Wrong number of points");
        assertEquals(List.of(p1), intersaction, "Ray crosses plane");

        //TC02: Ray does not intersect the plane
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1, 0, -1))),
                "Ray's line out of plane");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        // TC03: Ray included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 2, 1), new Vector(0, -1, 0))),
                "Ray's line out of plane");

        // TC04: Ray not included in the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 1, 0.5), new Vector(0, -1, 0))),
                "Ray's line out of plane");

        // **** Group: Ray is orthogonal to the plane
        // TC05: Ray start before plane
        p1 = new Point(0, 1, 1);
        intersaction = plane.findIntersections(new Ray(new Point(0, 1, 0.5), new Vector(0, 0, 0.5)));
        assertEquals(1, intersaction.size(), "Wrong number of points");
        assertEquals(List.of(p1), intersaction, "Ray crosses plane");

        // TC06: Ray start in plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 1, 2), new Vector(0, 0, 0.5))),
                "Wrong number of points");

        // TC07: Ray start after plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 1, 2), new Vector(0, 0, 0.5))),
                "Wrong number of points");

        // **** Group: Ray is neither orthogonal nor parallel to and begin at the plane
        // TC08:not the same point which appears as reference point in the plane
        assertNull(plane.findIntersections(new Ray(new Point(2, 0, 1), new Vector(-2, 0, 2))),
                "Ray's line out of plane");

        // TC09:the same point which appears as reference point in the plane
        assertNull(plane.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))),
                "Wrong number of points");
    }

}