package com.a4455jkjh.d;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;
import com.s1243808733.aide.util.AndResGuard;
import com.s1243808733.aide.util.StringFog;
import java.io.File;
import com.s1243808733.aide.util.ProjectUtils;
public class h implements Runnable {

	c a;

	@Override
	public void run() {
		Throwable ex=null;
		try {
			File currentProject=ProjectUtils.getCurrentProject();
			if (AdvancedSetting.isEnableResGuard()) {
				AIDEUtils.updateBuild(a, "ResGuarding...");
				AndResGuard.proguard(currentProject);
			}
			if (AdvancedSetting.isEnableStringFog()) {
				AIDEUtils.updateBuild(a, "StringFoging...");
				StringFog.doFog(currentProject);
			}
		} catch (final Throwable t) {
			ex = t;
		}

		if (ex == null) {
			AIDEUtils.updateBuild(a, "Proguarding...");
			proguard();
		} else {
			String stackTrace=AIDEUtils.getStackTrace(ex);
			a.onCallback(stackTrace);
		}

	}


	public void proguard() {
		//原run方法
	}


}
