package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

public class AppRadioButtonMenuItem extends JRadioButtonMenuItem {

    public AppRadioButtonMenuItem(String text, boolean state, Character mnemonic, String tip) {
        this(text, null, state, mnemonic, tip);
    }

    public AppRadioButtonMenuItem(String text, Icon icon, boolean state, Character mnemonic, String tip) {
        super(text, icon, state);
        if (mnemonic != null) {
            setMnemonic(mnemonic);
        }
        if (Utils.hasValue(tip)) {
            if (mnemonic != null) {
                tip += " Shortcut: Alt+" + mnemonic;
            }
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