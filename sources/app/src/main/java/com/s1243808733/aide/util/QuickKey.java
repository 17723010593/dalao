package com.s1243808733.aide.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.util.Locale;
import com.s1243808733.util.Utils;

public class QuickKey {

    public static void showDialog(final Activity activity) {
        final String[] keys={".java",".xml"};
        AlertDialog dialog=new AlertDialog.Builder(activity)
        .setTitle("选择")
        .setItems(keys, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showModifyKeyDialog(activity, keys[which]);
            }
        })
        .create();
        dialog.show();
    }

    public static void showModifyKeyDialog(Activity activity, final String key) {
        ScrollView sl=new ScrollView(activity);
        LinearLayout ll=new LinearLayout(activity);
        final EditText edit=new EditText(activity);
        edit.setText(getKey(key));
        edit.setMaxLines(15);
        ll.addView(edit, -1, -2);
        sl.addView(ll, -1, -1);
        AlertDialog dialog=new AlertDialog.Builder(activity)
        .setTitle("修改快捷键(" + key + ")")
        .setView(sl)
        .setNeutralButton("默认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.getSp().edit().putString(key, getDefaultKey(key)).commit();
                AIDEUtils.initQuickKey();
            }
        })
        .setPositiveButton("保存", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.getSp().edit().putString(key, edit.getText().toString()).commit();
                AIDEUtils.initQuickKey();
            }
        })

        .setNegativeButton("取消", null)
        .create();
        dialog.show();
    }

    public static String getKey(String suffix) {
        if (check(suffix, ".xml", ".htm", ".html")) {
            suffix = ".xml";
        }
        return Utils.getSp().getString(suffix, getDefaultKey(suffix));
    }

    private static String getDefaultKey(String suffix) {
        if (check(suffix, ".java")) {
            return "#sp\n{\n}\n(\n)\n;\n=\n\"\n|\n&\n!\n[\n]\n<\n>\n+\n-\n/\n*\n?\n:\n_";
        } else if (check(suffix, ".xml", ".htm", ".html")) {
            return "#sp\n<\n>\n/\n=\n\"\n:\n@\n+\n(\n)\n;\n,\n.\n?\n|\n\\\n&\n!\n[\n]\n{\n}\n_\n-";
        }
        return "#sp\n{\n}\n(\n)\n;\n=\n\"\n|\n&\n!\n[\n]\n<\n>\n+\n-\n/\n*\n?\n:\n_";
    }

    private static boolean check(String p1, String... p2) {
        for (int i=0;i < p2.length;i++) {
            if (p1.toLowerCase(Locale.ENGLISH)
                .endsWith(p2[i].toLowerCase(Locale.ENGLISH))) {
                return true;
            }
        }
        return false;
    }

}

