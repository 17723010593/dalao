package com.s1243808733.androidr;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.s1243808733.view.ColorBackgroundTextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FragmentColor extends BaseRFragment {

	@Override
	public BaseRBaseAdapter onCreateAdapter(Bundle savedInstanceState) throws IllegalArgumentException, IllegalAccessException {
		List<BaseRItem> list=new ArrayList<>();
		Field[] fields=getClazz().getFields();
		for (int i=0;i < fields.length;i++) {
			Field field=fields[i];
			String name = field.getName();
			int resid=field.getInt(null);
			BaseRItem item=new BaseRItem(name, resid);
			list.add(item);
		}
		return new AdapterColor(getActivity(), list);
	}


	@Override
	public Class<?> getClazz() {
		return android.R.color.class;
	}

	public class AdapterColor extends BaseRBaseAdapter {

		private Context ctx;

		private List<BaseRItem> list=new ArrayList<>();
		private List<BaseRItem> list_backup=new ArrayList<>();

		public AdapterColor(Context ctx, List<BaseRItem> list) {
			this.list = list;
			this.list_backup = list;
			this.ctx = ctx;
		}

		@Override
		public void setList(List<BaseRItem> list) {
			this.list = list;
		}

		@Override
		public List<BaseRItem> getListBackup() {
			return list_backup;
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			if (view == null) {
				view = new ItemView(ctx);
			} 
			ViewHolder holder = ((ItemView)view).holder;

			BaseRItem item=getItem(position);

			holder.title.setText(new SpannableStringBuilder(position + 1 + ". ").append(getHighlightText(ctx, item.name, item.hig_name)));

			int color=ctx.getResources().getColor(item.id);
			holder.subtitle.setColor(color);
			return view;
		}

		private class ViewHolder {
			public TextView title;
			public ColorBackgroundTextView subtitle;
		}

		private class ItemView extends LinearLayout {

			public ViewHolder holder=new ViewHolder();

			public ItemView(Context ctx) {
				super(ctx);
				setOrientation(VERTICAL);
				
				TextView title=new TextView(ctx);
				title.setTextAppearance(ctx, android.R.style.TextAppearance_Large);
				title.setTextSize(15);
				title.setTextSize(15);
				title.setPadding(0, 0, 0, dp2px(5));

				ColorBackgroundTextView subtitle=new ColorBackgroundTextView(ctx);
				
				holder.title = title;
				holder.subtitle = subtitle;

				addView(title);
				addView(subtitle);

				setGravity(Gravity.CENTER_VERTICAL);
				setPadding(dp2px(16), dp2px(10), dp2px(16), dp2px(10));

			}

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public BaseRItem getItem(int p1) {
			return list.get(p1);
		}

		@Override
		public long getItemId(int p1) {
			return p1;
		}

	}	

}
