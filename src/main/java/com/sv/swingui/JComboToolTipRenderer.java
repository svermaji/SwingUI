package com.sv.swingui;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Helper class to show tooltip in combo box for each item
 */
public class JComboToolTipRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // I'd extract the basic "text" representation of the value
        // and pass that to the super call, which will apply it to the
        // JLabel via the setText method, otherwise it will use the
        // objects toString method to generate a representation
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null && Utils.hasValue(value.toString())) {
            setToolTipText(value.toString());
        }
        return this;
    }
}
