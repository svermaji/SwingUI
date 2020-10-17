package com.sv.swingui;

import com.sv.core.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

/**
 * Wrapper class for JTable
 * <p>
 * String array will be passed to make column names
 * By default non-editable
 */
public class AppTable extends JTable {

    public AppTable(DefaultTableModel model) {
        super(model);
        makeNonEditable();
        setScrollProps();
    }

    public void setScrollProps() {
        setAutoscrolls(true);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public void makeNonEditable() {
        // For making contents non editable
        setDefaultEditor(Object.class, null);
    }

}
