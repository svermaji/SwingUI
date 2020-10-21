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
    public static final Border BLUE_BORDER = new LineBorder(Color.BLUE, 1);

    public enum ColorsNFonts {
        CYAN_BLACK(Color.CYAN, Color.BLACK, "Calibri"),
        BLACK_GREEN(Color.BLACK, Color.GREEN, "Algerian"),
        GRAY_WHITE(Color.GRAY, Color.WHITE, "Elephant"),
        GREEN_WHITE(new Color(57, 172, 170), Color.WHITE, "Lucida Bright"),
        WHITE_BLUE(Color.WHITE, Color.BLUE, "Lucida Calligraphy Italic"),
        BLACK_RED(Color.BLACK, Color.RED, "Segoe UI"),
        MAGENTA_YELLOW(Color.MAGENTA, Color.YELLOW, "Tahoma"),
        BLUE_WHITE(new Color(32, 145, 255), Color.WHITE, "Times New Roman"),
        BLACK_PURPLE(Color.BLACK, new Color(143, 85, 173), "Vardana"),
        TEALGREEN_WHITE(new Color(0, 128, 128), Color.WHITE, "Arial Black"),
        ORANGE_WHITE(Color.ORANGE, Color.WHITE, "Comic Sans MS"),
        DEFAULT(Color.LIGHT_GRAY, Color.BLACK, "Consolas");

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
