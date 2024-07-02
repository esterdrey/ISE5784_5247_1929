package renderer;

import primitives.Point;
import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.*;


/**
 * Camera class represents a camera in 3D space using the Builder Pattern. The
 * class implements Cloneable to support cloning of the Camera object.
 *
 * @author Ester Drey and Avigail Bash
 */
public class Camera implements Cloneable {

    /**
     * The position of the camera in the 3D space.
     */
    private Point place;

    /**
     * The direction vector representing the up direction of the camera.
     */
    private Vector vUp;

    /**
     * The direction vector towards which the camera is pointing.
     */
    private Vector vTo;

    /**
     * The direction vector representing the right direction of the camera.
     */
    private Vector vRight;

    /**
     * The height of the view plane.
     */
    private Double height = 0.0;

    /**
     * The width of the view plane.
     */
    private Double width ;

    /**
     * The distance from the camera to the view plane.
     */
    private Double distance ;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    /**
     * Private constructor
     */
    private Camera() {
        height = 0.0;
        width = 0.0;
        distance = 0.0;
    }

    public Double getDistance() {
        return distance;
    }

    /**
     * Retrieves the width of the view plane.
     *
     * @return The width of the view plane.
     */
    public Double getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the view plane.
     *
     * @return The height of the view plane.
     */
    public Double getHeight() {
        return height;
    }


    /**
     * Returns a new Builder object for Camera.
     *
     * @return a Builder object
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray from the camera through a pixel.
     *
     * @param nX The number of pixels in the x-axis.
     * @param nY The number of pixels in the y-axis.
     * @param j  The x-coordinate of the pixel.
     * @param i  The y-coordinate of the pixel.
     * @return a Ray object
     */
    public Ray constructRay(int nX, int nY, int j, int i) {

        Point Pc = place.add(vTo.scale(distance));
        double Ry = height / nY;
        double Rx = width / nX;

        double Xj = (j - (nX - 1) / 2.0) * Rx;
        double Yi = -1 * (i - (nY - 1) / 2.0) * Ry;

        Point Pij = Pc;
        if (!isZero(Xj)) {
            Pij = Pij.add(vRight.scale(Xj));
        }

        if (!isZero(Yi)) {
            Pij = Pij.add(vUp.scale(Yi));
        }

        return new Ray(place, Pij.subtract(place));


    }

    /**
     * This method performs image rendering by casting rays of light for each pixel
     * in the image and computing their color.
     *
     * @return The current state of the camera, for further use within this class or
     * in closely related classes.
     */
    public Camera renderImage() {
        for (int i = 0; i < imageWriter.getNy(); i++)
            for (int j = 0; j < imageWriter.getNx(); j++)
                castRay(imageWriter.getNx(), imageWriter.getNy(), j, i);
        return this;
    }

    /**
     * Casts a ray through a specific pixel in the image
     *
     * @param nX The width of the image.
     * @param nY The height of the image.
     * @param j  The column index of the pixel.
     * @param i  The row index of the pixel.
     */
    private void castRay(int nX, int nY, int j, int i) {
        Ray ray = constructRay(nX, nY, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * This method prints a grid pattern onto the image, with specified intervals
     * between grid lines and color for the grid lines.
     *
     * @param interval The interval between grid lines. Must be greater than 0.
     * @param color    The color of the grid lines.
     * @return The current state of the camera, for further use within this class or
     * in closely related classes.
     */
    public Camera printGrid(int interval, Color color) {
        for (int j = 0; j < imageWriter.getNx(); j++)
            for (int i = 0; i < imageWriter.getNy(); i++)
                if (isZero(j % interval) || isZero(i % interval))
                    imageWriter.writePixel(j, i, color);
        return this;
    }

    /**
     * Writes the image to a file using the appropriate method of the image writer.
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    /**
     * Builder class for Camera, implementing the Builder Pattern.
     */
    public static class Builder {

        /**
         * Represents a builder for constructing Camera objects.
         */
        private final Camera camera = new Camera();

        /**
         * Sets the position of the camera.
         *
         * @param p location The position to set for the camera.
         * @return The current Builder object.
         */
        public Builder setLocation(Point p) {
            camera.place = p;
            return this;
        }

        /**
         * Sets the direction of the camera.
         *
         * @param to the direction vector (towards)
         * @param up the direction vector (up)
         * @return the current Builder object
         */
        public Builder setDirection(Vector to, Vector up) {
            if (up.dotProduct(to) != 0)
                throw new IllegalArgumentException("The vectors are not orthogonals");
            camera.vUp = up.normalize();
            camera.vTo = to.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Sets the view plane size.
         *
         * @param w the view plane width
         * @param h the view plane height
         * @return the current Builder object
         */
        public Builder setVpSize(double w, double h) {
            if (w < 0 || h < 0)
                throw new IllegalArgumentException("One of the parameters is negative");
            camera.width = w;
            camera.height = h;
            return this;
        }

        /**
         * Sets the view plane distance.
         *
         * @param d the view plane distance
         * @return the current Builder object
         */
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

        /**
         * Sets the ray tracer base used by the camera to trace rays and render the scene.
         *
         * @param simpleRayTracer the ray tracer base to set
         * @return the current Builder object
         */
        public Builder setRayTracer(SimpleRayTracer simpleRayTracer) {
            camera.rayTracer = simpleRayTracer;
            return this;
        }

        /**
         * Sets the image writer used by the camera to write the rendered image.
         *
         * @param imageWriter the image writer to set
         * @return the current Builder object
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Builds the Camera object.
         *
         * @return the built Camera object
         */
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
                throw new MissingResourceException(missingResource, Camera.class.getName(), "imageWriter");

            if (camera.rayTracer == null)
                throw new MissingResourceException(missingResource, Camera.class.getName(), "rayTracer");


            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return camera;
        }
    }
}
