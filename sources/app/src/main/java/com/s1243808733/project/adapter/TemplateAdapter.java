package com.s1243808733.project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.s1243808733.project.PUtils;
import com.s1243808733.project.jsonbean.Category;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Category> list=new ArrayList<>();

	private Map<Object,Bitmap> iconMap=new HashMap<>();

	public TemplateAdapter(Context context) {
		this.context = context;
	}

	public TemplateAdapter(Context context, List<Category> list) {
		this.context = context;
		this.list = list;
	}

	public TemplateAdapter add(Category projects) {
		getList().add(projects);
		return this;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TemplateView(context);			
		}
		TemplateView.ViewHolder holder=((TemplateView)convertView).holder;
		Category item=getGroup(groupPosition);

		holder.setIcon(getIcon(item.getIcon()));

		holder.setTitle(item.getTitle());
		holder.setSubtitle(item.getDescribes());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TemplateView(context);			
		}
		TemplateView.ViewHolder holder=((TemplateView)convertView).holder;
		Category.Templates item=getChild(groupPosition, childPosition);

		holder.setIcon(getIcon(item.getIcon()));

		holder.setTitle(item.getTitle());
		holder.setSubtitle(item.getDescribes());

		return convertView;
	}

	private Bitmap getIcon(String file) {
		Bitmap icon=iconMap.get(file);
		if (icon == null) {
			icon = PUtils.getIcon(context, file);
			iconMap.put(file, icon);
		}
		return icon;
	}

	public void setList(List<Category> list) {
		this.list = list;
	}

	public List<Category> getList() {
		return list;
	}

	@Override
	public int getGroupCount() {
		return getList().size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return getGroup(groupPosition).getTemplates().size();
	}

	@Override
	public Category getGroup(int groupPosition) {
		return getList().get(groupPosition);
	}

	@Override
	public Category.Templates getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition).getTemplates().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int p1, int p2) {
		return true;
	}

}
