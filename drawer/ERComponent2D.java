package drawer;

import java.awt.*;
import java.util.ArrayList;

public abstract class ERComponent2D extends ERComponent {
    int width, height;
    String name;
    static Dimension minimumSize = new Dimension(40, 40);
    Point namePosition;
    ArrayList<Line> lines;

    ERComponent2D(Canvas parent, Point origin, int componentIdx) {
        super(parent, origin);
        this.name = "" + componentIdx;
        this.width = 100;
        this.height = 60;
        controlPoints = new ControlPoints2D(this);
        lines = new ArrayList<>();

    }
    ERComponent2D(ERComponent2D erC) {
        super(erC.parent, erC.firstPoint);
        this.name = erC.name;
        controlPoints = new ControlPoints2D(this);
        lines = new ArrayList<>();
        Rectangle r = erC.getBounds();
        this.setBounds(new Point(r.x + 3, r.y + 3), r.width, r.height);
        controlPoints.setLocation();
        setNamePosition();
    }

    public void setNamePosition() {
        if (namePosition == null) {
            namePosition = new Point();
        }
        int nameWidth = this.parent.fontMetrics.stringWidth(name);
        namePosition.x = firstPoint.x + (this.width - nameWidth) / 2 - 1;
        namePosition.y = firstPoint.y + this.height / 2 + 7;
    }

    public Rectangle getBounds() {
        return new Rectangle(firstPoint.x, firstPoint.y, width, height);
    }
    public void setBounds(Rectangle r) {
        this.firstPoint = new Point(r.x, r.y);
        this.width = r.width;
        this.height = r.height;
    }
    public void setBounds(Point p, int width, int height) {
        this.firstPoint = p;
        this.width = width;
        this.height = height;
    }

    public Point getNearestPoint(Point point) {
        int mx = firstPoint.x + this.width / 2;
        int my = firstPoint.y + this.height / 2;
        int wx = firstPoint.x + this.width;
        int wy = firstPoint.y + this.height;

        int x = firstPoint.x;
        int y = my;
        double d = point.distance(new Point(firstPoint.x, my));
        double tmp = point.distance(new Point(mx, wy));
        if (tmp < d) {
            x = mx;
            y = wy;
            d = tmp;
        }
        tmp = point.distance(new Point(wx, my));
        if (tmp < d) {
            x = wx;
            y = my;
            d = tmp;
        }
        tmp = point.distance(new Point(mx, firstPoint.y));
        if (tmp < d) {
            x = mx;
            y = firstPoint.y;
        }
        return new Point(x, y);
    }

    @Override
    public void delete() {
        this.name = null;
        namePosition = null;
        if (lines != null) {
            for (Line l : lines) {
                l.delete();
                if (lines.isEmpty()) {
                    break;
                }
            }
        }
        controlPoints.delete();
        super.delete();
    }
}
