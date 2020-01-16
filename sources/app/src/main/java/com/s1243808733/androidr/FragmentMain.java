package com.s1243808733.androidr;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment {
	private FragmentAdapter fragmentAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return new ListView(getActivity());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ListView mListView=(ListView)view;

		fragmentAdapter = new FragmentAdapter();
		fragmentAdapter.add("String", FragmentTag.TAG_STRING);
		fragmentAdapter.add("Color", FragmentTag.TAG_COLOR);
		fragmentAdapter.add("Theme", FragmentTag.TAG_THENCE);
		fragmentAdapter.add("Drawable", FragmentTag.TAG_DRAWABLE);
		fragmentAdapter.add("TextAppearance", FragmentTag.TAG_TEXTAPPEARANCE);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1); 
		for (int i=0;i < fragmentAdapter.getCount();i++) {
			adapter.add(fragmentAdapter.getTitle(i));
		}

		mListView.setAdapter(adapter); 
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
				startActivity(new Intent(getActivity(), AndroidRActivity.class)
											.putExtra("tag", fragmentAdapter.getTag(position))
											.putExtra("title",fragmentAdapter.getTitle(position)));
			}
		});

	}

	private class FragmentAdapter {

		private List<String> fragmentTitleList=new ArrayList<>();

		private List<String> fragmentTagList=new ArrayList<>();

		public int getCount() {
			return fragmentTitleList.size();
		}

		public void add(String title, String tag) {
			fragmentTitleList.add(title);
			fragmentTagList.add(tag);
		}

		public String getTitle(int position) {
			return fragmentTitleList.get(position);
		}

		public String getTag(int position) {
			return fragmentTagList.get(position);
		}

	}



}
