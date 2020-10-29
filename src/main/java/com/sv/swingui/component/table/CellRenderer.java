package com.sv.swingui.component.table;

import com.sv.core.Constants;
import com.sv.core.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.sv.swingui.UIConstants.*;

/**
 * Wrapper class for CellRenderer
 * <p>
 * If cell value of column 1 (column is fixed) starts with "Failed" or "Cancelled"
 * it will be marked reds
 * <p>
 * Considering:
 * 0th column will always be used for tooltip
 * 1st column will be used for indexing
 * 2nd column will be used for data/naming
 */
public abstract class CellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String val = table.getValueAt(row, column < 2 ? 0 : 2).toString();
        boolean isFailedOrCancelled = val.startsWith(Constants.FAILED) ||
                val.startsWith(Constants.CANCELLED);
        c.setForeground(isFailedOrCancelled ? Color.RED : Color.BLACK);

        setToolTipText(table.getValueAt(row, 0).toString());
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
