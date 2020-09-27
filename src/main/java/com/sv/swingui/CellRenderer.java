package com.sv.swingui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Wrapper class for CellRenderer
 */
public abstract class CellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setToolTipText(table.getValueAt(row, 0).toString());
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

}
