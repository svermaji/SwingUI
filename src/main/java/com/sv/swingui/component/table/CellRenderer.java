package com.sv.swingui.component.table;

import com.sv.core.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

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

    private boolean showSameTipOnRow = true;
    private boolean highlightFailed = true;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String val = table.getValueAt(row, column < 2 ? 0 : 2).toString();
        boolean isFailedOrCancelled = highlightFailed &&
                val.startsWith(Constants.FAILED) ||
                val.startsWith(Constants.CANCELLED);
        c.setForeground(isFailedOrCancelled ? Color.RED : Color.BLACK);

        if (showSameTipOnRow) {
            // tooltip on row will be applied with first col value
            setToolTipText(((AppTable)table).getTooltipFor(row, 0, table.getValueAt(row, 0).toString()));
        } else {
            setToolTipText(((AppTable)table).getTooltipFor(row, column, table.getValueAt(row, column).toString()));
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    public void setShowSameTipOnRow(boolean showSameTipOnRow) {
        this.showSameTipOnRow = showSameTipOnRow;
    }

    public void setHighlightFailed(boolean highlightFailed) {
        this.highlightFailed = highlightFailed;
    }

}
