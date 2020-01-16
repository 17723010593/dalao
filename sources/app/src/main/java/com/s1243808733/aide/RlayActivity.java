package com.s1243808733.aide;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.aide.ui.MainActivity;
import com.s1243808733.util.Utils;

public class RlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        intent.setComponent(new ComponentName(this, MainActivity.class));
        startActivity(intent);
        finish();
        
    }

}
