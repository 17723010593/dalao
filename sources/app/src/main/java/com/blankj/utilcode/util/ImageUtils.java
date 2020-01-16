package com.blankj.utilcode.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/12
 *     desc  : utils about image
 * </pre>
 */
public final class ImageUtils {

	public static Bitmap drawColor(Bitmap src, int color) {

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        final int width = src.getWidth(), height = src.getHeight();
        final Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(dest);
        final Bitmap alpha = src.extractAlpha();
        canvas.drawBitmap(alpha, 0, 0, paint);


        return dest;
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
										 drawable.getOpacity() != PixelFormat.OPAQUE
										 ? Bitmap.Config.ARGB_8888
										 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
										 drawable.getIntrinsicHeight(),
										 drawable.getOpacity() != PixelFormat.OPAQUE
										 ? Bitmap.Config.ARGB_8888
										 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


	public static boolean save(final Bitmap src,
                               final File file,
                               final CompressFormat format,
                               final boolean recycle) {
        if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) return false;
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }



    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }


    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}

