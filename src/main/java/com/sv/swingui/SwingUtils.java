package com.sv.swingui;

import com.sv.core.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.sv.swingui.UIConstants.*;

/**
 * Static methods as helper methods
 * Works only for Swing application
 */
public class SwingUtils {

    /**
     * Returns plain font
     *
     * @param size font size
     * @return new Font
     */
    public static Font getPlainCalibriFont(int size) {
        return getCalibriFont(Font.PLAIN, size);
    }

    /**
     * Returns Calibri font of size if available
     *
     * @param style PLAIN or BOLD
     * @param size  font size
     * @return new Font
     */
    public static Font getCalibriFont(int style, int size) {
        Font retVal = new Font(DEFAULT_FONT, style, size);
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (Font font : g.getAllFonts()) {
            if (font.getName().equals(PREFERRED_FONT)) {
                retVal = font;
                break;
            }
        }
        return new Font(retVal.getName(), retVal.getStyle(), size);
    }

    public static Font getDefaultFont(int style, int size) {
        return new Font(DEFAULT_FONT, style, size);
    }

    public static void addEscKeyAction(JFrame frame) {
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
    }

    public static DefaultTableModel getTableModel(String[] cols) {
        return new DefaultTableModel() {

            @Override
            public int getColumnCount() {
                return cols.length;
            }

            @Override
            public String getColumnName(int index) {
                return cols[index];
            }
        };
    }

    public static void removeAndCreateEmptyRows(int colLen, int rows, DefaultTableModel model) {
        model.setRowCount(0);
        createEmptyRows(colLen, rows, model);
    }

    public static String htmlBGColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return htmlBGColor(String.format("rgb(%s, %s, %s)", r, g, b));
    }

    public static String htmlBGColor(Color color, String text) {
        return htmlBGColor(htmlBGColor(color), text);
    }

    /**
     * Return <font ..... > and font tag does not ends
     * @param color background color
     * @return string
     */
    public static String htmlBGColor(String color) {
        return FONT_PREFIX + color + FONT_PREFIX_END;
    }

    /**
     * Return <font style="background-color: param-color">text</font> format
     * @param color background color
     * @param text text
     * @return string
     */
    public static String htmlBGColor(String color, String text) {
        return htmlBGColor(color) + text + FONT_SUFFIX;
    }

    public static String htmlBGColor(Color bgcolor, Color color, String text) {
        return htmlBGColor(htmlBGColor(bgcolor), htmlBGColor(color), text);
    }

    public static String htmlBGColor(String bgcolor, String color, String text) {
        return FONT_PREFIX + bgcolor + FONT_FG_PREFIX + color + FONT_PREFIX_END + text + FONT_SUFFIX;
    }

    public static void createEmptyRows(int colLen, int rows, DefaultTableModel model) {
        String[] emptyRow = new String[colLen];
        Arrays.fill(emptyRow, Constants.EMPTY);
        IntStream.range(0, rows).forEach(i -> model.addRow(emptyRow));
    }
}
