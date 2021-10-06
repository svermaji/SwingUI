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
    public static final String FONT_PREFIX = "<font style=\"background-color:";
    public static final String FONT_FG_PREFIX = ";color:";
    public static final String FONT_PREFIX_END = "\">";
    public static final String Y_BG_FONT_PREFIX = FONT_PREFIX + "yellow" + FONT_PREFIX_END;
    public static final String R_FONT_PREFIX = FONT_PREFIX + "red" + FONT_PREFIX_END;
    public static final String FONT_SUFFIX = "</font>";
    public static final String HTML_STR = "<html>";
    public static final String HTML_END = "</html>";
    public static final String CENTER_STR = "<center>";
    public static final String CENTER_END = "</center>";
    public static final String SPAN_STR = "<span>";
    public static final String SPAN_END = "</span>";
    public static final String BODY_STR = "<body>";
    public static final String BODY_END = "</body>";
    public static final String BR = "<br>";
    public static final String SHORTCUT = " Shortcut: Alt+";
    public static final Color ORIG_COLOR = new Color(238, 238, 238);
    public static final Color COLOR_GREEN_DARK = new Color(57, 172, 170);
    public static final Color COLOR_BROWN = new Color(165, 42, 42);

    public enum ColorsNFonts {
        // BG, FG, selection BG, selection FG
        A(new Color(173, 209, 255), COLOR_BROWN, Color.BLUE, Color.yellow, "Calibri"),
        B(Color.black, Color.green, COLOR_GREEN_DARK, Color.white, "Algerian"),
        C(Color.pink, COLOR_BROWN, Color.red, Color.white, "Elephant"),
        D(Color.orange, Color.blue, Color.blue, Color.orange, "Lucida Calligraphy Italic"),
        E(new Color(150, 212, 210), COLOR_BROWN, COLOR_GREEN_DARK, Color.yellow, "Lucida Bright"),
        F(Color.yellow, Color.black, new Color(51, 143, 255), Color.white, "Segoe UI"),
        G(Color.white, Color.magenta, Color.orange, Color.white, "Tahoma"),
        H(new Color(166, 241, 195), Color.black, Color.MAGENTA, Color.white, "Verdana"),
        I(new Color(242, 195, 245), Color.darkGray, new Color(44, 121, 217), Color.white, "Times New Roman"),
        J(new Color(178, 241, 178), Color.darkGray, new Color(0, 128, 128), Color.white, "Arial Black"),
        K(new Color(239, 183, 239), COLOR_BROWN, new Color(234, 10, 234), Color.white, "Comic Sans MS"),
        L(Color.cyan, Color.black, Color.darkGray, Color.white, "Consolas");

        private Color bk, fg, selbk, selfg;
        private String font;

        ColorsNFonts(Color bk, Color fg, Color selbk, Color selfg, String font) {
            this.bk = bk;
            this.fg = fg;
            this.selbk = selbk;
            this.selfg = selfg;
            this.font = font;
        }

        public Color getBk() {
            return bk;
        }

        public Color getFg() {
            return fg;
        }

        public Color getSelbk() {
            return selbk;
        }

        public Color getSelfg() {
            return selfg;
        }

        public String getFont() {
            return font;
        }
    }
}
