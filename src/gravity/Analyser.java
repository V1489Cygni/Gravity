package gravity;

import gravity.simulation.GObject;
import gravity.simulation.Simulation;
import gravity.solvers.AdamsSolver;
import gravity.solvers.DESolver;
import gravity.solvers.EulerSolver;
import gravity.solvers.RKSolver;

public class Analyser {
    private static final double r = 10_000;

    private static final String solverName = "E";
    private static DESolver solver;

    public static void main(String[] args) {
        testPlanet("Mercury", 69_817_445_000d, 38860, 57_909_227_000d, 0.20563593);
        testPlanet("Earth", 152_098_232_000d, 29295, 149_598_261_000d, 0.01671123);
    }

    private static void testPlanet(String name, double d, double v, double a, double e) {
        load(6.67384, 1, 0, 0, 0, 0, 1.9891 * Math.pow(10, 19), d, 0, 0, v, 0.001);
        double a2 = Math.pow(a, 2), zero = d - a, b2 = a2 * (1 - Math.pow(e, 2));
        double[] last = new double[]{0, 0, 0, 0, d, 0, 0, v};
        double[] next = new double[8];
        boolean down = false;
        double maxS = 0, minS = Double.MAX_VALUE;
        double maxD = 0;
        while (true) {
            solver.next(next);
            if (next[5] < 0)
                down = true;
            if (down && next[5] >= 0)
                break;
            double res = Math.abs(next[5] * last[4] - next[4] * last[5]) / 2;
            maxS = Math.max(maxS, res);
            minS = Math.min(minS, res);
            double nd = Math.abs(1 - Math.pow(next[4] - zero, 2) / a2 - Math.pow(next[5], 2) / b2);
            maxD = Math.max(maxD, nd);
            double[] temp = next;
            next = last;
            last = temp;
        }
        System.out.println(name + " test finished: time=" + solver.currentX() / (24 * 3600) + " days, max_s=" + maxS + ", min_s=" + minS + ", gap=" + (maxS - minS) / maxS + ", max_d=" + maxD + ", r=" + Math.pow(a, 3) / Math.pow(solver.currentX(), 2));
    }

    private static void load(double G, double step, double... data) {
        if (data.length % 5 != 0) {
            throw new AssertionError("Error: Incorrect test data!");
        }
        double[] y = new double[4 * data.length / 5];
        double[] m = new double[data.length / 5];
        for (int i = 0; i < m.length; i++) {
            m[i] = data[5 * i + 4];
            System.arraycopy(data, 5 * i, y, 4 * i, 4);
        }
        loadSolver(G, step, m, y);
    }

    private static void loadSolver(double G, double step, double[] m, double[] y) {
        GObject[] objects = new GObject[m.length];
        for (int i = 0; i < m.length; i++)
            objects[i] = new GObject(m[i], r);
        switch (solverName) {
            case "E":
                solver = new EulerSolver(new Simulation(G, objects), step, 0, y);
                break;
            case "R":
                solver = new RKSolver(new Simulation(G, objects), step, 0, y);
                break;
            case "A":
                solver = new AdamsSolver(new Simulation(G, objects), step, 0, y, new EulerSolver(new Simulation(G, objects), step, 0, y));
                break;
        }
    }
}
