package com.sv.swingui.component;

import com.sv.swingui.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Wrapper class for JTabbedPane
 */
public class AppTabbedPane extends JTabbedPane {

    public AppTabbedPane() {
        super();
    }

    public AppTabbedPane(boolean needPopupMenu) {
        super();
        if (needPopupMenu) {
            initPopupMenu(this);
        }
    }

    private void initPopupMenu(AppTabbedPane pane) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    final int index = pane.getUI().tabForCoordinate(pane, e.getX(), e.getY());
                    if ( index != -1 )
                    {
                        tabRightClicked (pane, index);
                    }
                }
            }
        });
    }

    public void tabRightClicked(AppTabbedPane pane, int tabIdx) {
        // used to override for behavior
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
