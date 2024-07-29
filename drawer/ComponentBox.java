package drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComponentBox extends JPanel implements ActionListener {
    MainWindow parent;
    ButtonGroup btnGp;
    JToggleButton mouseBtn, entityBtn, attributeBtn, relationshipBtn, lineBtn;
    ComponentBox(MainWindow parent) {
        this.parent = parent;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        mouseBtn = new JToggleButton("Mouse");
        mouseBtn.setMaximumSize(new Dimension(10000, mouseBtn.getMinimumSize().height));
        entityBtn = new JToggleButton("Entity");
        entityBtn.setMaximumSize(new Dimension(10000, mouseBtn.getMinimumSize().height));
        attributeBtn = new JToggleButton("Attribute");
        attributeBtn.setMaximumSize(new Dimension(10000, mouseBtn.getMinimumSize().height));
        relationshipBtn = new JToggleButton("Relationship");
        relationshipBtn.setMaximumSize(new Dimension(10000, mouseBtn.getMinimumSize().height));
        lineBtn = new JToggleButton("Line");
        lineBtn.setMaximumSize(new Dimension(10000, mouseBtn.getMinimumSize().height));
        mouseBtn.addActionListener(this);
        entityBtn.addActionListener(this);
        attributeBtn.addActionListener(this);
        relationshipBtn.addActionListener(this);
        lineBtn.addActionListener(this);


        btnGp = new ButtonGroup();
        btnGp.add(mouseBtn);
        btnGp.add(entityBtn);
        btnGp.add(attributeBtn);
        btnGp.add(relationshipBtn);
        btnGp.add(lineBtn);

        this.add(mouseBtn);
        this.add(entityBtn);
        this.add(attributeBtn);
        this.add(relationshipBtn);
        this.add(lineBtn);
    }
    public void back2Default() {
        mouseBtn.setSelected(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton source = (JToggleButton)e.getSource();
        if (this.parent.pages.getTabCount() == 0) {
            mouseBtn.setSelected(true);
            return;
        }
        JScrollPane j = new JScrollPane();
        Canvas currentCanvas = (Canvas) ((JScrollPane)this.parent.pages.getSelectedComponent()).getViewport().getView();
        if (source == mouseBtn) {
            currentCanvas.state = State.basic;
        }
        else if (source == entityBtn) {
            currentCanvas.state = State.ready2createEntity;
        }
        else if (source == attributeBtn) {
            currentCanvas.state = State.ready2createAttribute;
        }
        else if (source == relationshipBtn) {
            currentCanvas.state = State.ready2createRelationship;
        }
        else if (source == lineBtn) {
            currentCanvas.state = State.ready2createLine;
        }
    }
}
