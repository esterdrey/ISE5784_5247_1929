package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;


import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {


    private final Point p001 = new Point(0, 0, 1);
    private final Point p100 = new Point(1, 0, 0);
    private final Vector v001 = new Vector(0, 0, 1);

    @Test
    void testGetNormal() {
    }

    @Test
    void testFindIntersections()
    {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310))
                .stream().sorted(Comparator.comparingDouble(p ->p.distance(p01))).toList();
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        Ray ray = new Ray(new Point(0.5, 0.5, 0), new Vector(1,1,1));
        assertEquals(1, result1.size(), "Wrong number of points");

        // TC04: Ray starts after the sphere (0 points)
        Ray ray1 = new Ray(new Point(5,5,5), new Vector(1,1,1));
        assertNull(sphere.findIntersections(ray1), "Ray's line out of sphere");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        Ray ray3 = new Ray(new Point(1,1,0), new Vector(-1, 3, -1));
        assertEquals(1, result1.size(), "Wrong number of points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        Ray ray4 = new Ray(new Point(1,1,0), new Vector(1, 3, 1));
        assertEquals(1, result1.size(), "Wrong number of points");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        Ray ray5 = new Ray(new Point(5,0,0), new Vector(-1,0,0));
        final var result2=sphere.findIntersections(ray5) .stream().sorted(Comparator.comparingDouble(p ->p.distance(p01))).toList();
        assertEquals(2, result2.size(), "Wrong number of points");
        // TC14: Ray starts at sphere and goes inside (1 points)
        Ray ray6 = new Ray(new Point(2,0,0), new Vector(-1,0,0));
        assertEquals(1, sphere.findIntersections(ray6).size(), "Wrong number of points");
        // TC15: Ray starts inside (1 points)
        Ray ray7 = new Ray(new Point(1.5,0,0), new Vector(-1,0,0));
        assertEquals(1, sphere.findIntersections(ray7).size(), "Wrong number of points");
        // TC16: Ray starts at the center (1 points)
        Ray ray8 = new Ray(new Point(1,0,0), new Vector(-1,0,0));
        assertEquals(1, sphere.findIntersections(ray8).size(), "Wrong number of points");
        // TC17: Ray starts at sphere and goes outside (0 points)
        Ray ray9 = new Ray(new Point(2,0,0), new Vector(1,0,0));
        assertNull(sphere.findIntersections(ray9) , "Ray's line out of sphere");
        // TC18: Ray starts after sphere (0 points)
        Ray ray10 = new Ray(new Point(3,0,0), new Vector(1,0,0));
        assertNull(sphere.findIntersections(ray10) , "Ray's line out of sphere");
        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        Ray ray11 = new Ray(new Point(2,0,-1), new Vector(0,0,1));
        assertNull(sphere.findIntersections(ray11) , "Ray's line out of sphere");
        // TC20: Ray starts at the tangent point
        Ray ray12 = new Ray(new Point(2,0,0), new Vector(0,0,1));
        assertNull(sphere.findIntersections(ray12) , "Ray's line out of sphere");
        // TC21: Ray starts after the tangent point
        Ray ray13= new Ray(new Point(2,0,3), new Vector(0,0,1));
        assertNull(sphere.findIntersections(ray13) , "Ray's line out of sphere");
        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        Ray ray14= new Ray(new Point(4,0,0), new Vector(0,0,1));
        assertNull(sphere.findIntersections(ray14) , "Ray's line out of sphere");
    }
}