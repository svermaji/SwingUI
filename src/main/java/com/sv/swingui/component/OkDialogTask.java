package com.sv.swingui.component;

import com.sv.core.Utils;

import javax.swing.*;
import java.util.TimerTask;

public class OkDialogTask extends TimerTask {

    private final AppFrame appFrame;
    private final JOptionPane optionPane;
    private final JDialog dialog;
    private int seconds;

    public OkDialogTask(AppFrame appFrame, JOptionPane optionPane, JDialog dialog, int seconds) {
        this.appFrame = appFrame;
        this.optionPane = optionPane;
        this.dialog = dialog;
        this.seconds = seconds;
    }

    @Override
    public void run() {
        do {
            appFrame.changeOkText(appFrame, optionPane, dialog, seconds);
            seconds--;
            Utils.sleep1Sec();
        } while (seconds >= 0);
    }
}
