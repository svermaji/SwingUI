package com.sv.swingui.component.table;

import com.sv.core.Constants;
import com.sv.core.Utils;
import com.sv.swingui.component.AppLabel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static com.sv.swingui.UIConstants.COLOR_BLUE_SHADE;

public class CellRendererStringImg implements TableCellRenderer {

    private final Map<String, String> iconPaths;
    private boolean showTextWithIcon;

    public CellRendererStringImg(Map<String, String> iconPaths) {
        this (iconPaths, true);
    }

    public CellRendererStringImg(Map<String, String> iconPaths, boolean showTextWithIcon) {
        this.iconPaths = iconPaths;
        this.showTextWithIcon = showTextWithIcon;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        String val = table.getValueAt(row, column).toString();
        AppLabel lbl = new AppLabel(showTextWithIcon ? val : Constants.EMPTY,
                iconPaths.get(val), val);
        lbl.setForeground(getCellTextColor(val));
        return lbl;
    }

    protected String[] badColorText = {"wrong", "false", "no",
            Constants.FAILED, Constants.CANCELLED};
    protected String[] goodColorText = {"correct", "true", "yes"};
    protected Color getCellTextColor(String val) {
        Color c = Color.black;
        java.util.List<AppCellColorInfo> cellColors = new ArrayList<>();
        cellColors.add(new AppCellColorInfo(Color.red, badColorText));
        cellColors.add(new AppCellColorInfo(COLOR_BLUE_SHADE, goodColorText));

        for (AppCellColorInfo cci : cellColors) {

            boolean result = Utils.isInArrayMatchStart(cci.getTextList(), val);
            if (result) {
                c = cci.getTextColor();
                break;
            }
        }

        return c;
    }

}
