package com.s1243808733.aide.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.UriUtils;
import com.s1243808733.aide.AppTheme;
import com.s1243808733.aide.completion.translate.TranslateUtils;
import com.s1243808733.aide.highlight.HighlightActivity;
import com.s1243808733.util.Toasty;
import com.s1243808733.util.Utils;
import java.io.File;

public class AdvancedSettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppTheme.initTheme(this);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2131755018);

        getActionBar().setTitle(String.format("高级设置（v%s）", AdvancedSetting.VERSION_NAME));

        init();
    }

    private AdvancedSettingsActivity getActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            File file = UriUtils.uri2File(this, data.getData());
            if (file == null) {
                Toasty.error("file == null").show();
            } else {
                Utils.getSp().edit().putString("advanced_setting_editor_bg", file.getAbsolutePath()).commit();
                Toasty.success("设置成功：" + file.getAbsolutePath()).show();
                AIDEUtils.setMainBackground();
            }

        }

    }

    private void init() {

        //initPreferenceScreenClickListener("preference_screen");

        findPreference("advanced_setting_editor_bg").setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener(){

            @Override
            public boolean onPreferenceChange(Preference p1, Object p2) {
                Utils.getSp().edit().putString("advanced_setting_editor_bg", p2.toString()).commit();
                AIDEUtils.setMainBackground();
                return false;
            }
        });

        setOnPreferenceClickListener("choose_bg_image", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.addCategory("android.intent.category.OPENABLE");
                try {
                    startActivityForResult(Intent.createChooser(intent, "choose"), 100);
                } catch (ActivityNotFoundException e) {
                    Toasty.error(e.getMessage()).show();
                }
                return false;
            }
        });

		setOnPreferenceClickListener("advanced_setting_editor_font", new EditTextPreference.OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference p1, Object p2) {
				AIDEUtils.notifyThemeChanged();
				return true;
			}
		});

		setOnPreferenceClickListener("advanced_setting_enable_drawer", new EditTextPreference.OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference p1, Object p2) {
				AIDEUtils.notifyThemeChanged();
				return true;
			}
		});


        setOnPreferenceClickListener("preference_stringfog", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                String msg="编译时加密Class中的字符串，防止直接暴露。支持使用@StringFogIgnore()注解来忽视某个类。<br>暂不支持加密项目导入的jar<br><a href=\"https://github.com/MegatronKing/StringFog\">@Github</a>";
                AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle(p1.getTitle())
                .setMessage(Utils.fromHtml(msg))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
                dialog.show();
                Utils.setAlertDialogMovementMethod(dialog);
                return false;
            }
        });

        setOnPreferenceClickListener("add_proguard_config", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                final File project=ProjectUtils.getCurrentProject();
                if (project == null) {
                    Toasty.error("获取当前工程路径失败");
                    return false;
                }
                new AlertDialog.Builder(getActivity())
                .setTitle("将添加到")
                .setMessage(project.getAbsolutePath())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String resguard=Utils.getAssetsDataFile("resguard").getAbsolutePath();
                        if (ResourceUtils.copyFileFromAssets(resguard, project.getAbsolutePath())) {
                            Toasty.success("已添加到：" + project.getAbsolutePath()).show();
                        } else {
                            Toasty.error("failed");
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create().show();
                return false;
            }
        });

        setOnPreferenceClickListener("s1243808733", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                String[] items={"反馈","捐赠"};
                AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        if (which == 0) {
                            Utils.openUrl("mqqwpa://im/chat?chat_type=wpa&uin=" + (1243808733));
                        } else {
                            File path=Utils.getAssetsDataFile("donation.png");
                            File dest=new File(new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES), path.getName());
                            ResourceUtils.copyFileFromAssets(path, dest);
                            Toasty.success("已保存微信赞赏码").show();
                        }
                    }
                })
                .create();
                dialog.show();
                return false;
            }
        });


        setOnPreferenceClickListener("advanced_setting_enable_resguard", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                AndResGuard.delectResourcesAps();
                return false;
            }
        });


        setOnPreferenceClickListener("preference_highlight", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                Utils.startActivity(getActivity(), HighlightActivity.class);
                return false;
            }
        });

        setOnPreferenceClickListener("clear_translate", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle(p1.getTitle())
                .setMessage("是否清除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            TranslateUtils.getDb().dropDb();
                            Toasty.success("已清除翻译文件").show();
                        } catch (Throwable e) {
                            AIDEUtils.toast(e.getMessage());
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
                dialog.show();
                return false;
            }
        });

        setOnPreferenceClickListener("preference_quick_key", new Preference.OnPreferenceClickListener(){

            @Override
            public boolean onPreferenceClick(Preference p1) {
                QuickKey.showDialog(getActivity());
                return false;
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("更新日志").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem p1) {
                AIDEUtils.showUpdateLogDialog(AdvancedSettingsActivity.this);
                return false;
            }
        });
        menu.add("获取新版本").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem p1) {
                AIDEUtils.openUrl("https://pan.baidu.com/s/1FDu2fE1FeilJdiB4bYaH2g");
                AIDEUtils.toast("如果有新版本作者会在网盘里更新，密码：14g0");
                return false;
            }
        });

        menu.add("获取Yandex翻译密钥").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem p1) {
                AIDEUtils.openUrl("https://tech.yandex.com/keys/get/?service=trnsl");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    /*
     private void initPreferenceScreenClickListener(String key) {
     final PreferenceScreen root=(PreferenceScreen)findPreference(key);
     for (int i=0;i < root.getPreferenceCount();i++) {
     Preference preference=root.getPreference(i);
     if (preference instanceof PreferenceScreen) {
     final PreferenceScreen preferenceScreen=(PreferenceScreen) preference;
     preferenceScreen.setOnPreferenceClickListener(new PreferenceScreen.OnPreferenceClickListener(){

     @Override
     public boolean onPreferenceClick(Preference p1) {
     Dialog dialog=preferenceScreen.getDialog();
     if (dialog != null) {

     }
     return false;
     }
     });
     }

     }

     }
     */


    private void setOnPreferenceClickListener(String key, Preference.OnPreferenceClickListener onPreferenceClickListener) {
        Preference preference=findPreference(key);
        if (preference != null) {
            preference.setOnPreferenceClickListener(onPreferenceClickListener);
        }
    }

    private void setOnPreferenceClickListener(String key, Preference.OnPreferenceChangeListener onPreferenceClickListener) {
        Preference preference=findPreference(key);
        if (preference != null) {
            preference.setOnPreferenceChangeListener(onPreferenceClickListener);
        }
    }



}

