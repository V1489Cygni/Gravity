package gravity;

public class Vector {
    private double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector div(double d) {
        return new Vector(x / d, y / d);
    }

    public Vector mul(double d) {
        return new Vector(x * d, y * d);
    }

    public Vector normalize() {
        double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return new Vector(Constants.G * x / r, Constants.G * y / r);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
