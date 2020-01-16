package com.s1243808733.androidr;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

public abstract class BaseRFragment extends Fragment {

	private BaseRBaseAdapter adapter;

	public BaseRBaseAdapter getAdapter() {
		return adapter;
	}

	public class ViewHolder {
		public EditText mSearchView;
		public ListView mListView;
	}

    public void copyToClipboard(String data) {
        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(data);
    }

	public abstract Class<?> getClazz();

	public BaseRBaseAdapter onCreateAdapter(Bundle savedInstanceState)throws IllegalArgumentException, IllegalAccessException {
		return null;
	};

	public void showDialog(final BaseRItem item) {
		final String name=item.name;
		final int id=item.id;
		Class<?> clazz=getClazz();
		final String code_xml;

		String id_hex="0x" + Integer.toHexString(id);
		final String code_java= clazz.getCanonicalName() + "." + name;

		code_xml = "@" + clazz.getPackage().getName() + ":" + clazz.getSimpleName() + "/" + name;

		StringBuffer sb=new StringBuffer();
		sb.append("名称：").append(name);
		sb.append("\n\n资源ID：");
		sb.append("\n10进制：").append(id);
		sb.append("\n16进制：").append(id_hex);
		sb.append("\n\n引用：");
		sb.append("\nJava：").append(code_java);

		if (!FragmentTag.TAG_TEXTAPPEARANCE.equals(getTag())) {
			sb.append("\nXML：").append(code_xml);
		}

		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
		.setTitle("详细信息")
		.setMessage(sb.toString())
		.setPositiveButton(android.R.string.ok, null)
		.setNegativeButton(android.R.string.cancel, null)
		.setNeutralButton("复制", null);

		if (FragmentTag.TAG_DRAWABLE.equals(getTag())) {
			builder.setIcon(item.id);
		}

		if (FragmentTag.TAG_THENCE.equals(getTag())) {
			builder.setNegativeButton("预览", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(getActivity(), AndroidRActivity.class)
								  .putExtra("themeid", item.id)
								  .putExtra("tag", FragmentTag.TAG_THEME_PREVIEW)
								  .putExtra("title", name));

				}
			});
		}

		AlertDialog dialog=builder.create();
		dialog.show();

		Button neutral=dialog.getButton(dialog.BUTTON_NEUTRAL);
		neutral.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				PopupMenu popup=new PopupMenu(getActivity(), view);
				Menu menu=popup.getMenu();
				menu.add("复制名称").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						copyToClipboard(name);
						return false;
					}
				});
				menu.add("复制ID").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						copyToClipboard(id + "");
						return false;
					}
				});
				menu.add("复制Java引用").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						copyToClipboard(code_java);
						return false;
					}
				});

				if (!FragmentTag.TAG_TEXTAPPEARANCE.equals(getTag())) {
					menu.add("复制XML引用").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

						@Override
						public boolean onMenuItemClick(MenuItem p1) {
							copyToClipboard(code_xml);
							return false;
						}
					});
				}

				popup.show();
			}
		});

		TextView messageView=(TextView) dialog.findViewById(android.R.id.message);
		if (messageView != null) {
			messageView.setTextIsSelectable(true);
		}

	}

	public void onViewCreated(ViewHolder holder, Bundle savedInstanceState) {
		if (adapter == null)return;
		holder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
				final BaseRItem item=adapter.getItem(position);
				showDialog(item);
			}
		});


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout ll=new LinearLayout(getActivity());
		ll.setOrientation(ll.VERTICAL);
		ll.addView(new EditText(getActivity()), -1, -2);
		ll.addView(new ListView(getActivity()), -1, -1);
		return ll;
	}

	@Override
	public final void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LinearLayout ll=(LinearLayout)view;
		ViewHolder holder=new ViewHolder();

		holder.mSearchView = (EditText)ll.getChildAt(0);
		holder.mListView = (ListView)ll.getChildAt(1);

		holder.mSearchView.setHint("输入搜索内容...");
		holder.mSearchView.setSingleLine(true);
		holder.mListView.setFastScrollEnabled(true);

		try {
			adapter = onCreateAdapter(savedInstanceState);
			if (adapter != null) {
				holder.mListView.setAdapter(adapter);
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
		} catch (Exception e) {} 

		onViewCreated(holder, savedInstanceState);

	}

    public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
