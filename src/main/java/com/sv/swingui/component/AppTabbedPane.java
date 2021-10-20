package com.sv.swingui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JTabbedPane
 */
public class AppTabbedPane extends JTabbedPane {

    public AppTabbedPane() {
        super();
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
