package com.s1243808733.aide.util;

import android.content.pm.PackageInfo;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.s1243808733.util.Utils;
import com.tencent.mm.resourceproguard.InputParam;
import com.tencent.mm.resourceproguard.Main;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndResGuard {

    public static void proguard(File project) throws Throwable {
        File projectBinPath=ProjectUtils.getBin(project);
        File resources_ap=ProjectUtils.getBinResourcesAp(project) ;
        //init
        InputParam.Builder builder=new InputParam.Builder();

        builder.setWhiteList(new ArrayList<String>());
        builder.setCompressFilePattern(new ArrayList<String>());

        File configFile=new File(project, "proguard-resources.json");
        if (configFile.exists() && configFile.isFile()) {
            String jsonData=FileIOUtils.readFile2String(configFile);
            if (jsonData != null && jsonData.trim().length() != 0) {

                PackageInfo packageInfo=Utils.getApp().getPackageManager().getPackageArchiveInfo(resources_ap.getAbsolutePath(), 0);
                String packageName=packageInfo.packageName;

                try {
                    JSONObject jb=new JSONObject(jsonData);

                    builder.setKeepRoot(JsonUtils.getBoolean(jb, "keepRoot", false));

                    String fixedResName=JsonUtils.getString(jb, "fixedResName", null);
                    if (fixedResName != null && fixedResName.length() > 0) {
                        builder.setFixedResName(fixedResName);
                    }

                    String mapping=JsonUtils.getString(jb, "mappingFile", null);
                    if (mapping != null) {
                        File f=new File(project, mapping);
                        if (f.exists() && f.isFile()) {
                            builder.setMappingFile(f);
                        }
                    }

                    JSONArray whiteList=JsonUtils.getJSONArray(jb, "whiteList", null);
                    if (whiteList != null) {
                        ArrayList<String> list=new ArrayList<String>();
                        for (int i=0;i < whiteList.length();i++) {
                            String item=whiteList.getString(i);
                            list.add(packageName + "." + item);
                        }
                        builder.setWhiteList(list);
                    }

                    JSONArray compressFilePattern=JsonUtils.getJSONArray(jb, "compressFilePattern", null);
                    if (compressFilePattern != null) {
                        ArrayList<String> list=new ArrayList<String>();
                        for (int i=0;i < compressFilePattern.length();i++) {
                            String item=compressFilePattern.getString(i);
                            list.add(item);
                        }
                        builder.setCompressFilePattern(list);
                    }

                } catch (JSONException e) {
                    throw new JSONException("解析json出错，请检查json是否错误！：\n" + e.getMessage());
                }
            }
        }

        //init end

        File resources_apk=new File(projectBinPath , "resources.apk") ;
        FileUtils.copyFile(resources_ap, resources_apk);

        builder.setApkPath(resources_apk.getAbsolutePath());

        File outBuilderDir=new File(projectBinPath, "andresguard");
        builder.setOutBuilder(outBuilderDir.getAbsolutePath());

        try {
            InputParam inputParam=builder.create();
            Main.gradleRun(inputParam);
            File f=new File(outBuilderDir, "resources_unsigned.apk");
            if (f.exists()) {
                FileUtils.moveFile(f.getAbsolutePath(), resources_ap.getAbsolutePath());
            }
        } catch (Throwable e) {
            throw e;
        } finally {
            FileUtils.delete(resources_apk);
            FileUtils.deleteFilesInDirWithFilter(outBuilderDir, new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".txt") ?false: true;
                    }
                });
        }

    }

    public static void delectResourcesAps() {
        File aps=ProjectUtils.getBinResourcesAp(ProjectUtils.getCurrentProject());
        if (aps != null && aps.exists()) {
            aps.delete();
        }
    }

}
