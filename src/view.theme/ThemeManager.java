package view.theme;

import java.awt.Color;

public class ThemeManager {

    public static final Theme LIGHT_THEME = new Theme(
        "Light",
        new Color(255, 255, 255),
        new Color(25, 118, 210),
        new Color(33, 33, 33)
    );

    public static final Theme DARK_THEME = new Theme(
        "Dark",
        new Color(18, 18, 18),
        new Color(90, 147, 224),
        new Color(255, 255, 255)
    );

    // Current theme is initially set to LIGHT_THEME
    private static Theme currentTheme = LIGHT_THEME;

    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }
}