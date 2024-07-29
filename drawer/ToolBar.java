package drawer;

import javax.swing.*;
import java.awt.*;

public class ToolBar extends JPanel {
    MainWindow parent;
    JButton newBtn, saveBtn, openBtn, exportJPGBtn, exportFunctionBtn, undoBtn, redoBtn;
    ToolBar(MainWindow parent) {
        this.parent = parent;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        newBtn = new JButton("新增畫布");
        newBtn.addActionListener(e -> parent.pages.addTab());
        saveBtn = new JButton("儲存");
        openBtn = new JButton("開啟");
        exportJPGBtn = new JButton("匯出圖片");
        exportJPGBtn.addActionListener(e -> this.parent.fileManage.savePic(
                (Canvas) ((JScrollPane) this.parent.pages.getSelectedComponent()).getViewport().getView()
        ));
        exportFunctionBtn = new JButton("編譯");
        exportFunctionBtn.addActionListener(e ->  this.parent.exportArea.compile());
        undoBtn = new JButton("復原");
        redoBtn = new JButton("取消復原");
        this.add(newBtn);
        this.add(saveBtn);
        this.add(openBtn);
        this.add(exportJPGBtn);
        this.add(exportFunctionBtn);
        //this.add(undoBtn);
        //this.add(redoBtn);
    }
}
