package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {


    @Test
    public void testConstructor(){
        // ============ Equivalence Partitions Tests ==============

        // TC01: tests the correct construction of plane
        try {
            new Plane(new Point(0, 0, 1),new Point(1, 0, 0), new Point(0, 1, 0));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct plane");
        }
        // =============== Boundary Values Tests ==================

        // TC11: The first and second points are the same
        assertThrows(IllegalArgumentException.class,() -> new Plane(new Point(0, 0, 1),new Point(0, 0, 1),new Point(0, 1, 0)),"Constructed a Plane with the same first and second points ");
        // TC12: The points are on the same line
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(5, 2, 4),new Point(1, 1, 1),new Point(9, 3, 7)),  "Constructed a plane with the points are on the same line");
    }

    private final double DELTA = 0.000001;
    @Test
    void testGetNormal()
    {
        Point p1 =new Point(0, 0, 1);
        Point p2= new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane p = new Plane(p1, p2, p3);

        // generate the test result
        Vector result = p.getNormal();
        // ensure |result| = 1
        assertEquals(1.0, result.length(), DELTA, "Plane's normal is not a unit vector");

        assertEquals(new Vector(-1,-1,-1).normalize(),p.getNormal(),"Plane's normal is not the expected value");
    }


}