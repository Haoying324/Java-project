package drawer;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Canvas extends JPanel {
    DrawArea parent;
    JScrollPane scrollPane;

    ERComponent focusedComponent;
    State state;
    int entityIdx = 0, attributeIdx = 0, relationshipIdx = 0;
    ArrayList<ERComponent> components;
    Point positionINMoving;
    FontMetrics fontMetrics;
    Name tfName;
    Font font;
    MouseAdapter mouseAdapter;
    static int stringWidth = 100;
    Point lineFirst, lineSecond;


    Canvas(DrawArea parent) {
        scrollPane = new JScrollPane(this);
        this.setPreferredSize(new Dimension(2000, 1600));
        this.parent = parent;
        this.setBackground(Color.white);
        this.setLayout(null);
        this.setFocusable(true);
        this.state = State.basic;

        this.components = new ArrayList<>();


        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 127) { // typed delete
                    Canvas.this.deleteComponent();
                }
                if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    if (focusedComponent instanceof ERComponent2D) {
                        Canvas.this.parent.parent.fileManage.copy(Canvas.this);
                    }
                }
                if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    Canvas.this.parent.parent.fileManage.paste(Canvas.this);
                }
            }
        });


        this.setFont(getFont().deriveFont(16F));
        font = getFont();
        this.fontMetrics = getFontMetrics(font);

        this.tfName = new Name();


        mouseAdapter = (new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    if (Canvas.this.state == State.active) {
                        ERComponent check = Canvas.this.checkFocus(e.getPoint());
                        if (check == null) {
                            Canvas.this.state = State.basic;
                            Canvas.this.unFocus();
                        } else {
                            if (check != focusedComponent) {
                                focusedComponent.controlPoints.show(false);
                                check.controlPoints.show(true);
                                focusedComponent = check;
                            }
                            if (focusedComponent instanceof ERComponent2D) {
                                Canvas.this.state = State.changingComponentName;
                                Rectangle rectangle = ((ERComponent2D) focusedComponent).getBounds();
                                Canvas.this.tfName.setBounds(rectangle.x + (rectangle.width - 100) / 2, rectangle.y + 1, stringWidth, rectangle.height - 1);
                                Canvas.this.tfName.setText(((ERComponent2D) focusedComponent).name);
                                Canvas.this.tfName.setVisible(true);
                                Canvas.this.tfName.requestFocusInWindow();
                                Canvas.this.tfName.selectAll();
                                ((ERComponent2D) focusedComponent).name = "";
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                switch (Canvas.this.state) {
                    case basic -> {
                        if (Canvas.this.parent.tabNaming) {
                            Canvas.this.parent.finishNaming();
                            Canvas.this.parent.tabNaming = false;
                        }
                        Point point = e.getPoint();
                        focusedComponent = checkFocus(point);
                        Canvas.this.parent.parent.dataShow.showData();
                        if (focusedComponent != null) {
                            focusedComponent.controlPoints.show(true);
                            Canvas.this.requestFocusInWindow();
                            if (focusedComponent instanceof ERComponent2D) {
                                prepare2Move(point);
                            } else {
                                focusedComponent.state = State.moving;
                                Canvas.this.state = State.active;
                            }
                        }
                    }
                    case changingComponentName -> {
                        Canvas.this.state = State.active;
                        Canvas.this.tfName.readName();
                        mousePressed(e);
                    }
                    case active -> {
                        Point point = e.getPoint();
                        ERComponent check = checkFocus(point);
                        if (check == null) {
                            Canvas.this.state = State.basic;
                            focusedComponent.controlPoints.show(false);
                            if (focusedComponent instanceof ERComponent2D) {
                                for (Line l : ((ERComponent2D) focusedComponent).lines) {
                                    l.state = State.basic;
                                }
                                Canvas.this.repaint();
                            } else {
                                focusedComponent.state = State.basic;
                            }
                            focusedComponent = null;
                            Canvas.this.parent.parent.dataShow.showData();
                        }
                        else {
                            if (check != focusedComponent) {
                                focusedComponent.controlPoints.show(false);
                                check.controlPoints.show(true);
                                focusedComponent = check;
                            }
                            Canvas.this.requestFocusInWindow();
                            if (focusedComponent instanceof ERComponent2D) {
                                prepare2Move(point);
                            }
                        }
                    }
                    case ready2createEntity -> {
                        Entity entity = new Entity(Canvas.this, e.getPoint(), Canvas.this.entityIdx++);
                        components.add(entity);
                        Canvas.this.repaint();
                        Canvas.this.parent.parent.componentBox.back2Default();
                        Canvas.this.state = State.basic;
                        Canvas.this.unFocus();
                    }
                    case ready2createAttribute -> {
                        Attribute attribute = new Attribute(Canvas.this, e.getPoint(), Canvas.this.attributeIdx++);
                        components.add(attribute);
                        Canvas.this.repaint();
                        Canvas.this.parent.parent.componentBox.back2Default();
                        Canvas.this.state = State.basic;
                        Canvas.this.unFocus();
                    }
                    case ready2createRelationship -> {
                        Relationship relationship = new Relationship(Canvas.this, e.getPoint(), Canvas.this.relationshipIdx++);
                        components.add(relationship);
                        Canvas.this.repaint();
                        Canvas.this.parent.parent.componentBox.back2Default();
                        Canvas.this.state = State.basic;
                        Canvas.this.unFocus();
                    }
                    case ready2createLine -> {
                        Point point = e.getPoint();
                        Canvas.this.unFocus();
                        focusedComponent = Canvas.this.checkFocus(point);
                        if (focusedComponent instanceof ERComponent2D) {
                            Canvas.this.lineFirst = ((ERComponent2D) focusedComponent).getNearestPoint(point);
                            Canvas.this.lineSecond = Canvas.this.lineFirst;
                            Canvas.this.state = State.drawingLine;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (Canvas.this.state) {
                    case moving -> {
                        Canvas.this.state = State.active;
                        Canvas.this.parent.parent.dataShow.showData();
                    }
                    case drawingLine -> {
                        ERComponent check = Canvas.this.checkFocus(e.getPoint());
                        if (check instanceof ERComponent2D && check != focusedComponent) {
                            Canvas.this.parent.parent.componentBox.back2Default();
                            lineSecond = ((ERComponent2D) check).getNearestPoint(lineFirst);
                            Line line = new Line(Canvas.this, lineFirst, lineSecond);
                            line.firstERC = (ERComponent2D) focusedComponent;
                            line.secondERC = (ERComponent2D) check;
                            ((ERComponent2D) check).lines.add(line);
                            ((ERComponent2D) focusedComponent).lines.add(line);
                            Canvas.this.components.add(line);
                            Canvas.this.state = State.basic;
                        } else {
                            Canvas.this.state = State.ready2createLine;
                        }
                        Canvas.this.repaint();
                    }
                }
            }
        });
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (Canvas.this.state) {
                    case moving -> {
                        Canvas.this.parent.parent.dataShow.showData();
                        Point point = e.getPoint();
                        int dx = point.x - positionINMoving.x;
                        int dy = point.y - positionINMoving.y;
                        focusedComponent.moving(dx, dy);
                        focusedComponent.controlPoints.setLocation();
                        for (Line l : ((ERComponent2D) focusedComponent).lines) {
                            if (l.firstERC != focusedComponent) {
                                l.swapPoint();
                            }
                            l.moving(dx, dy);
                            l.setMinLine();
                        }
                        Canvas.this.repaint();
                        positionINMoving = point;
                        Rectangle r = new Rectangle(e.getX(), e.getY(),1,1);
                        ((JPanel) e.getSource()).scrollRectToVisible(r);
                    }
                    case drawingLine -> {
                        Point point = e.getPoint();
                        Canvas.this.lineFirst = ((ERComponent2D) focusedComponent).getNearestPoint(point);
                        Canvas.this.lineSecond = point;
                        Canvas.this.repaint();
                    }
                }
            }
        });
    }

    private void prepare2Move(Point point) {
        Canvas.this.state = State.moving;
        for (Line l : ((ERComponent2D) focusedComponent).lines) {
            l.state = State.moving;
        }
        Canvas.this.repaint();
        positionINMoving = point;
    }

    private void unFocus() {
        if (focusedComponent != null) {
            focusedComponent.controlPoints.show(false);
        }
        focusedComponent = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (ERComponent erComponent : components) {
            erComponent.paint(g);
        }
        if (this.state == State.drawingLine) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(Line.focusStrokeWidth));
            g2d.drawLine(lineFirst.x, lineFirst.y, lineSecond.x, lineSecond.y);
            g2d.setStroke(new BasicStroke(Line.basicStrokeWidth));
        }
    }

    public ERComponent checkFocus(Point point) {
        for (int i = components.size() - 1; i >= 0; i--) {
            if (components.get(i).checkEntered(point)) {
                return components.get(i);
            }
        }
        return null;
    }

    public void deleteComponent() {
        if (this.focusedComponent != null) {
            focusedComponent.delete();
            this.components.removeIf(erComponent -> erComponent == focusedComponent);
            focusedComponent = null;
            this.parent.parent.dataShow.showData();
            this.state = State.basic;
        }
    }


    public class Name extends JTextField {
        int stringHeight;

        Name() {
            stringHeight = Canvas.this.fontMetrics.getHeight();
            this.setHorizontalAlignment(JTextField.CENTER);
            this.setFont(Canvas.this.font);
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setOpaque(false);
            ((AbstractDocument) this.getDocument()).setDocumentFilter(new LimitDocumentFilter());
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point point = e.getPoint();
                    int stringWidth = Canvas.this.fontMetrics.stringWidth(Name.this.getText());
                    if (
                            point.x < (Name.this.getWidth() - stringWidth) / 2
                                    || point.x > (Name.this.getWidth() + stringWidth) / 2
                                    || point.y < (Name.this.getHeight() - stringHeight) / 2
                                    || point.y > (Name.this.getHeight() + stringHeight) / 2
                    ) {
                        readName();
                        Canvas.this.state = State.active;
                        Canvas.this.mouseAdapter.mousePressed(e);
                    }

                }
            });
            Canvas.this.add(this);
            this.setVisible(false);
        }


        public void readName() {
            if (Canvas.this.focusedComponent instanceof ERComponent2D) {
                ((ERComponent2D) Canvas.this.focusedComponent).name = this.getText();
            }
            this.setVisible(false);
        }

        public static class LimitDocumentFilter extends DocumentFilter {
            int limit = 10;

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                int currentLength = fb.getDocument().getLength();
                int overLimit = (currentLength - text.length() - limit - length);
                if (overLimit > 0) {
                    text = text.substring(0, text.length() - overLimit);
                }
                if (text.length() > 0) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
    }
}
