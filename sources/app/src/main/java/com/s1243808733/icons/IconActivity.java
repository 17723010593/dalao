package com.s1243808733.icons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.caverock.androidsvg.SVG;
import com.github.megatronking.svg.generator.svg.Svg2Vector;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.ProjectUtils;
import com.s1243808733.app.base.BaseActivity;
import com.s1243808733.util.Utils;
import com.s1243808733.view.ColorBackgroundTextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.margaritov.preference.colorpicker.AlphaPatternDrawable;
import net.margaritov.preference.colorpicker.ColorPickerDialog;

public class IconActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private EditText mEditText;

    private GridView mGridView; 

    private LinearLayout mProgressView;

    private static List<Icon> list=new ArrayList<>();

    private IconAdapter adapter;

    public static String assets_path="_data/materialicon";

    private static String[] dpi={"mdpi","hdpi","xhdpi","xxhdpi","xxxhdpi"};

    private static int[] dpi_value={24,36,48,72,96};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        getActionBar().setTitle("图标中心");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new loadIconTask().execute();
    }
    
    private void showMsgDialog(String t, String m) {
        Utils.showMsgDialog(this, t, m);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
        final Icon item=adapter.getItem(position);
        PopupMenu mPopupMenu=new PopupMenu(this, view);
        Menu menu=mPopupMenu.getMenu();

        final File pictures=new File(new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES), "Icon");
        final File projectResPath=ProjectUtils.getRes(ProjectUtils.getCurrentProject());

        SubMenu subMenu=menu.addSubMenu("添加到工程");
        subMenu.add(1, 0, 0, "添加为PNG").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    exportToPNG(getActivity(), item.getFilePath(), projectResPath, "drawable", item.getFileName(".png"), 0);
                    return false;
                }
            });
        subMenu.add(1, 0, 0, "添加为Vector").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    LinearLayout view=new LinearLayout(getActivity());
                    view.setPadding(AIDEUtils.dp2px(24), AIDEUtils.dp2px(16), AIDEUtils.dp2px(24), 0);

                    view.setOrientation(LinearLayout.VERTICAL);
                    TextView view_txt=new TextView(getActivity());
                    view_txt.setText("图标名");
                    final EditText view_editor=new EditText(getActivity());
                    view_editor.setText(item.getFileName(".xml"));
                    view.addView(view_txt);
                    view.addView(view_editor);

                    final AlertDialog dialog=new AlertDialog.Builder(getActivity())
                        .setTitle(menuItem.getTitle())
                        .setView(view)
                        .setPositiveButton("导入", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String toPath=new File(new File(projectResPath, "drawable"), view_editor.getText().toString()).getAbsolutePath();
                                String data=parseSvgToXml(ResourceUtils.readAssets2String(item.getFilePath()));
                                FileIOUtils.writeFileFromString(toPath, data);
                                AIDEUtils.toast(String.format("已添加至：%s", AIDEUtils.subString(toPath, projectResPath + "/", null)));
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                    dialog.show();
                    view_editor.addTextChangedListener(new TextWatcher(){

                            @Override
                            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {

                            }

                            @Override
                            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                                dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(p1));
                            }

                            @Override
                            public void afterTextChanged(Editable p1) {
                            }
                        });
                    return false;
                }
            });
        subMenu.setGroupEnabled(1, projectResPath == null ?false: true);


        SubMenu subMenu2=menu.addSubMenu("导出图标");
        subMenu2.add("导出为PNG").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    File toPath=new File(new File(pictures, "png"), item.getFileName(""));

                    exportToPNG(getActivity(), item.getFilePath(), toPath, "drawable", item.getFileName(".png"), 1);

                    return false;
                }
            });
        subMenu2.add("导出为Vector").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    String toPath=new File(new File(pictures, "vector"), item.getFileName(".xml")).getAbsolutePath();
                    String data=parseSvgToXml(ResourceUtils.readAssets2String(item.getFilePath()));
                    FileIOUtils.writeFileFromString(toPath, data);
                    showMsgDialog("已导出至", toPath);

                    return false;
                }
            });

        subMenu2.add("导出为SVG").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    String toPath=new File(new File(pictures, "svg"), item.getFileName(".svg")).getAbsolutePath();
                    ResourceUtils.copyFileFromAssets(item.getFilePath(), toPath);
                    showMsgDialog("已导出至", toPath);

                    return false;
                }
            });
        menu.add("转换为Vector").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    final String result=parseSvgToXml(ResourceUtils.readAssets2String(item.getFilePath()));
                    AlertDialog dialog=new AlertDialog.Builder(getActivity())
                        .setTitle(menuItem.getTitle())
                        .setMessage(result)
                        .setPositiveButton("复制", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AIDEUtils.copyToClipboard(result);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                    dialog.show();
                    return false;
                }
            });


        mPopupMenu.show();
    }


    public static String parseSvgToXml(String svg) {
        File f=new File(AIDEUtils.getApp().getFilesDir(), "1.tmp");
        FileIOUtils.writeFileFromString(f, svg);
        Svg2Vector. parseSvgToXml(f, f, -1, -1);
        String result= FileIOUtils.readFile2String(f);
        f.delete();
        return result;
    }

    private void exportToPNG(final Activity activity, final String aeetesFile, final File toPath, final String parentFileName, final String fileName, final int code) {

        LinearLayout view=new LinearLayout(activity);
        view.setOrientation(LinearLayout.VERTICAL);
        TextView view_txt=new TextView(activity);
        view_txt.setText("图标名");
        final EditText view_editor=new EditText(activity);
        view_editor.setText(fileName);
        view.addView(view_txt);
        view.addView(view_editor);


        final LinearLayout view2=new LinearLayout(activity);
        view2.setOrientation(LinearLayout.VERTICAL);
        TextView view2_txt=new TextView(activity);
        view2_txt.setText("图标尺寸");
        view2_txt.setPadding(0, AIDEUtils.dp2px(10), 0, 0);

        final EditText view2_editor=new EditText(activity);
        view2_editor.setInputType(InputType.TYPE_CLASS_NUMBER);
        view2_editor.setVisibility(8);

        Spinner view2_spinner=new Spinner(activity);
        final Object[] items={48,72,96,144,192,512,"自定义"};

        view2_spinner.setAdapter(new ArrayAdapter<Object>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, items));
        view2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                    Object obj=items[p3];
                    if (obj instanceof String) {
                        view2_editor.setVisibility(0);
                    } else {
                        view2_editor.setVisibility(8);
                        view2_editor.setText(String.valueOf(obj));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> p1) {
                }
            });

        for (int i=0;i < items.length;i++) {
            Object obj=items[i];
            if (obj.equals(96)) {
                view2_spinner.setSelection(i);
            }
        }

        view2.addView(view2_txt);
        view2.addView(view2_editor, -1, -2);
        view2.addView(view2_spinner, -1, -2);

        final LinearLayout view3=new LinearLayout(activity);
        view3.setOrientation(LinearLayout.VERTICAL);
        final TextView view3_txt=new TextView(activity);
        view3_txt.setText("图标颜色");
        view3_txt.setPadding(0, 0, 0, AIDEUtils.dp2px(5));

        LinearLayout view3_bth_root=new LinearLayout(activity);
        view3_bth_root.setBackgroundDrawable(new AlphaPatternDrawable(12));
        final ColorBackgroundTextView view3_bth=new ColorBackgroundTextView(activity);
		view3_bth.setTextSize(14);
		view3_bth_root.addView(view3_bth, -1, -2);
        view3_bth.setColor(0xFF757575);
        view3_bth.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    ColorPickerDialog dialog= new ColorPickerDialog(activity, Color.parseColor(view3_bth.getText().toString()));
                    dialog.setTitle(view3_txt.getText());
                    dialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener(){

                            @Override
                            public void onColorChanged(int color) {
                                view3_bth.setColor(color);
                                
                            }
                        });
                    dialog.show();

                }
            });
        view3.addView(view3_txt);
        view3.addView(view3_bth_root, -1, -2);

        final CheckBox checkbox_auto=new CheckBox(activity);
        checkbox_auto.setText("自适应图标");

        final TextView tips=new TextView(activity);
        tips.setText("将生成多个适应不同屏幕分辨率的图标。");

        checkbox_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton p1, boolean isChecked) {
                    if (isChecked) {
                        view2.setVisibility(8);
                        tips.setVisibility(0);
                    } else {
                        view2.setVisibility(0);
                        tips.setVisibility(8);
                    }
                }
            });
        checkbox_auto.setChecked(true);

        LinearLayout dialogView=new LinearLayout(activity);
        dialogView.setOrientation(LinearLayout.VERTICAL);
        dialogView.setPadding(AIDEUtils.dp2px(24), AIDEUtils.dp2px(16), AIDEUtils.dp2px(24), 0);
        dialogView.addView(view, -1, -2);
        dialogView.addView(view3, -1, -2);
        dialogView.addView(view2, -1, -2);
        View space=new View(activity);
        dialogView.addView(space, -1, AIDEUtils.dp2px(10));

        dialogView.addView(checkbox_auto, -2, -2);
        dialogView.addView(tips, -2, -2);

        if (code == 1) {
            checkbox_auto.setChecked(false);
        }

        final AlertDialog dialog=new AlertDialog.Builder(activity)
            .setTitle(code == 1 ?"导出PNG": "添加到项目")
            .setView(dialogView)
            .setPositiveButton(code == 1 ?"导出": "添加", null)
            .setNegativeButton("取消", null)
            .create();
        dialog.show();

        view_editor.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                    dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(p1));
                }

                @Override
                public void afterTextChanged(Editable p1) {
                }
            });

        view2_editor.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                    try {
                        boolean b=p1.toString().trim().length() == 0 || Long.parseLong(p1.toString()) < 1;
                        dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(b ?false: true);
                        view2_editor.setError(null);
                    } catch (Throwable e) {
                        dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(false);
                        view2_editor.setError(e.getMessage());
                    }
                }

                @Override
                public void afterTextChanged(Editable p1) {
                }
            });
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

                private void save(SVG svg, File savepath, int size) {
                    svg.setDocumentHeight(size);
                    svg.setDocumentWidth(svg.getDocumentHeight());
                    PictureDrawable draw=new PictureDrawable(svg.renderToPicture());
                    Bitmap bt = ImageUtils.drawColor(ImageUtils.drawable2Bitmap(draw), Color.parseColor(view3_bth.getText().toString()));
                    ImageUtils.save(bt, savepath, Bitmap.CompressFormat.PNG, true);

                }

                @Override
                public void onClick(View view) {
                    String mParentFileName=parentFileName == null ?"": parentFileName;
                    if (checkbox_auto.isChecked()) {
                        try {
                            ArrayList<File> listfile=new ArrayList<>();

                            SVG svg=SVG.getFromAsset(activity.getAssets(), aeetesFile);
                            for (int i=0;i < dpi.length;i++) {
                                File f=new File(new File(toPath, (mParentFileName) + "-" + dpi[i]), view_editor.getText().toString());
                                listfile.add(f);
                                save(svg, f, dpi_value[i]);
                            }
                            dialog.dismiss();
                            if (code == 1) {
                                showMsgDialog("已导出至", toPath.getAbsolutePath());
                            } else {
                                StringBuffer sb=new StringBuffer();
                                for (File f:listfile) {
                                    sb.append(f.getParentFile().getName());
                                    sb.append("\n");
                                }
                                showMsgDialog("已添加至", sb.toString().trim());
                            }
                        } catch (Throwable e) {
                            AIDEUtils.toast("error:" + e.getMessage());
                        }
                    } else {
                        try {
                            int size = Integer.parseInt(view2_editor.getText().toString());

                            SVG svg=SVG.getFromAsset(activity.getAssets(), aeetesFile);
                            File f= new File(code == 1 ?toPath.getParentFile(): new File(toPath, mParentFileName), view_editor.getText().toString());
                            save(svg, f, size);
                            dialog.dismiss();
                            if (code == 1) {
                                showMsgDialog("已导出至", new File(toPath.getParentFile(), view_editor.getText().toString()).getAbsolutePath());
                            } else {
                                showMsgDialog("已添加至",  Utils.subString(f.getAbsolutePath(), ProjectUtils.getRes(ProjectUtils.getCurrentProject()) + "/", null));
                            }
                        } catch (Throwable e) {
                            if (e instanceof OutOfMemoryError) {
                                view2_editor.setError("图片太大");
                            } else {
                                AIDEUtils.showExDialog(activity, e);
                            }
                        }
                    }

                }

            });

    }

    private class loadIconTask extends AsyncTask<Void,Void,List<Icon>> {

        public loadIconTask() {
        }

        @Override
        protected List<Icon> doInBackground(Void[] p1) {
            if (IconActivity.list.size() == 0) {
                List<Icon> list=new ArrayList<>();

                String[] iconList=ResourceUtils.getAssetsFileList(assets_path);

                for (String file:iconList) {
                    try {
                        Icon item=new Icon();
                        item.setFile(file);
                        item.setFilePath(assets_path + "/" + item.getFile());

                        SVG svg=SVG.getFromAsset(getAssets(), item.getFilePath());
                        item.setSvg(svg);

                        list.add(item);
                    } catch (Exception e) {}

                }

                return list;
            } else {
                return IconActivity.list;
            }
        }

        @Override
        protected void onPostExecute(List<Icon> result) {
            super.onPostExecute(result);
            list = result;
            adapter = new IconAdapter(IconActivity.this, list);
            mGridView.setAdapter(adapter);

            mEditText.setHint(String.format("在%d个图标中搜索...", list.size()));
            mProgressView.setVisibility(8);
        }

    }

    private View getContentView() {
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        mEditText = new EditText(this);
        mEditText.setSingleLine(true);
        mEditText.setHint("正在加载中...");
        mEditText.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
                }

                @Override
                public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                    if (adapter != null)adapter.search(p1.toString());
                }

                @Override
                public void afterTextChanged(Editable p1) {
                }
            });

        mGridView = new GridView(this);
        mGridView.setNumColumns(3);
        mGridView.setFastScrollEnabled(true);

        mGridView.setOnItemClickListener(this);

        root.addView(mEditText, -1, -2);
        root.addView(mGridView, -1, -1);

        mProgressView = new LinearLayout(this);
        mProgressView.setGravity(Gravity.CENTER);
        mProgressView.addView(new ProgressBar(this));

        RelativeLayout r=new RelativeLayout(this);
        r.addView(root, -1, -1);
        r.addView(mProgressView, -1, -1);

        return r;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


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

    private IconActivity getActivity() {
        return this;
    }


} 
