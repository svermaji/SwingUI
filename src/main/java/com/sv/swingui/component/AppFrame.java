package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.EncryptUtils;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;
import com.sv.swingui.SwingUtils;
import com.sv.swingui.UIConstants;
import com.sv.swingui.component.autolock.AutoLockTimerTask;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.sv.core.Constants.ELLIPSIS;
import static com.sv.core.Constants.MIN_10;

/**
 * Wrapper class for JFrame
 * <p>
 * Two list used for enabling/disabling
 * Action of enabling/disabling to controls componentToEnable will be applied
 * and negate of that action applied to componentContrastToEnable
 */
public class AppFrame extends JFrame {

    protected final String PWD_FILE = "secret.lock";
    protected final String PWD_SEP = "!!";
    protected final String TITLE;
    protected final String ADMIN_UN = "Admin";
    protected final String PRM_UN = "username";
    protected final Color DEFAULT_LOCKSCR_COLOR = UIConstants.COLOR_GREEN_DARK;

    protected Component[] componentToEnable;
    protected Component[] componentContrastToEnable;
    protected AppFrame lockScreen;
    protected MyLogger logger;
    protected JDialog changePwdScreen;
    protected AppPasswordField lockScreenPwd, oldPwd, newPwd, confirmPwd;
    protected AppLabel wrongPwdMsg, lblUsername, lblUsernameVal, lblOldPwd, lblNewPwd, lblConfirmPwd;
    protected AppLabel changePwdErrMsg;
    protected AppPanel changePwdPanel, pwdPanel, lockScreenPanel;
    protected java.util.Timer autoLockTimer;
    // color and hover colors
    protected Color fg = DEFAULT_LOCKSCR_COLOR, bg = DEFAULT_LOCKSCR_COLOR, hfg, hbg;
    protected Font tooltipFont;

    protected int appFontSize;
    protected char echoChar = '$';
    protected boolean windowActive;
    protected String lastClipboardText = "", usernameForPwd;
    protected Map<String, String> authenticationParams, changePwdParams;

    protected enum WindowChecks {
        WINDOW_ACTIVE, CLIPBOARD, AUTO_LOCK
    }

    public AppFrame(String title) {
        TITLE = title;
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("./icons/app-icon.png").getImage());
        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(title);
    }

    private void initPwdControls() {
        int pwdLen = 20;
        lockScreen = new AppFrame("");
        lockScreen.setIconImage(this.getIconImage());
        lockScreenPwd = new AppPasswordField(pwdLen, echoChar, "Enter password and hit enter");
        lockScreenPwd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (wrongPwdMsg.isVisible()) {
                    wrongPwdMsg.setVisible(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkPassword();
                }
            }
        });
        wrongPwdMsg = new AppLabel("", lockScreenPwd, 'P');
        wrongPwdMsg.setForeground(Color.red);
        wrongPwdMsg.setOpaque(true);

        Container container = lockScreen.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        lockScreenPanel = new AppPanel(new GridBagLayout());
        lockScreenPanel.add(wrongPwdMsg, gbc);
        lockScreenPanel.add(lockScreenPwd, gbc);
        lockScreenPanel.setSize(this.getSize());
        lockScreenPanel.setBorder(UIConstants.EMPTY_BORDER);
        container.add(lockScreenPanel);
        lockScreen.pack();
        lockScreen.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        lockScreen.setPreferredSize(this.getSize());
        lockScreen.setSize(this.getSize());
        lockScreen.setLocation(this.getLocation());
        lockScreen.setVisible(false);

        changePwdScreen = new JDialog(this, this.getTitle() + ": Change Password (Esc to hide)", true);
        changePwdScreen.setIconImage(this.getIconImage());
        oldPwd = new AppPasswordField(pwdLen, echoChar, "Enter old password");
        newPwd = new AppPasswordField(pwdLen, echoChar, "Enter new password");
        confirmPwd = new AppPasswordField(pwdLen, echoChar, "Confirm new password");
        confirmPwd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    changePassword();
                }
            }
        });
        changePwdErrMsg = new AppLabel("Invalid old password. Password must be 4+ char long.");
        changePwdErrMsg.setForeground(Color.red);
        lblUsername = new AppLabel("Username");
        lblUsernameVal = new AppLabel();
        lblOldPwd = new AppLabel("Old password", oldPwd, 'o', "Leave blank for first time");
        lblNewPwd = new AppLabel("New password", newPwd, 'n');
        lblConfirmPwd = new AppLabel("Confirm password", confirmPwd, 'c', "Hit enter after changing pwd");
        changePwdPanel = new AppPanel(new BorderLayout());
        JComponent[] pwdPanelChild = {
                lblUsername, lblUsernameVal, lblOldPwd, oldPwd,
                lblNewPwd, newPwd, lblConfirmPwd, confirmPwd,
        };
        int c = 2;
        int r = pwdPanelChild.length / c;
        pwdPanel = new AppPanel(new GridLayout(r, c));
        changePwdPanel.add(changePwdErrMsg, BorderLayout.NORTH);
        Arrays.stream(pwdPanelChild).forEach(cs -> pwdPanel.add(cs));

        changePwdPanel.add(pwdPanel, BorderLayout.CENTER);
        changePwdScreen.add(changePwdPanel);
        changePwdScreen.pack();
        changePwdScreen.setLocationRelativeTo(this);
        changePwdScreen.setVisible(false);
        changePwdErrMsg.setVisible(false);
        SwingUtils.addEscKeyAction(changePwdScreen, "escOnchangePwdScreen", this, logger);
    }

    protected void setLogger(MyLogger logger) {
        this.logger = logger;
    }

    public void setEchoChar(char echoChar) {
        this.echoChar = echoChar;
    }

    public void setAppColors(Color fg, Color bg, Color hfg, Color hbg) {
        this.fg = fg;
        this.bg = bg;
        this.hfg = hfg;
        this.hbg = hbg;
    }

    // will be passed along with other params on authentication event
    public void setAuthenticationParams(Map<String, String> authenticationParams) {
        this.authenticationParams = authenticationParams;
    }

    // will be passed along with other params on change pwd event
    public void setChangePwdParams(Map<String, String> changePwdParams) {
        this.changePwdParams = changePwdParams;
    }

    public void setAppFontSize(int appFontSize) {
        this.appFontSize = appFontSize;
    }

    public void setTooltipFont(Font tooltipFont) {
        this.tooltipFont = tooltipFont;
    }

    private void changePassword() {
        boolean pwdChanged = false;
        boolean result = !isSecretFileExists() || authenticate(oldPwd.getPassword());

        if (result) {
            result = newPwd.getPassword().length >= 4 && Arrays.equals(newPwd.getPassword(), confirmPwd.getPassword());
        }
        changePwdErrMsg.setVisible(!result);
        changePwdScreen.pack();
        changePwdScreen.setLocationRelativeTo(this);
        changePwdScreen.setVisible(true);

        if (result && savePassword(newPwd.getPassword())) {
            changePwdScreen.setVisible(false);
            pwdChanged = true;
        }
        if (changePwdParams == null) {
            changePwdParams = new ConcurrentHashMap<>();
        }
        changePwdParams.put(PRM_UN, usernameForPwd);
        if (changePwdParams.isEmpty()) {
            pwdChangedStatus(pwdChanged);
        } else {
            pwdChangedStatus(pwdChanged, changePwdParams);
        }
    }

    public void pwdChangedStatus(boolean pwdChanged, Map<String, String> params) {
        // to override
    }

    // to support old implementations
    public void pwdChangedStatus(boolean pwdChanged) {
        // to override
    }

    public void escOnchangePwdScreen() {
        // to override
    }

    protected void stopAutoLockTimer() {
        logger.info("Stopping auto lock timer if running");
        if (autoLockTimer != null) {
            autoLockTimer.cancel();
        }
    }

    protected void startAutoLockTimer() {
        logger.info("Starting auto lock timer...");
        if (autoLockTimer != null) {
            autoLockTimer.cancel();
        }
        autoLockTimer = new java.util.Timer("AutoLockTimer");
        autoLockTimer.schedule(new AutoLockTimerTask(this), MIN_10);
    }

    private boolean authenticate(char[] secret) {
        String[] arr = readPassword();
        boolean result = false;
        if (arr != null) {
            try {
                return EncryptUtils.getEncryptedPassword(new String(secret), arr[0]).equals(arr[1]);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.error("Error in authenticating. Details: ", e);
                result = false;
            }
        }
        return result;
    }

    private String[] readPassword() {
        java.util.List<String> lines = Utils.readFile(getSecretFileName(), logger);
        String firstLine = "";
        String[] arr = null;
        if (lines.size() > 0) {
            // expected password here only
            firstLine = lines.get(0);
            if (firstLine.contains(PWD_SEP)) {
                arr = firstLine.split(PWD_SEP);
                if (arr.length == 2) {
                    logger.info("Password read successfully.");
                } else {
                    logger.error("Cannot get password.  Array length is " + Utils.addBraces(arr.length));
                }
            }
        }
        return arr;
    }

    private boolean savePassword(char[] secret) {
        String toStore = "";
        try {
            String salt = EncryptUtils.getNewSalt();
            String pwd = EncryptUtils.getEncryptedPassword(new String(secret), salt);
            toStore = salt + PWD_SEP + pwd;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            if (logger != null) {
                logger.error("Error in encrypting password. Details: ", e);
            } else {
                e.printStackTrace();
            }
        }
        if (Utils.hasValue(toStore)) {
            return Utils.writeFile(getSecretFileName(), toStore, logger);
        }
        return false;
    }

    public void applyPwdScreensFont() {
        SwingUtils.changeFont(changePwdScreen, appFontSize);
        SwingUtils.changeFont(lockScreen, appFontSize);
        SwingUtils.applyTooltipColorNFontAllChild(changePwdScreen, hfg, hbg, tooltipFont);
        SwingUtils.applyTooltipColorNFontAllChild(lockScreen, hfg, hbg, tooltipFont);
    }

    private void checkPwdScreens() {
        if (changePwdScreen == null || lockScreen == null) {
            initPwdControls();
        }
    }

    public void showChangePwdScreen() {
        showChangePwdScreen(true);
    }

    public void showChangePwdScreen(String un, boolean showOldPwd) {
        usernameForPwd = un;
        showChangePwdScreen(showOldPwd);
    }

    public void showChangePwdScreen(boolean showOldPwd) {
        checkPwdScreens();
        changePwdErrMsg.setVisible(false);
        Arrays.stream(changePwdPanel.getComponents()).forEach(ca -> ca.setBackground(bg));
        JComponent[] cs = new JComponent[]{oldPwd, newPwd, confirmPwd};
        Arrays.stream(cs).forEach(ca -> ca.setForeground(fg));

        applyPwdScreensFont();

        lblUsernameVal.setText(getUsernameForPwd());
        oldPwd.setText("");
        newPwd.setText("");
        confirmPwd.setText("");
        lblOldPwd.setVisible(showOldPwd);
        oldPwd.setVisible(showOldPwd);
        SwingUtils.getInFocus(showOldPwd ? oldPwd : newPwd);
        changePwdScreen.pack();
        changePwdScreen.setLocationRelativeTo(this);
        changePwdScreen.setVisible(true);
    }

    private String getUsernameForPwd() {
        if (!Utils.hasValue(usernameForPwd)) {
            usernameForPwd = ADMIN_UN;
        }

        return usernameForPwd;
    }

    private void checkPassword() {
        if (authenticationParams == null) {
            authenticationParams = new ConcurrentHashMap<>();
        }
        authenticationParams.put(PRM_UN, usernameForPwd);
        if (authenticate(lockScreenPwd.getPassword())) {
            hideLockScreen();
            authenticationSuccess(authenticationParams);
        } else {
            wrongPwdMsg.setText("Wrong password ");
            wrongPwdMsg.setVisible(true);
            authenticationFailed(authenticationParams);
        }
    }

    // to override
    public void authenticationSuccess(Map<String, String> params) {

    }

    // to override
    public void authenticationFailed(Map<String, String> params) {

    }

    private void applyColorOnLockScreen() {
        lockScreen.getContentPane().setBackground(bg);
        lockScreenPanel.setBackground(bg);
        lockScreenPwd.setForeground(fg);
        wrongPwdMsg.setBackground(bg);
    }

    protected boolean isSecretFileExists() {
        boolean result = Files.exists(Utils.createPath(getSecretFileName()));
        if (logger != null) {
            logger.info("Password exists " + Utils.addBraces(result));
        }
        return result;
    }

    protected String getSecretFileName() {
        String fn = PWD_FILE;
        if (!Utils.hasValue(usernameForPwd)) {
            usernameForPwd = ADMIN_UN;
        }
        return getSecretFileNameFor(usernameForPwd);
    }

    protected String getSecretFileNameFor(String un) {
        String fn = PWD_FILE;
        if (!isAdminUser(un)) {
            fn = un + Constants.DASH + PWD_FILE;
        }
        return Utils.getCurrentDir() + Constants.F_SLASH + fn;
    }

    protected boolean isAdminUser() {
        return isAdminUser(usernameForPwd);
    }

    protected boolean deleteUserSecretFile(String un) {
        if (isAdminUser(un)) {
            logger.warn("Admin secret file cannot be deleted.");
            return false;
        }
        if (logger != null) {
            logger.warn("Deleting secret file " + getSecretFileNameFor(un));
        }
        return Utils.deleteFile(getSecretFileNameFor(un));
    }

    protected boolean isAdminUser(String un) {
        return un.equals(ADMIN_UN);
    }

    public void showLockScreen() {
        checkPwdScreens();
        lockScreen.setTitle(this.getTitle() + ": Enter Password for " + Utils.addBraces(getUsernameForPwd()));
        if (isSecretFileExists()) {
            applyColorOnLockScreen();
            applyPwdScreensFont();
            lockScreenPwd.setText("");
            this.setVisible(false);
            lockScreen.setVisible(true);
            lockScreen.pack();
            lockScreen.setPreferredSize(this.getSize());
            lockScreen.setSize(this.getSize());
            lockScreen.setLocation(this.getLocation());
        } else {
            showChangePwdScreen(false);
        }
    }

    public void hideLockScreen() {
        this.setVisible(true);
        lockScreen.setVisible(false);
    }

    public void applyWindowActiveCheck(WindowChecks[] checks) {
        java.util.List<WindowChecks> list = Arrays.asList(checks);
        boolean activeCheck = list.contains(WindowChecks.WINDOW_ACTIVE);
        boolean clipCheck = list.contains(WindowChecks.CLIPBOARD);
        boolean autoLock = list.contains(WindowChecks.AUTO_LOCK);

        logger.info("Applying window checks: active check " + Utils.addBraces(activeCheck) +
                ", clipboard check " + Utils.addBraces(clipCheck) +
                ", auto lock check " + Utils.addBraces(autoLock)
        );

        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                if (activeCheck) {
                    windowActive = true;
                }
                if (clipCheck) {
                    startClipboardAction();
                }
                if (autoLock) {
                    stopAutoLockTimer();
                }
                gainedFocusEvent();
            }

            public void windowLostFocus(WindowEvent e) {
                if (activeCheck) {
                    windowActive = false;
                }
                if (autoLock) {
                    startAutoLockTimer();
                }
                lostFocusEvent();
            }
        });
    }

    public boolean isWindowActive() {
        return windowActive;
    }

    public void startClipboardAction() {
        // override as per need
    }

    private void gainedFocusEvent() {
        Utils.callMethod(this, "appWindowGainedFocus", null, logger);
    }

    private void lostFocusEvent() {
        Utils.callMethod(this, "appWindowLostFocus", null, logger);
    }

    public void appWindowGainedFocus() {
        // override as per need
    }

    public void appWindowLostFocus() {
        // override as per need
    }

    public void copyClipboardYes(String data) {
        // override as per need
    }

    public void copyClipboardNo(String data) {
        // override as per need
    }

    public void copyClipboardFailed() {
        // override as per need
    }

    public void copyClipboard(MyLogger logger) {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Get data stored in the clipboard that is in the form of a string (text)
        try {
            String data = c.getData(DataFlavor.stringFlavor).toString().trim();
            if (Utils.hasValue(data) && !data.equals(lastClipboardText)) {
                createYesNoDialog("Use copied data ?", data, "copyClipboard");
                lastClipboardText = data;
            }
        } catch (Exception e) {
            Utils.callMethod(this, "copyClipboardFailed", null, logger);
            logger.error("Unable to complete clipboard check action.  Error: " + e.getMessage());
        }
    }

    public void createYesNoDialog(String title, String msg, String methodName) {
        final int showDataLimit = 100;
        AppLabel clipboardInfo = new AppLabel(msg.length() < showDataLimit ? msg :
                msg.substring(0, showDataLimit) + ELLIPSIS);
        if (tooltipFont != null && appFontSize != 0) {
            clipboardInfo.setFont(SwingUtils.getNewFontSize(tooltipFont, appFontSize));
        }
        int result = JOptionPane.showConfirmDialog(this,
                clipboardInfo,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            Utils.callMethod(this, methodName + "Yes", new String[]{msg}, logger);
        } else if (result == JOptionPane.NO_OPTION) {
            Utils.callMethod(this, methodName + "No", new String[]{msg}, logger);
        }
    }

    public void createOkDialog(String title, String msg) {
        createOkDialog(title, msg, false);
    }

    public void createOkDialog(String title, String msg, boolean isError) {
        final int showDataLimit = 100;
        AppLabel msgToShow = new AppLabel(msg.length() < showDataLimit ? msg :
                msg.substring(0, showDataLimit) + ELLIPSIS);
        if (tooltipFont != null && appFontSize != 0) {
            msgToShow.setFont(SwingUtils.getNewFontSize(tooltipFont, appFontSize));
        }
        JOptionPane.showMessageDialog(this,
                msgToShow,
                title,
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    public void setToCenter() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Dimension getCenterOfScreen() {
        Dimension dm = getSize();
        int xMid = (int) (dm.getWidth() / 2);
        int yMid = (int) (dm.getHeight() / 2);
        return new Dimension(xMid, yMid);
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
