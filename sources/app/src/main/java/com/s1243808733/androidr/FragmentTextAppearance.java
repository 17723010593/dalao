package com.s1243808733.androidr;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.text.SpannableStringBuilder;

public class FragmentTextAppearance extends BaseRFragment {

	@Override
	public BaseRBaseAdapter onCreateAdapter(Bundle savedInstanceState) throws IllegalArgumentException, IllegalAccessException {
		List<BaseRItem> list=new ArrayList<>();
		Field[] fields=getClazz().getFields();
		for (int i=0;i < fields.length;i++) {
			Field field=fields[i];
			String name = field.getName();
			if (name.startsWith("TextAppearance_")) {
				int resid=field.getInt(null);
				BaseRItem item=new BaseRItem(name, resid);
				list.add(item);
			}
		}
		return new AdapterTextAppearance(getActivity(), list);
	}
	
	@Override
	public Class<?> getClazz() {
		return android.R.style.class;
	}

	public class AdapterTextAppearance extends BaseRBaseAdapter {

		private Context ctx;

		private List<BaseRItem> list=new ArrayList<>();
		private List<BaseRItem> list_backup=new ArrayList<>();

		public AdapterTextAppearance(Context ctx, List<BaseRItem> list) {
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
			ViewHolder holder;
			if (view == null) {
				view = LayoutInflater.from(ctx).inflate(android.R.layout.simple_list_item_1, group, false);
				view.setPadding(dp2px(16), dp2px(10), dp2px(16), dp2px(10));
				holder = new ViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			BaseRItem item=getItem(position);

			holder.title.setText(new SpannableStringBuilder(position + 1 + ". ")
								 .append(getHighlightText(ctx,item.name, item.hig_name)));
			
			holder.title.setTextAppearance(ctx, item.id);

			return view;
		}

		private class ViewHolder {
			public TextView title;

			public ViewHolder(View v) {
				title = (TextView) v.findViewById(android.R.id.text1);
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
