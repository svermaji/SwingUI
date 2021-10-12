package com.sv.swingui;

import javax.swing.*;

public class KeyActionDetails {

    private KeyStroke keyStroke;
    private int keyEvent, inputEvent;
    private Action action;

    public KeyActionDetails(int keyEvent, int inputEvent, Action action) {
        this(KeyStroke.getKeyStroke(keyEvent, inputEvent), action);
        this.keyEvent = keyEvent;
        this.inputEvent = inputEvent;
    }

    public KeyActionDetails(KeyStroke keyStroke, Action action) {
        this.keyStroke = keyStroke;
        this.action = action;
    }

    public int getKeyEvent() {
        return keyEvent;
    }

    public int getInputEvent() {
        return inputEvent;
    }

    public Action getAction() {
        return action;
    }

    public KeyStroke getKeyStroke() {
        return keyStroke;
    }
}
