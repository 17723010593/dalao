package com.s1243808733.project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.s1243808733.project.PUtils;

public class TemplateView extends LinearLayout {
	private Context context;

	public final ViewHolder holder=new ViewHolder();
	public class ViewHolder {
		public View ll,lll;
		public ImageView icon;
		public TextView title;
		public TextView subtitle;

		public void setIcon(Bitmap icon) {
			this.icon.setImageBitmap(icon);
			checkEmpty(this.icon, icon);
		}

		public void setTitle(CharSequence title) {
			this.title.setText(title);
			checkEmpty(this.title, title);
		}

		public void setSubtitle(CharSequence subtitle) {
			this.subtitle.setText(subtitle);
			checkEmpty(this.subtitle, subtitle);
		}

		private void checkEmpty(View v, Object obj) {
			if (obj == null) {
				v.setVisibility(View.GONE);
			} else {
				v.setVisibility(View.VISIBLE);
			}
		}

	}

	public TemplateView(Context context) {
		super(context);
		this.context = context;
		ImageView icon=new ImageView(context);

		TextView title = new TextView(context);
		title.setTextAppearance(context, android.R.style.TextAppearance_Large);
		title.setTextSize(16);

		TextView subtitle = new TextView(context);
		subtitle.setTextAppearance(context, android.R.style.TextAppearance_Small);
		subtitle.setTextSize(14);

		LinearLayout ll=new LinearLayout(context);
		ll.setPadding(0, 0, PUtils.dp2px(10), 0);
		ll.addView(icon, PUtils.dp2px(36), PUtils.dp2px(36));

		LinearLayout lll=new LinearLayout(context);
		lll.setOrientation(VERTICAL);
		lll.addView(title);
		lll.addView(subtitle);

		holder.ll = ll;
		holder.lll = lll;

		holder.icon = icon;
		holder.title = title;
		holder.subtitle = subtitle;

		addView(ll);
		addView(lll);
		setPadding(PUtils.dp2px(32), PUtils.dp2px(16), PUtils.dp2px(16), PUtils.dp2px(16));
		setGravity(Gravity.CENTER_VERTICAL);
	}

}
