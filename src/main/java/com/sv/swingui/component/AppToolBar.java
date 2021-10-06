package com.sv.swingui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Wrapper class for JToolbar
 * <p>
 * When JMenu added to JToolbar it mis-aligns
 * so manually layout need to be applied.
 * @link https://stackoverflow.com/questions/69454055/java-swing-menu-control-not-align
 */
public class AppToolBar extends JToolBar {

    public static final int gap = 0;

    public AppToolBar() {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT, gap, gap));
        setFloatable(false);
        setRollover(false);
    }
}
