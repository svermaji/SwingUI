package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.sv.core.Constants.ELLIPSIS;

/**
 * Wrapper class for JFrame
 * <p>
 * Two list used for enabling/disabling
 * Action of enabling/disabling to controls componentToEnable will be applied
 * and negate of that action applied to componentContrastToEnable
 */
public class AppFrame extends JFrame {

    protected String lastClipboardText = "";
    protected boolean windowActive;
    protected Component[] componentToEnable;
    protected Component[] componentContrastToEnable;
    private final String TITLE;

    protected enum WindowChecks {
        WINDOW_ACTIVE, CLIPBOARD
    }

    public AppFrame(String title) {
        TITLE = title;
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("./icons/app-icon.png").getImage());
        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(title);
    }

    public void applyWindowActiveCheck(WindowChecks[] checks) {
        boolean activeCheck = Arrays.asList(checks).contains(WindowChecks.WINDOW_ACTIVE);
        boolean clipCheck = Arrays.asList(checks).contains(WindowChecks.CLIPBOARD);

        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                if (activeCheck) {
                    windowActive = true;
                }
                if (clipCheck) {
                    startClipboardAction();
                }
            }

            public void windowLostFocus(WindowEvent e) {
                if (activeCheck) {
                    windowActive = false;
                }
            }
        });
    }

    public void startClipboardAction() {}

    public void copyClipboard(MyLogger logger) {
        final int showDataLimit = 100;
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Get data stored in the clipboard that is in the form of a string (text)
        try {
            String data = c.getData(DataFlavor.stringFlavor).toString().trim();
            if (Utils.hasValue(data) && !data.equals(lastClipboardText)) {
                int result = JOptionPane.showConfirmDialog(this,
                        "Use data " +
                                Utils.addBraces(
                                        (data.length() < showDataLimit ? data :
                                                data.substring(0, showDataLimit) + ELLIPSIS)),
                        "Copy data from clipboard ?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    Utils.callMethod(this, "copyClipboardSuccess", new String[]{data}, logger);
                }
                lastClipboardText = data;
            }
        } catch (Exception e) {
            Utils.callMethod(this, "copyClipboardFailed", null, logger);
            logger.error("Unable to complete clipboard check action.  Error: " + e.getMessage());
        }
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
