package com.sv.swingui.helper;

import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;

import javax.swing.*;
import java.awt.*;

public class ApplyTheme extends SwingWorker<Integer, String> {

    private final Component obj;
    private final MyLogger logger;
    private final int idx;
    private final UIManager.LookAndFeelInfo lnf;

    public ApplyTheme(int idx, UIManager.LookAndFeelInfo lnf, Component obj, MyLogger logger) {
        this.obj = obj;
        this.logger = logger;
        this.lnf = lnf;
        this.idx = idx;
    }

    @Override
    protected Integer doInBackground() {
        logger.debug("Applying theme now...");
        try {
            UIManager.setLookAndFeel(lnf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            logger.warn("Unable to apply look and feel " + Utils.addBraces(lnf.getClassName()));
        }
        SwingUtilities.updateComponentTreeUI(obj);
        Utils.callMethod(obj, "themeApplied", new Object[]{idx, lnf}, logger);
        return 1;
    }
}
