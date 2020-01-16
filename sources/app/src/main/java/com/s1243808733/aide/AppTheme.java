package com.s1243808733.aide;

import android.app.Activity;
import com.a4455jkjh.R;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;
import com.s1243808733.util.Toasty;
import com.s1243808733.util.Utils;

public class AppTheme {

    public static final String THEME_DEFAULT="默认";

    public static final String THEME_INDIGO="靛蓝";

    public static String getThemeName() {
        return Utils.getSp().getString("theme_name", THEME_DEFAULT);
    }

    public static void setTheme(String themeName) {
        Utils.getSp().edit().putString("theme_name", themeName).commit();
    }

    public static void initTheme(Activity activity) {
        String themeName=AdvancedSetting.getThemeName();

        if (AIDEUtils.isTrainerMode()) {
            activity.setTheme(R.style.MyAppThemeLight);
            Utils.setSystemStatusBarLight(activity, true);
            Toasty.enable = false;
            return;
        }

        if (AIDEUtils.isLightTheme()) {
            if (AppTheme.THEME_INDIGO.equals(themeName)) {
                activity.setTheme(R.style.MyAppThemeDark_Indigo);
            } else {
                activity.setTheme(R.style.MyAppThemeLight);
                Utils.setSystemStatusBarLight(activity, true);
            }
        } else {
            if (AppTheme.THEME_INDIGO.equals(themeName)) {
                activity.setTheme(R.style.MyAppThemeDark_Indigo);
            } else {
                activity.setTheme(R.style.MyAppThemeDark);
            }
        }


        if (AppTheme.THEME_INDIGO.equals(themeName)) {
            Toasty.enable = true;
        } else {
            Toasty.enable = false;
        }
    }

}
