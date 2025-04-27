package view.theme;

import java.awt.Color;

public class Theme {
    private String name;
    private Color backgroundColor;
    private Color primaryColor;
    private Color textColor;

    public Theme(String name, Color backgroundColor, Color primaryColor, Color textColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.primaryColor = primaryColor;
        this.textColor = textColor;
    }

    // Геттеры
    public String getName() { return name; }
    public Color getBackgroundColor() { return backgroundColor; }
    public Color getPrimaryColor() { return primaryColor; }
    public Color getTextColor() { return textColor; }
}