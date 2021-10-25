package com.sv.swingui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Wrapper class for JTabbedPane
 */
public class AppTabbedPane extends JTabbedPane {

    protected JPopupMenu popupMenu;
    protected boolean needPopupMenu;
    protected int rightClickTabIdx = -1;

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
        miCloseOthers.addActionListener(e -> closeOtherTabs(pane));
        AppMenuItem miCloseTabsToRight = new AppMenuItem("Close tabs to right", 'r');
        miCloseTabsToRight.addActionListener(e -> closeTabsToRight(pane));
        AppMenuItem miCloseTabsToLeft = new AppMenuItem("Close tabs to left", 'l');
        miCloseTabsToLeft.addActionListener(e -> closeTabsToLeft(pane));
        AppMenuItem miCloseAll = new AppMenuItem("Close all", 'a');
        miCloseAll.addActionListener(e -> closeAllTabs(pane));
        popupMenu.add(miCloseOthers);
        popupMenu.add(miCloseTabsToRight);
        popupMenu.add(miCloseTabsToLeft);
        popupMenu.add(miCloseAll);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("is right - " + SwingUtilities.isRightMouseButton(e));
                if (SwingUtilities.isRightMouseButton(e)) {
                    final int index = pane.getUI().tabForCoordinate(pane, e.getX(), e.getY());
                    System.out.println("right click - " + index);
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
     * @param pane AppTabbedPane
     */
    public void closeOtherTabs(AppTabbedPane pane) {
        closeTabsToRight(pane);
        closeTabsToLeft(pane);
    }

    /**
     * Can override for custom behavior
     *
     * @param pane AppTabbedPane
     */
    public void closeAllTabs(AppTabbedPane pane) {
        int tc = pane.getTabCount();
        for (int i = 0; i < tc; i++) {
            // TODO: how help will work or make them closable
            checkAndClose(pane, 0);
        }
    }

    /**
     * Can override for custom behavior
     *
     * @param pane AppTabbedPane
     */
    public void closeTabsToLeft(AppTabbedPane pane) {
        if (rightClickTabIdx > -1) {
            for (int i = 0; i < rightClickTabIdx; i++) {
                checkAndClose(pane, 0);
            }
        }
    }

    /**
     * Can override for custom behavior
     *
     * @param pane AppTabbedPane
     */
    public void closeTabsToRight(AppTabbedPane pane) {
        // as with each remove tab count will change so not taking in var
        if (rightClickTabIdx > -1) {
            for (int i = rightClickTabIdx + 1; i < pane.getTabCount(); i++) {
                checkAndClose(pane, pane.getTabCount() - 1);
            }
        }
    }

    protected void checkAndClose(AppTabbedPane pane, int i) {
        System.out.println("checkAndClose - " + i + " tcc " + (pane.getTabComponentAt(i) instanceof TabCloseComponent));
        if (pane.getTabComponentAt(i) instanceof TabCloseComponent) {
            TabCloseComponent tcc = (TabCloseComponent) pane.getTabComponentAt(i);
            if (tcc.isClosable()) {
                pane.remove(i);
                tabClosed(pane, i, tcc.getTabLabel().getText());
            }
        } else {
            String title = pane.getTitleAt(i);
            pane.remove(i);
            tabClosed(pane, i, title);
        }
    }

    // to override
    public void tabClosed(AppTabbedPane pane, int removedTabIdx, String removedTabTitle) {

    }

    public int getRightClickTabIdx() {
        return rightClickTabIdx;
    }

    /**
     * Can override for custom behavior
     *
     * @param pane   AppTabbedPane
     * @param tabIdx tab index
     */
    public void tabRightClicked(AppTabbedPane pane, int tabIdx) {
        rightClickTabIdx = tabIdx;
        // used to override for behavior
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
