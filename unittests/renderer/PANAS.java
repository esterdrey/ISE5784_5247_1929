package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;
import org.junit.jupiter.api.Test;

public class PANAS {
    private final Scene scene = new Scene("Enhanced Solar System Scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene));

    private void createSolarSystem() {
        createFlashlight(new Point(new Double3(0, -100, 0)), 10, 20, 10, 7);
    }

    private void createFlashlight(Point baseCenter, double bodyWidth, double bodyHeight, double bodyDepth, double headRadius) {
        Material flashlightMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(30);
        Color flashlightColor = new Color(255, 0, 0); // צבע אדום לפנס
        Color blackColor = new Color(0, 0, 0); // צבע שחור לפסים ולכפתור

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

        // הוספת פסים שחורים לגוף הפנס
        double stripeWidth = bodyWidth * 0.8; // רוחב כל פס
        double stripeHeight = bodyHeight / 8; // גובה כל פס
        double stripeDepth = 0.1; // עומק כל פס
        double stripeOffset = bodyWidth * 0.1; // המרחק מהקצה
        double yOffset = bodyHeight * 0.2; // העלאת הפסים מעט למעלה

        for (int i = 0; i < 3; i++) {
            double yStart = stripeHeight * (2 * i + 1) + yOffset;
            Point sp1 = p1.add(new Vector(stripeOffset, yStart, stripeDepth));
            Point sp2 = sp1.add(new Vector(stripeWidth, 0, 0));
            Point sp3 = sp2.add(new Vector(0, stripeHeight, 0));
            Point sp4 = sp1.add(new Vector(0, stripeHeight, 0));
            Polygon stripe = new Polygon(sp1, sp2, sp3, sp4);
            stripe.setEmission(blackColor).setMaterial(flashlightMaterial);
            scene.geometries.add(stripe);
        }

        // יצירת כפתור על הגוף
        double buttonRadius = bodyWidth / 4;
        double buttonYOffset = bodyHeight * 0.6; // העלאת הכפתור מעט למעלה
        Point buttonCenter = baseCenter.add(new Vector(bodyWidth / 2, buttonYOffset, bodyDepth + buttonRadius));
        Sphere button = new Sphere(buttonCenter, buttonRadius);
        button.setEmission(blackColor).setMaterial(flashlightMaterial);
        scene.geometries.add(button);
    }

    @Test
    public void testEnhancedSolarSystem() {
        scene.setAmbientLight(new AmbientLight(new Color(50, 50, 100), 0.1));

        scene.lights.add(new DirectionalLight(new Color(100, 100, 150), new Vector(1, -1, -1)));

        scene.lights.add(new PointLight(new Color(100, 80, 150), new Point(new Double3(-200, 200, 100)))
                .setKL(0.0000001).setKQ(0.00000001));
        scene.lights.add(new PointLight(new Color(80, 60, 120), new Point(new Double3(0, 0, 300)))
                .setKL(0.0000001).setKQ(0.00000001));

        createSolarSystem();
        camera.setImageWriter(new ImageWriter("PANAS", 2000, 2000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build()
                .renderImage()
                .writeToImage();
    }
}
