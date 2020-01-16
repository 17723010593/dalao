package com.aide.ui.browsers;

import abcd.F;
import abcd.su;
import abcd.sv;
import abcd.tu;
import abcd.uu;
import abcd.vu;
import abcd.wu;
import abcd.xu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import com.aide.ui.U;
import com.aide.ui.browsers.FileBrowser.b;
import com.aide.ui.views.CustomKeysListView;
import com.s1243808733.util.ClassUtils;
import com.s1243808733.util.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.blankj.utilcode.util.ReflectUtils;
import com.s1243808733.aide.util.MainInterface;
import com.s1243808733.util.Toasty;

@xu
class j implements OnItemClickListener {
    @uu
    private static /* synthetic */ boolean DW;
    @tu
    private static /* synthetic */ boolean j6;
    final /* synthetic */ CustomKeysListView FH;
    @vu
    final /* synthetic */ FileBrowser Hw;

    static {
        wu.j6(j.class, 5630979006676181253L, 974175974983634845L);
    }

    j(FileBrowser fileBrowser, CustomKeysListView customKeysListView) {
        this.Hw = fileBrowser;
        this.FH = customKeysListView;
    }

    @su(method = 5077622207525730777L)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
		MainInterface.onFileBrowserItemClick(this,adapterView, view, i,j);
	}
	
	
}
