package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.util.List;

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
     * @param keys     list of string where 1st element should be char and 2nd additional key
     * @param tip      tooltip
     */
    public AppButton(String text, List<String> keys, String tip) {
        this(text, keys, tip, null, false);
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
        setToolTipText(" Shortcut: Alt+" + mnemonic);
        if (Utils.hasValue(tip)) {
            setToolTipText(tip + getToolTipText());
        }
        if (Utils.hasValue(iconPath)) {
            setIcon(new ImageIcon(iconPath));
        }
        if (drawAsImage) {
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /**
     * Creates button with given info
     *
     * @param text         string
     * @param keys         list of string where 1st element should be char and 2nd additional key
     * @param tip          tooltip
     * @param iconPath     path
     * @param drawAsImage boolean if true then remaining button area will not be drawn
     */
    public AppButton(String text, List<String> keys, String tip, String iconPath, boolean drawAsImage) {
        this(text, keys.get(0).charAt(0), tip, iconPath, drawAsImage);

        if (keys.size() > 1 && Utils.hasValue(keys.get(1))) {
            setToolTipText(getToolTipText() + " or " + keys.get(1));
        }
    }
}
