package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.util.TimerTask;

public class YesNoDialogTask extends TimerTask {

    private final AppFrame appFrame;
    private final JOptionPane optionPane;
    private final JDialog dialog;
    private int seconds;
    private final String initValue;

    public YesNoDialogTask(AppFrame appFrame, JOptionPane optionPane, JDialog dialog, int seconds, String initValue) {
        this.appFrame = appFrame;
        this.optionPane = optionPane;
        this.dialog = dialog;
        this.seconds = seconds;
        this.initValue = initValue;
    }

    @Override
    public void run() {
        do {
            appFrame.changeYesNoText(appFrame, optionPane, dialog, seconds, initValue);
            seconds--;
            Utils.sleep1Sec();
        } while (seconds >= 0);
    }
}
