package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JMenuItem
 */
public class AppMenuItem extends JMenuItem {

    public AppMenuItem(String text) {
        super(text);
        setName(Utils.hasValue(text) ? text : "AppMenuItem");
    }

    public AppMenuItem(String text, Character mnemonic) {
        super(text, mnemonic);
        setName(Utils.hasValue(text) ? text : "AppMenuItem");
    }

    public AppMenuItem(String text, Character mnemonic, String tip) {
        super(text, mnemonic);
        setName(Utils.hasValue(text) ? text : "AppMenuItem");
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
        Dimension d = getPreferredSize();
        d.setSize(d.getWidth(), d.getHeight() + (d.getHeight() / 2));
        tooltip.setPreferredSize(d);
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
        return "MenuItem: " + getName();
    }
}
