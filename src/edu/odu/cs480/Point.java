package edu.odu.cs480;

public class Point {
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        x = 0.0;
        y = 0.0;
    }

    public Point(Point other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Add Point b to the current point
     * @param b
     */
    public void add(Point b) {
        x += b.getX();
        y += b.getY();
    }

    /**
     * Compute the difference between two points and return the result
     * @param a
     * @param b
     * @return
     */
    public static Point difference(Point a, Point b) {
        Point point = new Point();
        point.setX(a.getX() - b.getX());
        point.setY(a.getY() - b.getY());
        return point;
    }

    public static Point add(Point a, Point b) {
        Point point = new Point();
        point.setX(a.getX() + b.getX());
        point.setY(a.getY() + b.getY());
        return point;
    }

    public static Point multiply(Point a, double multiple) {
        return new Point(a.getX() * multiple, a.getY() * multiple);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y;
    }
}
