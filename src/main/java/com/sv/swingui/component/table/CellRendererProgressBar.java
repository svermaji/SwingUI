package com.sv.swingui.component.table;

import com.sv.core.Utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CellRendererProgressBar extends JProgressBar
        implements TableCellRenderer {

    public CellRendererProgressBar() {
        super(0, 100);
        setValue(0);
        setString("0%");
        setStringPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        setToolTipText(table.getValueAt(row, 0).toString());
        if (Utils.hasValue(value.toString())) {
            setValue(Integer.parseInt(value.toString()));
            setString(value.toString()+"%");
        }
        return this;
    }
}
