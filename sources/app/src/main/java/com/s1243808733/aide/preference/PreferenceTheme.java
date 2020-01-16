package com.s1243808733.aide.preference;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.s1243808733.aide.AppTheme;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;
import com.s1243808733.util.Toasty;

public class PreferenceTheme extends Preference {

	private Switch cb;

    public PreferenceTheme(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
		if (getSummary() == null) {
			setSummary(AdvancedSetting.getThemeName());
		}
        setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

			private String[] items={
				AppTheme.THEME_DEFAULT,
				AppTheme.THEME_INDIGO
			};

			@Override
			public boolean onPreferenceClick(Preference p1) {
				String themeName=AdvancedSetting.getThemeName();

				int checkedItem=0;
				for (int i=0;i < items.length;i++) {
					if (items[i].equals(themeName)) {
						checkedItem = i;
						break;
					}
				}

				AlertDialog dialog=new AlertDialog.Builder(getContext())
				.setTitle(p1.getTitle())
				.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String themeName=items[which];
						if (!themeName.equals(AdvancedSetting.getThemeName())) {

							AdvancedSetting.setTheme(themeName);
                            if (themeName.equals(AppTheme.THEME_INDIGO)) {
                                AIDEUtils.setIsLightTheme(false);
                                cb.setChecked(true);
                                cb.setEnabled(false);
                            } else {
                                cb.setEnabled(true);
                            }
							AIDEUtils.notifyThemeChanged();
							setSummary(themeName);
							dialog.dismiss();
							Toasty.success("设置成功，返回生效！").show();


						}
					}
				})
				.setPositiveButton("关闭", null)
				.create();
				dialog.show();
				return false;
			}
		});
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
		LinearLayout widgetFrameView = ((LinearLayout) view.findViewById(android.R.id.widget_frame));
        if (widgetFrameView == null) return;

        widgetFrameView.setVisibility(View.VISIBLE);
        int count = widgetFrameView.getChildCount();
        if (count > 0) {
            widgetFrameView.removeViews(0, count);
        }

        cb = new Switch(getContext());
        cb.setText("夜间模式");
        cb.setChecked(!AIDEUtils.isLightTheme());
		cb.setFocusable(false);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2) {
				AIDEUtils.setIsLightTheme(!p2);
				AIDEUtils.notifyThemeChanged();
				Toasty.success("设置成功，返回生效！").show();

			}
		});
		if (cb != null) {
			cb.setEnabled(!AppTheme.getThemeName().equals(AppTheme.THEME_INDIGO));
		}
        widgetFrameView.addView(cb);

    }


}
