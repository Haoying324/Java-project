package drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    int initWidth = 1200;
    int initHeight = 900;
    MenuBar menuBar;
    ToolBar toolBar;
    ComponentBox componentBox;
    DrawArea pages;
    DataShow dataShow;
    ExportArea exportArea;
    FileManage fileManage;
    MainWindow(String title) {
        this.setTitle(title);
        this.setSize(initWidth, initHeight);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        // MenuBar
        menuBar = new MenuBar(this);
        this.setJMenuBar(menuBar);
        ////

        // ToolBar
        toolBar = new ToolBar(this);
        this.add(toolBar, BorderLayout.NORTH);
        ////

        // ComponentBox
        componentBox = new ComponentBox(this);
        this.add(componentBox, BorderLayout.WEST);
        ////


        JPanel DRAW = new JPanel(new BorderLayout());
        // DrawArea
        pages = new DrawArea(this);
        DRAW.add(pages, BorderLayout.CENTER);


        // DetailArea
        dataShow = new DataShow(this);
        DRAW.add(dataShow, BorderLayout.EAST);

        // ExportArea
        exportArea = new ExportArea(this);
        DRAW.add(exportArea, BorderLayout.SOUTH);

        //FileManage
        fileManage = new FileManage();

        this.add(DRAW, BorderLayout.CENTER);

        this.setVisible(true);

    }
    public void confirmExit() {
        int result=JOptionPane.showConfirmDialog(
                MainWindow.this,
                "確定要結束程式嗎?",
                "確認訊息",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        else {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
}
