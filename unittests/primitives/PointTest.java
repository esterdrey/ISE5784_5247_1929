package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class
 *  @author Ester Drey Avigail Bash
 */
class PointTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;

    @Test
    void testSubtract()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking a subtraction operation between two points
        Point p1=new Point(2,3,4);
        Point p2=new Point(1,2,3);
        assertEquals(new Vector(1,1,1),p1.subtract(p2),"ERROR: subtract() wrong value");

        // =============== Boundary Values Tests ==================
        // TC10:Subtracting a point from itself
        assertThrows(IllegalArgumentException.class,()->p1.subtract(p1),"ERROR: subtract() wrong value");
    }

    @Test
    void testAdd()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the function add
        Point p1=new Point(2,3,4);
        Vector v1=new Vector(1,1,1);
        assertEquals(new Point(3,4,5),p1.add(v1),"ERROR: add() wrong value");


        // =============== Boundary Values Tests ==================
        // TC10:Testing when we get the zero vector
        Vector v2=new Vector(-2,-3,-4);
        assertEquals(new Point(0,0,0),p1.add(v2),"ERROR: add() wrong value");

    }

    @Test
    void testDistanceSquared()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the function DistanceSquared
        Point p1=new Point(3,3,5);
        Point p2=new Point(1,2,3);
        assertEquals(9,p1.distanceSquared(p2),DELTA,"ERROR: DistanceSquared() wrong value");


        // =============== Boundary Values Tests ==================
        // TC10:Distance between a point and itself
        assertEquals(0,p1.distanceSquared(p1),DELTA,"ERROR: DistanceSquared() wrong value");
    }

    @Test
    void testDistance()
    {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the function Distance
        Point p1 = new Point(3, 4, 5);
        Point p2 = new Point(0, 0, 5);
        assertEquals(5, p1.distance(p2), DELTA, "ERROR: Distance() wrong value");


        // =============== Boundary Values Tests ==================
        // TC10:Distance between a point and itself
        assertEquals(0, p1.distance(p1), DELTA, "ERROR: Distance() wrong value");
    }
}