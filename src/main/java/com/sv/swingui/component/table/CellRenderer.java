package com.sv.swingui.component.table;

import com.sv.core.Constants;
import com.sv.core.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.sv.swingui.UIConstants.COLOR_BLUE_SHADE;

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

    protected boolean showSameTipOnRow = true;
    protected int sameTipColNum = 0;
    protected boolean showTip = true;
    protected boolean highlightText = true;
    protected boolean matchStartWith = true;
    protected String[] badColorText = {"wrong", "false", "no",
            Constants.FAILED, Constants.CANCELLED};
    protected String[] goodColorText = {"correct", "true", "yes"};
    protected java.util.List<AppCellColorInfo> cellColors = new ArrayList<>();
    protected Color defaultTextColor = Color.black;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // todo: need to check if color for 1st column only. Mostly 1st column is hidden for index
        // todo: check below line so color can be applied to only one column
        // commenting for now so to apply on all columns
        //String val = table.getValueAt(row, column < 2 ? 0 : 2).toString();
        String val = table.getValueAt(row, column).toString();
        c.setForeground(getCellTextColor(val));

        if (showTip) {
            if (showSameTipOnRow) {
                // tooltip on row will be applied with first col value
                setToolTipText(((AppTable) table).getTooltipFor(row, 0, table.getValueAt(row, sameTipColNum).toString()));
            } else {
                setToolTipText(((AppTable) table).getTooltipFor(row, column, table.getValueAt(row, column).toString()));
            }
        }
        return c;
    }

    protected Color getCellTextColor(String val) {
        if (!highlightText) {
            return defaultTextColor;
        }

        Color c = defaultTextColor;
        for (AppCellColorInfo cci : getCellColors()) {

            boolean result = matchStartWith ?
                    Utils.isInArrayMatchStart(cci.getTextList(), val) :
                    Utils.isInArray(cci.getTextList(), val);
            if (result) {
                c = cci.getTextColor();
                break;
            }
        }

        return c;
    }

    public boolean isMatchStartWith() {
        return matchStartWith;
    }

    public void setMatchStartWith(boolean matchStartWith) {
        this.matchStartWith = matchStartWith;
    }

    public void setShowSameTipOnRow(boolean showSameTipOnRow) {
        this.showSameTipOnRow = showSameTipOnRow;
    }

    public void setSameTipColNum(int sameTipColNum) {
        this.sameTipColNum = sameTipColNum;
    }

    public void setShowTip(boolean showTip) {
        this.showTip = showTip;
    }

    protected void setDefaultCellColors() {
        setCellColor(new AppCellColorInfo(Color.red, badColorText));
        setCellColor(new AppCellColorInfo(COLOR_BLUE_SHADE, goodColorText));
    }

    public void setCellColors(List<AppCellColorInfo> ccInfo) {
        cellColors = ccInfo;
    }

    public void setCellColor(AppCellColorInfo ccInfo) {
        cellColors.add(ccInfo);
    }

    public void setDefaultTextColor(Color defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public List<AppCellColorInfo> getCellColors() {
        return cellColors;
    }

    public Color getDefaultTextColor() {
        return defaultTextColor;
    }
}
