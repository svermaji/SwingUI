package com.sv.swingui;

import com.sv.core.Constants;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;
import com.sv.swingui.component.*;
import com.sv.swingui.component.table.AppTable;
import com.sv.swingui.component.table.AppTableHeaderToolTip;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
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

    public static void addKeyBindings(JComponent[] addBindingsTo, List<KeyActionDetails> kadList) {
        kadList.forEach(ka -> Arrays.stream(addBindingsTo).forEach(j ->
                j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ka.getKeyStroke(), ka.getAction())));
    }

    public static void getInFocus(JComponent c) {
        c.requestFocusInWindow();
        c.requestFocus();
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

    public static void addEscKeyAction(JDialog dialog) {
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

        dialog.getRootPane().getActionMap().put("Cancel", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
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
    public static AppMenu getFontsMenu(String name, char mnemonic, String tip, String selectedFont,
                                       Object obj, MyLogger logger) {
        AppMenu menuFonts = new AppMenu(name +
                (Utils.hasValue(selectedFont) ? Utils.addBraces(selectedFont) : ""),
                mnemonic, tip);
        int i = 'a';
        int x = 0;
        for (ColorsNFonts cnf : ColorsNFonts.values()) {
            AppMenuItem mi = new AppMenuItem((char) i + SP_DASH_SP + cnf.getFont());
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

    public static void setComponentColor(JComponent[] cs, Color bg, Color fg, Color hbg, Color hfg) {
        Arrays.stream(cs).forEach(c -> setComponentColor(c, bg, fg, hbg, hfg));
    }

    public static void setComponentColor(JComponent[] cs, Color bg, Color fg) {
        setComponentColor(cs, bg, fg, null, null);
    }

    /**
     * This method sets foreground/background color
     *
     * @param c  JComponent
     * @param bg foreground color (optional)
     * @param fg foreground color (optional)
     */
    public static void setComponentColor(JComponent c, Color bg, Color fg) {
        setComponentColor(c, bg, fg, null, null);
    }

    public static Border createTitledBorder(String heading, Color c) {
        return new TitledBorder(createLineBorder(c, 1), heading);
    }

    public static void applyTooltipColor(Component c, Color fg, Color bg) {
        applyTooltipColorNFont(c, fg, bg, null);
    }

    public static void applyTooltipColorNFontAllChild(Component c, Color fg, Color bg, Font tooltipFont) {
        applyTooltipColorNFont(c, fg, bg, tooltipFont);
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                applyTooltipColorNFontAllChild(child, fg, bg, tooltipFont);
            }
        }
    }

    public static void applyTooltipColorNFont(Component c, Color fg, Color bg, Font tooltipFont) {
        if (c instanceof AppPanel) {
            ((AppPanel) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppTabbedPane) {
            ((AppTabbedPane) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppCheckBox) {
            ((AppCheckBox) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppTextField) {
            ((AppTextField) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppPasswordField) {
            ((AppPasswordField) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof JMenuBar) {
            for (int i = 0; i < ((JMenuBar) c).getMenuCount(); i++) {
                applyMenuTooltipColorNFont((AppMenu) ((JMenuBar) c).getMenu(i), fg, bg, tooltipFont);
            }
        }
        if (c instanceof AppButton) {
            ((AppButton) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppLabel) {
            ((AppLabel) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppTable) {
            ((AppTable) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppTableHeaderToolTip) {
            ((AppTableHeaderToolTip) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
        if (c instanceof AppToolBar) {
            ((AppToolBar) c).setToolTipColorsNFont(fg, bg, tooltipFont);
        }
    }

    private static void applyMenuTooltipColor(AppMenu m, Color fg, Color bg) {
        applyMenuTooltipColorNFont(m, fg, bg, null);
    }

    private static void applyMenuTooltipColorNFont(AppMenu m, Color fg, Color bg, Font tooltipFont) {
        m.setToolTipColorsNFont(fg, bg, tooltipFont);
        int miCnt = m.getItemCount();
        for (int j = 0; j < miCnt; j++) {
            JMenuItem mi = m.getItem(j);
            if (mi instanceof AppMenu) {
                applyMenuTooltipColorNFont((AppMenu) mi, fg, bg, tooltipFont);
            } else if (mi instanceof AppMenuItem) {
                ((AppMenuItem) mi).setToolTipColorsNFont(fg, bg, tooltipFont);
            } else if (m.getItem(j) instanceof AppCheckBoxMenuItem) {
                ((AppCheckBoxMenuItem) mi).setToolTipColorsNFont(fg, bg, tooltipFont);
            } else if (m.getItem(j) instanceof AppRadioButtonMenuItem) {
                ((AppRadioButtonMenuItem) mi).setToolTipColorsNFont(fg, bg, tooltipFont);
            }
        }
    }

    private static void applyMenuFont(JMenu m, Font f) {
        m.setFont(f);
        int miCnt = m.getItemCount();
        for (int j = 0; j < miCnt; j++) {
            JMenuItem mi = m.getItem(j);
            if (mi instanceof JMenu) {
                applyMenuFont((JMenu) mi, f);
            } else {
                if (mi != null) {
                    mi.setFont(f);
                }
            }
        }
    }

    public static Border createLineBorder(Color c) {
        return createLineBorder(c, 1);
    }

    public static Border createLineBorder(Color c, int thickness) {
        return BorderFactory.createLineBorder(c, thickness);
    }

    public static TabCloseComponent makeTabClosable(int tabNum, String title, AppTabbedPane tabbedPane) {
        TabCloseComponent tbc = new TabCloseComponent(tabbedPane, tabNum, title);
        return makeTabClosable(tabNum, tbc, tabbedPane);
    }

    public static TabCloseComponent makeTabClosable(int tabNum, TabCloseComponent tcc, JTabbedPane tabbedPane) {
        tabbedPane.setTabComponentAt(tabNum, tcc);
        return tcc;
    }

    /**
     * This method sets foreground/background color and its hover colos
     *
     * @param c   JComponent
     * @param bg  background color (optional)
     * @param fg  foreground color (optional)
     * @param hbg hover background color (optional)
     * @param hfg hover foreground color (optional)
     */
    public static void setComponentColor(JComponent c, Color bg, Color fg, Color hbg, Color hfg) {
        c.setOpaque(true);
        if (bg != null) {
            c.setBackground(bg);
        }
        if (fg != null) {
            c.setForeground(fg);
        }

        c.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                c.setBackground(bg != null ? bg : c.getBackground());
                c.setForeground(fg != null ? fg : c.getForeground());
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (hbg != null) {
                    c.setBackground(hbg);
                }
                if (hfg != null) {
                    c.setForeground(hfg);
                }
            }
        });
    }

    public static AppMenu getThemesMenu(Component obj, MyLogger logger) {
        return getThemesMenu(obj, 'm', logger);
    }

    public static AppMenu getThemesMenu(Component obj, char mnemonic, MyLogger logger) {
        return getThemesMenu("Themes", mnemonic, "Select theme. ", obj, logger);
    }

    public static AppMenu getAppFontMenu(Component rootComp, Object obj, int toSelect, MyLogger logger) {
        return getAppFontMenu(rootComp, 'p', obj, toSelect, logger);
    }

    public static AppMenu getAppFontMenu(Component rootComp, char mnemonic, Object obj, int toSelect, MyLogger logger) {
        return getAppFontMenu("App Font", mnemonic, "Set font to complete application", rootComp, obj, toSelect, logger);
    }

    /**
     * Returns Font size to be applied to complete application
     * This will be useful when application is on higher resolution
     *
     * @param name     menu name
     * @param mnemonic menu shortcut key
     * @param tip      menu tooltip
     * @param rootComp class on which method 'appFontChange' will be called as event
     * @param obj      caller class on which method 'appFontChange' will be called as event
     * @param logger   MyLogger
     */
    public static AppMenu getAppFontMenu(String name, char mnemonic, String tip,
                                         Component rootComp, Object obj, int toSelect, MyLogger logger) {
        // If RESET to defaults functionality is used then default param can be added
        AppMenu menu = new AppMenu(name, mnemonic, tip);
        int MIN_SIZE = 8;
        int MAX_SIZE = 28;
        char menuMnemonic = 'a';
        ButtonGroup appFontBG = new ButtonGroup();

        for (int i = MIN_SIZE; i <= MAX_SIZE; i += 2) {
            // assuming these will be less than 26
            AppRadioButtonMenuItem mi = new AppRadioButtonMenuItem(
                    menuMnemonic + SP_DASH_SP + i, i == toSelect,
                    menuMnemonic++, "Apply font size " + Utils.addBraces(i));
            int finalI = i;
            mi.addActionListener(e -> applyAppFont(rootComp, finalI, obj, logger));
            menu.add(mi);
            appFontBG.add(mi);
        }
        return menu;
    }

    public static void applyAppFont(Component root, int fontSize, Object obj, MyLogger logger) {
        applyAppFont(new Component[]{root}, fontSize, obj, logger);
    }

    public static void applyAppFont(Component[] root, int fontSize, Object obj, MyLogger logger) {
        changeFont(root, fontSize);
        Utils.callMethod(obj, "appFontChanged", new Object[]{fontSize}, logger);
    }

    public static void changeFont(Component c, int fs) {
        changeFont(new Component[]{c}, fs);
    }

    /**
     * Same font is used only size is increased of decreased
     *
     * @param ca array of root component
     * @param fs font size
     */
    public static void changeFont(Component[] ca, int fs) {
        Arrays.stream(ca).forEach(c -> {
            //todo: check why menu need separate handling
            if (c != null) {
                c.setFont(getNewFontSize(c.getFont(), fs));
            }
            if (c instanceof JMenu) {
                applyMenuFont((JMenu) c, getNewFontSize(c.getFont(), fs));
            } else if (c instanceof Container) {
                for (Component child : ((Container) c).getComponents()) {
                    changeFont(child, fs);
                }
            }
        });
    }

    public static void changeFont(Component c, Font f) {
        c.setFont(f);
        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                changeFont(child, f);
            }
        }
    }

    /**
     * Returns themes (Available LookNFeel for Swing) menu.
     * All will be shortcut with keys alphabetically max till 'z'
     *
     * @param name     menu name
     * @param mnemonic menu shortcut key
     * @param tip      menu tooltip
     * @param obj      class on which method 'themeChange' will be called with first param as index and
     *                 2nd as LookAndFeelInfo
     * @param logger   MyLogger
     */
    public static AppMenu getThemesMenu(String name, char mnemonic, String tip,
                                        Component obj, MyLogger logger) {

        AppMenu menu = new AppMenu(name, mnemonic, tip);

        int i = 'a';
        int x = 0;
        for (UIManager.LookAndFeelInfo l : getAvailableLAFs()) {
            AppMenuItem mi = new AppMenuItem((char) i + SP_DASH_SP + l.getName());
            if (i <= 'z') {
                mi.setMnemonic(i);
            }
            int finalX = x;
            mi.addActionListener(e -> applyTheme(finalX, l, obj, logger));
            menu.add(mi);
            i++;
            x++;
        }
        return menu;
    }

    public static void applyTheme(int idx, UIManager.LookAndFeelInfo lnf, Component obj, MyLogger logger) {
        SwingUtilities.invokeLater(() -> {
            String pt = UIManager.getLookAndFeel().getName();
            logger.info("Present theme " + Utils.addBraces(pt)
                    + ", new theme to apply is " + Utils.addBraces(lnf.getName()));
            boolean applied = true;
            if (!pt.equals(lnf.getName())) {
                try {
                    UIManager.setLookAndFeel(lnf.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    applied = false;
                    logger.warn("Unable to apply look and feel " + Utils.addBraces(lnf.getClassName()));
                }
                SwingUtils.updateForTheme(obj);
            } else {
                applied = false;
                logger.warn(pt + " theme already applied.");
            }
            Utils.callMethod(obj, "themeApplied", new Object[]{idx, lnf, applied}, logger);
        });
    }

    public synchronized static void updateForTheme(Component obj) {
        SwingUtilities.updateComponentTreeUI(obj);
    }

    public static AppMenu getColorsMenu(boolean showHighlight,
                                        boolean showHighlightFG,
                                        boolean showSelected,
                                        boolean showFonts,
                                        boolean ignoreBlackAndWhite,
                                        Object obj, MyLogger logger) {
        return getColorsMenu(showHighlight, showHighlightFG, showSelected,
                showFonts, ignoreBlackAndWhite, 'o', obj, logger);
    }

    public static AppMenu getColorsMenu(boolean showHighlight,
                                        boolean showHighlightFG,
                                        boolean showSelected,
                                        boolean showFonts,
                                        boolean ignoreBlackAndWhite,
                                        char mnemonic,
                                        Object obj, MyLogger logger) {
        return getColorsMenu(showFonts ? "Colors and Fonts" : "Colors", mnemonic, "Colors",
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
    public static AppMenu getColorsMenu(String name, char mnemonic, String tip,
                                        boolean showHighlight,
                                        boolean showHighlightFG,
                                        boolean showSelected,
                                        boolean showFonts,
                                        boolean ignoreBlackAndWhite,
                                        Object obj, MyLogger logger) {
        AppMenu menuColors = new AppMenu(name, mnemonic, tip);
        int appfs = Utils.convertToInt(
                (String) Utils.callMethod(obj, "getAppFontSize", null, logger),
                UIConstants.DEFAULT_FONT_SIZE);
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
            AppMenuItem mi = new AppMenuItem((char) i + SP_DASH_SP + "Select this") {
                @Override
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    d.width = Math.max(d.width, (int) (finalCols * appfs * 8.5)); // set minimums
                    d.height = Math.max(d.height, (int) (appfs * 2.5));
                    return d;
                }
            };
            if (i <= 'z') {
                mi.setMnemonic(i++);
            }
            int finalX = x;
            mi.addActionListener(e -> Utils.callMethod(obj, "colorChange", new Object[]{finalX}, logger));
            mi.setLayout(new GridLayout(1, cols));
            mi.add(new AppLabel(""));
            mi.setToolTipText(prepareToolTip(
                    new Color[]{c.getBk(), c.getFg(), c.getSelbk(), c.getSelfg()},
                    showHighlight, showHighlightFG, showSelected, showFonts, c.getFont()));

            if (showHighlight) {
                AppLabel h = new AppLabel("Highlight Text");
                h.setOpaque(true);
                h.setBackground(c.getBk());
                if (showHighlightFG) {
                    h.setForeground(c.getFg());
                }
                h.setHorizontalAlignment(JLabel.CENTER);
                mi.add(h);
            }

            if (showSelected) {
                AppLabel s = new AppLabel("Selected Text");
                s.setOpaque(true);
                s.setBackground(c.getSelbk());
                s.setForeground(c.getSelfg());
                s.setHorizontalAlignment(JLabel.CENTER);
                mi.add(s);
            }

            if (showFonts) {
                AppLabel fo = new AppLabel(c.getFont());
                Font f = mi.getFont();
                Font nf = getNewFont(f, c.getFont());
                fo.setFont(getNewFontSize(nf, appfs));
                fo.setHorizontalAlignment(JLabel.CENTER);
                mi.add(fo);
            }

            menuColors.add(mi);
        }
        return menuColors;
    }

    public static UIManager.LookAndFeelInfo[] getAvailableLAFs() {
        return UIManager.getInstalledLookAndFeels();
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

    public static Font getNewFont(Font font, String name) {
        return new Font(name, font.getStyle(), font.getSize());
    }

    public static Font getNewFontSize(Font font, int size) {
        return new Font(font.getName(), font.getStyle(), size);
    }
}
