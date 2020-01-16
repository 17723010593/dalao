package com.s1243808733.aide.completion.translate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import com.aide.common.b;
import com.aide.engine.SourceEntity;
import com.aide.ui.U;
import com.aide.ui.activities.o;
import com.blankj.utilcode.util.KeyboardUtils;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;
import com.s1243808733.translate.TUtil;
import com.s1243808733.util.Utils;
import java.io.File;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

public class TranslateUtils {

    private static String lang="auto";

    private static String targetLang="zh";

    //////////////////////////////////////

    public static final int STATE_START =0;

    public static final int STATE_SUCCESS =1;

    public static final int STATE_FAILED =-1;

    private static DbManager db=null;

    public static void setTargetLang(String targetLang) {
        TranslateUtils.targetLang = targetLang;
    }

    public static String getTargetLang() {
        return targetLang;
    }

    public static void setLang(String lang) {
        TranslateUtils.lang = lang;
    }

    public static String getLang() {
        return lang;
    }

    private static void requestTranslate(TextView tv, final String source) {
        Translate.translate(getLang(), getTargetLang(), source);
    }

    public static String wordSegmentation(String content) {
        int index=content.lastIndexOf(" - ");
        String data =index > 0 ? content.substring(0, index): content;
        return TUtil.wordSegmentation(data);
    }

    public static DbManager getDb() {
        if (db == null) {
            try {
                DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
				.setDbName(getDbFile().getName())
				.setDbDir(getDbFile().getParentFile())
				.setDbVersion(1)
				.setDbOpenListener(new DbManager.DbOpenListener() {
					@Override
					public void onDbOpened(DbManager db) {
						db.getDatabase().enableWriteAheadLogging();
					}
				})
				.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

					}
				});
                db = x.getDb(daoConfig);
            } catch (Throwable e) {}
        }
        return db;
    }

    public static File getDbFile() {
        return Utils.getSdDataFile("translate/translate.db");
    }

    public static void onItemLongClick(final SourceEntity sourceEntity, View view, int position) {

        final Context context=AIDEUtils.getMainActivity();

        PopupMenu popupMenu=new PopupMenu(context, view);
        Menu menu=popupMenu.getMenu();

        if (AdvancedSetting.isEnableTranslate()) {

            TextView tv=(TextView) view.findViewById(2131230757);
            String tvText = Utils.subString(tv.getText().toString(), null, "\n");
            if (tvText == null) {
                tvText = tv.getText().toString();
            }

            final String source=tvText;

            final Table items=query(source);

            menu.add(items == null || items.getState() != TranslateUtils.STATE_SUCCESS ?"翻译内容": "修正翻译")
			.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {

					final ScrollView dialogView=new ScrollView(context);

					LinearLayout rootView=new LinearLayout(context);
					rootView.setOrientation(LinearLayout.VERTICAL);
					rootView.setPadding(AIDEUtils.dp2px(24), AIDEUtils.dp2px(10), AIDEUtils.dp2px(24), 0);

					EditText mEditText1=new EditText(context);
					mEditText1.setText(items == null ?source: items.getSource());
					mEditText1.setMaxLines(2);
					mEditText1.setEllipsize(TextUtils.TruncateAt.END);
					//mEditText1.setEnabled(false);


					TextView mText2=new TextView(context);
					mText2.setText("翻译后：");
					mText2.setPadding(0, AIDEUtils.dp2px(16), 0, AIDEUtils.dp2px(5));
					EditText mEditText2=new EditText(context);
					if (items != null && items.getState() == TranslateUtils.STATE_SUCCESS) {
						mEditText2.setText(items.getTranslation());
					}

					rootView.addView(mEditText1);

					rootView.addView(mText2);
					rootView.addView(mEditText2);

					final EditText editor=mEditText2;

					dialogView.addView(rootView, -1, -1);
					dialogView.setFillViewport(true);

					AlertDialog dialog=new AlertDialog.Builder(AIDEUtils.getMainActivity())
					.setTitle(p1.getTitle())
					.setView(dialogView)
					.setPositiveButton("保存", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String txt=editor.getText().toString();
							Table items=query(source);
							if (items == null) {
								try {
									Table item = new Table();
									item.setSource(source);
									item.setTranslation(txt);
									item.setState(TranslateUtils.STATE_SUCCESS);
									getDb().saveBindingId(item);
								} catch (Exception e) {
									AIDEUtils.toast(e.getMessage());
								}
							} else {
								items.setTranslation(txt);
								items.setState(TranslateUtils.STATE_SUCCESS);
								TranslateUtils.update(items);
							}

						}
					})
					.setNegativeButton("取消", null)
					.create();
					dialog.show();

					editor.setSelection(0, editor.getText().toString().length());
					editor.requestFocus();
					new Handler().postDelayed(new Runnable(){

						@Override
						public void run() {
							KeyboardUtils.showSoftInput(editor);
						}
					}, 100);

					return false;
				}
			});

            if (items != null) {
                menu.add("删除翻译").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						try {
							getDb().delete(items);
						} catch (DbException e) {}
						return false;
					}
				});
            }


        }

        menu.add("查看文档").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem p1) {
				U.lg().J8().J8();
				b.j6(U.lg(), sourceEntity.VH(), o.we().toString());
				return false;
			}
		}).setVisible(sourceEntity == null || sourceEntity.VH() == null ?false: true);

        popupMenu.show();

    }


    public static void translate(TextView tv) {
        if (!AdvancedSetting.isEnableTranslate()) return; 

        try {
            String source=tv.getText().toString();

            Table items=query(source);
            if (items == null) {
                Table item = new Table();
                item.setSource(source);
                item.setTranslation("");
                item.setState(TranslateUtils.STATE_START);
                getDb().saveBindingId(item);
                appendText(tv, "已写入数据，请滑动列表进行翻译。");
                requestTranslate(tv, source);   
                return;
            } 

            if (items.getState() == TranslateUtils.STATE_SUCCESS) {
                appendText(tv, items.getTranslation());
            } else {
                if (items.getState() == TranslateUtils.STATE_START) {
                    appendText(tv, "正在翻译中...");
                    requestTranslate(tv, source);   
                } else if (items.getState() == TranslateUtils.STATE_FAILED) {
                    String msg=items.getTranslation();
                    if (TextUtils.isEmpty(msg)) {
                        appendText(tv, "翻译失败");
                    } else {
                        appendText(tv, msg);
                    }
                    requestTranslate(tv, source);   
                }
            }

        } catch (Throwable e) {}

    }

    public static void update(Table items) {
        try {
            getDb().update(items);
        } catch (DbException e) {
        }
    }

    public static Table query(String source) {
        try {
            Table item=getDb().selector(Table.class)
			.where("source", "=", source).findFirst();
            return item;
        } catch (Throwable e) {}
        return null;
    }

    private static void appendText(TextView tv, String str) {
        if (str != null && str.length() != 0) {
            SpannableStringBuilder sb=new SpannableStringBuilder();
            sb.append("\n");
            SpannableString span=new SpannableString(str);
            span.setSpan(new ForegroundColorSpan(0xFFAAAAAA), 0, span.length(), 33);
            sb.append(span);
            tv.append(sb);
        }
    }


}
