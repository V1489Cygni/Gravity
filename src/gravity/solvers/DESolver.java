package gravity.solvers;

import gravity.systems.DESystem;

public abstract class DESolver {
    protected DESystem system;
    protected double step;
    protected double initialX;
    protected double[] initialY;

    public DESolver(DESystem system, double step, double initialX, double[] initialY) {
        this.system = system;
        this.step = step;
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public abstract void next(double[] result);

    public abstract double currentX();
}
