package com.s1243808733.aide.util;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ProjectUtils {

    public static final String MANIFEST_PACKAGE_NAME="package";

    public static final String MANIFEST_VERSION_CODE="android:versionCode";

    public static final String MANIFEST_VERSION_NAME="android:versionName";

    public static final String MANIFEST_MIN_SDK_VERSION="android:minSdkVersion";

    public static final String MANIFEST_TARGET_SDK_VERSION="android:targetSdkVersion";

    public static File getAppProjects() {
        return newFile(Environment.getExternalStorageDirectory(), "AppProjects");
    }

    public static File currentProject() {
        return getCurrentProject();
    }

    public static File getCurrentProject() {
        return newFile(AIDEUtils.getCurrentAppHome());
    }

    public static File getProjectFile(File project, String child) {
        return newFile(project, child);
    }

    //////////////////////////////////////////////////////////////////////

    public static File getBuild(File project) {
        return getProjectFile(project, "build");
    }

    public static File getBuild(File project, String child) {
        return newFile(getBuild(project), child);
    }

    //////////////////////////////////////////////////////////////////////

    public static File getBin(File project) {
        if (isGradleProject(project)) {
            return getBuild(project, "bin");
        } else if (isEclipseProject(project)) {
            return newFile(project, "bin");
        } else if (isJavaProject(project)) {
            return newFile(project, "bin");
        }
        return null;
    }

    public static File getBin(File project, String child) {
        return newFile(getBin(project), child);
    }
    //////////////////////////////////////////////////////////////////////

    public static File getBinClassesDebug(File project) {
        return getBin(project, "classesdebug");
    }

    public static File getBinClassesDebug(File project, String child) {
        return newFile(getBinClassesDebug(project), child);
    }
    //////////////////////////////////////////////////////////////////////

    public static File getBinClassesRelease(File project) {
        return getBin(project, "classesrelease");
    }

    public static File getBinClassesRelease(File project, String child) {
        return newFile(getBinClassesRelease(project), child);
    }
    //////////////////////////////////////////////////////////////////////

    public static File getBinInjectedManifest(File project) {
        return getBin(project, "injected/AndroidManifest.xml");
    }
    //////////////////////////////////////////////////////////////////////

    public static File getBinJarDex(File project) {
        return getBin(project, "jardex");
    }

    public static File getBinJarDex(File project, String child) {
        return newFile(getBinJarDex(project), child);
    }
    //////////////////////////////////////////////////////////////////////

    public static File getBinResourcesAp(File project) {
        return getBin(project, "resources.ap_");
    }

    //////////////////////////////////////////////////////////////////////

    public static File getLibs(File project) {
        return getProjectFile(project, "libs");
    }

    public static File getLibs(File project, String child) {
        return newFile(getLibs(project), child);
    }

    //////////////////////////////////////////////////////////////////////

    public static File getAssets(File project) {
        return getMain(project, "assets");
    }

    public static File getAssets(File project, String child) {
        return newFile(getAssets(project), child);
    }

    //////////////////////////////////////////////////////////////////////

    public static File getSrcJava(File project) {
        if (isGradleProject(project)) {
            return getMain(project, "java");
        } else if (isEclipseProject(project)) {
            return newFile(project, "src");
        } else if (isJavaProject(project)) {
            return newFile(project, "src");
        }
        return null;
    }

    public static File getSrcJava(File project, String child) {
        return newFile(getSrcJava(project), child);
    }

    public static File getSrcJavaPkg(File project) {
        String packageName=getPackageName(project);
        if (packageName == null)return null;
        return newFile(getSrcJava(project), packageName.replace(".", "/"));
    }

    //////////////////////////////////////////////////////////////////////

    public static File getRes(File project) {
        return getMain(project, "res");
    }

    public static File getRes(File project, String child) {
        return newFile(getRes(project), child);
    }

    //////////////////////////////////////////////////////////////////////

    public static File getAndroidManifest(File project) {
        return getMain(project, "AndroidManifest.xml");
    }

    public static String getPackageNameFromManifest(File manifest) {
        return getManifestValue(manifest, MANIFEST_PACKAGE_NAME);
    }

    public static String getVersionNameFromManifest(File manifest) {
        return getManifestValue(manifest, MANIFEST_VERSION_NAME);
    }

    public static String getPackageName(File project) {
        String packageName=getPackageNameFromManifest(getAndroidManifest(project));
        return packageName != null ?packageName: getPackageNameFromManifest(getBinInjectedManifest(project));
    }

    public static String getVersionName(File project) {
        String versionName=getVersionNameFromManifest(getAndroidManifest(project));
        return versionName != null ?versionName: getVersionNameFromManifest(getBinInjectedManifest(project));
    }

    //////////////////////////////////////////////////////////////////////

    public static File getProguardFile(File project) {
        File f1=newFile(project, "proguard-rules.pro");
        if (isExists(f1))return f1;
        File f2=newFile(project, "proguard-project.txt");
        if (isExists(f2))return f2;
        return null;
    }

    //////////////////////////////////////////////////////////////////////

    public static File getMain(File project) {
        if (isGradleProject(project)) {
            return newFile(project, "src/main");
        } else if (isEclipseProject(project)) {
            return project;
        } else if (isJavaProject(project)) {
            return project;
        }
        return null;
    }

    public static File getMain(File project, String child) {
        return newFile(getMain(project), child);
    }

    //////////////////////////////////////////////////////////////////////

    public static boolean isAndroidProject(File project) {
        return (isGradleProject(project) || isEclipseProject(project));
    }

    public static boolean isGradleProject(File project) {
        return (project != null
            && new File(project, "build.gradle").exists() 
            && new File(project, "src").exists());
    }

    public static boolean isEclipseProject(File project) {
        return (project != null
            && newFile(project, "AndroidManifest.xml").exists() 
            && newFile(project, "src").exists());
    }

    public static boolean isJavaProject(File project) {
        return (project != null
            && !isAndroidProject(project)
            && newFile(project, ".classpath").exists());
    }

    //////////////////////////////////////////////////////////////////////

    private static File newFile(String path) {
        try {
            return new File(path);
        } catch (Throwable e) {
            return null;
        }
    }

    private static File newFile(File parent, String child) {
        if (parent != null
            && child != null
            && child.length() != 0) {
            try {
                return new File(parent, child);
            } catch (Throwable e) {     
            }
        }
        return null;
    }

    private static boolean isExists(File file) {
        return (file != null && file.exists());
    }

    public static String getManifestValue(File manifest, String name) {
        if (MANIFEST_PACKAGE_NAME.equals(name)
            || MANIFEST_VERSION_CODE.equals(name)
            || MANIFEST_VERSION_NAME.equals(name)) {
            return getManifestAttributeValueByName(manifest, null, name);
        } else if (MANIFEST_MIN_SDK_VERSION.equals(name)
                   || MANIFEST_TARGET_SDK_VERSION.equals(name)) {
            return getManifestAttributeValueByName(manifest, "uses-sdk" , name);
        }
        return null;
    }

	//max2
    public static String getManifestAttributeValueByName(File manifest, String nodeName, String name) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new FileInputStream(manifest), "utf-8");
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if ("manifest".equals(parser.getName())) {
                    if (nodeName == null) {
                        for (int i=0;i < parser.getAttributeCount();i++) {
                            if (name.equals(parser.getAttributeName(i))) {
                                return parser.getAttributeValue(i);
                            }
                        }
                    } else {
                        parser.next();
                    }
                } else if (nodeName != null && nodeName.equals(parser.getName())) {
                    for (int i=0;i < parser.getAttributeCount();i++) {
                        if (name.equals(parser.getAttributeName(i))) {
                            return parser.getAttributeValue(i);
                        }
                    }
                }

                parser.next();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


}
