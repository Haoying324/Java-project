package drawer;

import java.awt.*;
import java.util.ArrayList;

public class Relationship extends ERComponent2D {
    ArrayList<Entity> entities;
    Relationship(Canvas parent, Point origin, int entityIdx) {
        super(parent, origin, entityIdx);
        this.name = "R" + this.name;
        setNamePosition();
        entities = new ArrayList<>();
    }

    Relationship(Relationship relationship) {
        super(relationship);
        entities = new ArrayList<>();
    }


    @Override
    public void paint(Graphics g) {
        int midW = firstPoint.x + this.width / 2;
        int backW = firstPoint.x + this.width;
        int midH = firstPoint.y + this.height / 2;
        int backH = firstPoint.y + this.height;
        int[] xs = { midW, backW, midW, firstPoint.x };
        int[] ys = { firstPoint.y, midH, backH, midH };
        g.setColor(Color.white);
        g.fillPolygon(xs, ys, 4);
        g.setColor(Color.black);
        g.drawPolygon(xs, ys, 4);
        setNamePosition();
        g.drawString(this.name, this.namePosition.x, this.namePosition.y);
    }


    @Override
    public boolean checkEntered(Point point) {
        int x = point.x - firstPoint.x;
        int y = point.y - firstPoint.y;
        int w = this.width, h = this.height;
        int a = 2 * w * y - 2 * h * x;
        int b = 2 * w * y + 2 * h * x;
        return a >= -w * h && a <= w * h && b >= w * h && b <= 3 * w * h;
    }

    public int getEntities() {
        int count = 0;
        for (Line l: lines) {
            if (l.getConnectType() == ConnectType.Entity2Relationship) {
                ++count;
            }
        }
        return count;
    }

    public int getKeyEntities() {
        int count = 0;
        for (Line l: lines) {
            if (l.getConnectType() == ConnectType.Entity2Relationship
                    && l.firstERC == this
                    && ((Entity) l.secondERC).haveKeyAttrs()) {
                ++count;
            }
        }
        return count;
    }
}
