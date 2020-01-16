package com.s1243808733.aide.highlight;
import java.util.Locale;

public class Item {
    private String title;

    private String type;

    private String defaultColor;

    public Item(String title, String type, String defaultColor) {
        this.title = title;
        this.type = type;
        this.defaultColor = defaultColor;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getColor(boolean isLight) {
        return HighlightUtils.getHSp().getString(HighlightUtils.getSpKey(getType(), isLight), getDefaultColor())
        .toUpperCase(Locale.ENGLISH);
    }

    public void setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSpKey(boolean isLightTheme) {
        return HighlightUtils.getSpKey(getType(), isLightTheme);
    }

}
