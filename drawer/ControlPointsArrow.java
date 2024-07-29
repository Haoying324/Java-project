package drawer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPointsArrow extends ControlPoints {
    Canvas parent;
    ControlPointArrow N, E, S, W;
    CPMouseAdapterArrow mouseAdapter;
    static int SIZE = 10;
    ControlPointsArrow(Canvas parent) {
        this.parent = parent;
        this.mouseAdapter = new CPMouseAdapterArrow();

        E = new ControlPointArrow(mouseAdapter);
        E.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        E.setName("E");
        this.parent.parent.add(E);

        W = new ControlPointArrow(mouseAdapter);
        W.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        W.setName("W");
        this.parent.parent.add(W);

        N = new ControlPointArrow(mouseAdapter);
        N.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        N.setName("N");
        this.parent.parent.add(N);

        S = new ControlPointArrow(mouseAdapter);
        S.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        S.setName("S");
        this.parent.parent.add(S);

    }
    private static class ControlPointArrow extends ControlPoints.ControlPoint {
        ControlPointArrow(MouseAdapter mouseAdapter) {
            super(mouseAdapter);
            setSize(SIZE, SIZE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillOval(0, 0, SIZE, SIZE);
        }
    }

    @Override
    public void show(boolean isShow) {
        if (isShow) {
            setLocation();
        }
        E.setVisible(isShow);
        W.setVisible(isShow);
        N.setVisible(isShow);
        S.setVisible(isShow);
    }

    @Override
    void setLocation() {
        int headX = this.parent.focusedComponent.firstPoint.x - SIZE;
        int midX = this.parent.focusedComponent.firstPoint.x + (((ERComponent2D)this.parent.focusedComponent).width - SIZE) / 2;
        int backX = this.parent.focusedComponent.firstPoint.x + ((ERComponent2D)this.parent.focusedComponent).width + 1;
        int headY = this.parent.focusedComponent.firstPoint.y - SIZE;
        int midY = this.parent.focusedComponent.firstPoint.y + (((ERComponent2D)this.parent.focusedComponent).height - SIZE) / 2;
        int backY = this.parent.focusedComponent.firstPoint.y + ((ERComponent2D)this.parent.focusedComponent).height + 1;
        E.setLocation(backX, midY);
        W.setLocation(headX, midY);
        N.setLocation(midX, headY);
        S.setLocation(midX, backY);
    }

    @Override
    void delete() {

    }

    private static class CPMouseAdapterArrow extends java.awt.event.MouseAdapter {
        Point origin, end;

        @Override
        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();
            //ControlPointsArrow.this.parent.componentsMenu.show(e.getComponent(), point.x, point.y);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }
        @Override
        public void mouseDragged(MouseEvent e) {

        }
        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }
}
