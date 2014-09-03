package gravity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GravityFrame extends JFrame {
    private GravityPanel panel;

    public GravityFrame(Simulation simulation) {
        panel = new GravityPanel(simulation);
        setTitle("Gravity");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //setSize(500, 500);
        setUndecorated(true);
        add(panel);
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
        setVisible(true);
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(this);
    }

    private static class KeyDispatcher implements KeyEventDispatcher {
        private boolean pressed;

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                pressed = true;
            } else if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_ESCAPE && pressed) {
                System.exit(0);
            }
            return false;
        }
    }

    public void repaint() {
        panel.repaintSimulation();
    }
}