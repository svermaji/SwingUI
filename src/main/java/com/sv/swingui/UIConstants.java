package com.sv.swingui;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Contains constants and enums
 */
public final class UIConstants {

    private UIConstants() {
    }

    public static final String PREFERRED_FONT = "Calibri";
    public static final String DEFAULT_FONT = "Dialog.plain";

    public static final Border EMPTY_BORDER = new EmptyBorder(new Insets(5, 5, 5, 5));
    public static final Border BLUE_BORDER = new LineBorder(Color.blue, 1);
    public static final String FONT_PREFIX = "<font style=\"background-color:";
    public static final String FONT_PREFIX_END = "\">";
    public static final String Y_BG_FONT_PREFIX = FONT_PREFIX+"yellow"+FONT_PREFIX_END;
    public static final String R_FONT_PREFIX = FONT_PREFIX+"red"+FONT_PREFIX_END;
    public static final String FONT_SUFFIX = "</font>";
    public static final String HTML_STR = "<html>";
    public static final String HTML_END = "</html>";
    public static final String SPAN_STR = "<span>";
    public static final String SPAN_END = "</span>";
    public static final String BODY_STR = "<body>";
    public static final String BODY_END = "</body>";
    public static final String BR = "<br>";

    public enum ColorsNFonts {
        CYAN_BLACK(Color.cyan, Color.black, "Calibri"),
        BLACK_GREEN(Color.black, Color.green, "Algerian"),
        GRAY_WHITE(Color.gray, Color.white, "Elephant"),
        GREEN_WHITE(new Color(57, 172, 170), Color.white, "Lucida Bright"),
        WHITE_BLUE(Color.white, Color.blue, "Lucida Calligraphy Italic"),
        BLACK_RED(Color.black, Color.red, "Segoe UI"),
        MAGENTA_YELLOW(Color.magenta, Color.yellow, "Tahoma"),
        BLUE_WHITE(new Color(32, 145, 255), Color.white, "Times New Roman"),
        BLACK_PURPLE(Color.black, new Color(143, 85, 173), "Vardana"),
        TEALGREEN_WHITE(new Color(0, 128, 128), Color.white, "Arial Black"),
        ORANGE_WHITE(Color.orange, Color.white, "Comic Sans MS"),
        DEFAULT(Color.lightGray, Color.black, "Consolas");

        private Color bk, fg;
        private String font;

        ColorsNFonts(Color bk, Color fg, String font) {
            this.bk = bk;
            this.fg = fg;
            this.font = font;
        }

        public Color getBk() {
            return bk;
        }

        public Color getFg() {
            return fg;
        }

        public String getFont() {
            return font;
        }
    }
}
