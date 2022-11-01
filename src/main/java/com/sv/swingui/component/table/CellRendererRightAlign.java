package com.sv.swingui.component.table;

import javax.swing.*;

/**
 * Sets table cell alignment to RIGHT
 */
public class CellRendererRightAlign extends CellRenderer {

    public CellRendererRightAlign() {
        setHorizontalAlignment(SwingConstants.RIGHT);
        setDefaultCellColors();
    }

    public CellRendererRightAlign(java.util.List<AppCellColorInfo> cci) {
        this();
        setCellColors(cci);
    }
}
