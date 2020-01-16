package com.aide.ui;

import abcd.su;
import abcd.tu;
import abcd.uu;
import abcd.vu;
import abcd.wu;
import abcd.xu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.aide.engine.SourceEntity;
import com.aide.ui.views.CompletionListView;
import com.s1243808733.aide.completion.translate.TranslateUtils;
import com.s1243808733.aide.completion.CompletionAdapter;
@xu
class g implements OnItemLongClickListener {
    @uu
    private static /* synthetic */ boolean DW;
    @tu
    private static /* synthetic */ boolean j6;
    final /* synthetic */ CompletionListView FH;
    @vu
    final /* synthetic */ j Hw;

    static {
        wu.j6(g.class, 1778539205842150985L, -7057826395358441455L);
    }

    g(j jVar, CompletionListView completionListView) {
        this.Hw = jVar;
        this.FH = completionListView;

    }

    @su(method = 2314983845235487112L)
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
		
		CompletionAdapter.onItemLongClick(this, view, i);
		
        return true;

    }
}
