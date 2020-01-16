package com.s1243808733.androidr;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.s1243808733.util.Utils;

public class FragmentString extends BaseRFragment {

	private FragmentString.AdapterString adapter;

	@Override
	public void onViewCreated(BaseRFragment.ViewHolder holder, Bundle savedInstanceState) {
		super.onViewCreated(holder, savedInstanceState);
		List<TwoLineItem> list=new ArrayList<>();
		Field[] fields=getClazz().getFields();
		for (int i=0;i < fields.length;i++) {
			try {
				Field field=fields[i];
				String name = field.getName();
				int resid=field.getInt(null);
				TwoLineItem item=new TwoLineItem(name, resid);
				item.subname = getString(resid);
				list.add(item);
			} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {}
		}
		adapter = new AdapterString(getActivity(), list);
		holder.mListView.setAdapter(adapter);
		holder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
				showDialog(adapter.getItem(position));
			}
		});
		holder.mSearchView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				adapter.search(p1.toString());
			}

			@Override
			public void afterTextChanged(Editable p1) {
			}
		});
	}



	@Override
	public Class<?> getClazz() {
		return android.R.string.class;
	}

	public class AdapterString extends BaseAdapter {

		private Context ctx;

		private List<TwoLineItem> list=new ArrayList<>();
		private List<TwoLineItem> list_backup=new ArrayList<>();

		private boolean searching;
		
		public AdapterString(Context ctx, List<TwoLineItem> list) {
			this.list = list;
			this.list_backup = list;
			this.ctx = ctx;
		}

		public void search(String search) {
			searching=true;
			if (search == null || search.trim().length() == 0) {
				this.list = list_backup;
				searching=false;
			} else {
				List<TwoLineItem> searchlist=new ArrayList<>();
				search = search.toLowerCase(Locale.ENGLISH);
				for (TwoLineItem bean:list_backup) {
					int indexName=bean.name.toLowerCase(Locale.ENGLISH).indexOf(search);
					int indexSubName=bean.subname.toLowerCase(Locale.ENGLISH).indexOf(search);
					if (indexName != -1 || indexSubName != -1) {
						bean.hig_name.start = indexName;
						bean.hig_name.end = indexName + search.length();
						bean.hig_subName.start = indexSubName;
						bean.hig_subName.end = indexSubName + search.length();
						searchlist.add(bean);
					}

				}
				this.list = searchlist;
			}
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			ViewHolder holder;
			if (view == null) {
				view = LayoutInflater.from(ctx).inflate(android.R.layout.two_line_list_item, group, false);
				view.setPadding(dp2px(16), dp2px(10), dp2px(16), dp2px(10));
				holder = new ViewHolder(view);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			TwoLineItem item=getItem(position);

			holder.title.setText(new SpannableStringBuilder(position + 1 + ". ")
								 .append(getHighlightText(item.name, item.hig_name)));

			holder.subtitle.setText(getHighlightText(item.subname, item.hig_subName));

			return view;
		}

		private SpannableStringBuilder getHighlightText(String text, BaseRItem.Highlight hig) {
			SpannableStringBuilder sb=new SpannableStringBuilder();
			SpannableString ss=new SpannableString(text);
			if (searching&& hig.start >= 0)   {
				BackgroundColorSpan bcs=new BackgroundColorSpan(Utils.getColorAccent(ctx));
				ForegroundColorSpan fcs=new ForegroundColorSpan(Color.WHITE);
				ss.setSpan(bcs, hig.start, hig.end, 33);
				ss.setSpan(fcs, hig.start, hig.end, 33);
			}
			sb.append(ss);

			return sb;
		}

		private class ViewHolder {
			public TextView title;
			public TextView subtitle;

			public ViewHolder(View v) {
				title = (TextView) v.findViewById(android.R.id.text1);
				subtitle = (TextView) v.findViewById(android.R.id.text2);
				title.setTextAppearance(v.getContext(), android.R.style.TextAppearance_Large);
				subtitle.setTextAppearance(v.getContext(), android.R.style.TextAppearance_Small);
				title.setTextSize(15);
				subtitle.setTextSize(14);
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public TwoLineItem getItem(int p1) {
			return list.get(p1);
		}

		@Override
		public long getItemId(int p1) {
			return p1;
		}

	}	

}
