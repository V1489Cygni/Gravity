package gravity;

import java.util.List;

public class GravityObject {
    private Vector position;
    private double m, r;
    private Vector v;

    public GravityObject(Vector position, double m, double r, Vector v) {
        this.position = position;
        this.m = m;
        this.r = r;
        this.v = v;
    }

    public GravityObject move(List<GravityObject> objects) {
        Vector a = new Vector(0, 0);
        for (GravityObject object : objects) {
            if (object != this) {
                double d1 = object.getX() - getX(), d2 = object.getY() - getY();
                double r2 = Math.pow(d1, 2) + Math.pow(d2, 2);
                a = a.add(new Vector(d1, d2).normalize().mul(object.m).div(r2));
            }
        }
        return new GravityObject(position.add(v), m, r, v.add(a));
    }

    public GravityObject collide(GravityObject object) {
        double nr = Math.sqrt(Math.pow(object.r, 2) + Math.pow(r, 2));
        Vector nv = v.mul(m).add(object.v.mul(object.m)).div(object.m + m);
        return new GravityObject(object.position.add(position).div(2), object.m + m, nr, nv);
    }

    public boolean touches(GravityObject object) {
        return Math.sqrt(Math.pow(object.getX() - getX(), 2) + Math.pow(object.getY() - getY(), 2)) <= object.r + r;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getR() {
        return r;
    }
}
