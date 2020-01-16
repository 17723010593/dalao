package com.s1243808733.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.KeyboardUtils;
import com.s1243808733.util.Utils;

public class KeyboardFillView extends View {

    public KeyboardFillView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Utils.getSp().getBoolean("use_adjustPan", false)) {

            Activity activity =(Activity)context;
            KeyboardUtils.registerSoftInputChangedListener(activity, new KeyboardUtils.OnSoftInputChangedListener(){

                @Override
                public void onSoftInputChanged(int height) {
                    ViewGroup.LayoutParams lp=getLayoutParams();
                    lp.height = height;
                    requestLayout();
                }
            });

        }
    }


}

