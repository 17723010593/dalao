package com.s1243808733.permission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.s1243808733.app.base.BaseActivity;
import com.s1243808733.util.Toasty;
import com.s1243808733.util.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ManifestActivity extends BaseActivity 
implements PermissionAdapter.OnSelectionListener,PermissionView.onItemClick,PermissionView.onItemLongClick {

	private PermissionView permissionView;

	private ActionMode actionMode;

	private boolean isDataChange;

	private File sourceFile;

	private static List<Permission> system_permission;

	private MenuItem menuItem_save;

	public void setDataChange(boolean isDataChange) {
		this.isDataChange = isDataChange;
		if (menuItem_save != null) {
			menuItem_save.setEnabled(isDataChange);
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String path=getIntent().getStringExtra("path");
		if (path != null) {
			sourceFile = new File(path);
		}
		if (sourceFile == null || !sourceFile.exists() || sourceFile.isDirectory()) {
			finish();
			return;
		}
		permissionView = new PermissionView(this);
        setContentView(permissionView);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("权限编辑");

		PermissionAdapter adapter=permissionView.getAdapter();
		adapter.setOnListDataChangeListener(new PermissionAdapter.OnListDataChangeListener(){

			@Override
			public List<Permission> onListDataChange(List<Permission> list) {
				if (list.isEmpty()) {
                    if (permissionView.getSearchView().getText().toString().length() != 0) {
                        list.add(new CommonItem("没有搜索结果", null));
                    } else {
                        list.add(new CommonItem("没有权限", "点击右上角“添加”按钮来添加权限"));
                    }
				}
				return list;
			}
		});

		permissionView.setOnSelectionListener(this);
		permissionView.setOnItemClickListener(this);
		permissionView.setOnItemLongClickListener(this);

        try {
            permissionView.load(sourceFile);
        } catch (Exception e) {
            Utils.toast(e);
            finish();
            return;
		}
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menuItem_save = menu.add("保存").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem p1) {
				savePermission(false);
				return false;
			}
		});
		menuItem_save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		setDataChange(false);

		menu.add("添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem p1) {
				showAddPermissionDialog();
				return false;
			}
		}).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		int checkGroupId=100;
		menu.add(checkGroupId, 0, 0, "自动注释权限").setChecked(PermissionUtils.isAnnotationPermission())
		.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem p1) {
				p1.setChecked(!p1.isChecked());
				return getSp().edit().putBoolean("annotation_permission", p1.isChecked()).commit();
			}
		});
		menu.setGroupCheckable(checkGroupId, true, false);

		menu.add("多选操作").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem p1) {
				multipleMode();
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	private void showAddPermissionDialog() {
		final PermissionView view=new PermissionView(this, -1, -2);
		final PermissionAdapter adapter=view.getAdapter();
		adapter.setOnListDataChangeListener(new PermissionAdapter.OnListDataChangeListener(){


			@Override
			public List<Permission> onListDataChange(List<Permission> list) {
				if (list.isEmpty()) {
					if (view.getSearchView().getText().toString().length() == 0) {
						list.add(new CommonItem("没有权限", null));
					} else {
						list.add(new CommonItem("没有搜索结果", null));
					}

				}
				return list;
			}
		});


        final List<Permission> selected=new ArrayList<>();
        selected.addAll(permissionView.getAdapter().getSourceList());

		final String format="添加权限 （已选%d项）";
		final AlertDialog dialog=new AlertDialog.Builder(this)
		.setTitle(String.format(format, 0))
		.setView(view)
		.setPositiveButton("添加", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				PermissionAdapter mainAdapter=permissionView.getAdapter();
                mainAdapter.setSourceList(selected);
				mainAdapter.setList(mainAdapter.getSourceList());
				if (permissionView.getSearchView().getText().toString().length() != 0) {
					permissionView.search(permissionView.getSearchView().getText().toString());
				} else {
					mainAdapter.notifyDataSetChanged();
				}
				setDataChange(true);
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.create();
        dialog.show();

		view.setSelectionMode(true);

		PermissionAdapter.OnSelectionListener onSelectionListener=new PermissionAdapter.OnSelectionListener(){

            @Override
            public void onSelection(List<Permission> list, int selectedCount) {
                for (Permission p:list) {
                    if (!PermissionUtils.hasAdded(selected, p)) {
                        selected.add(p);
                    }
                }
				dialog.setTitle(String.format(format, selected.size()));
            }

            @Override
            public void onCancelSelection(List<Permission> list, int selectedCount) {
                for (Iterator<Permission> iter = selected.iterator(); iter.hasNext();) {
                    Permission permission = iter.next();
                    for (int i=0;i < list.size();i++) {
                        if (list.get(i).getPermission().equals(permission.getPermission())) {
                            iter.remove();
                        }
                    }
                }
                dialog.setTitle(String.format(format, selected.size()));

            }


		};

		view.setOnSelectionListener(onSelectionListener);
        view.setOnItemClickListener(new PermissionView.onItemClick(){

            @Override
            public void onItemClick(Permission item, View view, int position) {

            }
        });
		view.setOnItemLongClickListener(new PermissionView.onItemLongClick(){

			@Override
			public boolean onItemLongClick(Permission item, View view, final int position) {
				if (!(item instanceof CommonItem)) {
					PermissionAdapter.ViewHolder holder=((PermissionAdapter.ItemView)view).holder;
					final boolean isChecked=holder.cb.isChecked();
					String describe=item.getDescribe();
					AlertDialog dialog=new AlertDialog.Builder(ManifestActivity.this)
					.setTitle(item.getName())
					.setMessage(describe == null ?"没有描述": describe)
					.setPositiveButton(isChecked ?"取消选择": "选择", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dia, int which) {
							adapter.setSelection(position, !isChecked);
							adapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton(android.R.string.cancel, null)
					.create();
					dialog.show();
					return true;
				}
				return false;
			}
		});

		if (system_permission == null) {
			system_permission = new ArrayList<>();
            Class<?> clazz=android.Manifest.permission.class;
            for (Field field:clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    system_permission.add(new Permission(field.get(null).toString()));
                } catch (Throwable e) {}

            }

			Collections.sort(system_permission);
		}

		view.load(system_permission);
		int j=-1;
		List<String> selectedlist=new ArrayList<>();
		for (int i=0;i < system_permission.size();i++) {
			Permission permission=system_permission.get(i);
			if (PermissionUtils.hasAdded(permissionView.getAdapter().getSourceList(), permission.getPermission())) {
				if (j < 0) {
					j = i;
				}
				selectedlist.add(permission.getPermission());
			}
		}
		if (j >= 0) {
			adapter.setSelectionList(selectedlist);
		}
        dialog.setTitle(String.format(format, selectedlist.size()));

		adapter.notifyDataSetChanged();
		view.getListView().setSelection(j);

	}

	@Override
	public void onItemClick(final Permission item, View view, final int position) {
		if (!permissionView.isSelectionMode() && !(item instanceof CommonItem)) {
			final PermissionAdapter adapter=permissionView.getAdapter();
			AlertDialog dialog=new AlertDialog.Builder(ManifestActivity.this)
			.setTitle(item.getName())
			.setMessage(item.getDescribe())
			.setPositiveButton("删除", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					List<Permission> source=adapter.getSourceList();
                    List<Permission> newlist=new ArrayList<>();
                    for (int i=0;i < source.size();i++) {
                        if (!source.get(i).getPermission().equals(item.getPermission())) {
                            newlist.add(source.get(i));
                        }
                    }
                    adapter.setSourceList(newlist);
                    adapter.setList(newlist);
                    adapter.notifyDataSetChanged();
					setDataChange(true);
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create();
			dialog.show();
		}
	}

	@Override
	public boolean onItemLongClick(Permission item, View view, final int position) {
		if (!permissionView.isSelectionMode() && !(item instanceof CommonItem)) {
			final PermissionAdapter adapter=permissionView.getAdapter();
			multipleMode();
			adapter.setSelection(position, true);
			return true;
		}

		return false;
	}

	private void multipleMode() {
		final PermissionAdapter adapter=permissionView.getAdapter();
		actionMode = startActionMode(new ActionMode.Callback(){

			@Override
			public boolean onCreateActionMode(final ActionMode p1, Menu menu) {

				menu.add("删除").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						List<Permission> source=adapter.getSourceList();
						List<Permission> newlist=new ArrayList<>();
						for (int i=0;i < source.size();i++) {
							Permission item=source.get(i);
							if (!adapter.isSelection(item.getPermission())) {
								newlist.add(item);
							}
						}
						adapter.setSourceList(newlist);
                        adapter.setList(newlist);
                        adapter.notifyDataSetChanged();
						setDataChange(true);
						p1.finish();
						return false;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

				menu.add("全选").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						permissionView.selectionAll(true);
						adapter.notifyDataSetChanged();
						return false;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

				menu.add("全不选").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						permissionView.selectionAll(false);
						adapter.notifyDataSetChanged();
						return false;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

				menu.add("反选").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem p1) {
						permissionView.invertSelection();
						adapter.notifyDataSetChanged();
						return false;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

				return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode p1, Menu p2) {
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode p1, MenuItem p2) {
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode p1) {
				permissionView.setSelectionMode(false);

			}
		});

		actionMode.setTitle("多选操作");
		actionMode.setSubtitle(null);

		permissionView.setSelectionMode(true);

	}

	private void savePermission(boolean saveAndFinish) {
		FileOutputStream fos=null;

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String data=ManifestUtils.annotationPermission(permissionView.getAdapter().getSourceList(), db.parse(sourceFile), PermissionUtils.isAnnotationPermission());
			fos = new FileOutputStream(sourceFile);
			fos.write(data.getBytes());
			Toast toast=Toasty.success("保存成功");
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			setDataChange(false);
			if (saveAndFinish) {
				finish();
			}
		} catch (Throwable e) {
			Utils.showExDialog(this, e);
		} finally {
			if (fos != null) {
				try {
					fos.flush();
				} catch (IOException e) {}
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (isDataChange) {
			AlertDialog dialog=new AlertDialog.Builder(this)
			.setTitle("保存修改")
			.setMessage("数据已发生变化，是否保存？")
			.setPositiveButton("保存", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					savePermission(true);
				}
			})
			.setNegativeButton("不保存", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.create();
			dialog.show();
			return;
		}
		super.onBackPressed();
	}



    @Override
    public void onSelection(List<Permission> list, int selectedCount) {
        onSelection(selectedCount);
    }

    @Override
    public void onCancelSelection(List<Permission> list, int selectedCount) {
        onSelection(selectedCount);
    }

	public void onSelection(int selectedCount) {
		if (actionMode != null) {
            actionMode.setSubtitle(String.format("已选 %d 项", selectedCount));
		}
	}

    SharedPreferences getSp() {
		return getSharedPreferences("PermissionEditor", 0);
	}

}
