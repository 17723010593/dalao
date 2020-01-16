package com.aide.ui.views;
import android.view.Gravity;
import android.view.ViewGroup;
import com.blankj.utilcode.util.ReflectUtils;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.MainInterface;

public class SplitView extends ViewGroup {
	public SplitView() {
		super(null);
	}
	@Override
	protected void onLayout(boolean p1, int p2, int p3, int p4, int p5) {
	}


	public void openSplit(boolean z, boolean z2) {
		MainInterface.onSplitViewOpenSplit(this,z,z2);
	}

	public void closeSplit(boolean z, Runnable runnable) {
		MainInterface.onSplitViewCloseSplit(this,z,runnable);
	}

}
