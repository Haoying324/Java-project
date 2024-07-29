package drawer;

import java.awt.*;
import java.util.ArrayList;

public class Entity extends ERComponent2D {
    ArrayList<Attribute> keyAttrs, attrs;
    Entity(Canvas parent, Point origin, int entityIdx) {
        super(parent, origin, entityIdx);
        this.name = "E" + this.name;
        keyAttrs = new ArrayList<>();
        attrs = new ArrayList<>();
    }
    Entity(Entity entity) {
        super(entity);
        keyAttrs = new ArrayList<>();
        attrs = new ArrayList<>();
    }

    @Override
    boolean checkEntered(Point point) {
        return point.x >= firstPoint.x
                && point.x < firstPoint.x + width
                && point.y >= firstPoint.y
                && point.y < firstPoint.y + height;
    }

    @Override
    void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(firstPoint.x, firstPoint.y, this.width, this.height);
        g.setColor(Color.black);
        g.drawRect(firstPoint.x, firstPoint.y, this.width, this.height);
        setNamePosition();
        g.drawString(this.name, this.namePosition.x, this.namePosition.y);
    }
    public boolean haveAttrs() {
        for (Line l: lines) {
            if (l.getConnectType() == ConnectType.Entity2KeyAttribute
                    || l.getConnectType() == ConnectType.Entity2nonKeyAttribute) {
                return true;
            }
        }
        return false;
    }
    public boolean haveKeyAttrs() {
        for (Line l: lines) {
            if (l.getConnectType() == ConnectType.Entity2KeyAttribute) {
                return true;
            }
        }
        return false;
    }
    public void setAttrs() {
        attrs.clear();
        keyAttrs.clear();
        for (Line l: lines) {
            if (l.getConnectType() == ConnectType.Entity2nonKeyAttribute) {
                if (l.firstERC instanceof Attribute) {
                    attrs.add((Attribute) l.firstERC);
                }
                else {
                    attrs.add((Attribute) l.secondERC);
                }
            }
            else if (l.getConnectType() == ConnectType.Entity2KeyAttribute) {
                if (l.firstERC instanceof Attribute) {
                    keyAttrs.add((Attribute) l.firstERC);
                }
                else {
                    keyAttrs.add((Attribute) l.secondERC);
                }
            }
        }
    }
}