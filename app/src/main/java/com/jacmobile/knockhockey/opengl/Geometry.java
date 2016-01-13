package com.jacmobile.knockhockey.opengl;

import com.jacmobile.knockhockey.utils.Logger;

public class Geometry
{
    /**
     * @param from origin
     * @param to   destination
     * @return the Vector between given points
     */
    public static Vector vectorBetween(Point from, Point to)
    {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    /**
     * The length of the cross product gives the area of an imaginary parallelogram having
     * the two vectors as sides. A parallelogram can be thought of as consisting of two triangles,
     * so this is the same as twice the area of the triangle defined by the two vectors.
     */
    public static float distanceBetween(Point point, Ray ray)
    {
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);

        return p1ToPoint.crossProduct(p2ToPoint).length() / ray.vector.length();
    }

    /**
     * @return  true if the ray intersects with the sphere
     */
    public static boolean intersects(Sphere sphere, Ray ray)
    {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * @param ray
     * @param plane
     * @return
     */
    public static Point intersectionPoint(Ray ray, Plane plane)
    {
        Vector rayToPlane = vectorBetween(ray.point, plane.point);
        float scaleFactor = rayToPlane.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);
        return ray.point.translate(ray.vector.scale(scaleFactor));
    }

    public static float clamp(float value, float min, float max)
    {
        return Math.min(max, Math.max(value, min));
    }

////Shapes

    public static class Point
    {
        public final float x, y, z;

        public Point(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translate(Vector v)
        {
            return new Point(x + v.x, y + v.y, z + v.z);
        }

        public Point translateX(float distance)
        {
            return new Point(x + distance, y, z);
        }

        public Point translateY(float distance)
        {
            return new Point(x, y + distance, z);
        }

        public Point translateZ(float distance)
        {
            return new Point(x, y, z + distance);
        }
    }

    public static class Vector
    {
        public final float x, y, z;

        public Vector(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector crossProduct(Vector v)
        {
            return new Vector(
                    (y * v.z) - (z * v.y),
                    (z * v.x) - (x * v.z),
                    (x * v.y) - (y * v.x));
        }

        public float dotProduct(Vector v)
        {
            return x * v.x + y * v.y + z * v.z;
        }

        public Vector scale(float f)
        {
            return new Vector(x * f, y * f, z * f);
        }

        public float length()
        {
            return (float) Math.sqrt(x*x + y*y + z*z);
        }
    }

    public static class Ray
    {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector)
        {
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Plane
    {
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal)
        {
            this.point = point;
            this.normal = normal;
        }
    }

    public static class Circle
    {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius)
        {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale)
        {
            return new Circle(center, radius * scale);
        }
    }

    public static class Sphere
    {
        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius)
        {
            this.center = center;
            this.radius = radius;
        }

        public Sphere scale(float scale)
        {
            return new Sphere(center, scale * radius);
        }
    }

    public static class Cylinder
    {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height)
        {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }
}