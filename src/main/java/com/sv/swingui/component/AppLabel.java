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
    }

    public AppLabel(String text) {
        this(text, null, null);
    }

    public AppLabel(String text, JComponent component, Character mnemonic) {
        this(text, component, mnemonic, null);
    }

    public AppLabel(String text, JComponent component, Character mnemonic, String tip) {
        setText(text);
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

    public void setToolTipColors(Color fg, Color bg) {
        this.bg = bg;
        this.fg = fg;
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
        return tooltip;
    }
}
