package com.s1243808733.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import android.os.Environment;

public class ChooseDir {
    private FileAdapter adapter;

    private ChooseListener chooseListener;
    public interface ChooseListener {
        void onChooseed(File dir);
    }

    public static void showDialog(Activity activity, File dir, ChooseListener chooseListener) {
        new ChooseDir(activity, dir, chooseListener);
    }

    public ChooseDir(final Activity activity, File dir, final ChooseListener chooseListener) {
        this.chooseListener = chooseListener;
		if (dir == null) {
			dir = Environment.getExternalStorageDirectory();
		}

        final AlertDialog dialog=new AlertDialog.Builder(activity)
            .setTitle(dir.getAbsolutePath())
            .setItems(new String[]{}, null)
            .setCancelable(false)
            .setPositiveButton("选择", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    chooseListener.onChooseed(adapter.currentDir);
                }
            })
            .setNegativeButton("取消", null)
            .setNeutralButton("新建文件夹", null)
            .create();

        dialog.show();

        dialog.getButton(dialog.BUTTON_NEUTRAL)
            .setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LinearLayout ll=new LinearLayout(activity);
                    final EditText input=new EditText(activity);
                    ll.addView(input, -1, -2);
                    final AlertDialog dialog2=new AlertDialog.Builder(activity)
                        .setTitle(((Button)view).getText())
                        .setView(ll)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null)
                        .create();
                    dialog2.show();
                    dialog2.getButton(DialogInterface.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                try {
                                    File f=new File(adapter.currentDir, input.getText().toString());
                                    if (!f.mkdirs()) {
                                        throw new Exception("!f.mkdirs()");
                                    }   
                                    adapter.reload();
                                    dialog2.dismiss();
                                } catch (Throwable e) {
                                    Utils.toast(e);
                                }
                            }
                        });
                    dialog2.setOnDismissListener(new DialogInterface.OnDismissListener(){

                            @Override
                            public void onDismiss(DialogInterface p1) {
                                //dialog.show();
                            }
                        });
                    //dialog.hide();

                }
            });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

                @Override
                public boolean onKey(DialogInterface p1, int p2, KeyEvent p3) {
                    if (p2 == p3.KEYCODE_BACK && p3.getAction() == 0) {
                        if (!adapter.goBack()) {
                            dialog.dismiss();
                        }
                    }
                    return false;
                }
            });

        adapter = new FileAdapter(activity, dialog);
        adapter.loadDir(dir);

    }


    private static class FileAdapter extends BaseAdapter {

        private Context context;

        private AlertDialog dialog;

        private List<Item> datalist=new ArrayList<>();

        private File currentDir;

        public FileAdapter(Context context, AlertDialog dialog) {
            this.context = context;
            this.dialog = dialog;
            dialog.getListView().setAdapter(this);
            dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
                        Item item=getItem(position);
                        if (item.isBack) {
                            goBack();
                        } else {
                            loadDir(item.file);
                        }
                    }

                });
        }


        private boolean goBack() {
            if (currentDir != null) {
                File parent=currentDir.getParentFile();
                if (parent != null) {
                    loadDir(parent);
                    return true;
                }
            }
            return false;
        }

        public void reload() {
            loadDir(currentDir);
        }

        public void loadDir(File dir) {
            if (dir == null)return;

            currentDir = dir;
            dialog.setTitle(dir.getAbsolutePath());

            File[] array =null;

            List<Item> list=new ArrayList<>();

            if (dir.getAbsolutePath().equals("/")) {
                array = new File[]{
                    new File("/storage")
                };
            } else {
                list.add(new Item(true));
                array = dir.listFiles(new FileFilter(){

                        @Override
                        public boolean accept(File p1) {
                            return p1.isDirectory();
                        }
                    });
            }

            if (array == null) {
                if (dir.getAbsolutePath().equals("/storage/emulated")) {
                    array = new File[]{new File(dir, "0")};
                } else {
                    array = new File[]{};
                }
            }

            for (File f:array) {
                list.add(new Item(f));
            }
            Collections.sort(list, new Comparator<Item>(){

                    @Override
                    public int compare(ChooseDir.FileAdapter.Item p1, ChooseDir.FileAdapter.Item p2) {
                        if (p1.isBack || p2.isBack) {
                            return 0;
                        }
                        File f1=p1.file;
                        File f2=p2.file;
                        String n1=f1.getName().toLowerCase(Locale.ENGLISH);
                        String n2 = f2.getName().toLowerCase(Locale.ENGLISH);
                        return n1.compareTo(n2);
                    }
                });

            datalist = list;

            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View view, ViewGroup vg) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, vg, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Item item=getItem(position);
            if (item.isBack) {
                holder.text1.setText(".../");
            } else {
                File file=item.file;
                holder.text1.setText(file.getName());
            }
            return view;
        }


        private class Item {
            public File file;

            public boolean isBack;

            public Item(File file) {
                this.file = file;
            }

            public Item(boolean isBack) {
                this.isBack = isBack;
            }

        }


        private class ViewHolder {
            public TextView text1;

            public ViewHolder(View view) {
                this.text1 = (TextView) view.findViewById(android.R.id.text1);
            }
        }

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Item getItem(int p1) {
            return datalist.get(p1);
        }

        @Override
        public long getItemId(int p1) {
            return p1;
        }


    }


}
