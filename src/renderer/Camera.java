package renderer;

import primitives.Point;
import primitives.Util;
import primitives.Vector;

public class Camera implements Cloneable {

    private Point place;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private Double hight = 0.0;
    private Double width = 0.0;
    private Double distance = 0.0;

    public static class Builder {
        private final Camera camera = new Camera();

        public Builder setLocation(Point p) {
            camera.place = p;
            return this;
        }

        public Builder setDirection(Vector up, Vector to) {
            if (up.dotProduct(to) != 0)
                throw new IllegalArgumentException("The vectors are not orthogonals");
            camera.vUp = up.normalize();
            camera.vTo = to.normalize();
            return this;
        }

        public Builder setVpSize(double w,double h)
        {
            camera.width=w;
            camera.hight=h;
            return this;
        }

        public Builder setVpDistance(double d)
        {
            camera.distance=d;
            return this;
        }

        public Camera build()
        {
            if()
        }


    }
        public Camera() {
            vTo = new Vector(0, 1, 0);
            vRight = new Vector(1, 0, 0);
            vUp = new Vector(0, 0, 1);
            place = new Point(0, 0, 0);

        }



        public Double getDistance() {
            return distance;
        }

        public Double getWidth() {
            return width;
        }

        public Double getHight() {
            return hight;
        }

        public Thread.Builder getBuilder() {
        }

        public Ray constructRay(int nX, int nY, int j, int i) {
        }

    }
