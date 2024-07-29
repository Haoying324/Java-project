package drawer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPoints2D extends ControlPoints {
    ERComponent2D parent;
    ControlPoint2D E, W, N, S, NE, NW, SE, SW;
    CPMouseAdapter2D mouseAdapter;
    ControlPoints2D(ERComponent2D parent) {
        this.parent = parent;
        this.mouseAdapter = new CPMouseAdapter2D();

        E = new ControlPoint2D(mouseAdapter);
        E.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        E.setName("E");
        this.parent.parent.add(E);

        W = new ControlPoint2D(mouseAdapter);
        W.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        W.setName("W");
        this.parent.parent.add(W);

        N = new ControlPoint2D(mouseAdapter);
        N.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        N.setName("N");
        this.parent.parent.add(N);

        S = new ControlPoint2D(mouseAdapter);
        S.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        S.setName("S");
        this.parent.parent.add(S);

        NE = new ControlPoint2D(mouseAdapter);
        NE.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
        NE.setName("NE");
        this.parent.parent.add(NE);

        NW = new ControlPoint2D(mouseAdapter);
        NW.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
        NW.setName("NW");
        this.parent.parent.add(NW);

        SE = new ControlPoint2D(mouseAdapter);
        SE.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        SE.setName("SE");
        this.parent.parent.add(SE);

        SW = new ControlPoint2D(mouseAdapter);
        SW.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
        SW.setName("SW");
        this.parent.parent.add(SW);
    }
    private static class ControlPoint2D extends ControlPoints.ControlPoint {
        ControlPoint2D(MouseAdapter mouseAdapter) {
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
        NE.setVisible(isShow);
        NW.setVisible(isShow);
        SE.setVisible(isShow);
        SW.setVisible(isShow);
    }

    @Override
    void setLocation() {
        int headX = this.parent.firstPoint.x - SIZE;
        int midX = this.parent.firstPoint.x + (this.parent.width - SIZE) / 2;
        int backX = this.parent.firstPoint.x + this.parent.width + 1;
        int headY = this.parent.firstPoint.y - SIZE;
        int midY = this.parent.firstPoint.y + (this.parent.height - SIZE) / 2;
        int backY = this.parent.firstPoint.y + this.parent.height + 1;
        E.setLocation(backX, midY);
        W.setLocation(headX, midY);
        N.setLocation(midX, headY);
        S.setLocation(midX, backY);
        NE.setLocation(backX, headY);
        NW.setLocation(headX, headY);
        SE.setLocation(backX, backY);
        SW.setLocation(headX, backY);
    }

    @Override
    void delete() {
        show(false);
        this.E = this.W = this.N = this.S = this.NE = this.NW = this.SE = this.SW = null;
    }

    private class CPMouseAdapter2D extends java.awt.event.MouseAdapter {
        Point origin, end;
        @Override
        public void mousePressed(MouseEvent e) {
            if (ControlPoints2D.this.parent.parent.state == State.changingComponentName) {
                ControlPoints2D.this.parent.parent.state = State.active;
                ControlPoints2D.this.parent.parent.tfName.readName();
            }
            if (ControlPoints2D.this.parent.parent.state == State.active) {
                ControlPoints2D.this.parent.parent.state = State.resizing;
                origin = e.getLocationOnScreen();
            }
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            if (ControlPoints2D.this.parent.parent.state == State.resizing) {
                ControlPoints2D.this.parent.parent.parent.parent.dataShow.showData();
                end = e.getLocationOnScreen();
                Rectangle oldBounds = ControlPoints2D.this.parent.getBounds();
                int dx, dy, dw, dh;

                dx = dw = end.x - origin.x;
                dy = dh = end.y - origin.y;
                Object obj = e.getSource();
                Component c = (Component) obj;
                switch (c.getName()) {
                    case "SE" -> dx = dy = 0;
                    case "E" -> dx = dy = dh = 0;
                    case "S" -> dx = dy = dw = 0;
                    case "NW" -> {
                        dw = -dw;
                        dh = -dh;
                    }
                    case "N" -> {
                        dx = dw = 0;
                        dh = -dh;
                    }
                    case "W" -> {
                        dy = dh = 0;
                        dw = -dw;
                    }
                    case "SW" -> {
                        dy = 0;
                        dw = -dw;
                    }
                    case "NE" -> {
                        dx = 0;
                        dh = -dh;
                    }
                }
                int nW = ControlPoints2D.this.parent.width + dw;
                int nH = ControlPoints2D.this.parent.height + dh;
                if (nW >= ERComponent2D.minimumSize.width) {
                    origin.x = end.x;
                }
                else {
                    dx = dw = 0;
                }
                if (nH >= ERComponent2D.minimumSize.height) {
                    origin.y = end.y;
                }
                else {
                    dy = dh = 0;
                }
                ControlPoints2D.this.parent.width += dw;
                ControlPoints2D.this.parent.height += dh;

                ControlPoints2D.this.parent.setBounds(
                        new Point(oldBounds.x + dx,oldBounds.y + dy),
                        oldBounds.width + dw,
                        oldBounds.height + dh
                );
                for (Line l : ControlPoints2D.this.parent.lines) {
                    if (l.firstERC != ControlPoints2D.this.parent) {
                        l.swapPoint();
                    }
                    l.moving(dx, dy);
                    l.setMinLine();
                }
                ControlPoints2D.this.setLocation();
                ControlPoints2D.this.parent.parent.repaint();
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if (ControlPoints2D.this.parent.parent.state == State.resizing) {
                ControlPoints2D.this.parent.parent.state = State.active;
                origin = end = null;
            }
        }
    }
}
