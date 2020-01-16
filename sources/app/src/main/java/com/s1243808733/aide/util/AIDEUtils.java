package com.s1243808733.aide.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.a4455jkjh.R;
import com.a4455jkjh.d.c;
import com.aide.ui.AIDEEditor;
import com.aide.ui.AIDEEditorPager;
import com.aide.ui.MainActivity;
import com.aide.ui.U;
import com.aide.ui.browsers.FileBrowser;
import com.aide.ui.build.android.B;
import com.aide.ui.build.android.fb;
import com.aide.ui.preferences.PreferencesActivity;
import com.aide.ui.views.CodeEditText;
import com.aide.ui.views.CodeEditText$c;
import com.aide.ui.views.SplitView;
import com.aide.ui.views.editor.OConsole;
import com.aide.ui.views.editor.OEditor;
import com.blankj.utilcode.util.ReflectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.s1243808733.aide.highlight.HighlightUtils;
import com.s1243808733.aide.highlight.color.ColorDefault;
import com.s1243808733.permission.ManifestActivity;
import com.s1243808733.translate.web.WebTranslateActivity;
import com.s1243808733.util.CrashUtils;
import com.s1243808733.util.Utils;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AIDEUtils extends Utils {

    public static void init(Application app) {
        Utils.setApp(app);
		CrashUtils.init(app);
		AppConfigured.init();
        HighlightUtils.init();
        initX();
		app.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks(app));
	}

    public static void setWindowSoftInputMode() {
        MainActivity activity=getMainActivity();
        Window window=activity.getWindow();
        if (getSp().getBoolean("use_adjustPan", false)) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    public static void setMainBackground() {
        MainActivity activity=getMainActivity();
        ImageView img = activity.findViewById(0x7F0801BC);
        try {
            String file=Utils.getSp().getString("advanced_setting_editor_bg", null);
            if (file != null && file.trim().length() != 0) {
                if (file.toLowerCase().endsWith(".gif")) {
                    Glide.with(activity)
                    .load(file)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);
                } else {
                    Glide.with(activity)
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img);
                }
                return;
            }
        } catch (Throwable e) {
        }
        img.setImageDrawable(new ColorDrawable(HighlightUtils.getColorInt(ColorDefault.BACKGROUND)));
    }

	public static void openProject(String project, List<File> openedFile) {
		File projectFile=new File(project);
		if (projectFile.isFile())return;
		closeProjects();

		U.vy().Ws(project);

		if (openedFile == null || openedFile.size() == 0) {
			setFileBrowserCurrentDir(project);
			return;
		} 
		openFile(openedFile);
		setCurrentFiles(openedFile);
		setFileBrowserCurrentDir(project);

	}

	public static void closeProjects() {
		U.vy().v5();
		U.J8().Hw();
	}

	public static void setFileBrowserCurrentDir(String dir) {
		U.J8().j6(dir);
		//U.lg().ro();
		getFileBrowserService().edit().putString("CurrentDir", dir).commit();
	}

	public static String getFileBrowserCurrentDir() {
		return getFileBrowserService().getString("CurrentDir", null);
	}

	public static SplitView getSplitView() {
		return getMainActivity().findViewById(0x7f080124);
	}

	public static void openSplit(boolean isAnimation) {
		ReflectUtils.reflect(getSplitView()).method("openSplit", isAnimation);
	}

	public static void closeSplit(boolean isAnimation) {
		ReflectUtils.reflect(getSplitView()).method("closeSplit", isAnimation);
	}

	public static View getMainDrawerLayout() {
		return getMainActivity().findViewById(R.id.mainDrawerLayout);
	}

	public static boolean isDrawerOpen() {
		View dl=getMainDrawerLayout();
		if (dl != null) return (Boolean)ReflectUtils.reflect(dl).method("isDrawerOpen", Gravity.START).get();
		return false;
	}

	public static void toggleSplit() {
		View dl=getMainDrawerLayout();
		if (dl != null) {
			if (isDrawerOpen()) {
				closeSplit(true);
			} else {
				openSplit(true);
			}
        } else {
			ReflectUtils.reflect(getSplitView()).method("toggleSplit", (Runnable)null);
		}
	}

    public static void preferenceActivityMenuCreate(final PreferencesActivity act, Menu menu) {
        int page = act.getIntent().getIntExtra("SHOW_PAGE", -1);
        if (page == -2) { //MainActivity -> public lp()
            menu.add("高级设置").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					WebTranslateActivity.initDefaultapi();
					startActivity(act, AdvancedSettingsActivity.class);
					return false;
				}
			}).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    public static void updateBuild(final c a, final String msg) {
        runOnUiThread(getMainActivity(), new Runnable(){

			@Override
			public void run() {
				B b=ReflectUtils.reflect(a).field("a").get();
				fb.j6(b, msg, 50, false);
			}
		});
    }

	public static void openFile(List<File> file) {
		for (int i=0;i < file.size();i++) {
			openFile(file.get(i));
		}
		if (file.size() > 0) {
			openFile(file.get(0));
		}
	}

	public static void openFile(File file) {
		ReflectUtils.reflect(getMainActivity()).method("Hw", file.getAbsolutePath());
	}

    public static void initQuickKey() {
        ReflectUtils.reflect(getMainActivity()).method("FN");
    }

    public static MainActivity getMainActivity() {
        return Utils.getMainActivity();
    }

	public static FileBrowser getFileBrowser() {
		return ReflectUtils.reflect(getMainActivity()).method("QX").get();
	}

    public static AIDEEditorPager getAIDEEditorPager() {
        return ReflectUtils.reflect(getMainActivity()).method("J8").get();
    }

    public static AIDEEditor getCurrentEditor() {
        try {
            ReflectUtils reflect=ReflectUtils.reflect(getAIDEEditorPager());
            return reflect.method("getCurrentEditor").get();
        } catch (Throwable e) {}
        return null;
    }

    public static CodeEditText getCodeEditText() {
        return getCurrentEditor();
    }

    public static CodeEditText$c getCodeEditText_c() {
        try {
            ReflectUtils reflect=ReflectUtils.reflect(getCurrentEditor());
            return reflect.method("getOEditorView").get();
        } catch (Throwable e) {}
        return null;
    }

    public static OEditor getOEditorView() {
        return getCodeEditText_c();
    }

    public static OConsole getOConsole() {
        return (OConsole)((Object)getOEditorView());
    }

    public static boolean checkEditorFileParentName(String name) {
        try {
            return getCurrentEditorFile().getParentFile().getName().startsWith(name);
        } catch (Throwable e) {}
        return false;
    }

    public static boolean checkEditorFileName(String name) {
        try {
            return getCurrentEditorFile().getName().equals(name);
        } catch (Throwable e) {}
        return false;
    }

    public static boolean checkEditorFileNameSuffix(String name) {
        try {
            return getCurrentEditorFile().getName().endsWith(name);
        } catch (Throwable e) {}
        return false;
    }

    public static File getCurrentEditorFile() {
        AIDEEditor et=getCurrentEditor();
        if (et == null)return null;
        String f=et.getFilePath();
        return f == null ?null: new File(f);
    }

    public static void addToEditor(String content) {
        OEditor mOEditor=getOEditorView();
        if (mOEditor == null)return;

        int lineCount = 0;
        for (int i=0;i < content.length();i++) {
            if (content.charAt(i) == '\n') {
                lineCount++;
            }
        }

        int caretLine =mOEditor.getCaretLine();
        int total = lineCount + caretLine;

        mOEditor.getEditorModel().j6(mOEditor.getCaretColumn(), mOEditor.getCaretLine(), mOEditor.tp(), mOEditor.getTabSize(), new StringReader(content), mOEditor);

        mOEditor.gn(caretLine, total);
    }

	//----------sp----------

	public static SharedPreferences getProjectService() {
		return Utils.getSp("ProjectService");	
	}

	public static String getCurrentAppHome() {
		return getProjectService().getString("CurrentAppHome", null);
	}

	public static void setCurrentAppHome(String project) {
		getProjectService().edit().putString("CurrentAppHome", project).commit();
	}

	//////////////////////////////////////////////////////////////////////

	public static SharedPreferences getFileBrowserService() {
		return Utils.getSp("FileBrowserService");	
	}

	public static SharedPreferences getOpenFileService() {
		return getSp("OpenFileService");
	}

	public static void setCurrentFileToTop(File file) {
		List<File> l=getCurrentFiles();
		for (Iterator<File> iter = l.iterator(); iter.hasNext();) {
			if (iter.next().getAbsolutePath().equals(file.getAbsolutePath())) {
				iter.remove();//用AndroidQ以下的jar
			}
		}
		l.add(0, file);
		setCurrentFiles(l);
	}

	public static void setCurrentFiles(List<File> filelist) {
		StringBuffer sb=new StringBuffer();
		for (int i=0;i < filelist.size();i++) {
			File file=filelist.get(i);
			if (i > 0) {
				sb.append(";");
			}
			sb.append(file.getAbsolutePath());
		}
		getOpenFileService().edit().putString("CurrentFiles", sb.toString()).commit();
	}

	public static List<File> getCurrentFiles() {
		List<File> list=new ArrayList<>();
		String[] array=getOpenFileService().getString("CurrentFiles", "").split(";");
		for (int i=0;i < array.length;i++) {
			String f=array[i];
			if (f.trim().length() != 0) {
				list.add(new File(f));
			}
		}
		return list;
	}

    //////////////////////////////////////////////////////////////////////

    public static void setIsLightTheme(boolean isLightTheme) {
        Utils.getSp().edit().putBoolean("light_theme", isLightTheme).commit();
    }

    public static void notifyThemeChanged() {
        setIsLightTheme(!isLightTheme());
        setIsLightTheme(!isLightTheme());
    }


    public static boolean isLightTheme() {
        if (isTrainerMode())return true;
        return getSp().getBoolean("light_theme", true);
    }

    public static boolean isTrainerMode() {
        return getApp().getSharedPreferences("TrainerMode", 0).getBoolean("Mode", false);
    }

    public static boolean isUseProguard() {
        return getSp().getBoolean("use_proguard", false);
    }

	//----------sp----------

    public static void launcherPermissionEditor(Context context, String xmlpath) {
        Intent intent=new Intent(context, ManifestActivity.class);
        intent.putExtra("path", xmlpath);
        Utils.startActivity(context, intent);
    }

}

