package com.sv.swingui.component.table;

import javax.swing.*;

/**
 * Sets table cell alignment to CENTER
 */
public class CellRendererCenterAlign extends CellRenderer {

    public CellRendererCenterAlign() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultCellColors();
    }

    public CellRendererCenterAlign(java.util.List<AppCellColorInfo> cci) {
        this();
        setCellColors(cci);
    }
}
