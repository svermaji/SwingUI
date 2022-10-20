package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;

public class AppComboBox extends JComboBox {

    public AppComboBox(Object[] items, int selectedIdx, String tip) {
        super(items);
        setName("AppComboBox");
        if (selectedIdx >= -1 && selectedIdx < items.length) {
            setSelectedIndex(selectedIdx);
        }
        if (Utils.hasValue(tip)) {
            setToolTipText(tip);
        }
    }

    public AppComboBox(Object[] items, Object selectedVal, String tip) {
        this (items, -2, tip);
        if (selectedVal != null) {
            setSelectedItem(selectedVal);
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
        return "ComboBox: " + getName();
    }
}
