package drawer;

import javax.swing.*;
import java.awt.*;

public class ExportArea extends JTabbedPane {
    JPanel pSchemas, pCommands;
    JTextArea taSchemas, taCommands;
    MainWindow parent;
    StringBuilder msgError, msgCommends, msgSchemas;
    ExportArea(MainWindow parent) {
        this.setPreferredSize(new Dimension(800, 150));
        this.parent = parent;
        pSchemas = new JPanel(new BorderLayout());
        pCommands = new JPanel(new BorderLayout());
        taSchemas = new JTextArea(8, 10);
        taSchemas.setLineWrap(true);
        taSchemas.setEditable(false);

        taCommands = new JTextArea(8, 10);
        taCommands.setLineWrap(true);
        taCommands.setEditable(false);

        msgSchemas = msgCommends = msgError = new StringBuilder();

        pSchemas.add(taSchemas);
        pCommands.add(taCommands);
        this.add("Schemas", pSchemas);
        this.add("MySQL pCommands", pCommands);
    }
    public void compile() {
        if (isCorrect()) {
            taSchemas.setForeground(Color.black);
            taCommands.setForeground(Color.black);
            msgSchemas.setLength(0);
            msgCommends.setLength(0);
            StringBuilder msgKeyAttrs = new StringBuilder();
            for (ERComponent erc: ((drawer.Canvas) ((JScrollPane) this.parent.pages.getSelectedComponent()).getViewport().getView()).components) {
                if (erc instanceof Entity entity) {
                    taSchemas.append(entity.name + "(");
                    taCommands.append("CREATE TABLE " + entity.name + "(");

                    entity.setAttrs();
                    boolean addComma = false;
                    for (Attribute attr: entity.keyAttrs) {
                        if (addComma) {
                            taSchemas.append(", ");
                            taCommands.append(", ");
                            msgKeyAttrs.append(", ");
                        }
                        else {
                            addComma = true;
                        }
                        taSchemas.append("|" + attr.name + "|");
                        taCommands.append(attr.name + " varchar(20)");
                        msgKeyAttrs.append(attr.name);
                    }
                    for (Attribute attr: entity.attrs) {
                        if (addComma) {
                            taSchemas.append(", ");
                            taCommands.append(", ");
                        }
                        else {
                            addComma = true;
                        }

                        taSchemas.append(attr.name);
                        taCommands.append(attr.name + " varchar(20)");
                    }
                    msgKeyAttrs.append(")");
                    taSchemas.append(")\n");
                    if (!entity.keyAttrs.isEmpty()) {
                        taCommands.append(", PRIMARY KEY(" + msgKeyAttrs + ");\n");
                    }
                    else {
                        taCommands .append(");\n");
                    }
                }
                msgKeyAttrs.setLength(0);
            }
        }
    }
    private boolean isCorrect() {
        taSchemas.setForeground(Color.red);
        taCommands.setForeground(Color.red);
        StringBuilder msgError = new StringBuilder();
        for (ERComponent erC: ((Canvas) ((JScrollPane) this.parent.pages.getSelectedComponent()).getViewport().getView()).components) {
            if (erC instanceof Entity) {
                if (!((Entity) erC).haveAttrs()) {
                    msgError.append("實體(Entity)\"").append(((ERComponent2D) erC).name).append("\"沒有連接任何屬性\n");
                }
            }
            else if (erC instanceof Relationship) {
                int count = ((Relationship) erC).getEntities();
                if (count < 2) {
                    msgError.append("關係(Relationship)\"")
                            .append(((ERComponent2D) erC).name).append("\"少於兩個實體(Entity)\n");
                    }
                else if (((Relationship) erC).getKeyEntities() != count) {
                    msgError.append("與關係(Relationship)\"")
                            .append(((ERComponent2D) erC).name).append("\"連線的實體(Entity)").append("缺少key\n");
                }
            }
        }
        taSchemas.setText("");
        taCommands.setText("");
        taCommands.append(msgError.toString());
        taSchemas.append(msgError.toString());
        return msgError.toString().equals("");
    }
}