package drawer;

import java.awt.*;

public class Attribute extends ERComponent2D {
    Boolean isKey;
    Attribute(Canvas parent, Point origin, int entityIdx) {
        super(parent, origin, entityIdx);
        this.name = "A" + this.name;
        isKey = false;
    }
    Attribute(Attribute attribute) {
        super(attribute);
        isKey = attribute.isKey;
    }

    @Override
    void paint(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillOval(firstPoint.x, firstPoint.y, this.width, this.height);
        g.setColor(Color.black);
        g.drawOval(firstPoint.x, firstPoint.y, this.width, this.height);
        setNamePosition();
        if (this.isKey) {
            g.setColor(Color.red);
        }
        g.drawString(this.name, this.namePosition.x, this.namePosition.y);
        if (this.isKey) {
            g.setColor(Color.black);
        }
    }

    @Override
    public boolean checkEntered(Point point) {
        double a = (double) this.width / 2;
        double b = (double) this.height / 2;
        double x = point.x - firstPoint.x - a;
        double y = point.y - firstPoint.y - b;
        return Math.pow(x / a, 2) + Math.pow(y / b, 2) <= 1;
    }
}

