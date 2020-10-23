package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;

/**
 * Wrapper class for JButton
 */
public class AppButton extends JButton {

    /**
     * Creates button with given info
     *
     * @param text     string
     * @param mnemonic char
     */
    public AppButton(String text, char mnemonic) {
        this(text, mnemonic, null, null, false);
    }

    /**
     * Creates button with given info
     *
     * @param text     string
     * @param mnemonic char
     * @param tip      tooltip
     */
    public AppButton(String text, char mnemonic, String tip) {
        this(text, mnemonic, tip, null, false);
    }

    /**
     * Creates button with given info
     *
     * @param text     string
     * @param mnemonic char
     * @param tip      tooltip
     * @param iconPath path
     */
    public AppButton(String text, char mnemonic, String tip, String iconPath) {
        this(text, mnemonic, tip, iconPath, true);
    }

    /**
     * Creates button with given info
     *
     * @param text         string
     * @param mnemonic     char
     * @param tip          tooltip
     * @param iconPath     path
     * @param drawAsImage boolean if true then remaining button area will not be drawn
     */
    public AppButton(String text, char mnemonic, String tip, String iconPath, boolean drawAsImage) {
        if (Utils.hasValue(text)) {
            setText(text);
        }
        setMnemonic(mnemonic);
        if (Utils.hasValue(tip)) {
            setToolTipText(tip + " Shortcut: Alt+" + mnemonic);
        }
        if (Utils.hasValue(iconPath)) {
            setIcon(new ImageIcon(iconPath));
        }
        if (drawAsImage) {
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
