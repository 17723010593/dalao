package com.s1243808733.util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class EscapeUtils {

    public static void showDialog(Activity activity, String text) {
        LinearLayout ll=new LinearLayout(activity);
        ll.setOrientation(ll.VERTICAL);

        final EditText input=new EditText(activity);
        if (text != null) {
            input.setText(text);
        }

        final CheckBox cb=new CheckBox(activity);
        cb.setText("appendLine");

        ll.addView(input, -1, -2);
        ll.addView(cb);

        ScrollView dialogView=new ScrollView(activity);
        dialogView.addView(ll, -1, -1);

        final AlertDialog dialog=new AlertDialog.Builder(activity)
            .setTitle("代码转义")
            .setView(dialogView)
            //.setCancelable(false)
            .setPositiveButton("转义", null)
            .setNegativeButton("反转义", null)
            .setNeutralButton("复制", null)
            .create();
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

                @Override
                public boolean onKey(DialogInterface p1, int p2, KeyEvent p3) {
                    if (p2 == p3.KEYCODE_BACK && p3.getAction() == 0) {
                        dialog.dismiss();
                    }
                    return false;
                }
            });
        dialog.getButton(dialog.BUTTON_POSITIVE)
            .setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    input.setText(escapeText(input.getText().toString(), cb.isChecked()));
                }
            });
        dialog.getButton(dialog.BUTTON_NEGATIVE)
            .setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    input.setText(invertEscapeText(input.getText().toString(), cb.isChecked()));
                }
            });
        dialog.getButton(dialog.BUTTON_NEUTRAL)
            .setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Utils.copyToClipboard(input.getText().toString());
                }
            });
    }

    public static String escapeText(String text, boolean appeneLine) {
        text = text.replace("\\", "\\\\");
        text = text.replace("\"", "\\\"");

        if (appeneLine) {
            text = text.replace("\n", "\"\n + \"\\n");
        } else {
            text = text.replace("\n", "\\n");
        }
        return text;
    }


    public static String invertEscapeText(String text, boolean appeneLine) {
        if (appeneLine) {
            text = text.replace("\"\n + \"\\n", "\n");
        } else {
            text = text.replace("\\n", "\n");
        }
        text = text.replace("\\\\", "\\");

        text = text.replace("\\\"", "\"");

        return text;
    }
}

