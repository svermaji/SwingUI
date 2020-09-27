package com.sv.swingui;

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
        this(text, mnemonic, null, null);
    }

    /**
     * Creates button with given info
     *
     * @param text     string
     * @param mnemonic char
     * @param tip      tooltip
     */
    public AppButton(String text, char mnemonic, String tip) {
        this(text, mnemonic, tip, null);
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
        this(text, mnemonic, tip, iconPath, false);
    }

    /**
     * Creates button with given info
     *
     * @param text         string
     * @param mnemonic     char
     * @param tip          tooltip
     * @param iconPath     path
     * @param drawAsButton boolean if true then remaining button area will not be drawn
     */
    public AppButton(String text, char mnemonic, String tip, String iconPath, boolean drawAsButton) {
        if (Utils.hasValue(text)) {
            setText(text);
        }
        setMnemonic(mnemonic);
        if (Utils.hasValue(tip)) {
            setToolTipText(tip + " Shortcut: Alt+" + mnemonic);
        }
        if (Utils.hasValue(iconPath)) {
            setIcon(new ImageIcon(iconPath));
            if (!drawAsButton) {
                setContentAreaFilled(false);
                setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
}
