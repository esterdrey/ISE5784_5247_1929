package stage8;
import geometries.*;
import lighting.*;
import org.junit.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;
/**
 * Image 8 after super sampling
 *
 * @author Ester Drey and Avigail Bash
 */
public class stage8After {
    private final Scene scene = new Scene("Enhanced Solar System Scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene)).setNumberOfRays(50);

    /**
     * Creates the solar system scene including planets, moons, and the sun with lighting effects.
     */
    private void createSolarSystem() {

        Material rockMaterial = new Material().setKD(0.3).setKS(0.7).setnShininess(30);

        createSaturn(new Point(new Double3(-55, -60, -400)), 25);

        // Create a moon
        Point moonCenter = new Point(new Double3(0, 0, -500));
        createMoon(moonCenter, 40, rockMaterial);

        // Create a glowing orange sun to the right of the moon
        Point sunCenter = new Point(new Double3(60, 65, -50));
        double sunRadius = 25;
        Sphere sun = new Sphere(sunCenter, sunRadius);

        // Set the material properties for the sun
        Material sunMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(100);
        sun.setEmission(new Color(255, 69, 0)).setMaterial(sunMaterial); // Glowing orange

        scene.geometries.add(sun);
    }


    /**
     * Creates a moon object and adds a light source on its surface.
     * @param center Center of the moon.
     * @param radius Radius of the moon.
     * @param material Material of the moon.
     */
    private void createMoon(Point center, double radius, Material material) {
        Sphere moon = new Sphere(center, radius);
        moon.setEmission(new Color(5, 5, 5)).setMaterial(material);

        // Add a bright white light to the upper left of the moon
        scene.lights.add(new PointLight(new Color(255, 255, 255), center.add(new Vector(-150, 150, 0)))
                .setKL(0.0001).setKQ(0.000001).setSize(5));

        scene.geometries.add(moon);
    }

    /**
     * Creates a Saturn-like planet with rings.
     * @param center Center of Saturn.
     * @param radius Radius of Saturn.
     */
    private void createSaturn(Point center, double radius) {
        Material saturnMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(30);
        Color saturnColor = new Color(210, 180, 140); // Light brown color for Saturn

        // Create Saturn body
        Sphere saturnBody = new Sphere(center, radius);
        saturnBody.setEmission(saturnColor)
                .setMaterial(saturnMaterial);

        // Create Saturn's ring
        double ringInnerRadius = radius * 1.2;
        double ringOuterRadius = radius * 2;

        // Rotate ring points manually
        double angle = Math.PI / 6; // 30 degrees
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        // Create multiple segments to make the ring visible
        int segments = 50;
        for (int i = 0; i < segments; i++) {
            double startAngle = 2 * Math.PI * i / segments;
            double endAngle = 2 * Math.PI * (i + 1) / segments;

            Point p1 = rotatePointAroundY(createPointOnRing(center, ringInnerRadius, startAngle), center, cosAngle, sinAngle);
            Point p2 = rotatePointAroundY(createPointOnRing(center, ringOuterRadius, startAngle), center, cosAngle, sinAngle);
            Point p3 = rotatePointAroundY(createPointOnRing(center, ringOuterRadius, endAngle), center, cosAngle, sinAngle);
            Point p4 = rotatePointAroundY(createPointOnRing(center, ringInnerRadius, endAngle), center, cosAngle, sinAngle);

            Geometry ringSegment = new Polygon(p1, p2, p3, p4)
                    .setEmission(new Color(153, 76, 0)) // Color for the ring
                    .setMaterial(new Material().setKD(0.4).setKS(0.6).setnShininess(50));

            scene.geometries.add(ringSegment);
        }

        scene.geometries.add(saturnBody);
    }

    /**
     * Creates a point on the ring of Saturn given an angle.
     * @param center Center of Saturn.
     * @param radius Radius of the ring.
     * @param angle Angle around the ring.
     * @return The point on the ring.
     */
    private Point createPointOnRing(Point center, double radius, double angle) {
        double x = center.getXyz().getD1() + radius * Math.cos(angle);
        double z = center.getXyz().getD3() + radius * Math.sin(angle);
        return new Point(x, center.getXyz().getD2(), z);
    }

    /**
     * Rotates a point around the Y-axis.
     * @param p Point to rotate.
     * @param center Center of rotation.
     * @param cosAngle Cosine of the rotation angle.
     * @param sinAngle Sine of the rotation angle.
     * @return The rotated point.
     */
    private Point rotatePointAroundY(Point p, Point center, double cosAngle, double sinAngle) {
        double x = p.getXyz().getD1() - center.getXyz().getD1();
        double z = p.getXyz().getD3() - center.getXyz().getD3();
        double newX = x * cosAngle + z * sinAngle;
        double newZ = -x * sinAngle + z * cosAngle;
        return new Point(
                newX + center.getXyz().getD1(),
                p.getXyz().getD2(),
                newZ + center.getXyz().getD3()
        );
    }

    /**
     * Test method for rendering the enhanced solar system scene.
     */
    @Test
    public void testEnhancedSolarSystem() {
        // Decrease ambient light to darken the scene
        scene.setAmbientLight(new AmbientLight(new Color(5, 5, 10), 0.1));

        // Adjusted directional light with very low intensity
        scene.lights.add(new DirectionalLight(new Color(50, 50, 40), new Vector(1, -1, -1)));

        // Adjusted point lights with very low intensity
        scene.lights.add(new PointLight(new Color(50, 40, 40), new Point(new Double3(-200, 200, 100)))
                .setKL(0.0000001).setKQ(0.00000001).setSize(5));
        scene.lights.add(new PointLight(new Color(40, 20, 20), new Point(new Double3(0, 0, 300)))
                .setKL(0.0000001).setKQ(0.00000001).setSize(5));

        createSolarSystem();
        camera.setImageWriter(new ImageWriter("stage 8 after", 2000, 2000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build()
                .renderImage()
                .writeToImage();
    }
}
