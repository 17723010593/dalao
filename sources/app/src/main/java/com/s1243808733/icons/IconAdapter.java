package com.s1243808733.icons;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.caverock.androidsvg.SVGImageView;
import com.github.megatronking.svg.generator.utils.Color;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IconAdapter extends BaseAdapter {

	private Context context;

	private List<Icon> list=new ArrayList<>();
	private List<Icon> backup_list=new ArrayList<>();

	private boolean searching;

	public IconAdapter(Context context, List<Icon> list) {
		this.context = context;
		this.list = list;
		this.backup_list = list;
	}

	public void search(String search) {
		if (TextUtils.isEmpty(search)) {
			this.list = backup_list;
			searching = false;
			notifyDataSetChanged();
		} else {
			List<Icon> searchlist=new ArrayList<>();

			for (Icon item:backup_list) {
				String name=item.getFileName().toLowerCase(Locale.ENGLISH);
				int index=name.indexOf(search);
				if (index >= 0) {
					item.hig.title.start = index;
					item.hig.title.end = index + search.length();
					searchlist.add(item);
				}
			}
			this.list = searchlist;
			searching = true;
			if (searchlist.size() == 0) {
				notifyDataSetInvalidated();
			} else {
				notifyDataSetChanged();
			}


		}
	}

	@Override
    public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = getItemView();
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}
		Icon item=getItem(position);
		holder.title.setText(getHighlightText(context, item.getFileName(), item.hig.title));

		try {
			holder.icon.setSVG(item.getSvg());
			holder.icon.setColorFilter(Color.WHITE);
		} catch (Exception e) {}

		return view;
	}

	public SpannableStringBuilder getHighlightText(Context ctx, String text, Icon.Highlight hig) {
		SpannableStringBuilder sb=new SpannableStringBuilder();
		SpannableString ss=new SpannableString(text);
		if (searching && hig.start >= 0)   {
			BackgroundColorSpan bcs=new BackgroundColorSpan(Utils.getColorAccent(ctx));
			ForegroundColorSpan fcs=new ForegroundColorSpan(Color.WHITE);
			ss.setSpan(bcs, hig.start, hig.end, 33);
			ss.setSpan(fcs, hig.start, hig.end, 33);
		}
		sb.append(ss);

		return sb;
	}

	public class ViewHolder {

		public SVGImageView icon;

		public TextView title;

		public ViewHolder(View view) {
			this.icon = (SVGImageView) view.findViewById(android.R.id.icon);
			this.title = (TextView) view.findViewById(android.R.id.title);
		}

	}

	private View getItemView() {
		LinearLayout root=new LinearLayout(context);
		int padding=AIDEUtils.dp2px(16);
		root.setPadding(padding, 0, padding, 0);
		root.setGravity(Gravity.CENTER);
		root.setOrientation(LinearLayout.VERTICAL);

		SVGImageView icon=new SVGImageView(context);
		icon.setId(android.R.id.icon);

		TextView title = new TextView(context);
		title.setId(android.R.id.title);
		title.setGravity(Gravity.CENTER);
		title.setPadding(0, AIDEUtils.dp2px(10), 0, 0);
		title.setMaxLines(2);
		title.setEllipsize(TextUtils.TruncateAt.END);
		title.setTextSize(13);

		root.addView(icon, Utils.dp2px(32), Utils.dp2px(32));
		root.addView(title);

		LinearLayout view=new LinearLayout(context);
		view.addView(root, -1, AIDEUtils.dp2px(110));
		return view;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Icon getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


}
