package com.a4455jkjh;
import android.app.Application;
import com.s1243808733.aide.util.AIDEUtils;
import com.aide.ui.AIDEEditor;

public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AIDEUtils.init(this);
		
    }

}
