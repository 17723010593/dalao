package com.s1243808733.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class View2Java {

    private static final String[] items=
    {"add field","add private","add m","root view","type conversion"};

    private static final boolean[] checkedItems_default=
    {true,true,true,false,true};

    private static final boolean[] checkedItems=
    new boolean[items.length];

	private static SharedPreferences getSp() {
		return Utils.getSp("View2Java");
	}

    public static void showDialog(final Activity activity, final String xmlcode) {
        for (int i=0;i < items.length;i++) {
            checkedItems[i] = getSp().getBoolean(items[i].replace(" ", "_"), checkedItems_default[i]);
        }
        final AlertDialog dialog=new AlertDialog.Builder(activity)
		.setTitle("配置")
		.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener(){

			@Override
			public void onClick(DialogInterface p1, int p2, boolean p3) {
				getSp().edit().putBoolean(items[p2].replace(" ", "_"), p3).commit();
			}
		})
		.setPositiveButton("生成", null)
		.setNegativeButton(android.R.string.cancel, null)
		.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				dialog.hide();
				try {
					final String result=getCode(xmlcode, checkedItems);

					AlertDialog dialog2=new AlertDialog.Builder(activity)
					.setTitle("生成结果(长按可复制)")
					.setMessage(result)
					.setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dia, int which) {
							Utils.copyToClipboard(result);
							dialog.dismiss();
						}
					})
					.setNegativeButton(android.R.string.cancel, null)
					.create();
					dialog2.show();

					dialog2.setOnDismissListener(new DialogInterface.OnDismissListener(){

						@Override
						public void onDismiss(DialogInterface p1) {
							dialog.show();
						}
					});

					Utils.setMessageIsSelectable(dialog2);

				} catch (XmlPullParserException e) {
					Utils.showExDialog(activity, "解析XML布局出错", e);
				} catch (Throwable e) {
					Utils.showExDialog(activity, e);
				}
				///
			}
		});
    }

    public static String getCode(String xmlCode, boolean[] configs) throws XmlPullParserException, Throwable {
        List<String> listName=new ArrayList<>();
        List<String> listId=new ArrayList<>();

        StringBuffer sb=new StringBuffer();
        StringBuffer sb_field=new StringBuffer();

        XmlPullParserFactory parserfactory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = parserfactory.newPullParser();
        xpp.setInput(new StringReader(xmlCode));  
        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            String nodeName=xpp.getName();
            if (!TextUtils.isEmpty(nodeName) && xpp.getAttributeCount() > 0) {
                for (int i=0;i < xpp.getAttributeCount();i++) {
                    if ("android:id".equals(xpp.getAttributeName(i))) {
                        String attributeValue=xpp.getAttributeValue(i);
                        int index = attributeValue.indexOf("/");
                        String id=index >= 0 ?attributeValue.substring(index + 1): attributeValue;
                        if (!TextUtils.isEmpty(id)) {
                            listName.add(nodeName);
                            listId.add(id);
                        }
                    }
                }
                xpp.next();
            }
            xpp.next();
        }


        boolean isAddField=configs[0];
        boolean isPrivate=configs[1];
        boolean isAddm=configs[2];
        boolean isRootView=configs[3];
        boolean isTypeConversion=configs[4];

        for (int i=0;i < listName.size();i++) {
            String name=listName.get(i);
            String id=listId.get(i);

            String viewName=getViewName(name);
            String idName=getIdName(id, isAddm);

            if (isAddField) {
                if (isPrivate) {
                    sb_field.append("private ");
                }
                sb_field.append(String.format("%1$s %2$s;\n", viewName, idName));
            }

            sb.append("    ");
            if (!isAddField) {
                sb.append(viewName);
                sb.append(" ");
            }
            sb.append(idName);
            sb.append(" = ");
            if (isTypeConversion) {
                sb.append(String.format("(%s)", viewName));
            }
            if (isRootView) {
                sb.append("view.");
            }
            sb.append(String.format("findViewById(R.id.%s);\n", id));

        }

        StringBuffer method=new StringBuffer();
        if (sb.length() > 0) {
            method.append(String.format("private void initView(%s)", isRootView ?"View view": ""));
            method.append(" {\n");
            method.append(sb);
            method.append("}");
        }

        StringBuffer result=new StringBuffer();
        if (isAddField) {
            result.append(sb_field);
            result.append("\n");
        }
        result.append(method);

        return result.toString().trim();
    }

    private static String getIdName(String idName, boolean isAddm) {
        if (!isAddm) {
            return idName;
        }
        if (idName.length() <= 1) {
            idName = idName.toUpperCase(Locale.ENGLISH);
        } else {
            String first=idName.substring(0, 1).toUpperCase(Locale.ENGLISH);
            idName = first + idName.substring(1);
        }
        return "m" + idName;
    }


    private static String getViewName(String viewName) {
        int index=viewName.lastIndexOf(".");
        if (index >= 0) {
            return viewName.substring(index + 1);
        }
        return viewName;
    }

}

