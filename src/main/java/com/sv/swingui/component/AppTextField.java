package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Wrapper class for JTextField
 * This supports auto-complete feature as well
 */
public class AppTextField extends JTextField {

    private String[] autoCompleteArr;

    /**
     * Creates text field with given info
     *
     * @param text string
     */
    public AppTextField(String text) {
        this(text, -1);
    }

    /**
     * Creates text field with given info
     *
     * @param text     String
     * @param colWidth columns
     */
    public AppTextField(String text, int colWidth) {
        this(text, colWidth, null);
    }

    /**
     * Creates text field with given info
     *
     * @param text     String
     * @param colWidth columns
     * @param arr      String array that has auto complete values
     */
    public AppTextField(String text, int colWidth, String[] arr) {
        super(text);
        setName(Utils.hasValue(text) ? text : "AppTextField");
        if (colWidth > 0) {
            setColumns(colWidth);
        }
        if (arr != null) {
            autoCompleteArr = arr;
            setupAutoComplete();
        }
        selectOnFocus();
    }

    /**
     * Focus listener by default will be applied on text field
     * If another focus listener need to be added this method
     * can be called separately.
     */
    public void selectOnFocus() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (Utils.hasValue(getText()) && isVisible()) {
                    selectAll();
                }
            }
        });
    }

    public void setAutoCompleteArr(String[] autoCompleteArr) {
        this.autoCompleteArr = autoCompleteArr;
    }

    private void setupAutoComplete() {
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (Character.isLetterOrDigit(e.getKeyChar()) || Utils.isSpecialChar(e.getKeyChar())) {
                    replaceSelection("");
                    String txt = getText();
                    int len = txt.length();
                    String match = Utils.getMatchedLCSubStr(txt, autoCompleteArr);
                    if (Utils.hasValue(match)) {
                        setText(txt + match);
                        setCaretPosition(len);
                        select(len, len + match.length());
                    }
                }
            }
        });
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

    @Override
    public String toString() {
        return "TextField: " + getName();
    }
}
