package com.sv.swingui.component;

import com.sv.core.Constants;
import com.sv.core.Utils;

public class LineGraphPanelData {
    private final int value;
    private final String nameToDisplay;
    private final boolean nameInTipOnly, appendValueInTip;
    private final int LIMIT = 15;

    public LineGraphPanelData(int value) {
        this(value, null);
    }

    public LineGraphPanelData(int value, String nameToDisplay) {
        this(value, nameToDisplay, Utils.hasValue(nameToDisplay), true);
    }

    public LineGraphPanelData(int value, String nameToDisplay, boolean nameInTipOnly, boolean appendValueInTip) {
        this.value = value;
        this.nameToDisplay = nameToDisplay;
        this.nameInTipOnly = nameInTipOnly;
        this.appendValueInTip = appendValueInTip;
    }

    public int getValue() {
        return value;
    }

    public boolean isNameInTipOnly() {
        return nameInTipOnly;
    }

    public String getNameToDisplay() {
        String dt = nameToDisplay + Constants.SPACE + Utils.addBraces(value);
        if (dt.length() > LIMIT) {
            dt = dt.substring(0, LIMIT) + Constants.ELLIPSIS;
        }
        return !isNameInTipOnly() && Utils.hasValue(nameToDisplay) ?
                dt : value + Constants.EMPTY;
    }

    public String getFullNameToDisplay() {
        String dt = nameToDisplay;
        if (appendValueInTip) {
            dt = dt + Constants.SPACE + Utils.addBraces(value);
        }
        return Utils.hasValue(nameToDisplay) ? dt : value + Constants.EMPTY;
    }
}