package com.aide.ui;
import abcd.su;
import abcd.tu;
import abcd.uu;
import abcd.vu;
import abcd.wu;
import abcd.xu;
import android.content.Context;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.aide.engine.SourceEntity;
import com.aide.ui.h;
import com.s1243808733.aide.completion.CompletionAdapter;
import java.util.List;

@xu
class j$a extends ArrayAdapter<SourceEntity> {
    @uu
    private static /* synthetic */ boolean DW;
    @tu
    private static /* synthetic */ boolean j6;
    @vu
    /* final synthetic */ j FH;

    static {
        wu.j6(j$a.class, -1568526089479113304L, -1568526089479113304L);
    }

    @su(method = 33955199291344128L)
    public j$a(j jVar, Context context, List<SourceEntity> list) {
        super(null, 0);
    }

    @su(method = 4322290645157112875L)
    private void j6(TextView textView, int i, int i2) {
        try {
            if (j6) {
                wu.j6(-714438875286499328L, this, textView, new Integer(i), new Integer(i2));
            }
            ((Spannable) textView.getText()).setSpan(new StyleSpan(1), i, i2, 33);
        } catch (Throwable th) {
            if (DW) {
                wu.j6(th, -714438875286499328L, this, textView, new Integer(i), new Integer(i2));
            }
        }
    }

    @su(method = 2903740456553979296L)
    private void j6(TextView textView, int i, int i2, int i3) {
        try {
            if (j6) {
                wu.j6(-3005756478013671907L, this, textView, new Integer(i), new Integer(i2), new Integer(i3));
            }
            ((Spannable) textView.getText()).setSpan(new ForegroundColorSpan(i3), i, i2, 33);
        } catch (Throwable th) {
            if (DW) {
                wu.j6(th, -3005756478013671907L, this, textView, new Integer(i), new Integer(i2), new Integer(i3));
            }
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {

		View inflate;
		if (view == null) {
			inflate = LayoutInflater.from(getContext()).inflate(2131361800, viewGroup, false);
		} else {
			inflate = view;
		}
		
		SourceEntity sourceEntity = (SourceEntity) getItem(i);
		if (sourceEntity == null) {
			((TextView) inflate.findViewById(2131230757)).setText("No matches");
			((ImageView) inflate.findViewById(2131230756)).setImageResource(2131165201);
			inflate.findViewById(2131230758).setVisibility(8);
			return inflate;
		}
		TextView textView = (TextView) inflate.findViewById(2131230757);
		CharSequence we = sourceEntity.we();
		int i2 = h.j6[sourceEntity.EQ().ordinal()];
		String stringBuilder;
		if (i2 == 1 || i2 == 2 || i2 == 3) {
			String J8 = sourceEntity.J8();
			if (J8 != null) {
				StringBuilder stringBuilder2 = new StringBuilder();
				stringBuilder2.append(we);
				stringBuilder2.append(J8);
				stringBuilder = stringBuilder2.toString();
				textView.setText(stringBuilder, BufferType.SPANNABLE);
				j6(textView, we.length(), stringBuilder.length(), j.DW(this.FH).getResources().getColor(2131034120));
			} else {
				textView.setText(we);
			}
		} else if (i2 != 4) {
			if (i2 != 5) {
				textView.setText(we);
			} else {
				textView.setText(we, BufferType.SPANNABLE);
				j6(textView, 0, we.length());
			}
		} else if (sourceEntity.j3()) {
			StringBuilder stringBuilder3 = new StringBuilder();
			stringBuilder3.append(we);
			stringBuilder3.append(" - ");
			stringBuilder3.append(sourceEntity.Zo());
			stringBuilder = stringBuilder3.toString();
			textView.setText(stringBuilder, BufferType.SPANNABLE);
			j6(textView, we.length(), stringBuilder.length(), j.DW(this.FH).getResources().getColor(2131034120));
		} else {
			textView.setText(we);
		}
		ImageView imageView = (ImageView) inflate.findViewById(2131230756);
		int i3 = h.j6[sourceEntity.EQ().ordinal()];
		if (i3 != 1) {
			if (i3 != 2) {
				if (i3 == 3) {
					imageView.setImageResource(2131165195);
				} else if (i3 != 4) {
					if (i3 != 6) {
						imageView.setImageResource(2131165201);
					} else {
						imageView.setImageResource(2131165424);
					}
				} else if (sourceEntity.XL()) {
					imageView.setImageResource(2131165423);
				} else {
					imageView.setImageResource(2131165422);
				}
			} else if (sourceEntity.XL()) {
				imageView.setImageResource(2131165196);
			} else {
				imageView.setImageResource(2131165195);
			}
		} else if (sourceEntity.XL()) {
			imageView.setImageResource(2131165198);
		} else {
			imageView.setImageResource(2131165200);
		}


		//右边的info图标
		View findViewById = inflate.findViewById(2131230758);
		findViewById.setVisibility(8);
		/*
		 String VH = sourceEntity.VH();
		 findViewById.setVisibility(VH != null ? 0 : 8);
		 if (VH != null) {
		 findViewById.setOnClickListener(new i(this, VH));
		 }
		 */
		CompletionAdapter.setView(this, i, inflate);

		return inflate;

    }
}
