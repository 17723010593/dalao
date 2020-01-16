package com.s1243808733.project;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.google.gson.Gson;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.aide.util.AdvancedSetting;
import com.s1243808733.project.adapter.TemplateAdapter;
import com.s1243808733.project.jsonbean.Category;
import com.s1243808733.project.jsonbean.CategoryList;
import com.s1243808733.project.jsonbean.Project;
import com.s1243808733.util.Toasty;
import com.s1243808733.util.Utils;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProjectDialog {

	public static void showCreateDialog(final Activity activity) {
		ExpandableListView expandableListView = new ExpandableListView(activity);
		final AlertDialog dialog=new AlertDialog.Builder(activity)
		.setTitle("创建工程")
		.setView(expandableListView)
		.setPositiveButton("更多", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ReflectUtils.reflect("com.aide.ui.ib").method("EQ_SOURCE", activity);
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.create();
		dialog.show();

		try {
			List<Category> list= new Gson().fromJson(new InputStreamReader(activity.getAssets().open(PUtils.getAssetsFile("template.json"))), CategoryList.class).getCategory();			
			final TemplateAdapter adapter = new TemplateAdapter(activity, list);
			expandableListView.setAdapter(adapter);
			expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					Category.Templates item=adapter.getChild(groupPosition, childPosition);
					createProject(activity, item);
					dialog.dismiss();
					return false;
				}
			});
			if (adapter.getGroupCount() > 0) {
				expandableListView.expandGroup(0, false);
			}
		} catch (Throwable e) {
		}


	}

	private static File getCreateLocation(File parent, String name, int i) {
		File f=new File(parent, i > 1 ? name + i: name);
		if (!f.exists()) {
			return f;
		}
		return getCreateLocation(parent, name, i + 1);
	}

	private static void createProject(final Activity act, final Category.Templates templates) {
		final Project projectInfo = templates.getProject();
		final CreateProjectView view=new CreateProjectView(act);

		AlertDialog.Builder builder=new AlertDialog.Builder(act)
		.setTitle(templates.getTitle())
		.setView(view)
		.setPositiveButton("创建", null)
		.setNegativeButton("取消", null);

		Bitmap icon=PUtils.getIcon(act, templates.getIcon());
		if (icon != null) {
			builder.setIcon(new BitmapDrawable(icon));
		}
		final AlertDialog dialog=builder.create();
        dialog.show();

		final EditText input_projectName=view.input_projectName.getEditText();
		final EditText input_packageName=view.input_packageName.getEditText();
		if (projectInfo.isNoPkg())	{
			input_packageName.setEnabled(false);
			view.input_packageName.setVisibility(View.GONE);
		}

        String fileBrowserCurrentDir=AIDEUtils.getFileBrowserCurrentDir();
		final File createLocation=fileBrowserCurrentDir != null ? new File(fileBrowserCurrentDir)
        : new File(Environment.getExternalStorageDirectory(), "AppProjects");


		final Button button1=dialog.getButton(dialog.BUTTON_POSITIVE);
		TextWatcher textWatcher=new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				if (input_projectName.getText().toString().trim().length() == 0
					|| new File(createLocation, input_projectName.getText().toString()).exists()) {
					button1.setEnabled(false);
				} else if (input_packageName.isEnabled() 
						   && input_packageName.getText().toString().trim().length() == 0) {
					button1.setEnabled(false);
				} else {
					button1.setEnabled(true);
				}
			}

			@Override
			public void afterTextChanged(Editable p1) {
			}
		};
		input_projectName.addTextChangedListener(textWatcher);
		input_packageName.addTextChangedListener(textWatcher);

		input_projectName.setText(getCreateLocation(createLocation, "MyApp", 1).getName());
		input_packageName.setText(AdvancedSetting.getPackagePrefix(input_projectName.getText().toString()));

		button1.setOnClickListener(new View.OnClickListener() {

			private String projectName;

			private String packageName;

			private Project projectInfo;

			@Override
			public void onClick(View view) {
				projectName = input_projectName.getText().toString();
				packageName = input_packageName.getText().toString();
				projectInfo = templates.getProject();
				dialog.dismiss();
				new CreateTask().execute();
			}

			private class CreateTask extends AsyncTask<Void,Void,Void> {

				private File dest;

				private ProgressDialog pd;

				public CreateTask() {
					pd = ProgressDialog.show(act, null, "正在创建工程中...", true, false);
				}

				@Override
				protected Void doInBackground(Void[] p1) {
					try {
						File srcZip=new File(act.getCacheDir(), "templates_temp.zip");
						ResourceUtils.copyFileFromAssets(PUtils.getAssetsFile(projectInfo.getTemplate()), srcZip.getAbsolutePath());
						dest = new File(createLocation, projectName);
						ZipUtils.unzipFile(srcZip, dest);
						srcZip.delete();
						replaces(dest);
					} catch (IOException e) {
						Toasty.error(Utils.getStackTrace(e));
					}

					return null;
				}				

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					try {
						File main=dest;
						if (!TextUtils.isEmpty(projectInfo.getMain())) {
							main = new File(dest, projectInfo.getMain());
						}
						List<File> openedFile=new ArrayList<>();
						for (String path:projectInfo.getOpenFile()) {
							if (path.trim().length() != 0) {
								path = path.replace("$package_name$", packageName.replace(".", "/"));
								File file=new File(main, path);
								if (file.exists() && file.isFile()) {
									openedFile.add(file);
								}
							}
						}
						AIDEUtils.openProject(main.getAbsolutePath(), openedFile);

					} catch (Throwable e) {
						Toasty.error(Utils.getStackTrace(e)).show();
					} finally {
						pd.dismiss();
					}
				}
			}

			private void replaces(File dir) {
				File[] lists=dir.listFiles(new FileFilter(){

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
				if (lists == null)return;
				for (File file:lists) {

					if (file.getName().contains("$package_name$")) {
						String pkg=file.isDirectory() ?packageName.replace(".", "/"): packageName;
						String newname=file.getName().replace("$package_name$", pkg);
						File newfile=new File(file.getParentFile(), newname);
						if (newfile.isDirectory()) {
							newfile.mkdirs();
						} else {
							newfile.getParentFile().mkdirs();
						}
						file.renameTo(newfile);
						file = newfile;
					}

					if (file.isFile()) {
						String data=FileIOUtils.readFile2String(file);
						if (data != null) {
							String newdata=data.replace("$package_name$", packageName)
							.replace("$project_name$", projectName);
							FileIOUtils.writeFileFromString(file, newdata);
						}
					} else {
						replaces(file);
					}

				}

			}


		});

	}

	private static class CreateProjectView extends LinearLayout {

		private TitleEditText input_projectName;

		private TitleEditText input_packageName;

		public CreateProjectView(Context ctx) {
			super(ctx);
			input_projectName = new TitleEditText(ctx, "项目名：");
			input_packageName = new TitleEditText(ctx, "包名：");

			setOrientation(VERTICAL);
			addView(input_projectName);
			addView(input_packageName);
			setPadding(PUtils.dp2px(24), PUtils.dp2px(10), PUtils.dp2px(24), 0);
		}
	}

	private static class TitleEditText extends LinearLayout {
		private EditText editText;
		public TitleEditText(Context ctx, CharSequence title) {
			super(ctx);
			editText = new EditText(ctx);
			setOrientation(VERTICAL);
			TextView tv=new TextView(ctx);
			tv.setText(title);
			tv.setTextSize(17);
			addView(tv);
			addView(editText, -1, -2);
		}
		public EditText getEditText() {
			return editText;
		}
	}



}
