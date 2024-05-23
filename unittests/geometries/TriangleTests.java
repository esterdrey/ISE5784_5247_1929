package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTests
{
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
    void testGetNormal (){
        // ============ Equivalence Partitions Tests ==============
        // TC01: simple check
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(1, 0, 0), new Point(1, 1, 0));

        boolean bool = new Vector(0, 0, -1).equals(t.getNormal(new Point(0, 1, 0)))
                || new Vector(0, 0, 1).equals(t.getNormal(new Point(0, 1, 0)));
        assertTrue(bool, "TC01: getNormal does not work correctly");
    }

}