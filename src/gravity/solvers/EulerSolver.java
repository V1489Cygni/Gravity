package gravity.solvers;

import gravity.systems.DESystem;

public class EulerSolver extends DESolver {
    private double lastX;
    private double[] lastY;
    private double[] f;

    public EulerSolver(DESystem system, double step, double initialX, double[] initialY) {
        super(system, step, initialX, initialY);
        int n = system.getSize();
        lastX = initialX;
        lastY = new double[n];
        System.arraycopy(initialY, 0, lastY, 0, n);
        f = new double[n];
    }

    @Override
    public void next(double[] result) {
        int n = system.getSize();
        system.evaluate(lastX, lastY, f);
        lastX += step;
        for (int i = 0; i < n; i++) {
            result[i] = lastY[i] + step * f[i];
            lastY[i] = result[i];
        }
    }

    @Override
    public double currentX() {
        return lastX;
    }
}
