package com.s1243808733.aide.highlight;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.s1243808733.aide.highlight.color.Colors;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.app.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class HighlightActivity extends BaseActivity
implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private List<Item> list=new ArrayList<>();

    private HighlightAdapter adapter;

    private boolean isDataChangeed=false;

    private String[] item={"亮主题高亮配置","暗主题高亮配置"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = new ListView(this);
        setContentView(mListView);
        mListView.setOnItemClickListener(this);

        initActionBar();
    }

    private void toggleList(boolean isLight) {
        list = new ArrayList<>();
        for (Colors color:HighlightUtils.getHighlightColor().values()) {
            list.add(new Item(color.label, color.name, isLight ?color.lightColor: color.darkColor));
        }
        adapter = new HighlightAdapter(this, list);
        adapter.setIsLight(isLight);
        mListView.setAdapter(adapter);
    }

    private void initActionBar() {
        ActionBar actionBar=getActionBar();
        actionBar.setTitle(null);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, item);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, new ActionBar.OnNavigationListener(){

			@Override
			public boolean onNavigationItemSelected(int position, long p2) {
				toggleList(position == 0);
				return false;
			}
		});
        actionBar.setSelectedNavigationItem(AIDEUtils.isLightTheme() ?0: 1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
        final Item item=adapter.getItem(position);

        ColorPickerDialog mDialog = new ColorPickerDialog(this, Color.parseColor(item.getColor(adapter.isLight())));
        mDialog.setTitle(item.getTitle());
        mDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener(){

			@Override
			public void onColorChanged(int color) {
				HighlightUtils.getHSp().edit().putString(item.getSpKey(adapter.isLight()), ColorPickerPreference.convertToARGB(color)).commit();
				adapter.notifyDataSetChanged();
				isDataChangeed = true;
			}
		});
        mDialog.setButton(mDialog.BUTTON_NEUTRAL, "默认", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				HighlightUtils.getHSp().edit().putString(item.getSpKey(adapter.isLight()), item.getDefaultColor()).commit();
				adapter.notifyDataSetChanged();
				isDataChangeed = true;

			}
		});
        mDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("还原").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem p1) {

				new AlertDialog.Builder(HighlightActivity.this)
				.setItems(new String[]{p1.getTitle().toString(),"还原所有"}, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp=HighlightUtils.getHSp();
						SharedPreferences.Editor edit=sp.edit();

						if (which == 0) {
							for (Item item:list) {
								edit.putString(item.getSpKey(adapter.isLight()), item.getDefaultColor());
							}
						} else {
							edit.clear();
							for (Colors color:HighlightUtils.getHighlightColor().values()) {
								edit.putString(HighlightUtils.getSpKey(color.name, true), color.lightColor);
								edit.putString(HighlightUtils.getSpKey(color.name, false), color.darkColor);
							}
						}

						edit.commit();
						adapter.notifyDataSetChanged();

						isDataChangeed = true;
						AIDEUtils.toast("已还原至默认");

					}
				})
				.create().show();

				return false;
			}
		});
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDataChangeed) {
            AIDEUtils.getAIDEEditorPager().Zo();
            AIDEUtils.setMainBackground();
        }
    }

}
