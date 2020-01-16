package com.s1243808733.aide.util;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.aide.ui.MainActivity;
import java.io.File;
import com.s1243808733.util.Toasty;

public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

	private Application app;

	public MyActivityLifecycleCallbacks(Application app) {
		this.app = app;
	}

	@Override
	public void onActivityCreated(Activity p1, Bundle p2) {
	}

	@Override
	public void onActivityStarted(Activity p1) {
	}

	@Override
	public void onActivityResumed(Activity p1) {
	}

	@Override
	public void onActivityPaused(Activity p1) {
	}

	@Override
	public void onActivityStopped(Activity p1) {
	}

	@Override
	public void onActivitySaveInstanceState(Activity p1, Bundle p2) {
	}

	@Override
	public void onActivityDestroyed(Activity p1) {
		if (isMainActivity(p1)) {
			File f=AIDEUtils.getCurrentEditorFile();
			if (f != null) {
                AIDEUtils.setCurrentFileToTop(f);
            }
			Toasty.cancel();
		}
	}

	private boolean isMainActivity(Activity p1) {
		return (p1 instanceof MainActivity);
	}

}
