package gravity.visualisation;

import gravity.simulation.GObject;
import gravity.solvers.DESolver;

import javax.swing.*;

public class Visualiser extends JFrame {
    private GravityPanel panel;

    public Visualiser(double x, double y, double scale, DESolver solver, GObject[] objects, double[] state) {
        panel = new GravityPanel(x, y, scale, solver, objects, state);
        setTitle("Gravity");
        setSize(1366, 768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        add(panel);
        addKeyListener(panel);
        setVisible(true);
    }

    public static void run(DESolver solver, GObject[] objects, double[] y, double cx, double cy, double cs) {
        SwingUtilities.invokeLater(() -> {
            Visualiser visualiser = new Visualiser(cx, cy, cs, solver, objects, y);
            new Timer(1, e -> visualiser.repaint()).start();
        });
    }

    public void repaint() {
        panel.repaintSimulation();
    }
}