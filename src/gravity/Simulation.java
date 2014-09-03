package gravity;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private double width, height;
    private List<GravityObject> objects;

    public Simulation(double width, double height, List<GravityObject> objects) {
        this.width = width;
        this.height = height;
        this.objects = objects;
    }

    public Simulation move() {
        List<GravityObject> newObjects = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            boolean collided = false;
            for (int j = 0; j < i; j++) {
                if (objects.get(i).touches(objects.get(j))) {
                    objects.set(i, objects.get(i).collide(objects.get(j)));
                    objects.remove(j);
                    collided = true;
                    break;
                }
            }
            if (collided) {
                i -= 2;
                continue;
            }
            for (int j = i + 1; j < objects.size(); j++) {
                if (objects.get(i).touches(objects.get(j))) {
                    objects.set(i, objects.get(i).collide(objects.get(j)));
                    objects.remove(j);
                    collided = true;
                    break;
                }
            }
            if (collided) {
                i--;
            }
        }
        for (GravityObject object : objects) {
            newObjects.add(object.move(objects));
        }
        return new Simulation(width, height, newObjects);
    }

    public List<GravityObject> getObjects() {
        return objects;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
