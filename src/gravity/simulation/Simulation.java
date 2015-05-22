package gravity.simulation;

import gravity.systems.DESystem;

public class Simulation implements DESystem {
    private final double G;
    private final GObject[] objects;

    public Simulation(double G, GObject[] objects) {
        this.G = G;
        this.objects = objects;
    }

    @Override
    public void evaluate(double x, double[] y, double[] result) {
        for (int i = 0; i < objects.length; i++) {
            result[4 * i] = y[4 * i + 2];
            result[4 * i + 1] = y[4 * i + 3];
            result[4 * i + 2] = result[4 * i + 3] = 0;
            for (int j = 0; j < objects.length; j++) {
                if (i != j) {
                    double dist2 = Math.pow(y[4 * i] - y[4 * j], 2) + Math.pow(y[4 * i + 1] - y[4 * j + 1], 2);
                    double a = G * objects[j].m / dist2;
                    double dist = Math.sqrt(dist2);
                    result[4 * i + 2] += a * (y[4 * j] - y[4 * i]) / dist;
                    result[4 * i + 3] += a * (y[4 * j + 1] - y[4 * i + 1]) / dist;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return 4 * objects.length;
    }
}