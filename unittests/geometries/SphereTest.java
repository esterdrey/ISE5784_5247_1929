package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {


    private final Point p001 = new Point(0, 0, 1);
    private final Point p100 = new Point(1, 0, 0);
    private final Vector v001 = new Vector(0, 0, 1);

    @Test
    void testGetNormal() {
        Sphere s1 = new Sphere(new Point(1, 2, 3), 1);
        // ============ Equivalence Partitions Tests ==============
        Vector v = s1.getNormal(new Point(2, 2, 3));
        // TC01: Test that the normal is the right one
        assertEquals(new Vector(1, 0, 0),
                v,
                "getNormal() wrong result");

    }

    @Test
    void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final Point gp3 = Point.ZERO;
        final Point gp4 = new Point(2, 0, 0);
        final Point gp5 = new Point(1, 1, 0);
        final var exp1 = List.of(gp1, gp2);
        final var exp2 = List.of(gp4, gp3);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310))
                .stream().sorted(Comparator.comparingDouble(p -> p.distance(p01))).toList();
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp1, result1, "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        var intersactions = sphere.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(3, 1, 0)));
        assertEquals(1, intersactions.size(), "Wrong number of points");

        // TC04: Ray starts after the sphere (0 points)
        Ray ray1 = new Ray(new Point(5, 5, 5), new Vector(1, 1, 1));
        assertNull(sphere.findIntersections(ray1), "Ray's line out of sphere");


        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        intersactions = sphere.findIntersections(new Ray(gp1, new Vector(3, 1, 0)));
        assertEquals(1, intersactions.size(), "Wrong number of points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        intersactions = sphere.findIntersections(new Ray(gp1, new Vector(-3, -1, 0)));
        assertNull(intersactions, "Wrong number of points");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        intersactions = sphere.findIntersections(new Ray(new Point(3, 0, 0), new Vector(-3, 0, 0)));
        assertNotNull(intersactions, "Wrong number of points");
        assertEquals(2, intersactions.size(), "Wrong number of points");
        assertEquals(exp2, intersactions, "Ray crocess sphere");

        // TC14: Ray starts at sphere and goes inside (1 points)
        intersactions = sphere.findIntersections(new Ray(gp3, new Vector(3, 0, 0)));
        assertEquals(1, intersactions.size(), "Wrong number of points");
        assertEquals(List.of(gp4), intersactions, "Ray starts at sphere and goes through the center");

        // TC15: Ray starts inside (1 points)
        intersactions = sphere.findIntersections(new Ray(new Point(1.5, 0, 0), new Vector(-3, 0, 0)));
        assertEquals(1, intersactions.size(), "Wrong number of points");
        assertEquals(List.of(gp3), intersactions, "Ray starts at sphere and goes through the center");

        // TC16: Ray starts at the center (1 points)
        intersactions = sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(3, 0, 0)));
        assertEquals(1, intersactions.size(), "Wrong number of points");
        assertEquals(List.of(gp4), intersactions, "Ray starts at the center");

        // TC17: Ray starts at sphere and goes outside (0 points)
        intersactions = sphere.findIntersections(new Ray(gp4, new Vector(3, 0, 0)));
        assertNull(intersactions, "Ray's line out of sphere");

        // TC18: Ray starts after sphere (0 points)
        intersactions = sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(-3, 0, 0)));
        assertNull(intersactions, "Ray's line out of sphere");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        intersactions = sphere.findIntersections(new Ray(new Point(-1, 1, 0), new Vector(3, 1, 0)));
        assertNull(intersactions, "Ray's line out of sphere");

        // TC20: Ray starts at the tangent point
        intersactions = sphere.findIntersections(new Ray(gp5, new Vector(3, 1, 0)));
        assertNull(intersactions, "Ray's line out of sphere");

        // TC21: Ray starts after the tangent point
        intersactions = sphere.findIntersections(new Ray(new Point(1.5, 1, 0), new Vector(3, 1, 0)));
        assertNull(intersactions, "Ray's line out of sphere");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        intersactions = sphere.findIntersections(new Ray(new Point(-1, 1, 0), new Vector(0, -1, 0)));
        assertNull(intersactions, "Ray's line out of sphere");

    }
}