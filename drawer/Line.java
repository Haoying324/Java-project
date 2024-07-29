package drawer;

import java.awt.*;

public class Line extends ERComponent {

    Point secondPoint;
    ERComponent2D firstERC, secondERC;
    static int focusStrokeWidth = 4;
    static int basicStrokeWidth = 1;

    Line(Canvas parent, Point first, Point second) {
        super(parent, first);
        secondPoint = second;
        controlPoints = new ControlPointsLine(this);
    }

    @Override
    boolean checkEntered(Point point) {
        int a = firstPoint.y - secondPoint.y;
        int b = secondPoint.x - firstPoint.x;
        int n1 = b * firstPoint.x - a * firstPoint.y;
        int n2 = b * secondPoint.x - a * secondPoint.y;
        if (n1 > n2) {
            int tmp = n1;
            n1 = n2;
            n2 = tmp;
        }
        int p = b * point.x - a * point.y;
        if (p < n1 || p > n2) {
            this.state = State.basic;
            return false;
        }
        int c = -a * firstPoint.x - b * firstPoint.y;
        double n = Math.abs(a * point.x + b * point.y + c) / Math.sqrt(a * a + b * b);
        return n <= 5;
    }
    public void swapPoint() {
        Point point = firstPoint;
        firstPoint = secondPoint;
        secondPoint = point;
        ERComponent2D tmp = firstERC;
        firstERC = secondERC;
        secondERC = tmp;
    }

    void setMinLine() {
        firstPoint = firstERC.getNearestPoint(secondPoint);
        secondPoint = secondERC.getNearestPoint(firstPoint);
        firstPoint = firstERC.getNearestPoint(secondPoint);
        secondPoint = secondERC.getNearestPoint(firstPoint);
    }


    @Override
    void paint(Graphics g) {
        if (this.state == State.moving) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(focusStrokeWidth));
            g2d.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
            g2d.setStroke(new BasicStroke(basicStrokeWidth));
        }
        else {
            g.drawLine(firstPoint.x, firstPoint.y, secondPoint.x, secondPoint.y);
        }
    }

    @Override
    public void delete() {
        if (firstERC != null) {
            firstERC.lines.removeIf(line -> line == this);
        }
        if (secondERC != null) {
            secondERC.lines.removeIf(line -> line == this);
        }
        firstERC = secondERC = null;
        this.parent.components.removeIf(erComponent -> erComponent == this);
        controlPoints.delete();
        super.delete();
    }

    public ConnectType getConnectType() {
        if (firstERC instanceof Entity || secondERC instanceof Entity) {
            if (firstERC instanceof Relationship || secondERC instanceof Relationship) {
                return ConnectType.Entity2Relationship;
            }
            else {
                boolean key;
                if (firstERC instanceof Attribute) {
                    key = ((Attribute) firstERC).isKey;
                }
                else {
                    assert secondERC instanceof Attribute;
                    key = ((Attribute) secondERC).isKey;
                }
                return key ? ConnectType.Entity2KeyAttribute : ConnectType.Entity2nonKeyAttribute;
            }
        }
        else {
            return ConnectType.Relationship2Attribute;
        }
    }
}

