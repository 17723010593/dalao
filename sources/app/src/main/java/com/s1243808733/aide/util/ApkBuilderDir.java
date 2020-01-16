package com.s1243808733.aide.util;
import android.os.Environment;
import java.io.File;

public class ApkBuilderDir {
    public static String getApkBuilderDir() {
return "";
    }

    public static String escape(String code) {
        File project=ProjectUtils.currentProject();
        if (project == null) {
            return null;
        }
        String packageName=ProjectUtils.getPackageName(project);

        return code.replace("[Project]", project.getAbsolutePath())
            .replace("[Sdcard]", Environment.getExternalStorageDirectory().getAbsolutePath())
            .replace("[PackageName]", packageName == null ?"unknown": packageName)
            .replace("[Name]", project.getName())

            ;

    }
}
