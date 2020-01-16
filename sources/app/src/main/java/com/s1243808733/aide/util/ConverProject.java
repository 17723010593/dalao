package com.s1243808733.aide.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import com.blankj.utilcode.util.FileIOUtils;
import com.s1243808733.util.Toasty;
import com.s1243808733.util.Utils;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ConverProject {

    public static void showDialog(final Activity act, final File project) {
		if (project == null) {
			Toasty.error("project == null").show();
			return;
		}

        new AlertDialog.Builder(act)
		.setItems(new String[]{"转换为AppCompat工程","转换为AndroidX工程"}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				ConverProject cp=new ConverProject(project);
				ConverProject.Listener listener=new ConverProject.Listener(){

					private ProgressDialog pg;

					@Override
					public void onStart() {
						pg = ProgressDialog.show(act, null, "正在转换...", true, false);

					}

					@Override
					public void onFinish(String msg) {
						if (pg != null && pg.isShowing())pg.dismiss();
						new AlertDialog.Builder(act)
						.setTitle("转换完成")
						.setMessage(msg)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						.setNegativeButton("取消", null)
						.create().show();
					}
				};
				switch (which) {
					case 0:
						cp.converToAppCompat(listener);
						break;
					case 1:
						cp.converToAndroidX(listener);
						break;

				}
			}
		})
		.create().show();

    }



    private File project;

    public interface Listener {
        void onStart();
        void onFinish(String msg);
    }

    public ConverProject(File project) {
        this.project = project;
    } 

    public void converToAppCompat(Listener listener) {
        new ConverProjectTask(project, 0, listener).execute();
    }	

    public void converToAndroidX(Listener listener) {
        new ConverProjectTask(project, 1, listener).execute();
    }	

    private class ConverProjectTask extends AsyncTask<Void,Void,Void> {

        private File project;
        private int code;
        private Listener listener;

        long startTime=0L;

        HashMap<String,String> artifact_map=new HashMap<>();
        HashMap<String,String> class_map=new HashMap<>();

        public ConverProjectTask(File project, int code, Listener listener) {
            this.project = project;
            this.code = code;
            this.listener = listener;
            startTime = System.currentTimeMillis();
            listener.onStart();
        }		

        @Override
        protected Void doInBackground(Void[] p1) {
            initMapping(artifact_map, Utils.getAssetsDataFile("androidx/androidx-artifact-mapping.csv").getAbsolutePath());
            initMapping(class_map, Utils.getAssetsDataFile("androidx/androidx-class-mapping.csv").getAbsolutePath());
			replaces(project, new FileFilter(){

				private String[] suffixs={".xml",".java",".gradle"};

				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					for (String suffix:suffixs) {
						if (f.getName().endsWith(suffix)) {
							return true;
						}
					}
					return false;
				}
			});
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            String time=((float)(System.currentTimeMillis() - startTime)) + "ms";
            StringBuffer sb=new StringBuffer();
            sb.append("请退出AIDE或者重新打开工程\n\n秏时：" + time);
            listener.onFinish(sb.toString());
        }

        private void replaces(File path, final FileFilter filter) {
            File[] lists = path.listFiles(filter);

            if (lists == null) return;

            for (File file:lists) {
                if (file.isDirectory()) {
                    replaces(file, filter);
                } else {
					String data=FileIOUtils.readFile2String(file);
                    if (data != null) {
                        for (Iterator<String> it=class_map.keySet().iterator();it.hasNext();) {
                            String old = it.next();
                            if (data.contains(old))
                                data = data.replace(old, class_map.get(old));
                        }
                        for (Iterator<String> it=artifact_map.keySet().iterator();it.hasNext();) {
                            String old = it.next(); 
                            if (data.contains(old))
                                data = data.replace(old, artifact_map.get(old));
                        }
                        FileIOUtils.writeFileFromString(file, data);
                    }

                }
            }

        }

        private void initMapping(HashMap<String,String> map, String filepath) {
            InputStream is=null;
            Scanner in=null ;
            try {
				if (filepath.startsWith("/")) {
					filepath = filepath.substring(1);
				}
                is = Utils.getApp().getAssets().open(filepath);
                in = new Scanner(is, "utf-8");
                in.nextLine();
                while (in.hasNextLine()) {
                    String[] lines = in.nextLine().split(",");
                    if (lines.length >= 2) {
                        String old=lines[0];
                        String news=lines[1];

                        if (code == 0) {
                            map.put(news, old);
                        } else {
                            map.put(old, news);
                        }
                    }
                }
            } catch (Throwable e) {
            } finally {
                try {
                    is.close();
                    in.close();
                } catch (Throwable e) {}
            }

        }

    }

} 

