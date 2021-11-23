package com.sv.swingui.component.autolock;


import com.sv.swingui.component.AppFrame;

import java.util.TimerTask;

public class AutoLockTimerTask extends TimerTask {

    private final AppFrame obj;

    public AutoLockTimerTask(AppFrame obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        obj.showLockScreen();
    }
}
