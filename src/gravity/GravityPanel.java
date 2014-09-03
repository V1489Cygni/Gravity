package gravity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GravityPanel extends JPanel {
    private Simulation simulation;

    public GravityPanel(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSimulation(g);
    }

    private void paintSimulation(Graphics g) {
        setBackground(Color.GRAY);
        double d = Math.min(getWidth() / simulation.getWidth(), getHeight() / simulation.getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fill(new Rectangle(0, 0, (int) (simulation.getWidth() * d), (int) (simulation.getHeight() * d)));
        g2d.setColor(Color.WHITE);
        for (GravityObject object : simulation.getObjects()) {
            double r = object.getR() * d;
            Ellipse2D.Double objectShape = new Ellipse2D.Double(object.getX() * d - r, object.getY() * d - r, 2 * r, 2 * r);
            g2d.fill(objectShape);
        }
    }

    public void repaintSimulation() {
        simulation = simulation.move();
        repaint();
    }
}
