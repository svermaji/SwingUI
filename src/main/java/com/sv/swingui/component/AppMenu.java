package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JMenu
 */
public class AppMenu extends JMenu {

    /**
     * Creates menu with given info
     *
     * @param text     string
     */
    public AppMenu(String text) {
        this(text, null);
    }

    /**
     * Creates menu with given info
     *
     * @param text     string
     * @param mnemonic char
     */
    public AppMenu(String text, Character mnemonic) {
        this(text, mnemonic, null);
    }

    /**
     * Creates menu with given info
     *
     * @param text     string
     * @param mnemonic char
     * @param tip      tooltip
     */
    public AppMenu(String text, Character mnemonic, String tip) {
        if (Utils.hasValue(text)) {
            setText(text);
        }
        setMnemonic(mnemonic);
        setToolTipText(" Shortcut: Alt+" + mnemonic);
        if (Utils.hasValue(tip)) {
            setToolTipText(tip + getToolTipText());
        }
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

}
