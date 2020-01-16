package com.s1243808733.aide.util;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import com.a4455jkjh.R;
import com.aide.ui.MainActivity;
import com.aide.ui.browsers.FileBrowser.b;
import com.aide.ui.browsers.j;
import com.aide.ui.views.CustomKeysListView;
import com.aide.ui.views.SplitView;
import com.aide.ui.views.editor.OConsole;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.s1243808733.aide.highlight.HighlightActivity;
import com.s1243808733.androidr.AndroidRActivity;
import com.s1243808733.icons.IconActivity;
import com.s1243808733.project.ProjectDialog;
import com.s1243808733.translate.resstring.ResStringTranslation;
import com.s1243808733.translate.web.WebTranslateActivity;
import com.s1243808733.util.BatchReplace;
import com.s1243808733.util.ClassUtils;
import com.s1243808733.util.EscapeUtils;
import com.s1243808733.util.Json2Bean;
import com.s1243808733.util.Toasty;
import com.s1243808733.util.Utils;
import com.s1243808733.util.View2Java;
import com.s1243808733.util.View2Style;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainInterface  {

	public static void onShowCreateProjectDialog(Activity activity) {
		ProjectDialog.showCreateDialog(activity);
		//ReflectUtils.reflect("com.aide.ui.ib").method("EQ_SOURCE");
	}

	public static void onShowSelectModeDialog(Activity activity) {
		//ReflectUtils.reflect("com.aide.ui.ib").method("J8_SOURCE");
	}


	public static void onSplitViewCloseSplit(SplitView p0, boolean z, Runnable runnable) {
		Object dl=AIDEUtils.getMainDrawerLayout();
		if (dl != null) {
			ReflectUtils.reflect(dl).method("closeDrawer", Gravity.LEFT);
		}
		ReflectUtils.reflect(p0).method("closeSplit_SOURCE", z, runnable);
	}

	public static void onSplitViewOpenSplit(SplitView p0, boolean z, boolean z2) {
		Object dl=AIDEUtils.getMainDrawerLayout();
		if (dl != null) {
			ReflectUtils.reflect(dl).method("openDrawer", Gravity.LEFT);
		} else {
			ReflectUtils.reflect(p0).method("openSplit_SOURCE", z, z2);
		}
	}

	public static void onDrawerOpened(View drawerView) {
		//..
    } 

    public static void onDrawerClosed(View drawerView) {
		//..
    } 

	public static void onFileBrowserItemClick(j j, final AdapterView<?> adapterView, final View view, final int position, final long itemid) {
		final ReflectUtils reflect=ReflectUtils.reflect(j);
		CustomKeysListView customKeysListView=reflect.field("FH").get();
		b item= (b)customKeysListView.getItemAtPosition(position);
		File file=item.v5 == null ?null: new File(item.v5);

		if (file != null && file.isFile() && AIDEUtils.getMainDrawerLayout() != null) {
            AIDEUtils.setCurrentFileToTop(file);
            AIDEUtils.closeSplit(false);
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    reflect.method("onItemClick_SOURCE", adapterView, view, position, itemid);
                }
            }, 200);
		} else {
			reflect.method("onItemClick_SOURCE", adapterView, view, position, itemid);
		}
	}

	public static boolean onKeyDown(MainActivity activity, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            Object dl=AIDEUtils.getMainDrawerLayout();
            if (dl != null) {
                if (AIDEUtils.isDrawerOpen()) {
                    AIDEUtils.closeSplit(false);
                    return true;
                }
            }
        }
		return ((Boolean)ReflectUtils.reflect(activity).method("onKeyDown_SOURCE", keyCode, event).get()).booleanValue();
	}

	public static void onCreate(MainActivity activity, Bundle savedInstanceState) {
		Utils.setMainActivity(activity);
        AIDEUtils.setWindowSoftInputMode();
        try {
			ReflectUtils.reflect(OConsole.class).field("j6", AdvancedSetting.getEditorTypeface());
		} catch (Throwable e) {}

		if (Build.VERSION.SDK_INT >= 23 && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
			activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
		}
        
		ReflectUtils.reflect(activity).method("onCreate_SOURCE", savedInstanceState);
        
        //open quickKeyBar
        ReflectUtils.reflect(activity).field("er").method("DW", true);


        if (AIDEUtils.isTrainerMode()) {
            activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            activity.getActionBar().setDisplayHomeAsUpEnabled(false);
        }

		if (AIDEUtils.getMainDrawerLayout() != null) {
			//AIDEUtils.closeSplit(false);
			ReflectUtils.reflect(AIDEUtils.getSplitView()).method("setSwipeEnabled", false);
		}

	}

	public static void setContentView(int layoutId) {
        MainActivity activity=AIDEUtils.getMainActivity();
		if (AdvancedSetting.isEnableDrawer() 
            && !AIDEUtils.isTrainerMode()) {
			activity.setContentView(R.layout.main_drawer);
        } else {
			activity.setContentView(layoutId);
		}
        AIDEUtils.setMainBackground();
	}

	public static void onRequestPermissionsResult(MainActivity activity, int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 100 && grantResults != null && grantResults.length > 0) {
			if (grantResults[0] == 0) {
				AIDEUtils.getFileBrowser().Zo();
                if (AIDEUtils.getMainDrawerLayout() != null && !AIDEUtils.isDrawerOpen()) {
                    AIDEUtils.openSplit(false);
                }
			} else {
                Toasty.error("缺失权限：" + Manifest.permission.WRITE_EXTERNAL_STORAGE + "，软件无法正常使用！").show();
			}
		}
		ReflectUtils.reflect(activity).method("onRequestPermissionsResult_SOURCE", requestCode, permissions, grantResults);
	}

    public static boolean onPrepareOptionsMenu(final MainActivity activity, Menu menu) {
		menu.clear();

		File editorFile=AIDEUtils.getCurrentEditorFile();
		if (editorFile == null) {
			activity.getActionBar().setTitle("AIDE");
			activity.getActionBar().setDisplayShowTitleEnabled(true);
		} else {
			activity.getActionBar().setDisplayShowTitleEnabled(false);
			activity.getActionBar().setTitle(null);
		}

		if (editorFile != null && AdvancedSetting.isEnableMenuCode() 
            && !AIDEUtils.checkEditorFileNameSuffix(".class")) {
            try {
                InputStream is=null;
                try {
                    String path="menucode/menu_code.xml";
                    File file=Utils.getSdDataFile(path);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                    }
                    if (is == null) {
                        String assets=Utils.getAssetsDataFile(path).getAbsolutePath().substring(1);
                        is = activity.getAssets().open(assets);
                        ResourceUtils.copyFileFromAssets(assets, file.getAbsolutePath());
                    }
                } catch (Throwable e) {}
                MenuCode[] items= MenuCode.add(menu, is, editorFile.getName(), new MenuCode.OnMenuItemClickListener(){

                    @Override
                    public void onMenuItemClick(MenuItem menuItem, String content) {
                        AIDEUtils.addToEditor(content.trim());
                    }
                });

                for (MenuCode i:items) {
                    MenuItem item=i.getMenuItem();
                    if (AIDEUtils.checkEditorFileNameSuffix(".xml")) {
                        item.setIcon(R.drawable.ic_vectoric_code_tags);
                    } else {
                        item.setIcon(R.drawable.ic_vectoric_code_braces);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
		boolean returnValue= ((Boolean)ReflectUtils.reflect(activity).method("onPrepareOptionsMenu_SOURCE", menu).get()).booleanValue();

	    final File currentProject=ProjectUtils.getCurrentProject();

		MenuItem mainMenuNavigateMode=menu.findItem(R.id.mainMenuNavigateMode);
        MenuItem mainMenuEditMode=menu.findItem(R.id.mainMenuEditMode);
		if (mainMenuNavigateMode != null && mainMenuEditMode != null) {
			if (AIDEUtils.checkEditorFileNameSuffix(".java")
				|| AIDEUtils.checkEditorFileNameSuffix(".class")) {
				mainMenuNavigateMode.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				mainMenuEditMode.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
		}

		MenuItem mainMenuAdMore=menu.findItem(R.id.mainMenuAdMore);
		if (mainMenuAdMore != null) {
			SubMenu sm=mainMenuAdMore.getSubMenu();

			sm.add("图标中心").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					Utils.startActivity(activity, IconActivity.class);
					return false;
				}
			});
			sm.add("代码高亮").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					Utils.startActivity(activity, HighlightActivity.class);
					return false;
				}
			});
			sm.add("代码转义").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					EscapeUtils.showDialog(activity, null);
					return false;
				}
			});
            sm.add("批量替换").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					File f=ProjectUtils.getMain(currentProject);
					BatchReplace.showDialog(activity, f == null ?null: f.getAbsolutePath());
					return false;
				}
			});

			sm.add("Json2Bean").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					Json2Bean.showDialog(activity, null);
					return false;
				}
			});

			if (ProjectUtils.isGradleProject(currentProject)) {
				sm.add("转换工程").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						ConverProject.showDialog(activity, currentProject);
						return false;
					}
				});
			}

			if (AIDEUtils.checkEditorFileParentName("values") 
				&& AIDEUtils.checkEditorFileName("strings.xml")) {
				sm.add("String翻译").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						ResStringTranslation.showDialog(activity, AIDEUtils.getCurrentEditorFile());
						return false;
					}
				});
			}

			sm.add("系统资源查看").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					Utils.startActivity(activity, AndroidRActivity.class);
					return false;
				}
			});

			sm.add("指定类分析").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					ClassUtils.showDialog(activity);
					return false;
				}
			});


		}

		return returnValue;
    }


    public static void onCreateActionMode(final ActionMode actionMode, Menu menu) {
        final MainActivity activity=AIDEUtils.getMainActivity();

        actionMode.setTitle("选择操作");

		//logcat menu
        if (menu.getItem(0).getItemId() == 0x7f0800e4) {
            return;
        }

        MenuItem editorMenuFix = menu.findItem(R.id.editorMenuFix);

        MenuItem editorMenuExpandSelection= menu.findItem(R.id.editorMenuExpandSelection);
        MenuItem editorMenuCut= menu.findItem(R.id.editorMenuCut);
        MenuItem editorMenuCopy= menu.findItem(R.id.editorMenuCopy);
        MenuItem editorMenuPaste= menu.findItem(R.id.editorMenuPaste);

        if (editorMenuFix.isVisible()) {
            editorMenuFix.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            if (TextUtils.isEmpty(getSelectionContent())) {
                editorMenuExpandSelection.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                editorMenuCut.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                editorMenuCopy.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                editorMenuPaste.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            } else {
                editorMenuExpandSelection.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                editorMenuCut.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                editorMenuCopy.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                editorMenuPaste.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);   
            }
        }

		MenuItem editorMenuAdTool=menu.findItem(R.id.editorMenuAdTool);
        if (editorMenuAdTool != null) {
			SubMenu sm=editorMenuAdTool.getSubMenu();

            MenuItem editorMenuTranslation=sm.add("翻译内容");
            editorMenuTranslation.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					WebTranslateActivity.translate(activity, getSelectionContent());
                    actionMode.finish();
					return false;
				}
			});

            if (AIDEUtils.checkEditorFileParentName("layout") //layout-xxx
                && AIDEUtils.checkEditorFileNameSuffix(".xml")) {
                SubMenu sm_xml=sm.addSubMenu("视图工具");
                MenuItem editorMenuView2Java=sm_xml.add("View2Java");
                MenuItem editorMenuView2Style=sm_xml.add("View2Style");
                editorMenuView2Java.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						View2Java.showDialog(activity, getSelectionContent());
                        actionMode.finish();
						return false;
					}
				});
                editorMenuView2Style.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						View2Style.showDialog(activity, getSelectionContent());
                        actionMode.finish();
						return false;
					}
				});
            }

            Utils.setOptionalIconsVisible(menu, true);
        }

    }

    public static void onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        final MainActivity activity=AIDEUtils.getMainActivity();
        int itemId=menuItem.getItemId();

    }

    public static boolean onOptionsItemSelected(MainActivity activity, MenuItem menuItem) {
		int itemId=menuItem.getItemId();

		return ((Boolean)ReflectUtils.reflect(activity).method("onOptionsItemSelected_SOURCE", menuItem).get()).booleanValue();
	}

    public static void onMenuOpened(MainActivity activity, int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			Utils.setOptionalIconsVisible(menu, true); 
        }

    }

    private static String getSelectionContent() {
        return AIDEUtils.getAIDEEditorPager().getSelectionContent();
    }


}
