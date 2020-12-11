package com.sv.swingui.helper;

import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;
import com.sv.swingui.SwingUtils;

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
        String pt = UIManager.getLookAndFeel().getName();
        logger.log("Present theme " + Utils.addBraces(pt)
                + ", new theme to apply is " + Utils.addBraces(lnf.getName()));
        boolean applied = true;
        if (!pt.equals(lnf.getName())) {
            try {
                UIManager.setLookAndFeel(lnf.getClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                applied = false;
                logger.warn("Unable to apply look and feel " + Utils.addBraces(lnf.getClassName()));
            }
            SwingUtils.updateForTheme(obj);
        } else {
            applied = false;
            logger.warn(pt + " theme already applied.");
        }
        Utils.callMethod(obj, "themeApplied", new Object[]{idx, lnf, applied}, logger);
        return 1;
    }
}
