package com.s1243808733.aide.highlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import com.s1243808733.aide.AppTheme;
import com.s1243808733.aide.highlight.color.ColorDefault;
import com.s1243808733.aide.highlight.color.ColorImpl;
import com.s1243808733.aide.highlight.color.Colors;
import com.s1243808733.aide.highlight.color.style.ColorIndigo;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;
import com.s1243808733.util.Utils;
import java.util.Locale;

public class HighlightUtils {

	public static ColorImpl getHighlightColor() {
        String themeName =AdvancedSetting.getThemeName();
        if (AppTheme.THEME_INDIGO.equals(themeName)) {
            return new ColorIndigo();
        } else {
        }
        return new ColorDefault();
	}

    public static void init() {
        initKey(new ColorDefault(), AppTheme.THEME_DEFAULT);
        initKey(new ColorIndigo(), AppTheme.THEME_INDIGO);
    }

	public static void initKey(ColorImpl colorImpl, String themeName) {
        SharedPreferences sp=getHSp(themeName);
        SharedPreferences.Editor editor=sp.edit();
        for (Colors color:colorImpl.values()) {
            String key_light=getSpKey(color.name, true);
            String key_dark = getSpKey(color.name, false);
            if (!sp.contains(key_light)) {
                editor.putString(key_light, color.lightColor);
            }
            if (!sp.contains(key_dark)) {
                editor.putString(key_dark, color.darkColor);
            }
        }
        editor.commit();
	}

    public static int getHighLightColor(String colorName, boolean isLight, int lightResId, int darkResId) {
        try {
            String colorStr=getHSp().getString(getSpKey(colorName, isLight), null);
            return Color.parseColor(colorStr);
        } catch (Throwable e) {
        }
        //  return Color.RED;
        return Utils.getColorFromResources(isLight ?lightResId: darkResId);
    }


    public static String getColorByKey(String key) {
        return getHSp().getString(key, null);
    }

    public static String getSpKey(String colorName, boolean isLight) {
        return (colorName.replaceAll("[^a-z|A-Z]", "_")).toLowerCase(Locale.ENGLISH) 
		+ (isLight ?"_light": "_dark");
    }

    public static SharedPreferences getHSp() {
        return getHSp(AdvancedSetting.getThemeName());
    }

    public static SharedPreferences getHSp(String themeName) {
        if (AIDEUtils.isTrainerMode()) {
            themeName = AppTheme.THEME_DEFAULT;
        }
		themeName = "_" + themeName;
        return Utils.getApp().getSharedPreferences("CodeHighlight" + Utils.toUpperCaseFirst(themeName), Context.MODE_PRIVATE);
    }

    ////

    public static int getColorInt(Colors color) {
        return getColorInt(color, AIDEUtils.isLightTheme());
    }

    public static int getColorInt(Colors color, boolean isLight) {
		try {
			return Color.parseColor(getColorByKey(getSpKey(color.name, isLight)));
		} catch (Throwable e) {
			return Color.parseColor(isLight ?color.lightColor: color.darkColor);
		}
    }

    public static int getColorInt(Colors colors, int lightResId, int darkResId) {
        if (AIDEUtils.isLightTheme()) {
            try {
                return getColorInt(colors, true);
            } catch (Exception e) {
                return Utils.getColorFromResources(lightResId);
            }
        } else {
            try {
                return getColorInt(colors, false);
            } catch (Exception e) {
                return Utils.getColorFromResources(darkResId);
            }
        } 
    }


}
