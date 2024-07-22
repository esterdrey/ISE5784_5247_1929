package renderer;
import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.awt.Color.BLUE;

public class my {
    private final Scene scene = new Scene("Enhanced Solar System Scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene));

    private void createSolarSystem() {
        Material rockMaterial = new Material().setKD(0.3).setKS(0.7).setnShininess(30);

        createSaturn(new Point(new Double3(5, -5, -400)), 25);

        // Create moon-like sphere
        Point moonCenter = new Point(new Double3(-35, 100, -800));
        createMoon(moonCenter, 35, rockMaterial);

        // Create blue-white glowing sun to the right of the moon
        Point sunCenter = new Point(new Double3(60, 80, -50));
        double sunRadius = 15;
        Sphere sun = new Sphere(sunCenter, sunRadius);

        // Set sun material properties

        sun.setEmission(new Color(255, 69, 0)).setMaterial(new Material()
                .setKD(0.5)
                .setKS(0.5)
                .setnShininess(100));

        addSunFlames(sunCenter, sunRadius);

        // Add lighting around the sun
        Vector[] lightDirections = {
                new Vector(1, 1, -1),
                new Vector(-1, 1, -1),
                new Vector(1, -1, -1),
                new Vector(-1, -1, -1)
        };

        for (Vector direction : lightDirections) {
            Point lightPosition = sunCenter.add(direction.scale(sunRadius * 2));
            scene.lights.add(new SpotLight(new Color(150, 100, 255), lightPosition, direction.scale(-1))
                    .setKL(0.0001).setKQ(0.000001)
                    .setNarrowBeam(20));
        }

//        // Add three more planets
//        addSmallPlanetWithLight(new Point(new Double3(-10, 40, -200)), 5);
//        addSmallPlanetWithLight(new Point(new Double3(-150, 75, -600)), 6);
//        addSmallPlanetWithLight(new Point(new Double3(200, 200, -900)), 7);

        createStarfield();
        //createMilkyWayInBoundaries();
// Add the flashlight to the bottom of the scene
        createFlashlight(new Point(new Double3(-5, -100, 20)), 10, 20, 10, 7);
        addBlackTriangles();
        scene.geometries.add(sun);
        scene.background = new Color(27, 27, 27);
    }

    private void createMilkyWayInBoundaries() {
        Random random = new Random();
        int numStars = 500; // מספר הכוכבים שברצונך להוסיף

        // גבולות לטווח ה-X ו-Y
        double minX = -400;
        double maxX = 400;
        double minY = -100;
        double maxY = 1000;

        // יצירת כוכבים רק בתוך האזור המוגדר
        for (int i = 0; i < numStars; i++) {
            double x = minX + random.nextDouble() * (maxX - minX); // מיקום רנדומלי בין גבולות ה-X
            double y = minY + random.nextDouble() * (maxY - minY); // מיקום רנדומלי בין גבולות ה-Y
            double z = -1000; // המרחק מהמצלמה

            // צבע רנדומלי לכוכב
            Color starColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

            // הוספת כדור קטן לייצוג הכוכב
            Sphere star = new Sphere(new Point(x, y, z), 0.1 + random.nextDouble() * 0.5);
            star.setEmission(starColor)
                    .setMaterial(new Material()
                            .setKD(0.5)
                            .setKS(0.5)
                            .setnShininess(30));

            scene.geometries.add(star);
        }
    }

    private void createFlashlight(Point baseCenter, double bodyWidth, double bodyHeight, double bodyDepth, double headRadius) {
        Material flashlightMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(30);
        Color flashlightColor = new Color(30,31,34);


        // יצירת הגוף של הפנס (פוליגון מלבני)
        Point p1 = baseCenter;
        Point p2 = baseCenter.add(new Vector(bodyWidth, 0, 0));
        Point p3 = baseCenter.add(new Vector(bodyWidth, bodyHeight, 0));
        Point p4 = baseCenter.add(new Vector(0, bodyHeight, 0));
        Point p5 = baseCenter.add(new Vector(0, 0, bodyDepth));
        Point p6 = baseCenter.add(new Vector(bodyWidth, 0, bodyDepth));
        Point p7 = baseCenter.add(new Vector(bodyWidth, bodyHeight, bodyDepth));
        Point p8 = baseCenter.add(new Vector(0, bodyHeight, bodyDepth));

        // יצירת הפוליגונים של הקובייה
        Polygon bodyFront = new Polygon(p1, p2, p3, p4);
        Polygon bodyBack = new Polygon(p5, p6, p7, p8);
        Polygon bodyLeft = new Polygon(p1, p5, p8, p4);
        Polygon bodyRight = new Polygon(p2, p6, p7, p3);
        Polygon bodyTop = new Polygon(p4, p3, p7, p8);
        Polygon bodyBottom = new Polygon(p1, p2, p6, p5);

        // הגדרת צבע וחומר לגוף הפנס
        bodyFront.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyBack.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyLeft.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyRight.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyTop.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        bodyBottom.setEmission(flashlightColor).setMaterial(flashlightMaterial);

        // יצירת הראש של הפנס (כדור)
        Point headCenter = baseCenter.add(new Vector(bodyWidth / 2, bodyHeight + headRadius - 2, bodyDepth / 2));
        Sphere head = new Sphere(headCenter, headRadius);
        head.setEmission(flashlightColor).setMaterial(flashlightMaterial);

        // הוספת החלקים לסצנה
        scene.geometries.add(bodyFront, bodyBack, bodyLeft, bodyRight, bodyTop, bodyBottom, head);



        // יצירת כפתור על הגוף
        double buttonRadius = bodyWidth / 4;
        double buttonYOffset = bodyHeight * 0.6; // העלאת הכפתור מעט למעלה
        Point buttonCenter = baseCenter.add(new Vector(bodyWidth / 2, buttonYOffset, bodyDepth + buttonRadius));
        Sphere button = new Sphere(buttonCenter, buttonRadius);
        button.setEmission(flashlightColor).setMaterial(flashlightMaterial);
        scene.geometries.add(button);
    }


    private void addSunFlames(Point sunCenter, double sunRadius) {

        int numFlames = 32;
        double flameHeight = sunRadius * 0.7;

        for (int i = 0; i < numFlames; i++) {
            double angle = 2 * Math.PI * i / numFlames;

            Point basePoint1 = getPointOnSphere(sunCenter, sunRadius, angle);
            Point basePoint2 = getPointOnSphere(sunCenter, sunRadius, angle + Math.PI / numFlames);
            Point flameTop = sunCenter.add(new Vector(
                    Math.cos(angle) * (sunRadius + flameHeight),
                    Math.sin(angle) * (sunRadius + flameHeight),
                    0
            ));

            Color flameColor = new Color(255, 69, 0); // Bright blue

            Triangle flameTriangle = new Triangle(basePoint1, basePoint2, flameTop);
            flameTriangle.setEmission(flameColor)
                    .setMaterial(new Material()
                    .setKD(0.5)
                    .setKS(0.5)
                    .setnShininess(100));

            scene.geometries.add(flameTriangle);
        }
    }

    private void addBlackTriangles() {
        Material blackMaterial = new Material()
                .setKD(0).setKS(0).setnShininess(0)
                .setKT(0).setKR(0);
        Color blackColor = new Color(0, 0, 0);

        Point bottomCenter = new Point(0, -100, 0);

        Point leftTop = new Point(-400, 1000, -0);
        Triangle leftTriangle = new Triangle(bottomCenter, new Point(-300, -100, -0), leftTop);
        leftTriangle.setEmission(blackColor)
                .setMaterial(blackMaterial);

        Point rightTop = new Point(400, 1000, -0);
        Triangle rightTriangle = new Triangle(bottomCenter, new Point(300, -100, -0), rightTop);
        rightTriangle.setEmission(blackColor)
                .setMaterial(blackMaterial);

        scene.geometries.add(leftTriangle, rightTriangle);
    }





    private Point getPointOnSphere(Point center, double radius, double angle) {
        return center.add(new Vector(
                Math.cos(angle) * radius,
                Math.sin(angle) * radius,
                0
        ));
    }

    private void createMoon(Point center, double radius, Material material) {
        Sphere moon = new Sphere(center, radius);
        moon  .setEmission(new Color(0,0,0))  // כחול כהה יותר
                .setMaterial(new Material()
                        .setKD(0.5)
                        .setKS(0.5)
                        .setnShininess(100));  // ערך נמוך יותר לברק פחות חזק

//
// scene.lights.add(new DirectionalLight( new Color(100,70,30),  new Vector(10, -2, 2)));

        scene.geometries.add(moon);
    }

    private void createSaturn(Point center, double radius) {
//        Material saturnMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(30);
//        Color saturnColor = new Color(150, 100, 200); // Purple color for Saturn

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


            scene.geometries.add(ringSegment);
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

    private Point createPointOnRing(Point center, double radius, double angle) {
        double x = center.getXyz().getD1() + radius * Math.cos(angle);
        double z = center.getXyz().getD3() + radius * Math.sin(angle);
        return new Point(x, center.getXyz().getD2(), z);
    }

    private Point rotatePointAroundXZ(Point p, Point center, double cosAngleX, double sinAngleX, double cosAngleZ, double sinAngleZ) {
        double y = p.getXyz().getD2() - center.getXyz().getD2();
        double z = p.getXyz().getD3() - center.getXyz().getD3();
        double x = p.getXyz().getD1() - center.getXyz().getD1();

        double newY = y * cosAngleX - z * sinAngleX;
        double newZ = y * sinAngleX + z * cosAngleX;
        y = newY;
        z = newZ;

        double newX = x * cosAngleZ - y * sinAngleZ;
        newY = x * sinAngleZ + y * cosAngleZ;

        return new Point(
                newX + center.getXyz().getD1(),
                newY + center.getXyz().getD2(),
                newZ + center.getXyz().getD3()
        );
    }


    private void createStarfield() {
        Random random = new Random();
        int numStars = 1000; // מספר הכוכבים שברצונך להוסיף

        // גבולות לטווח ה-X ו-Y, רק בין שני המשולשים השחורים
        double minX = -100;
        double maxX = 400;
        double minY = -180;
        double maxY = 200;
        double z = -1000; // המרחק מהמצלמה

        // יצירת כוכבים רק בתוך האזור המוגדר
        for (int i = 0; i < numStars; i++) {
            double x = minX + random.nextDouble() * (maxX - minX); // מיקום רנדומלי בין גבולות ה-X
            double y = minY + random.nextDouble() * (maxY - minY); // מיקום רנדומלי בין גבולות ה-Y

            // צבע רנדומלי לכוכב
            Color starColor = new Color(200 + random.nextInt(55), 200 + random.nextInt(55), 200 + random.nextInt(55));

            // הוספת כדור קטן לייצוג הכוכב
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
    public void testEnhancedSolarSystem() {
        scene.setAmbientLight(new AmbientLight(new Color(50, 50, 100), 0.1));

     scene.lights.add(new DirectionalLight(new Color(100, 100, 150), new Vector(1, -1, -1)));
//
//        scene.lights.add(new PointLight(new Color(100, 80, 150), new Point(new Double3(-200, 200, 100)))
//                .setKL(0.0000001).setKQ(0.00000001));
//        scene.lights.add(new PointLight(new Color(80, 60, 120), new Point(new Double3(0, 0, 300)))
//                .setKL(0.0000001).setKQ(0.00000001));

        createSolarSystem();
        camera.setImageWriter(new ImageWriter("enhancedSolarSystem", 2000, 2000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build()
                .renderImage()
                .writeToImage();
    }
}