package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;

/**
 * Wrapper class for JLabel
 */
public class AppLabel extends JLabel {

    public AppLabel(String text, JComponent component, char mnemonic) {
        this (text, component, mnemonic, null);
    }

    public AppLabel(String text, JComponent component, char mnemonic, String tip) {
        setText(text);
        setLabelFor(component);
        setDisplayedMnemonic(mnemonic);
        if (Utils.hasValue(tip)) {
            setToolTipText(tip);
        }
    }
}
