package drawer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPointsLine extends ControlPoints {
    Line parent;
    ControlPoint first, second;
    CPMouseAdapterLine mouseAdapter;
    static int SIZE = 6;
    ControlPointsLine(Line parent) {
        this.parent = parent;
        this.mouseAdapter = new CPMouseAdapterLine();

        first = new ControlPointLine(this.mouseAdapter);
        first.setName("First");
        this.parent.parent.add(first);

        second = new ControlPointLine(this.mouseAdapter);
        second.setName("Second");
        this.parent.parent.add(second);
    }
    private static class ControlPointLine extends ControlPoints.ControlPoint {
        ControlPointLine(MouseAdapter mouseAdapter) {
            super(mouseAdapter);
            this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            setSize(SIZE, SIZE);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillOval(0, 0, SIZE, SIZE);
        }
    }
    @Override
    void show(boolean isShow) {
        if (isShow) {
            setLocation();
        }
        first.setVisible(isShow);
        second.setVisible(isShow);
    }

    @Override
    void setLocation() {
        first.setLocation(this.parent.firstPoint.x - SIZE / 2, this.parent.firstPoint.y - SIZE / 2);
        second.setLocation(this.parent.secondPoint.x - SIZE / 2, this.parent.secondPoint.y - SIZE / 2);
    }

    @Override
    void delete() {
        show(false);
        this.first = this.second = null;

    }

    private class CPMouseAdapterLine extends MouseAdapter {
        Point origin, end;
        @Override
        public void mousePressed(MouseEvent e) {
            ControlPointsLine.this.parent.state = State.moving;
            if (e.getSource() == ControlPointsLine.this.second) {
                ControlPointsLine.this.parent.swapPoint();
            }
            origin = e.getLocationOnScreen();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (ControlPointsLine.this.parent.state == State.moving) {
                end = e.getLocationOnScreen();
                ControlPointsLine.this.parent.moving(end.x - origin.x, end.y - origin.y);
                ControlPointsLine.this.parent.parent.repaint();

                ControlPointsLine.this.setLocation();
                origin = end;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (ControlPointsLine.this.parent.state == State.moving) {
                Point point = ControlPointsLine.this.parent.firstPoint;
                ERComponent check = null;
                for (int i = ControlPointsLine.this.parent.parent.components.size() - 1; i >= 0; i--) {
                    ERComponent erC = ControlPointsLine.this.parent.parent.components.get(i);
                    if (erC.checkEntered(point) && erC != ControlPointsLine.this.parent) {
                        check = erC;
                        break;
                    }
                }
                if (check instanceof ERComponent2D && check != ControlPointsLine.this.parent.firstERC) {
                    ControlPointsLine.this.parent.firstERC.lines.removeIf(l -> l == ControlPointsLine.this.parent);
                    ((ERComponent2D) check).lines.add(ControlPointsLine.this.parent);
                    ControlPointsLine.this.parent.setMinLine();
                    ControlPointsLine.this.parent.firstERC = (ERComponent2D) check;
                    ControlPointsLine.this.parent.setMinLine();
                }
                else {
                    ControlPointsLine.this.parent.firstPoint =
                            ControlPointsLine.this.parent.firstERC.getNearestPoint(ControlPointsLine.this.parent.secondPoint);
                }
                ControlPointsLine.this.setLocation();
                ControlPointsLine.this.parent.parent.repaint();
                origin = end = null;
            }
        }
    }
}
