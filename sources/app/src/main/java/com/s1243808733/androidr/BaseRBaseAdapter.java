package com.s1243808733.androidr;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import com.s1243808733.util.Utils;

public abstract class BaseRBaseAdapter extends BaseAdapter {
	public boolean searching;
	public void search(String search) {
		searching = true;
		List<BaseRItem> list_backup=getListBackup();
		if (search == null || search.trim().length() == 0) {
			setList(list_backup);
			searching = false;
		} else {
			List<BaseRItem> searchlist=new ArrayList<>();
			search = search.toLowerCase(Locale.ENGLISH);
			for (BaseRItem bean:list_backup) {
				int in=bean.name.toLowerCase(Locale.ENGLISH).indexOf(search);
				if (in != -1) {
					bean.hig_name.start = in;
					bean.hig_name.end = in + search.length();
					searchlist.add(bean);
				}
			}
			setList(searchlist);
		}
		notifyDataSetChanged();
	}

	public SpannableStringBuilder getHighlightText(Context ctx,String text, BaseRItem.Highlight hig) {
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


	public abstract void setList(List<BaseRItem> list);
	public abstract List<BaseRItem> getListBackup();
	public abstract BaseRItem getItem(int position);


}
