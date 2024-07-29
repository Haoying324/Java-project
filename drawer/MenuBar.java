package drawer;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    MainWindow parent;
    JMenu fileMenu, editMenu, aboutMenu;
    JMenuItem newMI, openMI, saveMI, saveAsMI, exportJPGMI, exportFuncMI;
    JMenuItem selectAllMI, copyMI, pasteMI, deleteMI, undoMI, redoMI;
    JMenuItem aboutMI;
    MenuBar(MainWindow parent) {
        this.parent = parent;
        fileMenu = new JMenu("檔案");
        editMenu = new JMenu("編輯");
        aboutMenu = new JMenu("說明");
        newMI = new JMenuItem("新增畫布");
        openMI = new JMenuItem("開啟");
        saveMI = new JMenuItem("儲存");
        saveAsMI = new JMenuItem("另存新檔");
        exportJPGMI = new JMenuItem("匯出圖片");
        exportFuncMI = new JMenuItem("編譯");

        //editMI = new JMenuItem("");
        selectAllMI = new JMenuItem("全選");
        copyMI = new JMenuItem("複製");
        copyMI.addActionListener(e -> {
            if (!MenuBar.this.parent.pages.isEmpty()) {
                MenuBar.this.parent.fileManage.copy(
                        (Canvas) ((JScrollPane) MenuBar.this.parent.pages.getSelectedComponent()).getViewport().getView()
                );
            }
        });
        pasteMI = new JMenuItem("貼上");
        pasteMI.addActionListener(e -> {
            if (!MenuBar.this.parent.pages.isEmpty()) {
                MenuBar.this.parent.fileManage.paste(
                        (Canvas) ((JScrollPane) MenuBar.this.parent.pages.getSelectedComponent()).getViewport().getView()
                );
            }
        });
        deleteMI = new JMenuItem("刪除");
        undoMI = new JMenuItem("復原");
        redoMI = new JMenuItem("取消復原");

        aboutMI = new JMenuItem("關於");

        fileMenu.add(newMI);
        fileMenu.add(openMI);
        fileMenu.add(saveMI);
        fileMenu.add(saveAsMI);
        fileMenu.add(exportJPGMI);
        fileMenu.add(exportFuncMI);

        editMenu.add(selectAllMI);
        editMenu.add(copyMI);
        editMenu.add(pasteMI);
        editMenu.add(deleteMI);
        editMenu.add(undoMI);
        editMenu.add(redoMI);

        aboutMenu.add(aboutMI);

        this.add(fileMenu);
        this.add(editMenu);
        this.add(aboutMenu);
    }
}
