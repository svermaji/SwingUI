package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.Utils;

public class LineGraphPanelData {
    private final int value;
    private final String nameToDisplay;
    private final boolean displayValue;

    public LineGraphPanelData(int value) {
        this(value, null, false);
    }

    public LineGraphPanelData(int value, String nameToDisplay) {
        this(value, nameToDisplay, Utils.hasValue(nameToDisplay));
    }

    public LineGraphPanelData(int value, String nameToDisplay, boolean displayValue) {
        this.value = value;
        this.nameToDisplay = nameToDisplay;
        this.displayValue = displayValue;
    }

    public int getValue() {
        return value;
    }

    public boolean isDisplayValue() {
        return displayValue;
    }

    public String getNameToDisplay() {
        return isDisplayValue() && Utils.hasValue(nameToDisplay) ?
                nameToDisplay + Utils.addBraces(value) : value + Constants.EMPTY;
    }
}