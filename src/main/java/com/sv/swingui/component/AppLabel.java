package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JLabel
 */
public class AppLabel extends JLabel {

    public AppLabel() {
        super();
        setName("AppLabel");
    }

    public AppLabel(String text) {
        this(text, null, null, null);
    }

    public AppLabel(String text, JComponent component, Character mnemonic) {
        this(text, component, mnemonic, null);
    }

    public AppLabel(String text, Character mnemonic, String tip) {
        this(text, null, mnemonic, tip);
    }

    public AppLabel(String text, JComponent component, Character mnemonic, String tip) {
        setText(text);
        setName(Utils.hasValue(text) ? text : "AppLabel");
        if (component != null) {
            setLabelFor(component);
        }
        if (mnemonic != null) {
            setDisplayedMnemonic(mnemonic);
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
        return "Label: " + getName();
    }
}
