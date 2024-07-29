package drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawArea extends JTabbedPane {
    MainWindow parent;
    int pageCount;
    boolean tabNaming;
    DrawArea(MainWindow parent) {
        this.parent = parent;
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabNaming = false;
    }

    public boolean isEmpty() {
        return this.getTabCount() == 0;
    }

    public void addTab() {
        Canvas canvas = new Canvas(this);
        if (this.getTabCount() == 0) {
            pageCount = 1;
        }
        addTab("Page" + pageCount++, canvas.scrollPane);
    }

    @Override
    public void addTab(String title, Component component) {
        super.addTab(title, component);
        TabPanel tabPanel = new TabPanel(title, component);

        this.setTabComponentAt(this.getTabCount() - 1, tabPanel);
    }

    public void finishNaming() {
        for (int i = 0; i < this.getTabCount(); i++) {
            ((TabPanel)this.getTabComponentAt(i)).tFTitle.setFocusable(false);
        }
    }
    public class TabPanel extends JPanel {
        JTextField tFTitle;
        JButton closeBtn;
        TabPanel(String title, Component component) {
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 2));
            this.setOpaque(false);
            tFTitle = new JTextField(title);
            tFTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
            tFTitle.setOpaque(false);
            tFTitle.setFocusable(false);
            tFTitle.setFont(tFTitle.getFont().deriveFont(12F));
            tFTitle.setDisabledTextColor(Color.black);
            tFTitle.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        for (int i = 0; i < DrawArea.this.getTabCount(); i++) {
                            if (DrawArea.this.getTabComponentAt(i) == TabPanel.this) {
                                DrawArea.this.setSelectedIndex(i);
                                break;
                            }
                        }
                        finishNaming();
                    }
                    if (e.getClickCount() == 2) {
                        tFTitle.setFocusable(true);
                        tFTitle.requestFocusInWindow();
                        tFTitle.selectAll();
                        DrawArea.this.tabNaming = true;
                    }
                }
            });
            tFTitle.addActionListener(e -> {
                tFTitle.setFocusable(false);
            });
            tFTitle.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    DrawArea.this.finishNaming();
                }
            });

            closeBtn = new JButton(" x ");
            closeBtn.setContentAreaFilled(false);
            closeBtn.setBorder(BorderFactory.createEmptyBorder());
            closeBtn.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog (
                        DrawArea.this.parent,"是否刪除頁面"+ tFTitle.getText(),"刪除頁面",JOptionPane.YES_NO_OPTION) == 0) {
                    removeTabAt(indexOfComponent(component));
                }
                if (DrawArea.this.getTabCount() == 0) {
                    DrawArea.this.parent.componentBox.back2Default();
                }
            });
            closeBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    closeBtn.setBorder(BorderFactory.createMatteBorder(1,  1, 1, 1, Color.BLACK));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    closeBtn.setBorder(BorderFactory.createEmptyBorder());
                }
            });
            this.add(tFTitle);
            this.add(closeBtn);
        }
    }
}
