package drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public abstract class ControlPoints {
    abstract void show(boolean isShow);
    abstract void setLocation();
    static int SIZE = 6;
    protected static class ControlPoint extends JPanel {
        ControlPoint(MouseAdapter mouseAdapter) {
            this.setVisible(false);
            this.addMouseListener(mouseAdapter);
            this.addMouseMotionListener(mouseAdapter);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.black);
        }
    }
    abstract void delete();
}
