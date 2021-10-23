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

    protected JPopupMenu popupMenu;
    protected boolean needPopupMenu;

    public AppTabbedPane() {
        super();
    }

    public AppTabbedPane(boolean needPopupMenu) {
        super();
        this.needPopupMenu = needPopupMenu;
        if (needPopupMenu) {
            initPopupMenu(this);
        }
    }

    private void initPopupMenu(AppTabbedPane pane) {
        popupMenu = new JPopupMenu();
        AppMenuItem miCloseOthers = new AppMenuItem("Close others", 'o');
        AppMenuItem miCloseTabsToRight = new AppMenuItem("Close tabs to right", 'r');
        AppMenuItem miCloseTabsToLeft = new AppMenuItem("Close tabs to left", 'l');
        AppMenuItem miCloseAll = new AppMenuItem("Close all", 'a');
        popupMenu.add(miCloseOthers);
        popupMenu.add(miCloseTabsToRight);
        popupMenu.add(miCloseTabsToLeft);
        popupMenu.add(miCloseAll);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("is right clk " + SwingUtilities.isRightMouseButton(e));
                if (SwingUtilities.isRightMouseButton(e)) {
                    final int index = pane.getUI().tabForCoordinate(pane, e.getX(), e.getY());
                    if (index != -1) {
                        tabRightClicked(pane, index);
                    }
                }
            }
        });
    }

    /**
     * Can override for custom behavior
     *
     * @param pane   AppTabbedPane
     * @param tabIdx tab index
     */
    public void tabRightClicked(AppTabbedPane pane, int tabIdx) {
        // used to override for behavior
        System.out.println("right click " + tabIdx);
        final Rectangle tabBounds = pane.getBoundsAt(tabIdx);
        popupMenu.show(pane, tabBounds.x, tabBounds.y + tabBounds.height);
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public boolean isNeedPopupMenu() {
        return needPopupMenu;
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
