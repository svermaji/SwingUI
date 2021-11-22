package com.sv.swingui.component.table;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;

public class AppTableHeaderToolTip extends JTableHeader {

    private final String[] tips;

    public AppTableHeaderToolTip(TableColumnModel model, String[] tips) {
        super(model);
        this.tips = tips;
    }

    public String getToolTipText(MouseEvent e) {
        Point p = e.getPoint();
        int index = columnModel.getColumnIndexAtX(p.x);
        int realIndex = columnModel.getColumn(index).getModelIndex();
        return tips[realIndex];
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
