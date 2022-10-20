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
public class AppPasswordField extends JPasswordField {

    public AppPasswordField(int cols) {
        super(cols);
        setName("AppPasswordField");
    }

    public AppPasswordField(int cols, String tip) {
        super(cols);
        setName("AppPasswordField");
        setToolTipText(tip);
    }

    public AppPasswordField(int cols, Character echo, String tip) {
        super(cols);
        setName("AppPasswordField");
        setEchoChar(echo);
        setToolTipText(tip);
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
    public String toString () {
        return "PasswordField: " + getName();
    }
}
