package com.s1243808733.app.base;
import android.app.Activity;
import android.os.Bundle;
import com.s1243808733.aide.AppTheme;

public abstract class AbsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isApplyTheme()) {
            AppTheme.initTheme(this);
        }
    }

    public abstract boolean isApplyTheme();
}
