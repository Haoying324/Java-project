package drawer;

import java.awt.*;

public abstract class ERComponent {
    Canvas parent;
    Point firstPoint;
    State state;
    ControlPoints controlPoints;


    ERComponent(Canvas parent, Point firstPoint) {
        this.parent = parent;
        this.firstPoint = firstPoint;
        this.state = State.basic;
    }


    abstract boolean checkEntered(Point point);
    abstract void paint(Graphics g);
    void moving(int dx, int dy) {
        this.firstPoint.x += dx;
        this.firstPoint.y += dy;
    }
    public void delete() {
        this.parent = null;
        this.firstPoint = null;
        this.state = null;
        this.controlPoints = null;
    }

}