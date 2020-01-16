package com.s1243808733.androidr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.s1243808733.app.base.AbsActivity;

public class AndroidRActivity extends AbsActivity {

    @Override
    public boolean isApplyTheme() {
		return !getIntent().hasExtra("themeid");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		Intent intent=getIntent();
		if (intent.hasExtra("themeid")) {
			setTheme(intent.getIntExtra("themeid", 0));
		}
        super.onCreate(savedInstanceState);
		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			if (intent.hasExtra("title")) {
				getActionBar().setTitle(intent.getStringExtra("title"));
			} else {
				getActionBar().setTitle("系统资源查看");
			}
		}

		FragmentManager fm=getFragmentManager();
		FragmentTransaction bt=fm.beginTransaction();

		Fragment frag = null;
		String tag = null;
		tag = intent.getStringExtra("tag");

		if (FragmentTag.TAG_STRING.equals(tag)) {
			frag = new FragmentString();
		} else if (FragmentTag.TAG_COLOR.equals(tag)) {
			frag = new FragmentColor();
		} else if (FragmentTag.TAG_DRAWABLE.equals(tag)) {
			frag = new FragmentDrawable();
		} else if (FragmentTag.TAG_THENCE.equals(tag)) {
			frag = new FragmentTheme();
		} else if (FragmentTag.TAG_THEME_PREVIEW.equals(tag)) {
			frag = new FragmentThemePreview();
		} else if (FragmentTag.TAG_TEXTAPPEARANCE.equals(tag)) {
			frag = new FragmentTextAppearance();
		} 

		if (frag == null) {
			frag = new FragmentMain();
			tag = FragmentTag.TAG_MAIN;
		} 

		Fragment fg=fm.findFragmentByTag(tag);
		if (fg != null) {
			bt.remove(fg);
		}

		bt.replace(android.R.id.content, frag, tag).commit();
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}


}
