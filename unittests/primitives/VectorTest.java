package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class
 *
 * @author Ester Drey Avigail Bash
 */
class VectorTest {

    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking a scales operation between a vector and a number
        Vector v1 = new Vector(2, 4, 6);
        assertEquals(new Vector(4, 8, 12), v1.scale(2), "ERROR: scale() wrong value");

        // =============== Boundary Values Tests ==================
        // TC10:Scale a vector with zero
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "ERROR: It is not possible to get the zero vector");

    }

    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking a adding operation between two vectors
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(1, 0, 0);
        assertEquals(new Vector(2, 2, 3), v1.add(v2), "ERROR: add() wrong value");

        // =============== Boundary Values Tests ==================
        // TC10:Adding a vector from it opposite
        assertThrows(IllegalArgumentException.class,
                () -> v1.add(new Vector(-1, -2, -3)),
                "ERROR: Vector + -itself does not throw an exception");
    }

    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking of a product between two vectors returned a number
        Vector v1 = new Vector(2, 1, 1);
        Vector v2 = new Vector(2, 3, 4);
        assertEquals(11, v1.dotProduct(v2), "ERROR: dotProduct() wrong value");

        // =============== Boundary Values Tests ==================
        // TC10:Scalar multiplication between two vectors and zero is obtained
        Vector v3 = new Vector(-2, 2, 2);
        assertEquals(0, v1.dotProduct(v3), "ERROR: dotProduct() for orthogonal vectors is not zero");
    }

    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the vector product between two vectors
        Vector v1 = new Vector(0, 1, 1);
        Vector v2 = new Vector(2, 3, 4);
        assertEquals(new Vector(1, 2, -2), v1.crossProduct(v2), "ERROR: crossProduct() wrong value");

        // =============== Boundary Values Tests ==================
        // TC10:Checking the product between two mutually perpendicular vectors
        Vector v3 = new Vector(0, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3), "ERROR: crossProduct() for parallel vectors does not throw an exception");

    }

    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the vector product between two vectors
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(14, v1.lengthSquared(), 0.00001, "ERROR: lengthSquared() wrong value");
    }

    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking the vector product between two vectors
        Vector v1 = new Vector(2, 2, 1);
        assertEquals(3, v1.length(), 0.00001, "ERROR: length() wrong value");
    }

    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        // TC01:Checking normalize vector
        Vector v1 = new Vector(4, 0, 0);
        assertEquals(new Vector(1, 0, 0), v1.normalize(), "ERROR: normalize() wrong value");
        v1 = v1.normalize();
        assertEquals(1, v1.length(), 0.00001, "The vector is not a unit vector");
    }


    @Test
    void testSubtract() {
        Vector v1 = new Vector(1, 2, 3);

        assertThrows(IllegalArgumentException.class,
                () -> v1.subtract(v1),
                "ERROR: Vector - itself does not throw an exception");

        assertEquals(new Vector(3, 6, 9),
                v1.subtract(new Vector(-2, -4, -6)),
                "ERROR: Vector + Vector does not work correctly");

    }
}