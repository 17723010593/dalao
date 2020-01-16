package com.aide.ui.views;

import android.graphics.Color;
import com.aide.ui.views.editor.OEditor;
import com.aide.ui.views.editor.j;
import com.s1243808733.aide.highlight.HighlightUtils;
import com.s1243808733.aide.highlight.color.ColorDefault;

public class CodeEditText$c extends OEditor {
    CodeEditText PH;
    private boolean xg;

    public CodeEditText$c() {
        super(null);
    }

    public void Hw() {
        super.Hw();
        j jVar = null;
        if (this.PH != null) {
            j jVar2;
            if (com.aide.common.g.QX(getContext())) {//判断是不是material主题
                //this.aj = new j(getResources().getColor(this.PH.J0() ? 2131034151 : 2131034150));
                this.aj = new j(HighlightUtils.getColorInt(ColorDefault.SELECTED_BACKGROUND));
            } else {
                this.aj = new j(getResources().getColor(this.PH.J0() ? 2131034149 : 2131034148));
            }
            if (this.xg) {
                jVar2 = new j(HighlightUtils.getColorInt(ColorDefault.LINE_BACKGROUND));
                // jVar2 = new j(getResources().getColor(this.PH.J0() ? 2131034139 : 2131034138));
            } else {
                jVar2 = null;
            }
            this.g3 = jVar2;
            if (this.xg) {
                jVar = new j(getResources().getColor(this.PH.J0() ? 2131034155 : 2131034154));
            }
            this.lp = jVar;
            this.I = new j(getResources().getColor(this.PH.J0() ? 2131034137 : 2131034136));
            this.ca = new j(getResources().getColor(this.PH.J0() ? 2131034153 : 2131034152));
            this.sy = new j(getResources().getColor(this.PH.J0() ? 2131034145 : 2131034144));
            this.Qq = new j(getResources().getColor(this.PH.J0() ? 2131034143 : 2131034142));
            this.x9 = new j(getResources().getColor(this.PH.J0() ? 2131034141 : 2131034140));

            this.Mz = new j(HighlightUtils.getColorInt(ColorDefault.LINE_NUMBER));
            
            this.vJ = new j(Color.TRANSPARENT);

        }

    }





}
