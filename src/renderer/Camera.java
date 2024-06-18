package renderer;

import primitives.Point;
import primitives.*;

import java.util.MissingResourceException;

public class Camera implements Cloneable {

    private Point place;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private Double height = 0.0;
    private Double width = 0.0;
    private Double distance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;


    public Camera() {
        height = 0.0;
        width = 0.0;
        distance = 0.0;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {

        if (nY == 0 || nX == 0) {
            throw new IllegalArgumentException("It is impossible to divide by 0");
        }
        Point Pc = place.add(vTo.scale(distance));
        double Ry = height / nY;
        double Rx = width / nX;

        double Xj = (j - (nX - 1) / 2.0) * Rx;
        double Yi = -1 * (i - (nY - 1) / 2.0) * Ry;

        Point Pij = Pc;
        if (!Util.isZero(Xj)) {
            Pij = Pij.add(vRight.scale(Xj));
        }

        if (!Util.isZero(Yi)) {
            Pij = Pij.add(vUp.scale(Yi));
        }

        return new Ray(place, Pij.subtract(place));


    }

    public Camera renderImage()
    {
        for(int i=0;i< imageWriter.getNy();i++)
            for(int j=0;j< imageWriter.getNx();j++)
                castRay(imageWriter.getNx(),imageWriter.getNy(),j,i);
        return this;
    }

    private void castRay(int nX, int nY, int j, int i) {
        Ray ray=constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j,i,color);
    }


    public Camera printGrid(int interval, Color color) {
        for (int j = 0; j < imageWriter.getNx(); j++)
            for (int i = 0; i < imageWriter.getNy(); i++)
                if (Util.isZero(j % interval) ||Util.isZero(i % interval))
                    imageWriter.writePixel(j, i, color);
        return this;
    }

    public void writeToImage()
    {
        imageWriter.writeToImage();
    }


    public static class Builder {
        private final Camera camera = new Camera();

        public Builder setLocation(Point p) {
            camera.place = p;
            return this;
        }

        public Builder setDirection(Vector to, Vector up) {
            if (up.dotProduct(to) != 0)
                throw new IllegalArgumentException("The vectors are not orthogonals");
            camera.vUp = up.normalize();
            camera.vTo = to.normalize();
            return this;
        }

        public Builder setVpSize(double w, double h) {
            if (w < 0 || h < 0)
                throw new IllegalArgumentException("One of the parameters is negative");
            camera.width = w;
            camera.height = h;
            return this;
        }

        public Builder setVpDistance(double d) {
            if (d <= 0)
                throw new IllegalArgumentException("The distance can not be zero or negative number");
            camera.distance = d;
            return this;
        }

        public Builder setRayTracerBase(RayTracerBase rayTracerBase) {
            camera.rayTracer = rayTracerBase;
            return this;
        }
        public Builder setRayTracer(SimpleRayTracer simpleRayTracer) {
            camera.rayTracer=simpleRayTracer;
            return this;
        }

        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        public Camera build() {
            String missingResource = "Missing Resource";
            if (camera.place == null)
                throw new MissingResourceException(missingResource, Camera.class.getSimpleName(), "location");
            if (camera.vTo == null || camera.vUp == null)
                throw new MissingResourceException(missingResource, Camera.class.getSimpleName(), "direction");
            if (camera.height == 0.0 || camera.width == 0.0)
                throw new MissingResourceException(missingResource, Camera.class.getSimpleName(), "vpSize");// parameters== null מאותחל
            if (camera.distance == 0.0)
                throw new MissingResourceException(missingResource, Camera.class.getSimpleName(), "vpDistance");

            if (camera.vTo.crossProduct(camera.vUp).length() == 0)
                throw new IllegalArgumentException("Vto and Vup are parallel");
            if (camera.height < 0.0 || camera.width < 0.0)
                throw new IllegalArgumentException("Negative size");// checking the parameters himself
            if (camera.distance < 0.0)
                throw new IllegalArgumentException("Negative distance");
            if (camera.imageWriter == null)
                throw new MissingResourceException(missingResource,Camera.class.getName(),"imageWriter");

            if (camera.rayTracer == null)
                throw new MissingResourceException(missingResource, Camera.class.getName(), "rayTracer");


            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return camera;
        }



    }


}
