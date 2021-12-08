package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.Utils;

public class LineGraphPanelData {
    private final int value;
    private final String nameToDisplay;
    private final boolean displayValue;
    private final int LIMIT = 15;

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
        String dt = nameToDisplay + Constants.SPACE + Utils.addBraces(value);
        if (dt.length() > LIMIT) {
            dt = dt.substring(0, LIMIT) + Constants.ELLIPSIS;
        }
        return isDisplayValue() && Utils.hasValue(nameToDisplay) ?
                dt : value + Constants.EMPTY;
    }

    public String getFullNameToDisplay() {
        String dt = nameToDisplay + Constants.SPACE + Utils.addBraces(value);
        return isDisplayValue() && Utils.hasValue(nameToDisplay) ?
                dt : value + Constants.EMPTY;
    }
}