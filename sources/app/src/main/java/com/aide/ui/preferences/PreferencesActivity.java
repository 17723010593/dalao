package com.aide.ui.preferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import com.s1243808733.aide.AppTheme;
import com.s1243808733.aide.util.AIDEUtils;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppTheme.initTheme(this);
        /*
         if (aa.lg()) {
         b.light(this, 2131623944);
         } else {
         b.dark(this, 2131623943);
         }
         */
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AIDEUtils.preferenceActivityMenuCreate(this, menu);
        return super.onCreateOptionsMenu(menu);
    }




}
