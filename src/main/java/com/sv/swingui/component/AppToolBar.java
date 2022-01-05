package com.sv.swingui.component;

import com.sv.swingui.UIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JToolbar
 * <p>
 * When JMenu added to JToolbar it mis-aligns
 * so manually layout need to be applied.
 * <p>
 * https://stackoverflow.com/questions/69454055/java-swing-menu-control-not-align
 */
public class AppToolBar extends JToolBar {

    public static final int gap = 0;

    public AppToolBar() {
        this(true);
    }

    public AppToolBar(boolean fixedWidth) {
        super();
        if (fixedWidth) {
            setLayout(new FlowLayout(FlowLayout.LEFT, gap, gap));
        }
        setFloatable(false);
        setRollover(false);
        // no border
        setBorder(UIConstants.ZERO_BORDER);
    }

    public Component add(Component c) {
        c.setSize(new Dimension(c.getWidth(), getHeight()));
        return super.add(c);
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
