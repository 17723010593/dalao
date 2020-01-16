package com.s1243808733.util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.blankj.utilcode.util.FileIOUtils;
import java.io.File;
import java.io.FileFilter;

public class BatchReplace {

    public static interface Listener {
        boolean onReplace(File file);
    }
    public static void showDialog(final Activity activity, String path) {
        LinearLayout ll=new LinearLayout(activity);
        ll.setOrientation(ll.VERTICAL);
        final EditText et=new EditText(activity);
        et.setHint("目录或文件");
        if (path != null)  et.setText(path);
        final EditText et_old=new EditText(activity);
        et_old.setHint("原内容");
        final EditText et_new=new EditText(activity);
        et_new.setHint("新内容");
        final EditText et_filtration=new EditText(activity);
        et_filtration.setHint("后缀过滤 .java|.xml，可空");
        final CheckBox cb=new CheckBox(activity);
        cb.setText("替换子目录");
        cb.setChecked(true);

        ll.addView(et, -1, -2);
        ll.addView(et_old, -1, -2);
        ll.addView(et_new, -1, -2);
        ll.addView(et_filtration, -1, -2);
        ll.addView(cb, -1, -2);

        ScrollView sc=new ScrollView(activity);
        sc.addView(ll);
        AlertDialog dialog=new AlertDialog.Builder(activity)
		.setTitle("批量替换")
		.setView(sc)
		.setPositiveButton("确定", null)
		.setNegativeButton("取消", null)
		.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            private String targetDir;

            private String old;

            private String news;

            private String[] filter;

            private void replace(File dir, Listener listener) {
                if (dir.isFile()) {
                    boolean r=false;
                    if (filter == null) {
                        r = true;   
                    } else {
                        label:
                        for (String suffix:filter) {
                            if (suffix.trim().length() != 0 && dir.getName().endsWith(suffix)) {
                                r = true;
                                break label;
                            }
                        }
                    }
                    if (r) {
                        String data=FileIOUtils.readFile2String(dir);
                        if (data != null) {
                            if (data.contains(old) && listener.onReplace(dir)) {
                                FileIOUtils.writeFileFromString(dir, data.replace(old, news));
                            }
                        }
                    }
                } else {
                    File[] lists = dir.listFiles();
                    if (lists == null)return;
                    for (File file:lists) {
                        if (file.isDirectory()) {
                            if (cb.isChecked())
                                replace(file, listener);
                        } else {
                            replace(file, listener);     
                        }
                    }
                }
            }

			@Override
			public void onClick(View view) {
				final long startTime=System.currentTimeMillis();
				targetDir = et.getText().toString();
                old = et_old.getText().toString();
                news = et_new.getText().toString();
		        filter = et_filtration.getText().toString().trim().length() == 0 ?null: et_filtration.getText().toString().split("|");

                if (targetDir.trim().length() == 0
                    || !new File(targetDir).exists()) {
					et.setError("路径不存在");
					return;
				} else if (old.length() == 0) {
					et_old.setError("请输入要替换的内容");
					return;
				}
                final StringBuffer sb=new StringBuffer();
                replace(new File(targetDir), new Listener(){

                    @Override
                    public boolean onReplace(File file) {
                        sb.append("\nFile:" + file.getAbsolutePath());
                        return true;
                    }
                });

				final StringBuffer msg=new StringBuffer();
				msg.append("秏时：" + ((double)(System.currentTimeMillis() - startTime)));
				msg.append("ms\n");
				msg.append(sb.toString());
				new AlertDialog.Builder(activity)
				.setTitle("替换完成")
				.setMessage(msg.toString())
				.setPositiveButton("复制", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dia, int which) {
						Utils.copyToClipboard(msg.toString());
					}
				})
				.setNegativeButton("取消", null)
				.create().show();

			}
		});
    }

}
