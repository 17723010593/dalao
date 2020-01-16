package com.aide.ui.browsers;
import android.widget.TextView;
import com.s1243808733.aide.util.AdvancedSetting;

public class FindResultTextView extends TextView {
	public FindResultTextView() {
		super(null);
	}

	private void j6() {
        AdvancedSetting.setFindResultTextView(this);
	}

}
