package gravity.solvers;

import gravity.systems.DESystem;

public class RKSolver extends DESolver {
    private double lastX;
    private double[] lastY;
    private double[] k1, k2, k3, k4, temp;

    public RKSolver(DESystem system, double step, double initialX, double[] initialY) {
        super(system, step, initialX, initialY);
        int n = system.getSize();
        lastX = initialX;
        lastY = new double[n];
        System.arraycopy(initialY, 0, lastY, 0, n);
        k1 = new double[n];
        k2 = new double[n];
        k3 = new double[n];
        k4 = new double[n];
        temp = new double[n];
    }

    @Override
    public void next(double[] result) {
        int n = system.getSize();
        system.evaluate(lastX, lastY, k1);
        lastX += step / 2;
        for (int i = 0; i < n; i++) {
            temp[i] = lastY[i] + step * k1[i] / 2;
        }
        system.evaluate(lastX, temp, k2);
        for (int i = 0; i < n; i++) {
            temp[i] = lastY[i] + step * k2[i] / 2;
        }
        system.evaluate(lastX, temp, k3);
        lastX += step / 2;
        for (int i = 0; i < n; i++) {
            temp[i] = lastY[i] + step * k3[i];
        }
        system.evaluate(lastX, temp, k4);
        for (int i = 0; i < n; i++) {
            result[i] = lastY[i] + step * (k1[i] + 2 * k2[i] + 2 * k3[i] + k4[i]) / 6;
            lastY[i] = result[i];
        }
    }

    @Override
    public double currentX() {
        return lastX;
    }
}
