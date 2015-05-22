package gravity.solvers;

import gravity.systems.DESystem;

public class AdamsSolver extends DESolver {
    double[] nextY;
    private double[] lastX = new double[4];
    private double[][] lastY;
    private double[][] f;
    private int state;

    public AdamsSolver(DESystem system, double step, double initialX, double[] initialY, DESolver solver) {
        super(system, step, initialX, initialY);
        int n = system.getSize();
        lastX[0] = initialX;
        lastY = new double[4][n];
        System.arraycopy(initialY, 0, lastY[0], 0, n);
        for (int i = 1; i < 4; i++) {
            lastX[i] = lastX[i - 1] + step;
            solver.next(lastY[i]);
        }
        f = new double[4][n];
        for (int i = 0; i < 4; i++) {
            system.evaluate(lastX[i], lastY[i], f[i]);
        }
        nextY = new double[n];
    }

    @Override
    public void next(double[] result) {
        int n = system.getSize();
        if (state < 3) {
            System.arraycopy(lastY[++state], 0, result, 0, n);
        } else {
            for (int i = 0; i < n; i++) {
                nextY[i] = lastY[3][i] + step * (55 * f[3][i] - 59 * f[2][i] + 37 * f[1][i] - 9 * f[0][i]) / 24;
            }
            for (int i = 0; i < 3; i++) {
                lastX[i] = lastX[i + 1];
                for (int j = 0; j < n; j++) {
                    lastY[i][j] = lastY[i + 1][j];
                    f[i][j] = f[i + 1][j];
                }
            }
            lastX[3] = lastX[2] + step;
            System.arraycopy(nextY, 0, lastY[3], 0, n);
            system.evaluate(lastX[3], lastY[3], f[3]);
            for (int i = 0; i < n; i++) {
                result[i] = lastY[2][i] + step * (9 * f[3][i] + 19 * f[2][i] - 5 * f[1][i] + f[0][i]) / 24;
                lastY[3][i] = result[i];
            }
            system.evaluate(lastX[3], lastY[3], f[3]);
        }
    }

    @Override
    public double currentX() {
        return lastX[3];
    }
}
