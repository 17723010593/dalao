package com.s1243808733.translate.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.s1243808733.translate.TUtil;
import com.s1243808733.util.Utils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WebTranslateActivity extends Activity {

    public static final String default_api="https://dictweb.translator.qq.com/eduTranslate?sentence=%s&channel=tencent";

    public static final String pref_key="api_web_translation";

    private WebView webView;

    private LinearLayout progressView;

    private WebTranslateActivity thisActivity;


    public static void initDefaultapi() {
        getApi();
    }

    public static String getApi() {
        SharedPreferences sp=Utils.getSp();
        String api = sp.getString(pref_key, "");
        if (api.trim().length() == 0) {
            sp.edit().putString(pref_key, default_api).commit();
            return default_api;
        }
        return api;
    }

    public static void translate(Activity activity , CharSequence content) {
        if (content == null || content.toString().trim().length() == 0) {
            Utils.toast("请先选择一段内容");
        } else {
            Intent intent=new Intent(activity, WebTranslateActivity.class);
            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, content);
			activity.startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            thisActivity = this;
            initView();
            initWindows();

            Intent intent=getIntent();
            CharSequence content = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            webView.loadUrl(String.format(getApi(), TUtil.wordSegmentation(content.toString().replace(" _ ", " "))));
        } catch (Throwable e) {
            Utils.toast(e,true);
            finish();
        }
    }

    private void initView() {
        RelativeLayout root=new RelativeLayout(this);

        root.setBackgroundColor(Color.WHITE);
        root.addView(new View(this), -1, dp2px(64));

        progressView = new LinearLayout(this);
        int pi=dp2px(24);
        progressView.setPadding(pi, pi, pi, pi);
        progressView.setGravity(Gravity.CENTER_VERTICAL);

        TextView title=new TextView(this);
        title.setText("正在翻译中...");
        title.setTextSize(15);
        title.setPadding(dp2px(16), 0, 0, 0);

        progressView.addView(new ProgressBar(this));
        progressView.addView(title);

        webView = new WebView(this);

        root.addView(progressView);
        root.addView(webView);

        setContentView(root);

        initWebSetting(webView);

    }

    private void initWindows() {
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.width = getWindowManager().getDefaultDisplay().getWidth(); 
        wl.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wl);
    }

    private void initWebSetting(WebView webView) {
        WebSettings setting=webView.getSettings();

        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setSupportZoom(true);
        setting.setDisplayZoomControls(false);
        setting.setBuiltInZoomControls(true);
        setting.setTextSize(WebSettings.TextSize.SMALLER);
        setting.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebChromeClient(new WebChromeClient(){


            });

        webView.setWebViewClient(new WebViewClient(){

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                    if (url.startsWith("http://") 
                        || url.startsWith("https://")
                        || url.startsWith("file://")) {
                        view.loadUrl(url);
                        return true;
                    } else {
                        PackageManager pm=getPackageManager();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        List<ResolveInfo> list= pm.queryIntentActivities(intent, 0);
                        if (list != null && list.size() > 0) {
                            if (list.size() == 1) {
                                String title=null;
                                try {
                                    title = String.format("网页“%s”请求打开其它应用", new URL(url).getHost());
                                } catch (MalformedURLException e) {}
                                ResolveInfo info=list.get(0);
                                AlertDialog dialog=new AlertDialog.Builder(thisActivity)
                                    .setTitle(title)
                                    .setMessage(String.format("将打开“%s”", info.activityInfo.loadLabel(pm)))
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create();
                                dialog.show();
                            } else {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(Intent.createChooser(intent, "Choose"));
                            }
                        }

                    }
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressView.setVisibility(View.GONE);
                }

            });

        webView.setDownloadListener(new DownloadListener(){

                @Override
                public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    String title=null;
                    try {
                        title = String.format("网页“%s”请求下载", new URL(url).getHost());
                    } catch (MalformedURLException e) {}

                    AlertDialog dialog=new AlertDialog.Builder(WebTranslateActivity.this)
                        .setTitle(title)
                        .setMessage("大小：" + Formatter.formatFileSize(thisActivity, contentLength)
                                    + "\nmimetype=" + mimetype
                                    + "\nuserAgent=" + userAgent
                                    + "\ncontentDisposition=" + contentDisposition
                                    + "\nurl=" + url)
                        .setPositiveButton("浏览器下载", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .create();
                    dialog.show();
                }
            });


    }


    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        webView.stopLoading();
        webView.destroy();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}

