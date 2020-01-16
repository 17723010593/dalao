package com.s1243808733.permission;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.s1243808733.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PermissionAdapter extends BaseAdapter {

	private Context context;

	private boolean selectionMode;

	private List<Permission> list=new ArrayList<>();

	private List<Permission> sourceList=new ArrayList<>();

	private List<String> selectionList=new ArrayList<>();

	private OnSelectionListener onSelectionListener;

	private OnListDataChangeListener onListDataChangeListener;

	public void setList(List<Permission> list) {
		this.list = list;
	}

	public List<Permission> getList() {
		return list;
	}

	public void setSourceList(List<Permission> sourceList) {
		this.sourceList = sourceList;
	}

	public List<Permission> getSourceList() {
        List<Permission> l=new ArrayList<>();
        for (Permission p:sourceList) {
            if (!(p instanceof CommonItem)) {
                l.add(p);
            }
        }
		return l;
	}

	public void setSelectionList(List<String> selectionList) {
		this.selectionList = selectionList;
	}


	public List<String> getSelectionList() {
		return selectionList;
	}

	public List<Permission> getSelectionPermission() {
		List<Permission> list=new ArrayList<>();
		for (String permission:getSelectionList()) {
			list.add(new Permission(permission));
		}
		return list;
	}

	public interface OnSelectionListener {
		void onSelection(List<Permission> list, int selectedCount) 
        void onCancelSelection(List<Permission> list, int selectedCount) 

	}

	public interface OnListDataChangeListener {
		List<Permission> onListDataChange(List<Permission> list) 
	}

	public void setOnSelectionListener(OnSelectionListener listener) {
		this.onSelectionListener = listener;
	}

	public void setOnListDataChangeListener(OnListDataChangeListener onListDataChangeListener) {
		this.onListDataChangeListener = onListDataChangeListener;
	}

	public PermissionAdapter(Context context) {
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ItemView(context);
		}
		ItemView itemView = (ItemView)convertView;
		ViewHolder holder =itemView.holder;

		Permission item=getItem(position);

		String positionStr=(position + 1) + ". ";

		if (item instanceof CommonItem) {
			holder.setSelectionMode(false);
			CommonItem extra=(CommonItem)item;
			holder.title.setText(extra.getTitle());
			if (extra.getSubtitle() == null) {
				holder.subtitle.setVisibility(View.GONE);
			} else {
				holder.subtitle.setVisibility(View.VISIBLE);
				holder.subtitle.setText(extra.getSubtitle());
			}
			//itemView.setClickable(true);
		} else {
			//itemView.setClickable(false);
			Permission.Highlight hig_title=item.highlight.title;
			Permission.Highlight hig_subtitle=item.highlight.subtitle;

			holder.setSelection(isSelection(item.getPermission()));
			holder.setSelectionMode(isSelectionMode());

			holder.title.setText(new SpannableStringBuilder(positionStr)
								 .append(getHighlightText(item.getName(), hig_title)));

			holder.subtitle.setVisibility(View.VISIBLE);
			holder.subtitle.setText(getHighlightText(item.getPermission(), hig_subtitle));
		}
		return convertView;
	}

	private SpannableStringBuilder getHighlightText(String text, Permission.Highlight hig) {
		SpannableStringBuilder sb=new SpannableStringBuilder();
		SpannableString ss=new SpannableString(text);

		if (hig.start >= 0)   {
			BackgroundColorSpan bcs=new BackgroundColorSpan(Utils.getColorAccent(context));
			ForegroundColorSpan fcs=new ForegroundColorSpan(Color.WHITE);
			ss.setSpan(bcs, hig.start, hig.end, 33);
			ss.setSpan(fcs, hig.start, hig.end, 33);
		}
		sb.append(ss);

		return sb;
	}

	public boolean isSelection(String permission) {
        List<String> l=getSelectionList();
		for (String p:l) {
			if (p.equals(permission)) {
				return true;
			}
		}
		return false;
	}

	public void setSelection(int position, boolean selection) {
		setSelection(getItem(position).getPermission(), selection);
	}

	public void setSelection(String permission, boolean selection) {
		setSelection(new String[]{permission}, selection);
	}

	public void setSelection(String[] permissionArray, boolean selection) {
        List<Permission> selectionlist=new ArrayList<>();
        List<Permission> cancelselectionlist=new ArrayList<>();

        for (String permission:permissionArray) {
			if (selection) {
				if (!isSelection(permission)) {
					getSelectionList().add(permission);
                    selectionlist.add(new Permission(permission));
				}
			} else {
				List<String> list=getSelectionList();
				for (int i=0;i < list.size();i++) {
					if (list.get(i).equals(permission)) {
                        cancelselectionlist.add(new Permission(list.get(i)));
						list.remove(i);
                        break;
					}
				}
			}
		}
		if (onSelectionListener != null) {
            if (selection)
                onSelectionListener.onSelection(selectionlist, getSelectionList().size());
            else
                onSelectionListener.onCancelSelection(cancelselectionlist, getSelectionList().size());
		}
	}

	public void setSelectionMode(boolean selectionMode) {
		if (this.selectionMode != selectionMode) {
			selectionList.clear();
		}
		this.selectionMode = selectionMode;
	}

	public boolean isSelectionMode() {
		return selectionMode;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Permission getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	private boolean onListDataChange() {
		if (onListDataChangeListener != null) {
			List<Permission> result=onListDataChangeListener.onListDataChange(list);
			if (result != null) {
				setList(result);
				return true;
			}
		}
		return false;
	}

	@Override
	public void notifyDataSetChanged() {
		onListDataChange();
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		if (onListDataChange()) {
			super.notifyDataSetChanged();
		} else {
			super.notifyDataSetInvalidated();
		}
	}

	public class ViewHolder {
		public CheckBox cb;
		public TextView title;
		public TextView subtitle;

		public void setSelection(boolean selection) {
			cb.setChecked(selection);
		}

		public boolean invertSetSelection() {
			setSelection(!cb.isChecked());
			return cb.isChecked();
		}

		public void setSelectionMode(boolean checkeMode) {
			View parent=(View)cb.getParent();
			parent.setVisibility(checkeMode ?View.VISIBLE: View.GONE);
		}

	}

	public class ItemView extends LinearLayout {

		public ViewHolder holder;

		public ItemView(Context context) {
			super(context);
			CheckBox cb=new CheckBox(context);
			cb.setClickable(false);
			cb.setFocusable(false);
			cb.setFocusableInTouchMode(false);

			TextView title=new TextView(context);
			title.setTextAppearance(context, android.R.style.TextAppearance_Large);
			title.setTextSize(15);

			TextView subtitle = new TextView(context);
			subtitle.setTextAppearance(context, android.R.style.TextAppearance_Small);
			subtitle.setTextSize(14);

			LinearLayout cbView=new LinearLayout(context);
			cbView.addView(cb);
			cbView.setPadding(0, 0, PermissionUtils.dp2px(16), 0);

			LinearLayout titleView=new LinearLayout(context);
			titleView.setOrientation(LinearLayout.VERTICAL);
			titleView.addView(title);
			titleView.addView(subtitle);

			setGravity(Gravity.CENTER_VERTICAL);
			setPadding(PermissionUtils.dp2px(16), PermissionUtils.dp2px(13), PermissionUtils.dp2px(16), PermissionUtils.dp2px(13));
			addView(cbView);
			addView(titleView);

			holder = new ViewHolder();
			holder.cb = cb;
			holder.title = title;
			holder.subtitle = subtitle;

		}


	}

}

