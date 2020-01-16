package com.s1243808733.androidr;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.text.SpannableStringBuilder;

public class FragmentDrawable extends BaseRFragment {

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
		return new AdapterDrawable(getActivity(), list);
	}

	@Override
	public Class<?> getClazz() {
		return android.R.drawable.class;
	}

	public class AdapterDrawable extends BaseRBaseAdapter {

		private Context ctx;

		private List<BaseRItem> list=new ArrayList<>();
		private List<BaseRItem> list_backup=new ArrayList<>();

		public AdapterDrawable(Context ctx, List<BaseRItem> list) {
			this.list = list;
			this.list_backup = list;
			this.ctx = ctx;
		}

		@Override
		public void setList(List<BaseRItem> list) {
			this.list=list;
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

			holder.title.setText(new SpannableStringBuilder(position + 1 + ". ")
								 .append(getHighlightText(ctx,item.name, item.hig_name)));
			
			holder.icon.setImageResource(item.id);

			return view;
		}

		private class ItemView extends LinearLayout {

			public ViewHolder holder=new ViewHolder();

			public ItemView(Context ctx) {
				super(ctx);
				ImageView icon=new ImageView(ctx);
				TextView title=new TextView(ctx);
				title.setPadding(dp2px(16), 0, 0, 0);
				title.setTextAppearance(ctx, android.R.style.TextAppearance_Large);
				title.setTextSize(15);

				holder.icon = icon;
				holder.title = title;

				addView(icon, dp2px(36), dp2px(36));
				addView(title);

				setGravity(Gravity.CENTER_VERTICAL);
				setPadding(dp2px(16), dp2px(10), dp2px(16), dp2px(10));

			}

		}

		private class ViewHolder {
			public ImageView icon;
			public TextView title;
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
