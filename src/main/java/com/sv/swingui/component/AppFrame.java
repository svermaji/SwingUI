package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Wrapper class for JFrame
 *
 * Two list used for enabling/disabling
 * Action of enabling/disabling to controls componentToEnable will be applied
 * and negate of that action applied to componentContrastToEnable
 */
public class AppFrame extends JFrame {

    protected Component[] componentToEnable;
    protected Component[] componentContrastToEnable;
    private final String TITLE;

    public AppFrame(String title) {
        TITLE = title;
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("./icons/app-icon.png").getImage());
        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(title);
    }

    public void setToCenter() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setDialogFont() {
        Font baseFont = new Font("Dialog", Font.PLAIN, 12);
        setFont(baseFont);
    }

    public void setBlackAndWhite() {
        setBackground(Color.WHITE);
        setForeground(Color.black);
    }

    public void setComponentToEnable(Component[] c) {
        componentToEnable = c;
    }

    public void setComponentContrastToEnable(Component[] c) {
        componentContrastToEnable = c;
    }

    public void updateControls(boolean enable) {
        if (componentToEnable != null) {
            Arrays.stream(componentToEnable).forEach(c -> c.setEnabled(enable));
            updateContrastControls(!enable);
        }
    }

    public void updateContrastControls(boolean enable) {
        if (componentContrastToEnable != null) {
            Arrays.stream(componentContrastToEnable).forEach(c -> c.setEnabled(enable));
        }
    }

    public void disableControls() {
        updateControls(false);
    }

    public void enableControls() {
        updateControls(true);
    }

    public void updateTitle(String info) {
        setTitle((Utils.hasValue(info) ? TITLE + Constants.SP_DASH_SP + info : TITLE));
    }
}
