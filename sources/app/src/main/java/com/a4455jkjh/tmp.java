package com.a4455jkjh;
import abcd.fl;
import abcd.ml;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.blankj.utilcode.util.ReflectUtils;
import com.s1243808733.util.ClassUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.s1243808733.util.BatchReplace;

public class tmp extends Activity {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        BatchReplace.showDialog(this,"/storage/emulated/0/AppProjects/MyApp5/app/src/main/");
		
	}


}
