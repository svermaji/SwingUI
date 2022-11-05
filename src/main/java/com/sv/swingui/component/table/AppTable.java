package com.sv.swingui.component.table;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sv.core.exception.AppException;
import com.sv.swingui.SwingUtils;
import com.sv.swingui.component.AppTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Wrapper class for JTable
 * <p>
 * String array will be passed to make column names
 * By default non-editable
 */
public class AppTable extends JTable {

    protected TableRowSorter<DefaultTableModel> sorter;
    protected java.util.List<String[]> tooltips = new ArrayList<>();
    protected boolean alternateRowColor = true;
    protected boolean highlightRowOnMouseOver = true;
    protected Color color1stRow = Color.white,
            color2ndRow = new Color(233, 255, 233),
            colorRollOver = new Color(170, 196, 255);

    private int rollOverRowIndex = -1;
    private RollOverListener roll = null;

    public AppTable(DefaultTableModel model) {
        super(model);
        makeNonEditable();
        updateRollOverListeners();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void setScrollProps() {
        setAutoscrolls(true);
        setPreferredScrollableViewportSize(getPreferredSize());
    }

    public void makeNonEditable() {
        // For making contents non-editable
        setDefaultEditor(Object.class, null);
    }

    public void emptyRowTooltips() {
        tooltips = new ArrayList<>();
    }

    /**
     * Assuming data model for table is of type {@link DefaultTableModel}
     */
    public void emptyRows() {
        ((DefaultTableModel) getModel()).setRowCount(0);
        emptyRowTooltips();
    }

    public void gotoFirstRow() {
        changeSelection(1, 1, false, false);
    }

    public void addRowTooltip(String[] tips) {
        tooltips.add(tips);
    }

    /**
     * Get tooltip from List manually populated.
     * In case of tooltip not available defaultVal will be returned
     *
     * @param row        row number to search
     * @param col        column number to search
     * @param defaultVal default tooltip
     * @return Tip to show
     */
    public String getTooltipFor(int row, int col, String defaultVal) {
        String result = defaultVal;
        if (tooltips.size() > row) {
            String[] cols = tooltips.get(row);
            if (cols.length > col) {
                result = cols[col];
            }
        }
        return result;
    }

    public void hideFirstColumn() {
        // to hide first column
        TableColumn colIdx = getColumnModel().getColumn(0);
        if (colIdx != null) {
            colIdx.setMinWidth(-1);
            colIdx.setMaxWidth(-1);
        }
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
    public void setUpSorterAndFilter(DefaultTableModel model, Object caller, AppTextField tf, AbstractAction action, Object[] params) {
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

    /**
     * Caller should implement "handleDblClickOnRow" method
     * if custom method call is needed
     *
     * @param caller Parent object
     * @param params parameters to pass
     */
    public void addDblClickOnRow(Object caller, Object[] params) {
        addDblClickOnRow(caller, params, "handleDblClickOnRow");
    }

    public void addDblClickOnRow(Object caller, Object[] params, String methodName) {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
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

    public boolean isAlternateRowColor() {
        return alternateRowColor;
    }

    public void setAlternateRowColor(boolean alternateRowColor) {
        this.alternateRowColor = alternateRowColor;
    }

    public boolean isHighlightRowOnMouseOver() {
        return highlightRowOnMouseOver;
    }

    public void setHighlightRowOnMouseOver(boolean highlightRowOnMouseOver) {
        this.highlightRowOnMouseOver = highlightRowOnMouseOver;
        updateRollOverListeners();
    }

    private void updateRollOverListeners() {
        if (highlightRowOnMouseOver) {
            if (roll == null) {
                roll = new RollOverListener();
            }
            // remove any last registered
            removeMouseListener(roll);
            removeMouseMotionListener(roll);
            addMouseListener(roll);
            addMouseMotionListener(roll);
        } else {
            if (roll != null) {
                removeMouseListener(roll);
                removeMouseMotionListener(roll);
            }
        }
    }

    public void setAlternateRowColors(Color c1, Color c2) {
        this.color1stRow = c1;
        this.color2ndRow = c2;
    }

    public void setRollOverColor(Color c) {
        this.colorRollOver = c;
    }

    /**
     * AbstractAction object will be invoked when user
     * hits ENTER on a row.
     *
     * @param action AbstractAction object to be called
     */
    public void addEnterOnRow(AbstractAction action) {
        InputMap im = getInputMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Action.RunCmdCell");
        ActionMap am = getActionMap();
        am.put("Action.RunCmdCell", action);
    }

    public void applyChangeListener(AppTextField tf) {
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

    public void addFilter(AppTextField txtFilter) {
        RowFilter<DefaultTableModel, Object> rf;
        try {
            rf = RowFilter.regexFilter("(?i)" + txtFilter.getText(), 0);
        } catch (PatternSyntaxException e) {
            throw new AppException("Unable to add filter.");
        }
        sorter.setRowFilter(rf);
    }

    private boolean needBorderRepaint() {
        String lnf = UIManager.getLookAndFeel().getName();
        return !lnf.equals(((LookAndFeel) new NimbusLookAndFeel()).getName())
                &&
                !lnf.equals(((LookAndFeel) new MotifLookAndFeel()).getName());
    }

    // Workaround for bug where after Nimbus LnF line went off
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
        // to check if required to apply for other LnFs also
        if (needBorderRepaint()) {
            component.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TableHeader.cellBorder"));
        }

        if (alternateRowColor) {
            component.setBackground(row % 2 == 0 ? color1stRow : color2ndRow);
        }

        if (highlightRowOnMouseOver && row == rollOverRowIndex) {
            component.setBackground(colorRollOver);
        }

        if (!highlightRowOnMouseOver && isRowSelected(row)) {
            component.setBackground(SwingUtils.isWhiteOrBlack(bg) ? Color.lightGray : bg);
        }
        return component;
    }

    /* For coloring tooltip */
    private Color fg, bg;
    private Font tooltipFont;

    public void setToolTipColors(Color fg, Color bg) {
        this.bg = bg;
        this.fg = fg;
    }

    public void setToolTipColorsNFont(Color fg, Color bg, Font f) {
        this.bg = bg;
        this.fg = fg;
        this.tooltipFont = f;
    }

    public void setTooltipFont(Font tooltipFont) {
        this.tooltipFont = tooltipFont;
    }

    @Override
    public JToolTip createToolTip() {
        JToolTip tooltip = super.createToolTip();
        if (bg != null) {
            tooltip.setBackground(bg);
        }
        if (fg != null) {
            tooltip.setForeground(fg);
        }
        if (tooltipFont != null) {
            tooltip.setFont(tooltipFont);
        }
        return tooltip;
    }

    private class RollOverListener extends MouseInputAdapter {
        public void mouseExited(MouseEvent e) {
            rollOverRowIndex = -1;
            repaint();
        }

        public void mouseMoved(MouseEvent e) {
            int row = rowAtPoint(e.getPoint());
            if (row != rollOverRowIndex) {
                rollOverRowIndex = row;
                repaint();
            }
        }

    }
}
