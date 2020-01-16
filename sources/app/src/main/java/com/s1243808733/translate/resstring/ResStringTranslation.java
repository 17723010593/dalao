package com.s1243808733.translate.resstring;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.blankj.utilcode.util.FileIOUtils;
import com.s1243808733.aide.util.AIDEUtils;
import com.s1243808733.app.bean.BeanYandex;
import com.s1243808733.app.http.param.YandexParam;
import com.s1243808733.translate.YandexTranslate;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xutils.common.Callback;

public class ResStringTranslation {

    private List<Item> sourcelist;
    private String targetLang;
    private Listener listener;  

    private List<Item> translationlist=new ArrayList<>();

    public ResStringTranslation(List<Item> sourcelist, String targetLang, Listener listener) {
        this.sourcelist = sourcelist;
        this.targetLang = targetLang;
        this.listener = listener;
        translation(0);
    }

    private interface Listener {
        void onProgress(Item item);
        void onFinish(List<Item> list);

    }

    public void translation(final int position) {
        if (position >= sourcelist.size()) {
            listener.onFinish(translationlist);
            return;
        }
        final ResStringTranslation.Item item=sourcelist.get(position);
        YandexParam params=new YandexParam.Builder()
			.setTargetLang(targetLang)
			.setText(item.value)
			.create();
        YandexTranslate.translate(params, new Callback.CommonCallback<BeanYandex>() {

				private boolean isFail;

				@Override
				public void onSuccess(BeanYandex bean) {
					if (bean.isSuccess()) {
						List<String> text=bean.getText();
						if (text.size() > 0) {
							Item newitem=new Item(item.name, text.get(0));
							newitem.lang = bean.getLang();
							translationlist.add(newitem);
							listener.onProgress(newitem);
						}
					} else {
						Item newitem=new Item(item.name, null);
						newitem.errorMsg = bean.getErrorMessage();
						translationlist.add(newitem);
						listener.onProgress(newitem);
					}
				}

				@Override
				public void onError(Throwable ex, boolean isOnCallback) {
					isFail = true;
				}

				@Override
				public void onCancelled(CancelledException cex) {


				}

				@Override
				public void onFinished() {
					if (isFail) {
						listener.onFinish(translationlist);
					} else {
						translation(position + 1);
					}
				}
			});   
    }

    private static void translation(final List<Item> sourcelist, final  File outputFile, final String targetLang, final Listener listener) {
        new ResStringTranslation(sourcelist, targetLang, new Listener(){

				@Override
				public void onFinish(List<ResStringTranslation.Item> list) {
					int failCount=0;
					StringBuffer sb=new StringBuffer();
					sb.append("<resources>");
					for (int i=0;i < list.size();i++) {
						ResStringTranslation.Item item=list.get(i);
						sb.append("\n    ");
						if (item.errorMsg == null) {
							sb.append("<string name=\"" + item.name + "\">");
							sb.append(escape(item.value));
							sb.append("</string>");
						} else {
							failCount++;
							sb.append("<!--<string name=\"" + item.name + "\">");
							sb.append(escape(item.errorMsg));
							sb.append("</string>-->");
						}
					} 
					sb.append("\n</resources>");


					StringBuffer result=new StringBuffer();
					result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					result.append("\n<!-- 总数:" + list.size()
								  + ",失败:" + failCount);

					String lang= "? -> " + targetLang ;
					if (list.size() > 0) {
						ResStringTranslation.Item item=list.get(0);
						if (item.lang != null) {
							lang = item.lang;
						}
					}
					result.append(",翻译方向:" + lang);

					result.append(" -->\n");
					result.append(sb.toString());

					FileIOUtils.writeFileFromString(outputFile, result.toString());

					listener.onFinish(list);
				}

				private String escape(String s) {
					return s.replace("\"", "\\\"");
				}


				@Override
				public void onProgress(ResStringTranslation.Item item) {
					listener.onProgress(item);
				}

			});
    }


    public static void showDialog(final Activity activity, final File inputFile) {

        LinearLayout dialogView=new LinearLayout(activity);
        dialogView.setPadding(AIDEUtils.dp2px(24), AIDEUtils.dp2px(10), AIDEUtils.dp2px(24), 0);
        dialogView.setOrientation(dialogView.VERTICAL);

        final EditText et_input=new EditText(activity);
        et_input.setSingleLine(true);
        et_input.setText("en");
        dialogView.addView(et_input);

		final Spinner sn=new Spinner(activity);
		dialogView.addView(sn);

		String[][] langArray=YandexTranslate.getSupportedLanguage();
		String[] lang=new String[langArray.length];
		final String[] langCode=new String[langArray.length];
		for (int i=0;i < langArray.length;i++) {
			String[] item=langArray[i];
			String code=item[0];
			String name = item[1];
			lang[i] = name + " - " + code;
			langCode[i] = code;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, lang); 
		sn.setAdapter(adapter);
		sn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					et_input.setText(langCode[p3]);
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
				}
			});


        TextView tv_hint=new TextView(activity);
        tv_hint.setText("输入欲翻译成的语言国家代号");

        dialogView.addView(tv_hint);

        final AlertDialog dialog=new AlertDialog.Builder(activity)
			.setTitle("翻译Strings.xml")
			.setView(dialogView)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						String targetLang=et_input.getText().toString();

						final File outFile=new File(
							new File(inputFile.getParentFile().getParentFile(),
									 "values-" + targetLang), "strings.xml");

						final ProgressDialog progressDialog=ProgressDialog.show(activity, null, "正在翻译..", true, false);

						final List<ResStringTranslation.Item> list=getStringList(inputFile);
						translation(list, outFile, targetLang, new Listener(){
								int progress=1;

								@Override
								public void onProgress(ResStringTranslation.Item item) {
									progress++;
									progressDialog.setMessage(String.format("正在翻译.. %1$d/%2$d", list.size(), progress));
									progressDialog.show();
								}

								@Override
								public void onFinish(List<ResStringTranslation.Item> list) {
									progressDialog.dismiss();
									AIDEUtils.toast("已写入到：" + outFile.getAbsolutePath());
								}
							});
					} catch (Throwable e) {
						AIDEUtils.showExDialog(activity, e);
					}

				}
			})
			.setNegativeButton("取消", null)
			.create();
        dialog.show();
        et_input.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(p1.toString().trim().length() > 0);
				}

				@Override
				public void afterTextChanged(Editable p1) {
				}
			});
    }





    public static class Item {
        public String name;
        public String value;

        public String errorMsg;
		public String lang;

        public Item(String name, String value) {
            this.name = name;
            this.value = value;
        }

    }

    /**
     <resources>
     <string name="app_name"></string>
     */

    public static List<Item> getStringList(File input) throws Throwable {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(input); 
        Element rootElement=doc.getDocumentElement();
        List<Item> list=new ArrayList<>();

        NodeList childNodes=rootElement.getChildNodes();
        for (int i=0;i < childNodes.getLength();i++) {
            Node node=childNodes.item(i);
            if ("string".equals(node.getNodeName())) {
                NamedNodeMap attrMap=node.getAttributes();
                if (attrMap.getLength() == 1) {
                    Node attr=attrMap.item(0);
                    if ("name".equals(attr.getNodeName())) {
                        String name=attr.getNodeValue();
                        String value=node.getTextContent();
                        if (!TextUtils.isEmpty(name) && ! TextUtils.isEmpty(value)) {
                            list.add(new Item(name, value));
                        }
                    }
                }
            }
        }

        return list;
    }

}
