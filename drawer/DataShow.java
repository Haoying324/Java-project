package drawer;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;

public class DataShow extends JPanel{
    MainWindow parent;
    JLabel L1,L2,L3,L4,L5,L6,Type, isKey;
    TextInt SizeX,SizeY,LoactionX,LoactionY;
    JToggleButton btnIsKey;
    JTextField Name;
    ObjectPanelShow OPS;
    TFActionListener tfActionListener;
    DataShow(MainWindow parent) {
        this.setPreferredSize(new Dimension(200, 800));
        this.parent=parent;
        OPS = new ObjectPanelShow(this.parent);
        add(OPS,BorderLayout.CENTER);
        setVisible(false);
    }
    class ObjectPanelShow extends JPanel {
        ObjectPanelShow(MainWindow SF) {
            setVisible(true);
            L1=new JLabel("寬度:");
            L2=new JLabel("高度:");
            L3=new JLabel("相對位置X:");
            L4=new JLabel("相對位置Y:");
            L5=new JLabel("名稱:");
            L6=new JLabel("種類:");
            Type=new JLabel("");

            isKey = new JLabel("是否為Key");
            btnIsKey = new JToggleButton(" ");
            btnIsKey.addItemListener(e -> {
                ERComponent focus = ((drawer.Canvas) ((JScrollPane) DataShow.this.parent.pages.getSelectedComponent()).getViewport().getView()).focusedComponent;
                if (btnIsKey.isSelected()) {
                    btnIsKey.setText("V");
                }
                else {
                    btnIsKey.setText(" ");
                }
                if (focus instanceof Attribute) {
                    ((Attribute) focus).isKey = btnIsKey.isSelected();
                    focus.parent.repaint();
                }

            });

            tfActionListener = new TFActionListener();
            SizeX= new TextInt(10);
            SizeX.addActionListener(tfActionListener);

            SizeY= new TextInt(10);
            SizeY.addActionListener(tfActionListener);

            LoactionX= new TextInt(10);
            LoactionX.addActionListener(tfActionListener);

            LoactionY= new TextInt(10);
            LoactionY.addActionListener(tfActionListener);

            Name = new JTextField(10);
            ((AbstractDocument) Name.getDocument()).setDocumentFilter(new drawer.Canvas.Name.LimitDocumentFilter());
            Name.addActionListener(tfActionListener);

            L1.setSize(10,20);
            L2.setSize(10,20);
            L3.setSize(10,20);
            L4.setSize(10,20);
            L5.setSize(10,20);
            L6.setSize(10,20);
            isKey.setSize(10,20);

            setLayout(new GridLayout(20,3));
            new TypesettingPanel(this,L1,SizeX);
            new TypesettingPanel(this,L2,SizeY);
            new TypesettingPanel(this,L3,LoactionX);
            new TypesettingPanel(this,L4,LoactionY);
            new TypesettingPanel(this,L5,Name);

            JPanel pIsKey = new JPanel();
            pIsKey.add(isKey);
            pIsKey.add(btnIsKey);
            this.add(pIsKey);



            JPanel tp=new JPanel();
            tp.add(L6);
            tp.add(Type);
            add(tp);
        }
    }
    static class TextInt extends JTextField {
        TextInt(int cols) {
            super(cols);
            this.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    if(!(keyChar >= '0' && keyChar <= '9')){
                        e.consume(); //缺點，不能控制賦值黏貼的內容
                    }
                }
            });
        }
    }
    private class TFActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ERComponent2D focused =
                    (ERComponent2D) ((drawer.Canvas) ((JScrollPane) DataShow.this.parent.pages.getSelectedComponent()).getViewport().getView()).focusedComponent;
            Rectangle bounds = focused.getBounds();
            JTextField T = (JTextField) e.getSource();
            if (T == Name) {
                focused.name = T.getText();
            }
            else {
                int n = Integer.parseInt(T.getText());
                if (T == SizeX) {
                    bounds.width = n;
                }
                else if (T == SizeY) {
                    bounds.height = n;
                }
                else if (T == LoactionX) {
                    bounds.x = n;
                }
                else if (T == LoactionY) {
                    bounds.y = n;
                }
                focused.setBounds(bounds);
                focused.controlPoints.setLocation();
            }
            DataShow.this.parent.pages.getSelectedComponent().repaint();
        }
    }
    public void showData() {
        if (this.parent.pages.getTabCount() > 0) {
            setVisible(true);
            ERComponent focus = ((drawer.Canvas) ((JScrollPane) this.parent.pages.getSelectedComponent()).getViewport().getView()).focusedComponent;
            if(focus instanceof ERComponent2D) {
                if (focus instanceof Attribute) {
                    DataShow.this.btnIsKey.setSelected(((Attribute) focus).isKey);
                    DataShow.this.btnIsKey.setEnabled(true);
                }
                else {
                    DataShow.this.btnIsKey.setSelected(false);
                    DataShow.this.btnIsKey.setEnabled(false);
                }
                showObject();
                OPS.setVisible(true);
            }
            else {
                OPS.setVisible(false);
            }
        }
        else {
            this.setVisible(false);
        }
    }
    public void showObject() {
        String type = null;
        ERComponent2D focused = (ERComponent2D) (((drawer.Canvas) ((JScrollPane) this.parent.pages.getSelectedComponent()).getViewport().getView())).focusedComponent;
        if(focused instanceof Entity) {
            type = "Entity";
        }
        else if(focused instanceof Attribute) {
            type = "Attributes";
        }
        else if(focused instanceof Relationship) {
            type = "Relationship";
        }
        SizeX.setText(String.valueOf(focused.width));
        SizeY.setText(String.valueOf(focused.height));
        LoactionX.setText(String.valueOf(focused.firstPoint.x));
        LoactionY.setText(String.valueOf(focused.firstPoint.y));
        Name.setText(focused.name);
        Type.setText(type);
    }
    public class TypesettingPanel extends JPanel {
        TypesettingPanel(ObjectPanelShow OPS,JLabel L,JTextField T) {
            add(L);
            add(T);
            T.addInputMethodListener(new InputMethodListener() {
                public void inputMethodTextChanged(InputMethodEvent event) {
                    ERComponent2D focused =
                            (ERComponent2D) ((Canvas) DataShow.this.parent.pages.getSelectedComponent()).focusedComponent;
                    Rectangle bounds = focused.getBounds();
                    if (T == Name) {
                        focused.name = T.getText();
                    }
                    else {
                        int n = Integer.parseInt(T.getText());
                        if (T == SizeX) {
                            bounds.width = n;
                        }
                        else if (T == SizeY) {
                            bounds.height = n;
                        }
                        else if (T==LoactionX) {
                            bounds.x = n;
                        }
                        else if (T==LoactionY) {
                            bounds.y = n;
                        }
                        focused.setBounds(bounds);
                    }
                }
                public void caretPositionChanged(InputMethodEvent event) {
                }
            });
            OPS.add(this);
        }
    }
}
