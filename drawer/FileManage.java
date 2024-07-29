package drawer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FileManage {
    ERComponent2D copy;
    int copyIdx = 0;
    public void savePic(Canvas D) {                               //存檔
        int w=D.getWidth(),h=D.getHeight();
        JFileChooser fileChooser = new JFileChooser("./");
        fileChooser.setDialogTitle("選擇路徑");
        fileChooser.setSelectedFile(new File(
                ((DrawArea.TabPanel) D.parent.getTabComponentAt(D.parent.getSelectedIndex())).tFTitle.getText()
                        + ".png"
        ));
        String[] saveType = {"png","jpeg","jpg"};
        fileChooser.setFileFilter(new FileNameExtensionFilter("png", saveType));
        int returnVal = fileChooser.showSaveDialog(new JPanel());
        if(returnVal==JFileChooser.APPROVE_OPTION) {
            String FileName=fileChooser.getSelectedFile().getAbsolutePath();
            File f;
            String name=FileName;
            if(name.lastIndexOf(".png")==-1){
                FileName+=".png";
            }
            f=new File(FileName);
            System.out.println(f.getName());
            BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            D.paint(g2);
            try {
                ImageIO.write(image, "png", f);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void copy(Canvas parent) {
        copy = null;
        if (parent.focusedComponent instanceof Entity) {
            copy = new Entity((Entity) parent.focusedComponent);
            copyIdx = 0;
        }
        else if (parent.focusedComponent instanceof Attribute) {
            copy = new Attribute((Attribute) parent.focusedComponent);
            copyIdx = 0;
        }
        else if (parent.focusedComponent instanceof Relationship) {
            copy = new Relationship((Relationship) parent.focusedComponent);
            copyIdx = 0;
        }
    }
    public void paste(Canvas parent) {
        if (copy != null) {
            ERComponent2D paste;
            if (copy instanceof Entity) {
                paste = new Entity((Entity) copy);
            }
            else if (copy instanceof Attribute) {
                paste = new Attribute((Attribute) copy);
            }
            else if (copy instanceof Relationship) {
                paste =  new Relationship((Relationship) copy);
            }
            else {
                return;
            }
            paste.moving(5 * copyIdx, 5 * copyIdx);
            paste.controlPoints.show(true);
            parent.components.add(paste);
            if (parent.focusedComponent != null) {
                parent.focusedComponent.controlPoints.show(false);
            }
            parent.focusedComponent = paste;
            ++copyIdx;
            parent.repaint();
        }
    }

}
