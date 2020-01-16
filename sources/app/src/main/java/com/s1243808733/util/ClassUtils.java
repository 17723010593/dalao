package com.s1243808733.util;
import abcd.ll;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtils {

	public static void showDialog(final Activity activity) {
		LinearLayout ll=new LinearLayout(activity);
		final EditText input=new EditText(activity);
		input.setText(Activity.class.getCanonicalName());
		input.setHint("类名");
		ll.addView(input, -1, -2);
		AlertDialog dialog=new AlertDialog.Builder(activity)
		.setTitle("指定类分析")
		.setView(ll)
		.setPositiveButton("确定", null)
		.setNegativeButton("取消", null)
		.create();
		dialog.show();
		dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				try {
					Class<?> clazz=Class.forName(input.getText().toString());
					final StringBuffer sb=new StringBuffer();

					sb.append("类路径：" + clazz.getCanonicalName());
					sb.append("\n字段数：" + ClassUtils.getDeclaredFieldCount(clazz));
					sb.append("\n字段数(包含父类)：" + ClassUtils.getFieldCount(clazz));
					sb.append("\n方法数：" + ClassUtils.getDeclaredMethodCount(clazz));
					sb.append("\n方法数(包含父类)：" + ClassUtils.getMethodCount(clazz));
					sb.append("\n\n所有父类：\n" + ClassUtils.getSuperClassName(clazz));
					AlertDialog dialog=new AlertDialog.Builder(activity)
					.setTitle(clazz.getSimpleName())
					.setMessage(sb.toString())
					.setPositiveButton("复制", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Utils.copyToClipboard(sb.toString());
						}
					})
					.setNegativeButton("取消", null)
					.create();
					dialog.show();
					Utils.setMessageIsSelectable(dialog);
				} catch (Throwable e) {
					Utils.showExDialog(activity, e);
				}
			}
		});
	}

    public static String getSuperClassName(Class<?> clazz) {
        StringBuffer sb=new StringBuffer();
        sb.append(clazz.getCanonicalName());
        while ((clazz = clazz.getSuperclass()) != null) {
            sb.append("\n");
            sb.append(clazz.getCanonicalName());
        }
        return sb.toString();
    }


    public static int getMethodCount(Class<?> clazz) {
		Method[] mt= clazz.getMethods();
        return mt == null ?-1: mt.length;
    }
	public static int getDeclaredMethodCount(Class<?> clazz) {
		Method[] mt= clazz.getDeclaredMethods();
        return mt == null ?-1: mt.length;
    }

	public static int getFieldCount(Class<?> clazz) {
		Field[] fl= clazz.getFields();
        return fl == null ?-1: fl.length;
    }

	public static int getDeclaredFieldCount(Class<?> clazz) {
		Field[] fl= clazz.getDeclaredFields();
        return fl == null ?-1: fl.length;
    }

}
