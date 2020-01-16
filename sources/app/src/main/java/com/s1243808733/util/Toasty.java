package com.s1243808733.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.xutils.x;
import com.a4455jkjh.R;

/**
 * This file is part of Toasty.
 * <p>
 * Toasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Toasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Toasty.  If not, see <http://www.gnu.org/licenses/>.
 */

@SuppressLint("InflateParams")
public class Toasty {
    private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
    private static Typeface currentTypeface = LOADED_TOAST_TYPEFACE;
    private static int textSize = 14; // in SP

    private static boolean tintIcon = true;
    private static boolean allowQueue = true;

    private static Toast lastToast = null;

    public static boolean enable=false;

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    private Toasty() {
        // avoiding instantiation
    }

	public static Toast getToast() {
		return lastToast;
	}

	public static void cancel() {
		if (getToast() != null)getToast().cancel();
	}


	//////////////


	@CheckResult
    public static Toast toast(@StringRes int message) {
        return toast(x.app().getString(message));
    }

	@CheckResult
    public static Toast toast(CharSequence message) {
        return toast(x.app(), message, LENGTH_SHORT);
    }

	@CheckResult
    public static Toast toast(Context ctx, @StringRes int message, int duration) {
        return toast(ctx, ctx.getString(message), duration);
    }

	@CheckResult
    public static Toast toast(Context ctx, CharSequence message, int duration) {
        return info(message, duration, true);
    }


	@CheckResult
    public static Toast success(Context ctx, int message, int duration) {
        return success(message, duration, true);
    }


	@CheckResult
    public static Toast success(Context ctx, @NonNull CharSequence message, int duration) {
        return success(message, duration, true);
    }


	//////////////


    @CheckResult
    public static Toast normal(@StringRes int message) {
        return normal(x.app().getString(message), Toast.LENGTH_SHORT, null, false);
    }

    @CheckResult
    public static Toast normal(@NonNull CharSequence message) {
        return normal(message, Toast.LENGTH_SHORT, null, false);
    }

    @CheckResult
    public static Toast normal(@StringRes int message, Drawable icon) {
        return normal(x.app().getString(message), Toast.LENGTH_SHORT, icon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull CharSequence message, Drawable icon) {
        return normal(message, Toast.LENGTH_SHORT, icon, true);
    }

    @CheckResult
    public static Toast normal(@StringRes int message, int duration) {
        return normal(x.app().getString(message), duration, null, false);
    }

    @CheckResult
    public static Toast normal(@NonNull CharSequence message, int duration) {
        return normal(message, duration, null, false);
    }

    @CheckResult
    public static Toast normal(@StringRes int message, int duration,
                               Drawable icon) {
        return normal(x.app().getString(message), duration, icon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull CharSequence message, int duration,
                               Drawable icon) {
        return normal(message, duration, icon, true);
    }

    @CheckResult
    public static Toast normal(@StringRes int message, int duration,
                               Drawable icon, boolean withIcon) {
        return custom(x.app().getString(message), icon, ToastyUtils.getColor(R.color.normalColor),
					  ToastyUtils.getColor(R.color.defaultTextColor), duration, withIcon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull CharSequence message, int duration,
                               Drawable icon, boolean withIcon) {
        return custom(message, icon, ToastyUtils.getColor(R.color.normalColor),
					  ToastyUtils.getColor(R.color.defaultTextColor), duration, withIcon, true);
    }

    @CheckResult
    public static Toast warning(@StringRes int message) {
        return warning(x.app().getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast warning(@NonNull CharSequence message) {
        return warning(message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast warning(@StringRes int message, int duration) {
        return warning(x.app().getString(message), duration, true);
    }

    @CheckResult
    public static Toast warning(@NonNull CharSequence message, int duration) {
        return warning(message, duration, true);
    }

    @CheckResult
    public static Toast warning(@StringRes int message, int duration, boolean withIcon) {
        return custom(x.app().getString(message), ToastyUtils.getDrawable(R.drawable.ic_error_outline_white_24dp),
					  ToastyUtils.getColor(R.color.warningColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast warning(@NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(message, ToastyUtils.getDrawable(R.drawable.ic_error_outline_white_24dp),
					  ToastyUtils.getColor(R.color.warningColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast info(@StringRes int message) {
        return info(x.app().getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast info(@NonNull CharSequence message) {
        return info(message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast info(@StringRes int message, int duration) {
        return info(x.app().getString(message), duration, true);
    }

    @CheckResult
    public static Toast info(@NonNull CharSequence message, int duration) {
        return info(message, duration, true);
    }

    @CheckResult
    public static Toast info(@StringRes int message, int duration, boolean withIcon) {
        return custom(x.app().getString(message), ToastyUtils.getDrawable(R.drawable.ic_info_outline_white_24dp),
					  ToastyUtils.getColor(R.color.infoColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast info(@NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(message, ToastyUtils.getDrawable(R.drawable.ic_info_outline_white_24dp),
					  ToastyUtils.getColor(R.color.infoColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast success(@StringRes int message) {
        return success(x.app().getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast success(@NonNull CharSequence message) {
        return success(message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast success(@StringRes int message, int duration) {
        return success(x.app().getString(message), duration, true);
    }

    @CheckResult
    public static Toast success(@NonNull CharSequence message, int duration) {
        return success(message, duration, true);
    }

    @CheckResult
    public static Toast success(@StringRes int message, int duration, boolean withIcon) {
        return custom(x.app().getString(message), ToastyUtils.getDrawable(R.drawable.ic_check_white_24dp),
					  ToastyUtils.getColor(R.color.successColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast success(@NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(message, ToastyUtils.getDrawable(R.drawable.ic_check_white_24dp),
					  ToastyUtils.getColor(R.color.successColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast error(@StringRes int message) {

        return error(x.app().getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast error(@NonNull CharSequence message) {
        return error(message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast error(@StringRes int message, int duration) {
        return error(x.app().getString(message), duration, true);
    }

    @CheckResult
    public static Toast error(@NonNull CharSequence message, int duration) {
        return error(message, duration, true);
    }

    @CheckResult
    public static Toast error(@StringRes int message, int duration, boolean withIcon) {
        return custom(x.app().getString(message), ToastyUtils.getDrawable(R.drawable.ic_clear_white_24dp),
					  ToastyUtils.getColor(R.color.errorColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast error(@NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(message, ToastyUtils.getDrawable(R.drawable.ic_clear_white_24dp),
					  ToastyUtils.getColor(R.color.errorColor), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, true);
    }

    @CheckResult
    public static Toast custom(@StringRes int message, Drawable icon,
                               int duration, boolean withIcon) {
        return custom(x.app().getString(message), icon, -1, ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, false);
    }

    @CheckResult
    public static Toast custom(@NonNull CharSequence message, Drawable icon,
                               int duration, boolean withIcon) {
        return custom(message, icon, -1, ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, false);
    }

    @CheckResult
    public static Toast custom(@StringRes int message, @DrawableRes int iconRes,
                               @ColorRes int tintColorRes, int duration,
                               boolean withIcon, boolean shouldTint) {
        return custom(x.app().getString(message), ToastyUtils.getDrawable(iconRes),
					  ToastyUtils.getColor(tintColorRes), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, shouldTint);
    }

    @CheckResult
    public static Toast custom(@NonNull CharSequence message, @DrawableRes int iconRes,
                               @ColorRes int tintColorRes, int duration,
                               boolean withIcon, boolean shouldTint) {
        return custom(message, ToastyUtils.getDrawable(iconRes),
					  ToastyUtils.getColor(tintColorRes), ToastyUtils.getColor(R.color.defaultTextColor),
					  duration, withIcon, shouldTint);
    }

    @CheckResult
    public static Toast custom(@StringRes int message, Drawable icon,
                               @ColorRes int tintColorRes, int duration,
                               boolean withIcon, boolean shouldTint) {
        return custom(x.app().getString(message), icon, ToastyUtils.getColor(tintColorRes),
					  ToastyUtils.getColor(R.color.defaultTextColor), duration, withIcon, shouldTint);
    }

    @CheckResult
    public static Toast custom(@StringRes int message, Drawable icon,
                               @ColorRes int tintColorRes, @ColorRes int textColorRes, int duration,
                               boolean withIcon, boolean shouldTint) {
        return custom(x.app().getString(message), icon, ToastyUtils.getColor(tintColorRes),
					  ToastyUtils.getColor(textColorRes), duration, withIcon, shouldTint);
    }

    @SuppressLint("ShowToast")
    @CheckResult
    public static Toast custom(@NonNull CharSequence message, Drawable icon,
                               @ColorInt int tintColor, @ColorInt int textColor, int duration,
                               boolean withIcon, boolean shouldTint) {
        final Toast currentToast = Toast.makeText(x.app(), "", duration);
        if (!enable) {
            currentToast.setText(message);
            return currentToast;
        }
        final View toastLayout = ((LayoutInflater) x.app().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        .inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        if (shouldTint)
            drawableFrame = ToastyUtils.tint9PatchDrawableFrame(tintColor);
        else
            drawableFrame = ToastyUtils.getDrawable(R.drawable.toast_frame);
        ToastyUtils.setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null)
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            ToastyUtils.setBackground(toastIcon, tintIcon ? ToastyUtils.tintIcon(icon, textColor) : icon);
        } else {
            toastIcon.setVisibility(View.GONE);
        }

        toastTextView.setText(message);
        toastTextView.setTextColor(textColor);
        toastTextView.setTypeface(currentTypeface);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        currentToast.setView(toastLayout);
        
        if (!allowQueue) {
            if (lastToast != null)
                lastToast.cancel();
        }

        lastToast = currentToast;

        return currentToast;
    }

    public static class Config {
        private Typeface typeface = Toasty.currentTypeface;
        private int textSize = Toasty.textSize;

        private boolean tintIcon = Toasty.tintIcon;
        private boolean allowQueue = true;

        private Config() {
            // avoiding instantiation
        }

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        public static void reset() {
            Toasty.currentTypeface = LOADED_TOAST_TYPEFACE;
            Toasty.textSize = 14;
            Toasty.tintIcon = true;
            Toasty.allowQueue = true;
        }

        @CheckResult
        public Config setToastTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Config setTextSize(int sizeInSp) {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon) {
            this.tintIcon = tintIcon;
            return this;
        }

        @CheckResult
        public Config allowQueue(boolean allowQueue) {
            this.allowQueue = allowQueue;
            return this;
        }

        public void apply() {
            Toasty.currentTypeface = typeface;
            Toasty.textSize = textSize;
            Toasty.tintIcon = tintIcon;
            Toasty.allowQueue = allowQueue;
        }
    }




	static class ToastyUtils {
		private ToastyUtils() {
		}

		static Drawable tintIcon(@NonNull Drawable drawable, @ColorInt int tintColor) {
			drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
			return drawable;
		}

		static Drawable tint9PatchDrawableFrame(@NonNull @ColorInt int tintColor) {
			final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(R.drawable.toast_frame);
			return tintIcon(toastDrawable, tintColor);
		}

		static void setBackground(@NonNull View view, Drawable drawable) {
			view.setBackgroundDrawable(drawable);
		}

		static Drawable getDrawable(@DrawableRes int id) {
			return Utils.getApp().getResources().getDrawable(id);
		}

		static int getColor(@ColorRes int color) {
			return Utils.getColorFromResources(color);
		}
	}

}


