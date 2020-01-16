package com.s1243808733.aide.highlight;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.s1243808733.util.Utils;
import com.s1243808733.view.ColorBackgroundTextView;
import java.util.ArrayList;
import java.util.List;

public class HighlightAdapter extends BaseAdapter {

    private Context context;

    private List<Item> list=new ArrayList<>();

    private boolean isLight;

    public HighlightAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    public void setIsLight(boolean isLight) {
        this.isLight = isLight;
    }

    public boolean isLight() {
        return isLight;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = getItemView();
            holder = new ViewHolder(view);
			view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Item item=getItem(position);

        holder.title.setText(item.getTitle());

        try {
			String color=item.getColor(isLight);
            int colorInt=Color.parseColor(color);
			holder.subtitle.setColor(colorInt);
        } catch (Throwable e) {
        }

        return view;
    }


    private class ViewHolder {
        public View rootView;
        public TextView title;
		public ColorBackgroundTextView subtitle;

        public ViewHolder(View view) {
            this.rootView = view;
            this.title = (TextView) view.findViewById(android.R.id.text1);
            this.subtitle = (ColorBackgroundTextView) view.findViewById(android.R.id.text2);
        }

    }


    public View getItemView() {

        LinearLayout view=new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setGravity(Gravity.CENTER_VERTICAL);
		view.setPadding(Utils.dp2px(16), Utils.dp2px(14), Utils.dp2px(16), Utils.dp2px(14));
		
        TextView title=new TextView(context);
        title.setId(android.R.id.text1);
        title.setTextAppearance(context, android.R.style.TextAppearance_Large);
        title.setTextSize(16);
		title.setPadding(0, 0, 0, Utils.dp2px(5));
		
        ColorBackgroundTextView subtitle=new ColorBackgroundTextView(context);
		subtitle.setId(android.R.id.text2);
		subtitle.setTextSize(14);
		
		view.addView(title);
        view.addView(subtitle, -1, -2);
        
        return view;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Item getItem(int p1) {
        return list.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        return p1;
    }


}
