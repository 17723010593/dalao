package com.s1243808733.androidr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class FragmentThemePreview extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return getContentView();
	}

	private LinearLayout getContentView() {
		Activity ctx=getActivity();
		LinearLayout ll = new LinearLayout(ctx);
		ll.setOrientation(ll.VERTICAL);

		EditText et=new EditText(ctx);
		et.setHint(android.R.string.untitled);
		ll.addView(et, -1, -2);

		Button bt=new Button(ctx);
		bt.setText(AlertDialog.class.getSimpleName());
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog dialog=new AlertDialog.Builder(getActivity())
				.setTitle(((Button)view).getText())
				.setMessage(AlertDialog.class.getCanonicalName())
				.setPositiveButton(android.R.string.ok, null)
				.setNegativeButton(android.R.string.cancel, null)
				.create();
				dialog.show();
			}
		});
		ll.addView(bt, -1, -2);

		ll.addView(new SeekBar(ctx),-1, -2);

		return ll;
	}

}
