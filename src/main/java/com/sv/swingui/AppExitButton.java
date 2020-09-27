package com.sv.swingui;

/**
 * Created by Shailendra Verma
 */
public class AppExitButton extends AppButton {

    /**
     * Default constructor
     * exit icon path will be referred from calling application
     * Default exit icon path is <p>./icons/exit-icon.png</p>
     */
    public AppExitButton() {
        // exit icon need to be
        super("", 'x', "Exit application.", "./icons/exit-icon.png");
    }

    /**
     * Constructor with icon path as params
     * @param path icon path
     */
    public AppExitButton(String path) {
        super("", 'x', "Exit application.", path);
    }

    /**
     * Boolean value will be ignored and EXIT button will be created
     * @param noIcon boolean
     */
    public AppExitButton(boolean noIcon) {
        // value of noIcon ignored and exit icon will be removed
        super("Exit", 'x', "Exit application.");
    }
}
