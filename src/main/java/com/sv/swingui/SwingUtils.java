package com.sv.swingui;

import com.sv.core.Constants;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.sv.core.Constants.SP_DASH_SP;
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

    public static String htmlBGColorStr(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return String.format("rgb(%s, %s, %s)", r, g, b);
    }

    public static String htmlBGColor(Color color) {
        return htmlBGColor(htmlBGColorStr(color));
    }

    public static String htmlBGColor(Color color, String text) {
        return htmlBGColor(htmlBGColorStr(color), text);
    }

    /**
     * Return <font ..... > and font tag does not ends
     *
     * @param color background color
     * @return string
     */
    public static String htmlBGColor(String color) {
        return FONT_PREFIX + color + FONT_PREFIX_END;
    }

    /**
     * Return <font style="background-color: param-color">text</font> format
     *
     * @param color background color
     * @param text  text
     * @return string
     */
    public static String htmlBGColor(String color, String text) {
        return htmlBGColor(color) + text + FONT_SUFFIX;
    }

    public static String htmlBGColor(Color bgcolor, Color color, String text) {
        return htmlBGColor(htmlBGColorStr(bgcolor), htmlBGColorStr(color), text);
    }

    public static String htmlBGColor(String bgcolor, String color, String text) {
        return FONT_PREFIX + bgcolor + FONT_FG_PREFIX + color + FONT_PREFIX_END + text + FONT_SUFFIX;
    }

    public static void createEmptyRows(int colLen, int rows, DefaultTableModel model) {
        String[] emptyRow = new String[colLen];
        Arrays.fill(emptyRow, Constants.EMPTY);
        IntStream.range(0, rows).forEach(i -> model.addRow(emptyRow));
    }

    /**
     * Returns font menu with each font in its own font face as menu item.
     * All will be shortcut with keys alphabetically max till 'z'
     *
     * @param name         menu name
     * @param mnemonic     menu shortcut key
     * @param tip          menu tooltip
     * @param selectedFont if not null then name will be shown
     * @param obj          class on which method 'fontChange' will be called with first param as font and
     *                     2nd as index of menuitem
     * @param logger       MyLogger
     * @return menu object
     */
    public static JMenu getFontsMenu(String name, char mnemonic, String tip, String selectedFont,
                                     Object obj, MyLogger logger) {
        JMenu menuFonts = new JMenu(name +
                (Utils.hasValue(selectedFont) ? Utils.addBraces(selectedFont) : ""));
        menuFonts.setMnemonic(mnemonic);
        menuFonts.setToolTipText(tip + SHORTCUT + mnemonic);
        int i = 'a';
        int x = 0;
        for (ColorsNFonts cnf : ColorsNFonts.values()) {
            JMenuItem mi = new JMenuItem((char) i + SP_DASH_SP + cnf.getFont());
            if (i <= 'z') {
                mi.setMnemonic(i++);
            }
            Font f = mi.getFont();
            Font nf = getNewFont(f, cnf.getFont());
            mi.setFont(nf);
            int finalX = x;
            mi.addActionListener(e -> Utils.callMethod(obj, "fontChange", new Object[]{nf, finalX}, logger));

            menuFonts.add(mi);
            x++;
        }

        return menuFonts;
    }

    public static JMenu getColorsMenu(boolean showHighlight,
                                      boolean showHighlightFG,
                                      boolean showSelected,
                                      boolean showFonts,
                                      boolean ignoreBlackAndWhite,
                                      Object obj, MyLogger logger) {
        return getColorsMenu(showFonts ? "Colors" : "Colors and Fonts", 'o', "Colors",
                showHighlight, showHighlightFG, showSelected,
                showFonts, ignoreBlackAndWhite, obj, logger);
    }

    /**
     * Returns colors menu with each color as hightlighted and selected as menu item.
     * All will be shortcut with keys alphabetically max till 'z'
     *
     * @param name            menu name
     * @param mnemonic        menu shortcut key
     * @param tip             menu tooltip
     * @param showHighlight   whether to show highlight
     * @param showHighlightFG whether to show highlight foreground
     * @param showSelected    whether to show selected sample
     * @param obj             class on which method 'fontChange' will be called with first param as font and
     *                        2nd as index of menuitem
     * @param logger          MyLogger
     */
    public static JMenu getColorsMenu(String name, char mnemonic, String tip,
                                      boolean showHighlight,
                                      boolean showHighlightFG,
                                      boolean showSelected,
                                      boolean showFonts,
                                      boolean ignoreBlackAndWhite,
                                      Object obj, MyLogger logger) {
        JMenu menuColors = new JMenu(name);
        menuColors.setMnemonic(mnemonic);
        menuColors.setToolTipText(tip + SHORTCUT + mnemonic);
        int i = 'a';
        int x = -1;

        for (ColorsNFonts c : getFilteredCnF(ignoreBlackAndWhite)) {
            x++;

            int cols = 1;
            if (showHighlight) {
                cols++;
            }
            if (showSelected) {
                cols++;
            }
            if (showFonts) {
                cols++;
            }
            int finalCols = cols;
            JMenuItem mi = new JMenuItem((char) i + SP_DASH_SP + "Select this") {
                @Override
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    d.width = Math.max(d.width, finalCols * 100); // set minimums
                    d.height = Math.max(d.height, 30);
                    return d;
                }
            };
            if (i <= 'z') {
                mi.setMnemonic(i++);
            }
            int finalX = x;
            mi.addActionListener(e -> Utils.callMethod(obj, "colorChange", new Object[]{finalX}, logger));
            mi.setLayout(new GridLayout(1, cols));
            mi.add(new JLabel(""));
            mi.setToolTipText(prepareToolTip(
                    new Color[]{c.getBk(), c.getFg(), c.getSelbk(), c.getSelfg()},
                    showHighlight, showHighlightFG, showSelected, showFonts, c.getFont()));

            if (showHighlight) {
                JLabel h = new JLabel("Highlight Text");
                h.setOpaque(true);
                h.setBackground(c.getBk());
                if (showHighlightFG) {
                    h.setForeground(c.getFg());
                }
                h.setHorizontalAlignment(JLabel.CENTER);
                mi.add(h);
            }

            if (showSelected) {
                JLabel s = new JLabel("Selected Text");
                s.setOpaque(true);
                s.setBackground(c.getSelbk());
                s.setForeground(c.getSelfg());
                s.setHorizontalAlignment(JLabel.CENTER);
                mi.add(s);
            }

            if (showFonts) {
                JLabel fo = new JLabel(c.getFont());
                Font f = mi.getFont();
                Font nf = getNewFont(f, c.getFont());
                fo.setFont(nf);
                fo.setHorizontalAlignment(JLabel.CENTER);
                mi.add(fo);
            }

            menuColors.add(mi);
        }
        return menuColors;
    }

    public static ColorsNFonts[] getFilteredCnF(boolean ignoreBlackAndWhite) {
        ColorsNFonts[] allCnF = ColorsNFonts.values();

        if (!ignoreBlackAndWhite) {
            return allCnF;
        }

        java.util.List<ColorsNFonts> filteredCnF = new ArrayList<>();
        for (ColorsNFonts c : allCnF) {
            if (c.getBk() != Color.white && c.getBk() != Color.black) {
                filteredCnF.add(c);
            }
        }
        return filteredCnF.toArray(new ColorsNFonts[0]);
    }

    private static String prepareToolTip(Color[] c, boolean showHighlight,
                                         boolean showHighlightFG,
                                         boolean showSelected,
                                         boolean showFonts, String font) {

        StringBuilder sb = new StringBuilder();
        if (showHighlight) {
            sb.append("Sample highlight text: ");
            sb.append(showHighlightFG ? SwingUtils.htmlBGColor(c[0], c[1], "Highlight text")
                    : SwingUtils.htmlBGColor(c[0], "Highlight text"));
        }
        if (showSelected) {
            sb.append(showHighlight ? BR : "");
            sb.append("Sample selected text: ").append(SwingUtils.htmlBGColor(c[2], c[3], "Selected text"));
        }
        if (showFonts) {
            sb.append(showHighlight || showSelected ? BR : "");
            sb.append("Font name: ").append(font);
        }
        return HTML_STR + sb.toString() + HTML_END;
    }

    private static Font getNewFont(Font font, String name) {
        return new Font(name, font.getStyle(), font.getSize());
    }
}
