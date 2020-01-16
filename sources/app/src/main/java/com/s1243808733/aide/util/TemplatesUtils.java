package com.s1243808733.aide.util;

import abcd.Sk;
import abcd.gj;
import com.aide.ui.U;
import com.blankj.utilcode.util.ResourceUtils;
import com.s1243808733.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TemplatesUtils {
	public static List<Sk.a> getTemplates(gj gj) {

		boolean z = U.ef() || U.j6.equals("com.aide.ui");
		Sk.b bVar = new Sk.b(gj, 1, "Android App", "Gradle/Android SDK/Java/Xml", "MyApp", true, false, "com.aide.ui", "ANDROID", "course_android", z);

	
		List<Sk.a> list=new ArrayList<>();

		try {
			JSONArray JSONArray=new JSONArray(ResourceUtils.readAssets2String(AIDEUtils.getAssetsConfigFile("templates.json")));
			for (int i=0;i < JSONArray.length();i++) {
				JSONObject item=JSONArray.getJSONObject(i);
				String title=item.getString("title");
				int icon = item.getInt("icon");
				String zip = item.getString("zip");
				JSONArray file=item.getJSONArray("file");
				String name=item.getString("name");

				String[] fileArray=new String[file.length()];
				for (int i2=0;i2 < file.length();i2++) {
					fileArray[i2] = file.getString(i2);
				}
				list.add(new Sk.a(title , bVar, icon, zip, fileArray, name));

			}
		} catch (Throwable e) {
			Utils.toast(e);
		}
		
		
		Sk.b bVar2 = new Sk.b(gj, 2, "Mobile Game", "libGDX/Java", "MyGame", true, false, "com.aide.ui", "GAME", "course_game", U.ef() || U.j6.equals("com.aide.ui"));
		list.add(new Sk.a("Mobile Game", bVar2, 2131165304, "course_game_libgdx_project.zip", new String[]{"MyGdxGame.java"}, "gdx-game-android"));
		
		//....

		Sk.b bVar3 = new Sk.b(gj, 4, "Native Android App", "Android NDK/C/Java/Xml", "MyNDKApp", true, false, "com.aide.ui", "ANDROID_NATIVE", "course_ndk", U.ef() || U.j6.equals("com.aide.ui"));
		list.add(new Sk.a("Native Android App", bVar3, 2131165302, "JniHelloWorld.zip", new String[]{"HelloJni.java", "hello-jni.c"}, null));
		
		Sk.b bVar4 = new Sk.b(gj, 5, "PhoneGap App", "PhoneGap/Html/Css/JavaScript", "MyPhoneGapApp", true, false, "com.aide.phonegap", "PHONEGAP", null,  U.ef() || U.j6.equals("com.aide.phonegap"));
		list.add(new Sk.a("PhoneGap App", bVar4, 2131165307, "PhoneGapAppNew.zip", new String[]{"index.html", "index.js", "index.css"}, "app"));

		return list;
	}

}
	
	

