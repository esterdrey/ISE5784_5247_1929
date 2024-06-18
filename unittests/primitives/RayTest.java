package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void testFindClosestPoint() {

        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(2, 0, 0);
        Point p3 = new Point(3, 0, 0);
        Vector v = new Vector(1,0,0);
        List<Point> list = List.of(p1, p2, p3);

        // ============ Equivalence Partitions Tests ==============
        // EP01: closest point is in the middle of the list
        assertEquals(p2,new Ray(new Point(2.1,0,0),v).findClosestPoint(list),"closest point is in the middle of the list" );


        // =============== Boundary Values Tests ==================

        // BV01: empty list
        assertNull(new Ray(new Point(2.1,0,0),v).findClosestPoint(null),"empty list" );


        // BV02:  first point in the list
        assertEquals(p1,new Ray(new Point(1.1,0,0),v).findClosestPoint(list),"first point in the list" );


        // BV03: last point in the list
        assertEquals(p3,new Ray(new Point(3.1,0,0),v).findClosestPoint(list),"last point in the list" );
    }
}