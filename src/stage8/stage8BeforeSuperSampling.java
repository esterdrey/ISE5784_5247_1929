package stage8;
import geometries.*;
import lighting.*;
import org.junit.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;


public class stage8BeforeSuperSampling {
    private final Scene scene = new Scene("Enhanced Solar System Scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setLocation(new Point(0, 0, 1000))
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(new SimpleRayTracer(scene));



    private void createSolarSystem() {
        Material rockMaterial = new Material().setKD(0.3).setKS(0.7).setnShininess(30);

        createSaturn(new Point(new Double3(-55, -60, -400)), 25);

        // יצירת ירח דמוי כדור
        Point moonCenter = new Point(new Double3(0, 0, -500));
        createMoon(moonCenter, 40, rockMaterial);

        // יצירת שמש כתומה לוהטת מימין לירח
        Point sunCenter = new Point(new Double3(60, 65, -50));
        double sunRadius = 25;
        Sphere sun = new Sphere(sunCenter, sunRadius);

        // קביעת מאפייני החומר לשמש
        Material sunMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(100);
        sun.setEmission(new Color(255, 69, 0)).setMaterial(sunMaterial); // כתום זורח

        addSunFlames(sunCenter, sunRadius);

        // הוספת תאורה שתבוא על השמש ותאיר אותה ואת הסביבה
        Vector[] lightDirections = {
                new Vector(1, 1, -1),
                new Vector(-1, 1, -1),
                new Vector(1, -1, -1),
                new Vector(-1, -1, -1)
        };

        for (Vector direction : lightDirections) {
            Point lightPosition = sunCenter.add(direction.scale(sunRadius * 2));
            scene.lights.add(new SpotLight(new Color(255, 140, 0), lightPosition, direction.scale(-1))
                    .setKL(0.0001).setKQ(0.000001)
                    .setNarrowBeam(20));
        }



        scene.geometries.add(sun);
    }

    private void addSunFlames(Point sunCenter, double sunRadius) {
        Material flameMaterial = new Material().setKD(0.4).setKS(0.3).setnShininess(40);

        int numFlames = 32; // מספר הלהבות
        double flameHeight = sunRadius * 0.7; // גובה הלהבות

        for (int i = 0; i < numFlames; i++) {
            double angle = 2 * Math.PI * i / numFlames;

            Point basePoint1 = getPointOnSphere(sunCenter, sunRadius, angle);
            Point basePoint2 = getPointOnSphere(sunCenter, sunRadius, angle + Math.PI / numFlames);
            Point flameTop = sunCenter.add(new Vector(
                    Math.cos(angle) * (sunRadius + flameHeight),
                    Math.sin(angle) * (sunRadius + flameHeight),
                    0
            ));

            Color flameColor = new Color(255, 69, 0); // כתום זורח

            Triangle flameTriangle = new Triangle(basePoint1, basePoint2, flameTop);
            flameTriangle .setEmission(flameColor)
                    .setMaterial(flameMaterial);

            scene.geometries.add(flameTriangle);
        }
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
        moon.setEmission(new Color(5, 5, 5)).setMaterial(material);

        // הוספת אור לבן בהיר בפינה העליונה השמאלית של הירח
        scene.lights.add(new PointLight(new Color(255, 255, 255), center.add(new Vector(-150, 150, 0)))
                .setKL(0.0001).setKQ(0.000001));

        scene.geometries.add(moon);
    }

    private void createSaturn(Point center, double radius) {
        Material saturnMaterial = new Material().setKD(0.5).setKS(0.5).setnShininess(30);
        Color saturnColor = new Color(210, 180, 140); // צבע חום בהיר לשבתאי

        // יצירת גוף שבתאי
        Sphere saturnBody = new Sphere(center, radius);
        saturnBody.setEmission(saturnColor)
                .setMaterial(saturnMaterial);

        // יצירת הטבעת של שבתאי
        double ringInnerRadius = radius * 1.2;
        double ringOuterRadius = radius * 2;
        double ringThickness = radius * 0.1;

        // סיבוב נקודות הטבעת באופן ידני
        double angle = Math.PI / 6; // 30 מעלות
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        // יצירת מספר קטעים עבור הטבעת כדי להפוך אותה ליותר נראית
        int segments = 50;
        for (int i = 0; i < segments; i++) {
            double startAngle = 2 * Math.PI * i / segments;
            double endAngle = 2 * Math.PI * (i + 1) / segments;

            Point p1 = rotatePointAroundY(createPointOnRing(center, ringInnerRadius, startAngle), center, cosAngle, sinAngle);
            Point p2 = rotatePointAroundY(createPointOnRing(center, ringOuterRadius, startAngle), center, cosAngle, sinAngle);
            Point p3 = rotatePointAroundY(createPointOnRing(center, ringOuterRadius, endAngle), center, cosAngle, sinAngle);
            Point p4 = rotatePointAroundY(createPointOnRing(center, ringInnerRadius, endAngle), center, cosAngle, sinAngle);

            Geometry ringSegment = new Polygon(p1, p2, p3, p4)
                    .setEmission(new Color(153, 76, 0)) // צבע אדום לטבעת
                    .setMaterial(new Material().setKD(0.4).setKS(0.6).setnShininess(50));

            scene.geometries.add(ringSegment);
        }

        scene.geometries.add(saturnBody);
    }

    private Point createPointOnRing(Point center, double radius, double angle) {
        double x = center.getXyz().getD1() + radius * Math.cos(angle);
        double z = center.getXyz().getD3() + radius * Math.sin(angle);
        return new Point(x, center.getXyz().getD2(), z);
    }

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


    @Test
    public void testEnhancedSolarSystem() {
        // Decrease ambient light to darken the scene
        // הפחתת אור הסביבה כדי להחשיך את הסצנה
        scene.setAmbientLight(new AmbientLight(new Color(5, 5, 10), 0.1));

        // תאורה כיוונית מותאמת בעוצמה נמוכה מאוד
        scene.lights.add(new DirectionalLight(new Color(50, 50, 40), new Vector(1, -1, -1)));

        // אורות נקודתיים מותאמים בעוצמה נמוכה מאוד
        scene.lights.add(new PointLight(new Color(50, 40, 40), new Point(new Double3(-200, 200, 100)))
                .setKL(0.0000001).setKQ(0.00000001));
        scene.lights.add(new PointLight(new Color(40, 20, 20), new Point(new Double3(0, 0, 300)))
                .setKL(0.0000001).setKQ(0.00000001));


        createSolarSystem();
        camera.setImageWriter(new ImageWriter("stage 8 before", 2000, 2000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build()
                .renderImage()
                .writeToImage();
    }
}