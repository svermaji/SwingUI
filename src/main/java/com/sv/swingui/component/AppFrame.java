package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.EncryptUtils;
import com.sv.core.Utils;
import com.sv.core.logger.MyLogger;
import com.sv.swingui.SwingUtils;
import com.sv.swingui.UIConstants;

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

import static com.sv.core.Constants.ELLIPSIS;
import static com.sv.core.Constants.EMPTY;

/**
 * Wrapper class for JFrame
 * <p>
 * Two list used for enabling/disabling
 * Action of enabling/disabling to controls componentToEnable will be applied
 * and negate of that action applied to componentContrastToEnable
 */
public class AppFrame extends JFrame {

    protected String lastClipboardText = "";
    protected final String PWD_FILE = "/secret.lock";
    protected final String PWD_SEP = "!!";
    protected boolean windowActive;
    protected Component[] componentToEnable;
    protected Component[] componentContrastToEnable;
    private final String TITLE;
    protected JFrame lockScreen;
    protected MyLogger logger;
    protected JDialog changePwdScreen;
    protected JPasswordField lockScreenPwd, oldPwd, newPwd, confirmPwd;
    protected AppLabel wrongPwdMsg, lblOldPwd, lblNewPwd, lblConfirmPwd;
    protected JLabel changePwdErrMsg;
    protected JPanel changePwdPanel, pwdPanel;

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

        initPwdControls();
    }

    private void initPwdControls() {
        int pwdLen = 20;
        char echoChar = '$';
        lockScreen = new JFrame(this.getTitle() + ": Enter Password");
        lockScreen.setIconImage(this.getIconImage());
        lockScreenPwd = new JPasswordField(pwdLen);
        lockScreenPwd.setEchoChar(echoChar);
        lockScreenPwd.setToolTipText("Enter password and hit enter");
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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(wrongPwdMsg, gbc);
        panel.add(lockScreenPwd, gbc);
        panel.setSize(this.getSize());
        panel.setBorder(UIConstants.EMPTY_BORDER);
        container.add(panel);
        lockScreen.pack();
        lockScreen.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        lockScreen.setPreferredSize(this.getSize());
        lockScreen.setSize(this.getSize());
        lockScreen.setLocation(this.getLocation());
        lockScreen.setVisible(false);

        changePwdScreen = new JDialog(this, this.getTitle() + ": Change Password (Esc to hide)", true);
        changePwdScreen.setIconImage(this.getIconImage());
        oldPwd = new JPasswordField(pwdLen);
        oldPwd.setEchoChar(echoChar);
        oldPwd.setToolTipText("Enter old password");
        newPwd = new JPasswordField(pwdLen);
        newPwd.setEchoChar(echoChar);
        newPwd.setToolTipText("Enter new password");
        confirmPwd = new JPasswordField(pwdLen);
        confirmPwd.setEchoChar(echoChar);
        confirmPwd.setToolTipText("Confirm new password");
        confirmPwd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    changePassword();
                }
            }
        });
        changePwdErrMsg = new JLabel("Invalid old password. Password must be 4+ char long.");
        changePwdErrMsg.setForeground(Color.red);
        lblOldPwd = new AppLabel("Old password", oldPwd, 'o', "Leave blank for first time");
        lblNewPwd = new AppLabel("New password", newPwd, 'n');
        lblConfirmPwd = new AppLabel("Confirm password", confirmPwd, 'c', "Hit enter after changing pwd");
        changePwdPanel = new JPanel(new BorderLayout());
        pwdPanel = new JPanel(new GridLayout(3, 2));
        changePwdPanel.add(changePwdErrMsg, BorderLayout.NORTH);
        pwdPanel.add(lblOldPwd);
        pwdPanel.add(oldPwd);
        pwdPanel.add(lblNewPwd);
        pwdPanel.add(newPwd);
        pwdPanel.add(lblConfirmPwd);
        pwdPanel.add(confirmPwd);
        changePwdPanel.add(pwdPanel, BorderLayout.CENTER);
        changePwdScreen.add(changePwdPanel);
        changePwdScreen.pack();
        changePwdScreen.setLocationRelativeTo(this);
        changePwdScreen.setVisible(false);
        changePwdErrMsg.setVisible(false);
        SwingUtils.addEscKeyAction(changePwdScreen);
    }

    protected void setLogger(MyLogger logger) {
        this.logger = logger;
    }

    private void changePassword() {
        boolean pwdChanged = false;
        boolean result = !checkIfSecretFileExists() || authenticate(oldPwd.getPassword());

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
        pwdChangedStatus (pwdChanged);
    }

    public void pwdChangedStatus(boolean pwdChanged) {
        // to override
    }

    private boolean authenticate(char[] secret) {
        String[] arr = readPassword();
        boolean result = false;
        if (arr != null) {
            try {
                return EncryptUtils.getEncryptedPassword(new String(secret), arr[0]).equals(arr[1]);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.error("Error in authenticating password. Details: ", e);
                result = false;
            }
        }
        return result;
    }

    private String[] readPassword() {
        java.util.List<String> lines = Utils.readFile(Utils.getCurrentDir() + PWD_FILE, logger);
        String firstLine = "";
        String[] arr = null;
        if (lines.size() > 0) {
            // expected password here only
            firstLine = lines.get(0);
            if (firstLine.contains(PWD_SEP)) {
                arr = firstLine.split(PWD_SEP);
                if (arr.length == 2) {
                    logger.info("Secret read successfully.");
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
            return Utils.writeFile(Utils.getCurrentDir() + PWD_FILE, toStore, logger);
        }
        return false;
    }

    public void showChangePwdScreen() {
        showChangePwdScreen(UIConstants.COLOR_GREEN_DARK);
    }

    public void showChangePwdScreen(Color c) {
        changePwdErrMsg.setVisible(false);
        JComponent[] cs = {lblOldPwd, lblNewPwd, lblConfirmPwd, changePwdPanel, pwdPanel};
        Arrays.stream(cs).forEach(ca -> ca.setBackground(c));
        cs = new JComponent[]{oldPwd, newPwd, confirmPwd};
        Arrays.stream(cs).forEach(ca -> ca.setForeground(c));

        oldPwd.setText("");
        newPwd.setText("");
        confirmPwd.setText("");
        changePwdScreen.pack();
        changePwdScreen.setLocationRelativeTo(this);
        changePwdScreen.setVisible(true);
    }

    private void checkPassword() {
        if (authenticate(lockScreenPwd.getPassword())) {
            hideLockScreen();
        } else {
            wrongPwdMsg.setText("Wrong password ");
            wrongPwdMsg.setVisible(true);
        }
    }

    private void setLockScreenColor(Color c) {
        lockScreen.getContentPane().setBackground(c);
        lockScreenPwd.setForeground(c);
        wrongPwdMsg.setBackground(c);
    }

    public void showLockScreen() {
        showLockScreen(UIConstants.COLOR_GREEN_DARK);
    }

    private boolean checkIfSecretFileExists() {
        boolean result = Files.exists(Utils.createPath(Utils.getCurrentDir() + PWD_FILE));
        if (logger != null) {
            logger.info("Secret file exists " + Utils.addBraces(result));
        }
        return result;
    }

    public void showLockScreen(Color c) {
        if (checkIfSecretFileExists()) {
            setLockScreenColor(c);
            lockScreenPwd.setText("");
            this.setVisible(false);
            lockScreen.setVisible(true);
            lockScreen.pack();
            lockScreen.setPreferredSize(this.getSize());
            lockScreen.setSize(this.getSize());
            lockScreen.setLocation(this.getLocation());
        } else {
            showChangePwdScreen();
        }
    }

    public void hideLockScreen() {
        this.setVisible(true);
        lockScreen.setVisible(false);
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

    public void startClipboardAction() {
        // override as per need
    }

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

    public void addLockScreen() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
