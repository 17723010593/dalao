package com.s1243808733.aide.util;

import android.graphics.Typeface;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.TextView;
import com.s1243808733.aide.AppTheme;
import com.s1243808733.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSetting {

    public static final String VERSION_NAME="2.0";

    public static boolean isEnableStringFog() {
        return Utils.getSp().getBoolean("advanced_setting_enable_stringfog", false);
    }

    public static boolean isEnableResGuard() {
        return Utils.getSp().getBoolean("advanced_setting_enable_resguard", false);
    }

    public static boolean isEnableTranslate() {
        return Utils.getSp().getBoolean("advanced_setting_enable_translate", true);
    }

    public static boolean isEnableAdrt() {
        return Utils.getSp().getBoolean("advanced_setting_enable_adrt", true);
    }

    public static boolean isEnableMenuCode() {
        return Utils.getSp().getBoolean("advanced_setting_enable_menu_code", true);
    }

	public static boolean isEnableDrawer() {
        return Utils.getSp().getBoolean("advanced_setting_enable_drawer", false);
    }

    public static String getThemeName() {
        return AppTheme.getThemeName();
    }

    public static void setTheme(String themeName) {
        AppTheme.setTheme(themeName);
    }

    public static void setFindResultTextView(TextView p0) {
        p0.setSingleLine(false);
		p0.setMaxLines(2);
    }

    public static String getBuildApkPath(String path) {
        File file=new File(path);
        String fileName=file.getName() ;

        String ver=ProjectUtils.getVersionName(ProjectUtils.currentProject());
        //fileName += ver;
        fileName += ".apk";

        if (AIDEUtils.getSp().getBoolean("advanced_setting_adjust_apk_build_path", false)) {
            File bin=ProjectUtils.getBin(ProjectUtils.getCurrentProject());
            return new File(bin, fileName).getAbsolutePath();
        }
        return new File(Environment.getExternalStorageDirectory(),
                        String.format("Android/data/%1$s/cache/apk/%2$s", Utils.getApp().getPackageName(), fileName)).getAbsolutePath();
    }

    public static String getEditorQuickKey() {
        File file=AIDEUtils.getCurrentEditorFile();
		return QuickKey.getKey(file == null || file.getName().indexOf(".") < 0 ?".java": Utils.getFileNameSuffix(file.getName()));
    }

    public static Typeface getEditorTypeface() {
        try {
            return Typeface.createFromFile(Utils.getSp().getString("advanced_setting_editor_font", null));
        } catch (Exception e) {
        }
        return Typeface.MONOSPACE;
    }

    public static String getPackagePrefix(String name) {
        String s = AIDEUtils.getSp().getString("advanced_setting_package_prefix", null);
        if (TextUtils.isEmpty(s)) {
            s = "com.mycompany.";
        } 
        if (!s.endsWith(".")) {
            s += ".";
        }
        return s + name.toLowerCase();
    }


    public static List<String> getDependencyList() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("com.android.support:appcompat-v7:27.1.1");
        arrayList.add("com.android.support:design:27.1.1");
        arrayList.add("com.android.support:cardview-v7:27.1.1");
        arrayList.add("com.android.support:recyclerview-v7:27.1.1");

        arrayList.add("com.android.support:support-v4:27.1.1");
        arrayList.add("com.android.support:support-annotations:27.1.1");

        arrayList.add("com.android.support:multidex:1.0.0");

        arrayList.add("com.android.support:preference-v14:27.1.1");
        arrayList.add("com.android.support:preference-v7:27.1.1");


        return arrayList;
    }

}

