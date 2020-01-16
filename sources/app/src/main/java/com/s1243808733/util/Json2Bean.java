package com.s1243808733.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import com.blankj.utilcode.util.FileIOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.ProjectUtils;
import java.io.File;

public class Json2Bean {

    public static void showDialog(final Activity activity, String json) {

        final Json2Bean.DialogView dialogView=new DialogView(activity);

        final Json2Java.Configurd configurd=new Json2Java.Configurd();

        final AlertDialog dialog=new AlertDialog.Builder(activity)

            .setTitle("Json2Bean")
            .setView(dialogView.getView())
            .setPositiveButton("生成", null)
            .setNegativeButton("格式化", null)
            .setNeutralButton("配置", null)
            .create();
        dialog.show();

        if (json != null) {
            dialogView.input_json.setText(json);
        }

        final File currentProject=ProjectUtils.getCurrentProject();
        final File srcJavaPackageName=ProjectUtils.getSrcJavaPkg(currentProject);

        if (ProjectUtils.getPackageName(currentProject) != null) {
            dialogView.input_package.setText(ProjectUtils.getPackageName(currentProject));
        }

        Button positiveButton=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        positiveButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    final String json=dialogView.input_json.getText().toString();
                    final String packageName=dialogView.input_package.getText().toString();
                    final String className = dialogView.input_className.getText().toString();

                    configurd.setPackageName(packageName.length() == 0 ?null: packageName);
                    configurd.setClassName(className.length() == 0 ?null: className);

                    try {

                        final String result=Json2Java.createBean(json, configurd);

                        PopupMenu popupMenu=new PopupMenu(activity, view);
                        Menu menu=popupMenu.getMenu();
                        menu.add("导出至java").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                                @Override
                                public boolean onMenuItemClick(MenuItem p1) {
                                    LinearLayout ll=new LinearLayout(activity);
									ll.setOrientation(ll.VERTICAL);
                                    final EditText input_fileName=new EditText(activity);
                                    final EditText input_outpath=new EditText(activity);
                                    input_fileName.setHint("文件名..");
                                    input_outpath.setHint("导出路径..");
                                    ll.addView(input_fileName, -1, -2);
                                    ll.addView(input_outpath, -1, -2);


                                    if (srcJavaPackageName != null) {
                                        input_outpath.setText(srcJavaPackageName.getAbsolutePath());
                                    }else{
                                        input_outpath.setText(Environment.getExternalStorageDirectory().getAbsolutePath());
                                    }

                                    String fileName=dialogView.input_className.getText().toString();
                                    if (!TextUtils.isEmpty(fileName)) {
                                        input_fileName.setText(fileName + ".java");
                                    }

                                    ScrollView sv=new ScrollView(activity);
                                    sv.addView(ll, -1, -1);
                                    AlertDialog dialog2=new AlertDialog.Builder(activity)
                                        .setTitle(p1.getTitle())
                                        .setView(sv)
                                        .setPositiveButton("导出", null)
                                        .setNegativeButton("取消", null)
                                        .setNeutralButton("选择路径", null)
                                        .create();
                                    dialog2.show();

                                    dialog2.getButton(DialogInterface.BUTTON_POSITIVE)
                                        .setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    File f=new File(input_outpath.getText().toString(),
                                                                    input_fileName.getText().toString());
                                                    if (FileIOUtils.writeFileFromString(f, result)) {
                                                        Utils.toast("已导出到:" + f.getAbsolutePath());
                                                    } else {
                                                        Utils.toast("output failed!");
                                                    }
                                                } catch (Throwable e) {
                                                    Utils.toast(e, true);
                                                }
                                            }
                                        });
                                    dialog2.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                File f=srcJavaPackageName == null ?Environment.getExternalStorageDirectory(): srcJavaPackageName;
                                                ChooseDir.showDialog(activity, f, new ChooseDir.ChooseListener(){

                                                        @Override
                                                        public void onChooseed(File dir) {
                                                            input_outpath.setText(dir.getAbsolutePath());
                                                        }
                                                    });
                                            }
                                        });

                                    dialog.hide();
                                    dialog2.setOnDismissListener(new DialogInterface.OnDismissListener(){

                                            @Override
                                            public void onDismiss(DialogInterface p1) {
                                                dialog.show();
                                            }
                                        });

                                    return false;
                                }
                            });

                        menu.add("添加到当前编辑框").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                                @Override
                                public boolean onMenuItemClick(MenuItem p1) {
                                    AIDEUtils.addToEditor(result);
                                    //dialog.dismiss();
                                    return false;
                                }
                            }).setVisible(AIDEUtils.getCurrentEditorFile() != null);

                        menu.add("预览代码").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                                @Override
                                public boolean onMenuItemClick(MenuItem p1) {
                                    AlertDialog dialog2=new AlertDialog.Builder(activity)
                                        .setTitle(p1.getTitle())
                                        .setMessage(result)
                                        .setPositiveButton("复制", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Utils.copyToClipboard(result);
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create();
                                    dialog2.show();

                                    dialog.hide();
                                    dialog2.setOnDismissListener(new DialogInterface.OnDismissListener(){

                                            @Override
                                            public void onDismiss(DialogInterface p1) {
                                                dialog.show();
                                            }
                                        });
                                    return false;
                                }
                            });

                        popupMenu.show();

                    } catch (Throwable e) {
                        Utils.showExDialog(activity, e);
                    }


                }
            });
        negativeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final String jsonData=dialogView.input_json.getText().toString();
                    PopupMenu popupMenu=new PopupMenu(activity, view);
                    Menu menu=popupMenu.getMenu();
                    menu.add("压缩").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                            @Override
                            public boolean onMenuItemClick(MenuItem p1) {
                                try {
                                    String result=compressionJson(jsonData);
                                    if (result == null || "null".equals(result)) {
                                        //throw new Exception("result == null"); 
                                    } else {
                                        dialogView.input_json.setText(result);
                                    }
                                } catch (Exception e) {
                                    Utils.showExDialog(activity, e);
                                }
                                return false;
                            }
                        });
                    menu.add("格式化").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                            @Override
                            public boolean onMenuItemClick(MenuItem p1) {
                                try {
                                    String result=formatJson(jsonData);
                                    if (result == null || "null".equals(result)) {
                                        //throw new Exception("result == null"); 
                                    } else {
                                        dialogView.input_json.setText(result);
                                    }
                                } catch (Exception e) {
                                    Utils.showExDialog(activity, e);
                                }
                                return false;
                            }
                        });


                    popupMenu.show();
                }
            });
        neutralButton.setOnClickListener(new View.OnClickListener() {
                private String[] item={"添加set方法","添加get方法","排序 set、get方法","public field","List<?> -> ?[]","int -> Integer","toString"};
                @Override
                public void onClick(View view) {

                    boolean[] checkedItem={
                        configurd.isSetters(),
                        configurd.isGetters(),
                        configurd.isSortMethod(),
                        configurd.isPublicField(),
                        configurd.isUseArray(),
                        configurd.isUseInteger(),
                        configurd.isToString()
                    };
                    new AlertDialog.Builder(activity)
                        .setTitle("配置")
                        .setMultiChoiceItems(item, checkedItem, new DialogInterface.OnMultiChoiceClickListener(){

                            @Override
                            public void onClick(DialogInterface p1, int p2, boolean p3) {
                                switch (p2) {
                                    case 0:
                                        configurd.setSetters(p3);
                                        break;
                                    case 1:
                                        configurd.setGetters(p3);
                                        break;
                                    case 2:
                                        configurd.setSortMethod(p3);
                                        break;
                                    case 3:
                                        configurd.setPublicField(p3);
                                        break;
                                    case 4:
                                        configurd.setUseArray(p3);
                                        break;
                                    case 5:
                                        configurd.setUseInteger(p3);
                                        break;
                                    case 6:
                                        configurd.setToString(p3);
                                        break;


                                }
                            }
                        })
                        .create().show();

                }
            });

    }


    private static class DialogView  {
        private Context context;

        private ScrollView view;

        public EditText input_package;

        public EditText input_className;

        public EditText input_json;


        public DialogView(Context context) {
            this.context = context;
            view = new ScrollView(context);
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(ll.VERTICAL);

            input_package = new EditText(context);
            input_package.setHint("包名（可空）");

            input_className = new EditText(context);
            input_className.setHint("类名（可空）");

            input_json = new EditText(context);
            input_json.setHint("json");

            ll.addView(input_package, -1, -2);
            ll.addView(input_className, -1, -2);
            ll.addView(input_json, -1, -2);


            view.addView(ll, -1, -1);
        }


        public View getView() {
            return view;
        }


    }


    public static String formatJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = new JsonParser().parse(json);
        return gson.toJson(je);
    }

    public static String compressionJson(String json) {
        Gson gson = new GsonBuilder().setLenient().create();
        JsonElement je = new JsonParser().parse(json);
        return gson.toJson(je);
    }

}
