package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {


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
    @Test
    void testFindIntersections()
    {
        // ============ Equivalence Partitions Tests ==============
        //The Ray must be neither orthogonal nor parallel to the plane
        //TC01: Ray intersects the plane
        Plane p1=new Plane(new Point(1,0,0),new Point(0,1,0),new Point(0,0,1));
        Ray r1=new Ray(new Point(-2,0,0),new Vector(1,1,1));
        assertEquals(1,p1.findIntersections(r1).size(),"Wrong number of points");

        //TC02: Ray does not intersect the plane
        Ray r2=new Ray(new Point(-2,0,0),new Vector(-1,0,-1));
        assertNull(p1.findIntersections(r1),"Wrong number of points");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        // TC03: Ray included in the plane
        Ray r3=new Ray(new Point(1,2,-2),new Vector(1,-1,0));
        assertNull(p1.findIntersections(r3),"Wrong number of points");
        // TC04: Ray not included in the plane
        Ray r4=new Ray(new Point(1,1,1),new Vector(1,-1,0));
        assertNull(p1.findIntersections(r4),"Wrong number of points");

        // **** Group: Ray is orthogonal to the plane
        // TC05: Ray start before plane
        Ray r5=new Ray(new Point(0,0,-1),new Vector(1,1,1));
        assertEquals(1,p1.findIntersections(r5).size(),"Wrong number of points");

        // TC06: Ray start in  plane
        Ray r6=new Ray(new Point(1,2,-2),new Vector(1,1,1));
        assertNull(p1.findIntersections(r6),"Wrong number of points");

        // TC06: Ray start after  plane
        Ray r7=new Ray(new Point(0,0,4),new Vector(1,1,1));
        assertNull(p1.findIntersections(r7),"Wrong number of points");

        // TC07:A ray that is neither parallel nor perpendicular to the plane but starts inside the plane
        Ray r8=new Ray(new Point(1,2,-2),new Vector(2,2,-3));
        assertNull(p1.findIntersections(r8),"Wrong number of points");

        // TC07:A ray that is neither parallel nor perpendicular to the plane but starts inside the plane and the
        // beginning of the beam exactly at the "reference point" of the plain
        Ray r9=new Ray(new Point(0,1,0),new Vector(2,2,-3));
        assertNull(p1.findIntersections(r9),"Wrong number of points");
    }

}