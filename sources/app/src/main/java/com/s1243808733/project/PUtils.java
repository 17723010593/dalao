package com.s1243808733.project;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.s1243808733.util.Utils;
import java.io.IOException;
import java.io.InputStream;

public class PUtils {
	public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

	public static String getAssetsFile(String child) {
		return Utils.getAssetsDataFile("templates/" + child).getAbsolutePath().substring(1);
	}

	public static Bitmap getIcon(Context ctx, String file) {
		try {
			InputStream is=ctx.getAssets().open(PUtils.getAssetsFile("icons/" + file));
			return BitmapFactory.decodeStream(is);
		} catch (IOException e) {}

		return null;
	}
}
