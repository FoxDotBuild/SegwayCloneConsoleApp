package processing.mode.android;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/* compiled from: Permissions */
class CheckBoxList extends JList {
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    int checkboxWidth = new JCheckBox().getPreferredSize().width;

    /* compiled from: Permissions */
    protected class CellRenderer implements ListCellRenderer {
        protected CellRenderer() {
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected ? CheckBoxList.this.getSelectionBackground() : CheckBoxList.this.getBackground());
            checkbox.setForeground(isSelected ? CheckBoxList.this.getSelectionForeground() : CheckBoxList.this.getForeground());
            checkbox.setEnabled(list.isEnabled());
            checkbox.setFont(CheckBoxList.this.getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : CheckBoxList.noFocusBorder);
            return checkbox;
        }
    }

    public CheckBoxList() {
        setCellRenderer(new CellRenderer());
        this.checkboxWidth += 5;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (CheckBoxList.this.isEnabled()) {
                    int index = CheckBoxList.this.locationToIndex(e.getPoint());
                    if (index != -1) {
                        JCheckBox checkbox = (JCheckBox) CheckBoxList.this.getModel().getElementAt(index);
                        if (e.getX() < CheckBoxList.this.checkboxWidth) {
                            checkbox.setSelected(!checkbox.isSelected());
                            CheckBoxList.this.repaint();
                        }
                    }
                }
            }
        });
        setSelectionMode(0);
    }
}
