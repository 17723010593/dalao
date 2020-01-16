package com.s1243808733.util;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.aide.ui.MainActivity;

public class CrashUtils {

    private static String FORMAT="%1$s (%2$d)";

    public static void init(final Application app) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    PackageInfo packageInfo=null;
                    try {
                        packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {}

                    StringBuffer sb=new StringBuffer();
                    sb.append("BRAND=" + Build.BRAND);
                    sb.append("\nMODEL=" + Build.MODEL);
                    sb.append("\nSDK Level=" + String.format(FORMAT, Build.VERSION.RELEASE, Build.VERSION.SDK_INT));

                    if (packageInfo != null) {
                        sb.append("\nVersion=" + String.format(FORMAT, packageInfo.versionName, packageInfo.versionCode));
                    }

                    sb.append("\n\n" + Utils.getStackTrace(throwable));

                    app.startActivity(new Intent(app, ExActivity.class)
                                      .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                      .putExtra(Intent.EXTRA_TEXT, sb.toString()));

                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
    }

    public static class ExActivity extends Activity {

        private String log;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTheme(android.R.style.Theme_DeviceDefault);
            getActionBar().setTitle("软件崩溃");

            ScrollView mScrollView=new ScrollView(this);
            HorizontalScrollView mHorizontalScrollView = new HorizontalScrollView(this);
            TextView mMessage = new TextView(this);

            mHorizontalScrollView.addView(mMessage);
            mScrollView.addView(mHorizontalScrollView, -1, -1);

            setContentView(mScrollView);

            log = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            mMessage.setText(log);
            mMessage.setTextIsSelectable(true);

            int padding=Utils.dp2px(16);
            mMessage.setPadding(padding, padding, padding, padding);

            mScrollView.setFillViewport(true);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            menu.add("复制").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                    @Override
                    public boolean onMenuItemClick(MenuItem p1) {
                        Utils.copyToClipboard(log);
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("重启").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                    @Override
                    public boolean onMenuItemClick(MenuItem p1) {
                        startActivity(new Intent(ExActivity.this, MainActivity.class));
                        finish();
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return super.onCreateOptionsMenu(menu);
        }

    }

}
