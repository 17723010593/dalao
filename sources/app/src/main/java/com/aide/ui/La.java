package com.aide.ui;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;

public class La {

    private View Hw;
    private MainActivity FH;
    private String v5;

    public void j6(String str) {
        str = AdvancedSetting.getEditorQuickKey();
        if (this.Hw != null && str != null && !this.v5.equals(str)) {

            this.v5 = str;
            LayoutInflater from = LayoutInflater.from(this.FH);
            ViewGroup viewGroup = (ViewGroup) this.Hw.findViewById(2131231043);
            viewGroup.removeAllViews();
            int height=AIDEUtils.dp2px(40);

            for (String key : str.split("\n")) {
                if (key.length() != 0) {
                    TextView tv = (TextView) from.inflate(2131361852, null);
                    if ("#sp".equals(key)) {
                        tv.setText("→");
                        tv.setOnClickListener(new Ka(this, "    "));
                    } else {
                        tv.setText(key);
                        tv.setOnClickListener(new Ka(this, key));
                    }
                    viewGroup.addView(tv, -2, height);
                }
            }
        }

    }



}
