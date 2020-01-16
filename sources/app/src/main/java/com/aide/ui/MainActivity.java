package com.aide.ui;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.s1243808733.aide.util.MainInterface;
import com.s1243808733.app.base.BaseActivity;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MainInterface.onCreate(this, savedInstanceState);
	}

	protected void onCreate_SOURCE(Bundle savedInstanceState) {
		MainInterface.setContentView(0);
	}

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        MainInterface.onMenuOpened(this, featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		MainInterface.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		return MainInterface.onOptionsItemSelected(this, item);
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return MainInterface.onPrepareOptionsMenu(this, menu);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return MainInterface.onKeyDown(this, keyCode, event);
	}
	

}
