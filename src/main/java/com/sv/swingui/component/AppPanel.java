package com.sv.swingui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JPanel
 */
public class AppPanel extends JPanel {

    public AppPanel() {
        super();
    }

    public AppPanel(LayoutManager layout) {
        super(layout);
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

    public Dimension getCenterOfScreen() {
        Dimension dm = getSize();
        int xMid = (int) (dm.getWidth()/2);
        int yMid = (int) (dm.getHeight()/2);
        return new Dimension(xMid, yMid);
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
