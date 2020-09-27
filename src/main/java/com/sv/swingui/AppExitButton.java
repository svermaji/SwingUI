package com.sv.swingui;

/**
 * Created by Shailendra Verma
 */
public class AppExitButton extends AppButton {

    /**
     * Default constructor
     * exit icon path will be referred from calling application
     * At root there must be png file with name as <p>exit-icon.png</p>
     */
    public AppExitButton() {
        // exit icon need to be
        super("", 'x', "Exit application.", "./exit-icon.png");
    }

    /**
     * Boolean value will be ignored and EXIT button will be created
     */
    public AppExitButton(boolean noIcon) {
        // value of noIcon ignored and exit icon will be removed
        super("Exit", 'x', "Exit application.");
    }
}
