package com.s1243808733.aide.util;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import com.s1243808733.aide.AppTheme;
import com.s1243808733.util.Utils;

public class AppConfigured {
	public static class VERSION {
		public static final int BASE=1;
	}

	public static void init() {
        initWhatsNewDialog();

		SharedPreferences sp=Utils.getSp();
		if (sp.getInt("app_configured_version", -1) < VERSION.BASE) {
			SharedPreferences.Editor edit=sp.edit();
			initDefaultTheme(edit);
			initJavaNewLine(edit);
			edit.putString("editor_font_size", "9");
            edit.putString("max_single_imports", "100");
            edit.putInt("app_configured_version", VERSION.BASE);

			edit.commit();
		}
	}


    private static void initWhatsNewDialog() {
        try {
            Application app=Utils.getApp();
            SharedPreferences sp=app.getSharedPreferences("WhatsNew", 0);
            PackageInfo pkgInfo=app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
            if (sp.getInt("ShownVersion", -1) < pkgInfo.versionCode) {
                SharedPreferences.Editor edit=sp.edit();
                edit.putInt("ShownVersion", pkgInfo.versionCode);
                edit.commit();
            }
        }   catch (Throwable e) {}
    }

	private static void initDefaultTheme(SharedPreferences.Editor edit) {
		AdvancedSetting.setTheme(AppTheme.THEME_DEFAULT);
		edit.putBoolean("light_theme", true);
	}

	private static void initJavaNewLine(SharedPreferences.Editor edit) {
		String[] keys={
            "ADJUST_NEWLINES",
			"TYPE_NEWLINE",
			"METHOD_NEWLINE",
			"BLOCK_NEWLINE",
			"ELSE_NEWLINE",
			"CATCH_NEWLINE",
			"FINALLY_NEWLINE",
		};
		for (int i=0;i < keys.length;i++) {
            String name=keys[i];
			edit.putBoolean("java_" + name, i == 0);
		}

	}


}
