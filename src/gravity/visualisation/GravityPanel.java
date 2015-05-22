package gravity.visualisation;

import gravity.simulation.GObject;
import gravity.solvers.DESolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;

public class GravityPanel extends JPanel implements KeyListener {

    private double x, y;
    private double scale;
    private double addX, addY;
    private double speed = 1;
    private boolean running = true, info;

    private DESolver solver;
    private GObject[] objects;
    private double[] state;

    private DecimalFormat format = new DecimalFormat("##.##");

    public GravityPanel(double x, double y, double scale, DESolver solver, GObject[] objects, double[] state) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.solver = solver;
        this.objects = objects;
        this.state = state;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSimulation(g);
    }

    private void paintSimulation(Graphics g) {
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < objects.length; i++) {
            double xv = (state[4 * i] - x - objects[i].r) * scale;
            double yv = (state[4 * i + 1] - y - objects[i].r) * scale;
            Ellipse2D.Double objectShape = new Ellipse2D.Double(xv + getWidth() / 2, yv + getHeight() / 2, 2 * objects[i].r * scale, 2 * objects[i].r * scale);
            g2d.fill(objectShape);
        }
        if (info) {
            g2d.setColor(Color.GREEN);
            g2d.drawString("x=" + format.format(x), 10, 20);
            g2d.drawString("y=" + format.format(y), 10, 35);
            g2d.drawString("t=" + format.format(solver.currentX()), 10, 50);
        }
    }

    public void repaintSimulation() {
        if (running)
            solver.next(state);
        this.x += addX * speed / scale;
        this.y += addY * speed / scale;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            addY = -2;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            addY = 2;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            addX = -2;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            addX = 2;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            scale *= 1.6;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            scale /= 1.6;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            speed *= 1.6;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            speed /= 1.6;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            addY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            addY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            addX = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            addX = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            running = !running;
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            info = !info;
        }
    }
}

