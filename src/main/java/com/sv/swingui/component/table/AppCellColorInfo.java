package com.sv.swingui.component.table;

import com.sv.core.Utils;

import java.awt.*;
import java.util.Arrays;

/**
 * Wrapper class for JTable
 * <p>
 * String array will be passed to make column names
 * By default non-editable
 */
public class AppCellColorInfo {

    private final Color textColor;
    private final String[] textList;

    public AppCellColorInfo(Color textColor, String[] textToMatch) {
        this.textColor = textColor;
        this.textList = textToMatch;
    }

    public Color getTextColor() {
        return textColor;
    }

    public String[] getTextList() {
        return textList;
    }

    public boolean isMatch(String text) {
        return Utils.isInArray(textList, text);
    }

    @Override
    public String toString() {
        return "AppCellColorInfo{" +
                "textColor=" + textColor +
                ", textList=" + Arrays.deepToString(textList) +
                '}';
    }
}
