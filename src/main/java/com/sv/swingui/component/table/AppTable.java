package com.sv.swingui.component.table;

import com.sv.core.exception.AppException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.PatternSyntaxException;

/**
 * Wrapper class for JTable
 * <p>
 * String array will be passed to make column names
 * By default non-editable
 */
public class AppTable extends JTable {

    TableRowSorter<DefaultTableModel> sorter;

    public AppTable(DefaultTableModel model) {
        super(model);
        makeNonEditable();
    }

    public void setScrollProps() {
        setAutoscrolls(true);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public void makeNonEditable() {
        // For making contents non editable
        setDefaultEditor(Object.class, null);
    }

    /**
     * TODO: Need to analyze
     * This method not working if called and wrapped 5 calls in one.
     * But if in same sequence methods being called by caller then it works.
     *
     * @param model  Table model
     * @param caller Class calling this method
     * @param tf     text field used for filtering
     * @param action Action taken on event
     * @param params params to be passed with event
     */
    public void setUpSorterAndFilter(DefaultTableModel model, Object caller, JTextField tf, AbstractAction action, Object[] params) {
        addSorter(model);
        addFilter(tf);
        addDblClickOnRow(caller, params);
        addEnterOnRow(action);
        applyChangeListener(tf);
    }

    public TableRowSorter<DefaultTableModel> getAppTableSorter() {
        return sorter;
    }

    public void addSorter(DefaultTableModel model) {
        sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
    }

    public void addDblClickOnRow(Object caller, Object[] params) {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                String methodName = "handleDblClickOnRow";
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    try {
                        caller.getClass().getDeclaredMethod(methodName, table.getClass(), Object[].class).invoke(caller, table, params);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new AppException("Unable to call method " + methodName
                                + " on " + caller.getClass().getName());
                    }
                }
            }
        });
    }

    public void addEnterOnRow(AbstractAction action) {
        InputMap im = getInputMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Action.RunCmdCell");
        ActionMap am = getActionMap();
        am.put("Action.RunCmdCell", action);
    }

    public void applyChangeListener(JTextField tf) {
        tf.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        addFilter(tf);
                    }

                    public void insertUpdate(DocumentEvent e) {
                        addFilter(tf);
                    }

                    public void removeUpdate(DocumentEvent e) {
                        addFilter(tf);
                    }
                });
    }

    public void addFilter(JTextField txtFilter) {
        RowFilter<DefaultTableModel, Object> rf;
        try {
            rf = RowFilter.regexFilter("(?i)" + txtFilter.getText(), 0);
        } catch (PatternSyntaxException e) {
            throw new AppException("Unable to add filter.");
        }
        sorter.setRowFilter(rf);
    }

}
