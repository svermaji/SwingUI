package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

public class AppCheckBoxMenuItem extends JCheckBoxMenuItem {

    public AppCheckBoxMenuItem(String text, boolean state, Character mnemonic, String tip) {
        this(text, null, state, mnemonic, tip);
    }

    public AppCheckBoxMenuItem(String text, Icon icon, boolean state, Character mnemonic, String tip) {
        super(text, icon, state);
        setName(Utils.hasValue(text) ? text : "AppCheckBoxMenuItem");
        if (mnemonic != null) {
            setMnemonic(mnemonic);
        }
        if (Utils.hasValue(tip)) {
            setToolTipText(tip);
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

    @Override
    public String toString () {
        return "CheckBoxMenuItem: " + getName();
    }
}
