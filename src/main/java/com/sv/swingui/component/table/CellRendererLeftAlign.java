package com.sv.swingui.component.table;

import javax.swing.*;

/**
 * Sets table cell alignment to LEFT
 */
public class CellRendererLeftAlign extends CellRenderer {

    public CellRendererLeftAlign() {
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public CellRendererLeftAlign(java.util.List<AppCellColorInfo> cci) {
        this();
        setCellColors(cci);
    }
}
