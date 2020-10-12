package com.sv.swingui;

import com.sv.core.Utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Wrapper class for JTextField
 * This supports auto-complete feature as well
 */
public class AppTextField extends JTextField {

    private String[] autoCompleteArr;

    /**
     * Creates text field with given info
     *
     * @param text string
     */
    public AppTextField(String text) {
        this(text, -1);
    }

    /**
     * Creates text field with given info
     *
     * @param text     String
     * @param colWidth columns
     */
    public AppTextField(String text, int colWidth) {
        this(text, colWidth, null);
    }

    /**
     * Creates text field with given info
     *
     * @param text     String
     * @param colWidth columns
     * @param arr      String array that has auto complete values
     */
    public AppTextField(String text, int colWidth, String[] arr) {
        super(text);
        if (colWidth > 0) {
            setColumns(colWidth);
        }
        if (arr != null) {
            autoCompleteArr = arr;
            setupAutoComplete();
        }
    }

    public void setAutoCompleteArr(String[] autoCompleteArr) {
        this.autoCompleteArr = autoCompleteArr;
    }

    private void setupAutoComplete() {
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (Character.isLetterOrDigit(e.getKeyChar()) || Utils.isSpecialChar(e.getKeyChar())) {
                    replaceSelection("");
                    String txt = getText();
                    int len = txt.length();
                    String match = Utils.getMatchedLCSubStr(txt, autoCompleteArr);
                    if (Utils.hasValue(match)) {
                        setText(txt + match);
                        setCaretPosition(len);
                        select(len, len + match.length());
                    }
                }
            }
        });
    }
}
