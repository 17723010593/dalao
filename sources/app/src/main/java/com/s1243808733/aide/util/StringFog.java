package com.s1243808733.aide.util;

import com.blankj.utilcode.util.ResourceUtils;
import com.github.megatronking.stringfog.plugin.StringFogClassInjector;
import com.github.megatronking.stringfog.plugin.StringFogMappingPrinter;
import com.s1243808733.util.Utils;
import java.io.File;
import java.io.IOException;

public class StringFog {

    public static void doFog(File project) throws Throwable {

        File projectBin=ProjectUtils.getBin(project);
        File projectClassesRelease=ProjectUtils.getBinClassesRelease(project);

        String[] fogPackages={};

        String implementation ="com.github.megatronking.stringfog.xor.StringFogImpl";
        String fogClassName =implementation;
        String key=com.github.megatronking.stringfog.xor.StringFogImpl.CHARSET_NAME_UTF_8;

        StringFogMappingPrinter mMappingPrinter = new StringFogMappingPrinter(
            new File(projectBin.getAbsolutePath() , "stringFogMapping.txt"));

        StringFogClassInjector mInjector = new StringFogClassInjector(fogPackages, key, implementation, fogClassName, mMappingPrinter);

        mMappingPrinter.startMappingOutput();
        mMappingPrinter.ouputInfo(key, implementation);

        mInjector.doFog2ClassInDir(projectClassesRelease);

        if (!ResourceUtils.copyFileFromAssets(Utils.getAssetsDataFile("stringfog"), projectClassesRelease)) {
            throw new IOException();
        }

    }

}
