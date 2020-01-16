package com.s1243808733.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.aide.ui.MainActivity;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.ResourceUtils;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import org.xutils.x;

public class Utils {
	private static Toast toast;

    private static Application app;

	private static MainActivity mainActivity;

	private static final String ASSETS_DATA_DIR="_data";

	private static final String SDCARD_DATA_DIR="/sdcard/.aide/_data";

	public static void setApp(Application app) {
		Utils.app = app;
	}

    public static Application getApp() {
        return app;
    }

	public static void setMainActivity(MainActivity mainActivity) {
		Utils.mainActivity = mainActivity;
	}

	public static MainActivity getMainActivity() {
	    return mainActivity;
    }

    public static void initX() {
        x.Ext.init(getApp());
        x.Ext.setDebug(false);
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
    }

    public static View getRootView(View v) {
        View vp;
        while ((vp = (View) v.getParent()) != null) {
            v = vp;
        }
        return vp;
    }

    public static void setSystemStatusBarLight(Activity activity, boolean isLight) {
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = activity.getWindow().getDecorView();
            if (isLight) {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 8192);
            } else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & -8193);
            }
        }
    }

	public static int getColorAccent(Context ctx) {
		if (Build.VERSION.SDK_INT >= 21) {
			TypedValue typedValue = new  TypedValue();
			ctx.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
			return typedValue.data;
		}
		return 0;
	}

    public static Spanned fromHtml(String msg) {
        return Build.VERSION.SDK_INT >= 24 ? Html.fromHtml(msg, 0): Html.fromHtml(msg);
    }

    public static void setAlertDialogMovementMethod(AlertDialog dialog) {
        TextView tv=(TextView)dialog.findViewById(android.R.id.message);
        if (tv != null)tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setMessageIsSelectable(AlertDialog dialog) {
        TextView messageView=(TextView)dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextIsSelectable(true);
        }
    }


	public static String getFileNamePrefix(String fileName) {
        int last=fileName.lastIndexOf(".");
        if (last >= 0) {
            return fileName.substring(0, last);
        }
        return null;
    }

    public static String getFileNameSuffix(String fileName) {
        int last=fileName.lastIndexOf(".");
        if (last >= 0) {
            return fileName.substring(last);
        }
        return null;
    }


	public static int getColorFromResources(int resID) {
		return getApp().getResources().getColor(resID);
	}

    public static void setOptionalIconsVisible(Menu menu, boolean visible) {
        try {
            // menu MenuBuilder
            ReflectUtils.reflect(menu).method("setOptionalIconsVisible", visible);
        } catch (Throwable e) {
        }
    }

	public static String toUpperCaseFirst(String data) {
        if (data == null || data.length() == 0)return data;
        char[] charArray=data.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return String.valueOf(charArray);
    }

    public static <T extends Object> List<T> toList(T[] array) {
		return Arrays.asList(array);
    }

    public static boolean hasAdded(List<String> list, String item) {
        for (String i:list) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

	public static void startActivity(Context context, Class<?> clazz) {
		startActivity(context, clazz, false);
	}

	public static void startActivity(Context context, Class<?> clazz, boolean isNewTask) {
		Intent intent=new Intent(context, clazz);
		if (isNewTask
			|| (Build.VERSION.SDK_INT < 24 && ! (context instanceof Activity))) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		startActivity(context, intent);
	}

	public static void startActivity(Context context, Intent intent) {
		try {
			context.startActivity(intent);
		} catch (Throwable e) {
			Toasty.error(e.getMessage());
		}
	}

	public static Toast toast(Object obj) {
		return toast(obj.toString());
	}

	public static Toast toast(Throwable e) {
		return toast(e, false);
	}

	public static Toast toast(Throwable e, boolean isFull) {
		return toastError(isFull ?getStackTrace(e): e.getMessage());
	}

	public static Toast toast(String str) {
        if (toast != null) toast.cancel();
        toast = Toasty.toast(getApp(), str, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast toastError(String str) {
        if (toast != null) toast.cancel();
        toast = Toasty.error(str, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }


	public static File getSdDataFile() {
        return new File(SDCARD_DATA_DIR);
    }

    public static File getSdDataFile(String child) {
        return new File(getSdDataFile(), child);
    }

    public static File getSdConfigFile(String child) {
        return new File(getSdDataFile("config"), child);
    }

    /////////

    public static File getAssetsDataFile() {
        return new File(ASSETS_DATA_DIR);
    }

    public static File getAssetsDataFile(String child) {
        return new File(getAssetsDataFile(), child);
    }

    public static File getAssetsConfigFile(String child) {
        return new File(getAssetsDataFile("config"), child);
    }

    //////////

	public static void runOnUiThread(Activity act, Runnable run) {
        act.runOnUiThread(run);
    }

    public static SharedPreferences getSp() {
        return PreferenceManager.getDefaultSharedPreferences(getApp());
    }

    public static SharedPreferences getSp(String name) {
        return getApp().getSharedPreferences(name, 0);
    }

    public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void openUrl(String url) {
        startActivity(getApp(),
                      new Intent(Intent.ACTION_VIEW, Uri.parse(url))
                      .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                      );
    }
    public static void showUpdateLogDialog(Activity act) {
        String log=ResourceUtils.readAssets2String(getAssetsDataFile("update.txt"));
        final AlertDialog dialog=new AlertDialog.Builder(act)
		.setTitle("更新日志")
		.setMessage(log)
		.setPositiveButton("确定", null)
		.setNegativeButton("取消", null)
		.create();
        dialog.show(); 
    }

    public static void showExDialog(final Activity activity, final Throwable e) {
        showExDialog(activity, "发生错误", e);
    }

    public static void showExDialog(final Activity activity, String title, final Throwable e) {
        AlertDialog d=new AlertDialog.Builder(activity)
		.setTitle(title)
		.setMessage(e.getMessage())
		.setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dia, int which) {
				copyToClipboard(e.getMessage());
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.setNeutralButton("详情", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {

				AlertDialog d2=new AlertDialog.Builder(activity)
				.setTitle("错误详情")
				.setMessage(getStackTrace(e))
				.setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dia, int which) {
						copyToClipboard(getStackTrace(e));
					}
				})
				.setNegativeButton(getApp().getString(android.R.string.cancel), null)
				.create();
				d2.show();
				Utils.setMessageIsSelectable(d2);

			}
		})
		.create();
        d.show();
        Utils.setMessageIsSelectable(d);
    }


    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static void copyToClipboard(String data) {
        copyToClipboard(data, true);
    }

    public static void copyToClipboard(String data, boolean isToast) {
        ClipboardManager clipboard = (ClipboardManager)getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(data);
        if (isToast) {
            toast("已复制剪贴板");
        }
    }
    public static String subString(String str, String start, String end) {
        try {
            if (start != null && end != null) {
                return str.substring(str.indexOf(start) + start.length(), str.indexOf(end));
            } else if (start == null && end != null) {
                return str.substring(0, str.indexOf(end));
            } else if (start != null && end == null) {
                return str.substring(str.indexOf(start) + start.length(), str.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void showMsgDialog(Activity a, String t, String m) {
        AlertDialog dialog=new AlertDialog.Builder(a)
		.setTitle(t)
		.setMessage(m)
		.setPositiveButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		})
		.setNegativeButton("取消", null)
		.create();
        dialog.show();
        Utils.setMessageIsSelectable(dialog);

    }





}
