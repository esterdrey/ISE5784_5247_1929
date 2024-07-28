package Stage9;
import geometries.*;
import lighting.*;
import org.junit.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;


import java.util.Random;
/**
 * Image 9
 *
 * @author Ester Drey and Avigail Bash
 */

public class Stage_9_Before {

    private final Scene scene = new Scene("Enhanced Solar System Scene");

    // Set up the camera with adaptive super-sampling, multi-threading, and other properties
    private final Camera.Builder camera = Camera.getBuilder()
            .setadaptive(true)
            .setMultiThreading(3)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene).setNumberOfPoints(50)).setNumberOfRays(50);



    // Method to create the solar system scene
    private void createSolarSystem() {

        // Create Saturn at the specified position and size
        createSaturn(new Point(new Double3(5, -5, -400)), 25);


        // Create the moon with a specified material
        Material rockMaterial = new Material().setKD(0.3).setKS(0.7).setnShininess(30);
        Point moonCenter = new Point(new Double3(-35, 100, -800));
        createMoon(moonCenter, 35, rockMaterial);

        // Create blue-white glowing sun to the right of the moon
        Point sunCenter = new Point(new Double3(60, 80, -50));
        double sunRadius = 15;
        Sphere sun = new Sphere(sunCenter, sunRadius);
        scene.geometries.add(sun);

        // Set sun material properties
        sun.setEmission(new Color(255, 69, 0)).setMaterial(new Material()
                .setKD(0.5)
                .setKS(0.5)
                .setnShininess(100));

        addSunFlames(sunCenter, sunRadius);

        createStarfield();


        // Add the flashlight to the bottom of the scene
        createFlashlight(new Point(new Double3(-5, -100, 20)), 10, 20, 10, 7);

        addBlackTriangles();

        scene.background = new Color(27, 27, 27);
    }

    // Method to create a flashlight
    private void createFlashlight(Point baseCenter, double bodyWidth, double bodyHeight, double bodyDepth, double headRadius) {
        Material flashlightMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(30);
        Color flashlightColor = new Color(30,31,34);


        // Create the body of the flashlight as a rectangular prism
        Point p1 = baseCenter;
        Point p2 = baseCenter.add(new Vector(bodyWidth, 0, 0));
        Point p3 = baseCenter.add(new Vector(bodyWidth, bodyHeight, 0));
        Point p4 = baseCenter.add(new Vector(0, bodyHeight, 0));
        Point p5 = baseCenter.add(new Vector(0, 0, bodyDepth));
        Point p6 = baseCenter.add(new Vector(bodyWidth, 0, bodyDepth));
        Point p7 = baseCenter.add(new Vector(bodyWidth, bodyHeight, bodyDepth));
        Point p8 = baseCenter.add(new Vector(0, bodyHeight, bodyDepth));

        // Create the polygons for the body of the flashlight
        Polygon bodyFront = new Polygon(p1, p2, p3, p4);
        Polygon bodyBack = new Polygon(p5, p6, p7, p8);
        Polygon bodyLeft = new Polygon(p1, p5, p8, p4);
        Polygon bodyRight = new Polygon(p2, p6, p7, p3);
        Polygon bodyTop = new Polygon(p4, p3, p7, p8);
        Polygon bodyBottom = new Polygon(p1, p2, p6, p5);

        // Set emission color and material properties for the flashlight body
        bodyFront.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyBack.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyLeft.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyRight.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyTop.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyBottom.setEmission(flashlightColor).setMaterial(flashlightMaterial);

        // Create the head of the flashlight as a sphere
        Point headCenter = baseCenter.add(new Vector(bodyWidth / 2, bodyHeight + headRadius - 2, bodyDepth / 2));
        Sphere head = new Sphere(headCenter, headRadius);
        head.setEmission(flashlightColor).setMaterial(flashlightMaterial);


        scene.geometries.add(bodyFront, bodyBack, bodyLeft, bodyRight, bodyTop, bodyBottom, head);



        // Create a button on the flashlight body
        double buttonRadius = bodyWidth / 4;
        double buttonYOffset = bodyHeight * 0.6;
        Point buttonCenter = baseCenter.add(new Vector(bodyWidth / 2, buttonYOffset, bodyDepth + buttonRadius));
        Sphere button = new Sphere(buttonCenter, buttonRadius);
        button.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        scene.geometries.add(button);
    }

    // Method to add flames around the sun
    private void addSunFlames(Point sunCenter, double sunRadius) {

        int numFlames = 32;
        double flameHeight = sunRadius * 0.7;

        for (int i = 0; i < numFlames; i++) {
            double angle = 2 * Math.PI * i / numFlames;


            // Calculate the base points of the flame
            Point basePoint1 = getPointOnSphere(sunCenter, sunRadius, angle);
            Point basePoint2 = getPointOnSphere(sunCenter, sunRadius, angle + Math.PI / numFlames);
            Point flameTop = sunCenter.add(new Vector(
                    Math.cos(angle) * (sunRadius + flameHeight),
                    Math.sin(angle) * (sunRadius + flameHeight),
                    0
            ));

            Color flameColor = new Color(255, 69, 0);


            // Create the flame triangle
            Triangle flameTriangle = new Triangle(basePoint1, basePoint2, flameTop);
            flameTriangle.setEmission(flameColor)
                    .setMaterial(new Material()
                            .setKD(0.5)
                            .setKS(0.5)
                            .setnShininess(100));

            scene.geometries.add(flameTriangle);
        }
    }


    // Method to add black triangles to the scene
    private void addBlackTriangles() {
        Material blackMaterial = new Material()
                .setKD(0).setKS(0).setnShininess(0)
                .setKT(0).setKR(0);
        Color blackColor = new Color(0, 0, 0);

        Point bottomCenter = new Point(0, -100, 0);

        // Create left black triangle
        Point leftTop = new Point(-400, 1000, -0);
        Triangle leftTriangle = new Triangle(bottomCenter, new Point(-300, -100, -0), leftTop);
        leftTriangle.setEmission(blackColor)
                .setMaterial(blackMaterial);

        // Create right black triangle
        Point rightTop = new Point(400, 1000, -0);
        Triangle rightTriangle = new Triangle(bottomCenter, new Point(300, -100, -0), rightTop);
        rightTriangle.setEmission(blackColor)
                .setMaterial(blackMaterial);

        scene.geometries.add(leftTriangle, rightTriangle);
    }

    // Method to get a point on the sphere's surface given an angle
    private Point getPointOnSphere(Point center, double radius, double angle) {
        return center.add(new Vector(
                Math.cos(angle) * radius,
                Math.sin(angle) * radius,
                0
        ));
    }

    // Method to create the moon
    private void createMoon(Point center, double radius, Material material) {
        Sphere moon = new Sphere(center, radius);
        moon  .setEmission(new Color(0,0,0))  // כחול כהה יותר
                .setMaterial(new Material()
                        .setKD(0.5)
                        .setKS(0.5)
                        .setnShininess(100));  // ערך נמוך יותר לברק פחות חזק



        scene.geometries.add(moon);
    }


    // Method to create Saturn
    private void createSaturn(Point center, double radius) {
        Sphere saturnBody = new Sphere(center, radius);
        saturnBody.setEmission(new Color(210, 180, 140))  // כחול כהה יותר
                .setMaterial(new Material()
                        .setKD(0.5)
                        .setKS(0.5)
                        .setnShininess(100));

        double ringInnerRadius = radius * 1.4;
        double ringOuterRadius = radius * 1.6;

        double tiltAngleX = Math.PI / 6;
        double tiltAngleZ = Math.PI / 12;
        double cosTiltX = Math.cos(tiltAngleX);
        double sinTiltX = Math.sin(tiltAngleX);
        double cosTiltZ = Math.cos(tiltAngleZ);
        double sinTiltZ = Math.sin(tiltAngleZ);

        int segments = 50;
        for (int i = 0; i < segments; i++) {
            double startAngle = 2 * Math.PI * i / segments;
            double endAngle = 2 * Math.PI * (i + 1) / segments;

            Point p1 = rotatePointAroundXZ(createPointOnRing(center, ringInnerRadius, startAngle), center, cosTiltX, sinTiltX, cosTiltZ, sinTiltZ);
            Point p2 = rotatePointAroundXZ(createPointOnRing(center, ringOuterRadius, startAngle), center, cosTiltX, sinTiltX, cosTiltZ, sinTiltZ);
            Point p3 = rotatePointAroundXZ(createPointOnRing(center, ringOuterRadius, endAngle), center, cosTiltX, sinTiltX, cosTiltZ, sinTiltZ);
            Point p4 = rotatePointAroundXZ(createPointOnRing(center, ringInnerRadius, endAngle), center, cosTiltX, sinTiltX, cosTiltZ, sinTiltZ);

            Geometry ringSegment = new Polygon(p1, p2, p3, p4)
                    .setEmission(new Color(153, 76, 0))  // כחול כהה יותר
                    .setMaterial(new Material()
                            .setKD(0.5)
                            .setKS(0.5)
                            .setnShininess(100));

            if (!p1.equals(p2) && !p2.equals(p3) && !p3.equals(p4) && !p4.equals(p1)) {
                scene.geometries.add(ringSegment);
            }
        }

        Point tiltedCenter = rotatePointAroundXZ(center, center, cosTiltX, sinTiltX, cosTiltZ, sinTiltZ);
        Sphere tiltedSaturnBody = new Sphere(tiltedCenter, radius);
        tiltedSaturnBody.setEmission(new Color(210, 180, 140))  // כחול כהה יותר
                .setMaterial(new Material()
                        .setKD(0.5)
                        .setKS(0.5)
                        .setnShininess(100));

        scene.geometries.add(tiltedSaturnBody);
    }

    // Method to create the rings of Saturn

    private Point createPointOnRing(Point center, double radius, double angle) {
        double x = center.getXyz().getD1() + radius * Math.cos(angle);
        double z = center.getXyz().getD3() + radius * Math.sin(angle);
        return new Point(x, center.getXyz().getD2(), z);
    }



    private Point rotatePointAroundXZ(Point p, Point center, double cosAngleX, double sinAngleX, double cosAngleZ, double sinAngleZ) {
        double x = p.getXyz().getD1() - center.getXyz().getD1();
        double y = p.getXyz().getD2() - center.getXyz().getD2();
        double z = p.getXyz().getD3() - center.getXyz().getD3();

        double newY = y * cosAngleX - z * sinAngleX;
        double newZ = y * sinAngleX + z * cosAngleX;

        double newX = x * cosAngleZ - newY * sinAngleZ;
        newY = x * sinAngleZ + newY * cosAngleZ;

        return new Point(
                newX + center.getXyz().getD1(),
                newY + center.getXyz().getD2(),
                newZ + center.getXyz().getD3()
        );
    }



    // Method to create a starfield background
    private void createStarfield() {
        Random random = new Random();
        int numStars = 500;


        double minX = -100;
        double maxX = 400;
        double minY = -180;
        double maxY = 200;
        double z = -1000;


        for (int i = 0; i < numStars; i++) {
            double x = minX + random.nextDouble() * (maxX - minX);
            double y = minY + random.nextDouble() * (maxY - minY);


            Color starColor = new Color(200 + random.nextInt(55), 200 + random.nextInt(55), 200 + random.nextInt(55));


            Sphere star = new Sphere(new Point(x, y, z), 0.1 + random.nextDouble() * 0.5);
            star.setEmission(starColor)
                    .setMaterial(new Material()
                            .setKD(0.5)
                            .setKS(0.5)
                            .setnShininess(30));

            scene.geometries.add(star);
        }
    }


    @Test
    public void testStage_9_Before() {
        scene.setAmbientLight(new AmbientLight(new Color(50, 50, 100), 0.1));

        scene.lights.add(new DirectionalLight(new Color(100, 100, 150), new Vector(1, -1, -1)));
        scene.lights.add(new DirectionalLight(new Color(0, 100, 150), new Vector(0,1,0)));

        scene.lights.add(new SpotLight(new Color(50, 50, 100), new Point(new Double3(60, 80, -50)), new Vector(0, -1, 0))
                .setKL(0.0004).setKQ(0.0006).setSize(5));


        scene.lights.add(new PointLight(new Color(100, 80, 150), new Point(new Double3(-200, 200, 100)))
                .setKL(0.0000001).setKQ(0.00000001).setSize(5));

        scene.lights.add(new PointLight(new Color(20, 20, 20), new Point(new Double3(0, 200, 100)))
                .setKL(0.0000001).setKQ(0.00000001).setSize(5));


        createSolarSystem();
        camera.setImageWriter(new ImageWriter("Stage_9_Before", 2000, 2000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build()
                .renderImage()
                .writeToImage();
    }
}