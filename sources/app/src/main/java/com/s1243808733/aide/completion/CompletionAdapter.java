package com.s1243808733.aide.completion;
import android.view.View;
import android.widget.TextView;
import com.aide.engine.SourceEntity;
import com.aide.ui.g;
import com.aide.ui.j$a;
import com.aide.ui.views.CompletionListView;
import com.blankj.utilcode.util.ReflectUtils;
import com.s1243808733.aide.completion.translate.TranslateUtils;
import com.s1243808733.aide.util.AdvancedSetting;

public class CompletionAdapter {

	public static void setView(j$a adapter, int position, View inflate) {
		TextView textView=(TextView)inflate.findViewById(2131230757);
		Object itemObject =adapter.getItem(position);
		SourceEntity sourceEntity=itemObject instanceof SourceEntity 
			?(SourceEntity)itemObject: null;


		TranslateUtils.translate(textView);
		

	}

    public static void onItemLongClick(g listener, View view, int position) {
		ReflectUtils reflect=ReflectUtils.reflect(listener);
		CompletionListView completionListView=reflect.field("FH").get();

		Object itemObject =completionListView.getItemAtPosition(position);
		SourceEntity sourceEntity=itemObject instanceof SourceEntity 
			?(SourceEntity)itemObject: null;

		TranslateUtils.onItemLongClick(sourceEntity, view, position);

	}

}
