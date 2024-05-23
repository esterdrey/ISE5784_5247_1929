package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.*;

class SphereTests {
    private final double DELTA = 0.000001;

    @Test
    void testGetNormal()
    {
        Sphere s = new Sphere( Point.ZERO,1);
        //test that getNormal for sphere does not throw an exception
        Vector v1 = s.getNormal(new Point(3,0,0));
         v1= v1.normalize();
        assertEquals(new Vector(-1,0,0),v1,"ERROR: getNormal() wrong value");
    }
}