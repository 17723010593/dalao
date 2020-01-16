package com.s1243808733.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import java.util.Locale;
import android.graphics.ColorFilter;

public class ColorBackgroundTextView extends TextView {

	private int rectangleSize = 10;

	{
		setPadding(dp2px(5), 0, dp2px(5), 0);
	}

	public ColorBackgroundTextView(Context ctx) {
		super(ctx);
	}

	public ColorBackgroundTextView(Context ctx, int color) {
		super(ctx);
		setColor(color);
	}

	public void setRectangleSize(int rectangleSize) {
		this.rectangleSize = rectangleSize;
	}

	public void setColor(int color) {
		setText(convertToARGB(color));
		AlphaPatternDrawable draw = new AlphaPatternDrawable(rectangleSize, color);
		setBackgroundDrawable(draw);
		setTextColor(color < 0xff757575 ?Color.WHITE: Color.BLACK);
	}

	public static String convertToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));

        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }

        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return ("#" + alpha + red + green + blue).toUpperCase(Locale.ENGLISH);
    }

	public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

	public class AlphaPatternDrawable extends Drawable {

		@Override
		public void setAlpha(int p1) {
		}

		@Override
		public void setColorFilter(ColorFilter p1) {
		}


		private int mRectangleSize = 10;

		private Paint mPaint = new Paint();
		private Paint mPaintWhite = new Paint();
		private Paint mPaintGray = new Paint();

		private int numRectanglesHorizontal;
		private int numRectanglesVertical;

		/**
		 * Bitmap in which the pattern will be cahched.
		 */
		private Bitmap mBitmap;

		private int backgroundColor=Color.TRANSPARENT;

		{
			mPaintWhite.setColor(0xffffffff);
			mPaintGray.setColor(0xffcbcbcb);
		}

		public AlphaPatternDrawable(int rectangleSize, int backgroundColor) {
			mRectangleSize = rectangleSize;
			this.backgroundColor = backgroundColor;
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawBitmap(mBitmap, null, getBounds(), mPaint);
			canvas.drawColor(backgroundColor);
		}

		@Override
		public int getOpacity() {
			return 0;
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);

			int height = bounds.height();
			int width = bounds.width();

			numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
			numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);

			generatePatternBitmap();

		}

		/**
		 * This will generate a bitmap with the pattern
		 * as big as the rectangle we were allow to draw on.
		 * We do this to chache the bitmap so we don't need to
		 * recreate it each time draw() is called since it
		 * takes a few milliseconds.
		 */
		private void generatePatternBitmap() {

			if (getBounds().width() <= 0 || getBounds().height() <= 0) {
				return;
			}

			mBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mBitmap);

			Rect r = new Rect();
			boolean verticalStartWhite = true;
			for (int i = 0; i <= numRectanglesVertical; i++) {

				boolean isWhite = verticalStartWhite;
				for (int j = 0; j <= numRectanglesHorizontal; j++) {

					r.top = i * mRectangleSize;
					r.left = j * mRectangleSize;
					r.bottom = r.top + mRectangleSize;
					r.right = r.left + mRectangleSize;

					canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

					isWhite = !isWhite;
				}

				verticalStartWhite = !verticalStartWhite;

			}

		}

	}


}

