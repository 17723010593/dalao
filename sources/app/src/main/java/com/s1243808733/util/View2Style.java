package com.s1243808733.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.s1243808733.aide.util.AIDEUtils;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class View2Style {

    public static void showDialog(final Activity activity, final String selectionContent) {
        LinearLayout dialogView=new LinearLayout(activity);
        dialogView.setPadding(AIDEUtils.dp2px(24), AIDEUtils.dp2px(10), AIDEUtils.dp2px(24), AIDEUtils.dp2px(0));
        final EditText et_input=new EditText(activity);
        et_input.setHint("输入Style名");
        dialogView.addView(et_input, -1, -2);

        final AlertDialog dialog=new AlertDialog.Builder(activity)
		.setTitle("配置")
		.setView(dialogView)
		.setPositiveButton("确定", null)
		.setNegativeButton("取消", null)
		.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String styleName=et_input.getText().toString().trim();
				try {
					final String data= xml2Style(selectionContent, styleName);
					AlertDialog dialog2=new AlertDialog.Builder(activity)
					.setTitle("结果")
					.setMessage(data)
					.setPositiveButton("复制", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Utils.copyToClipboard(data);
						}
					})
					.setNegativeButton("取消", null)
					.create();
					dialog2.show();
					Utils.setMessageIsSelectable(dialog2);
					dialog.dismiss();

				} catch (Throwable e) {
					AIDEUtils.showExDialog(activity, e);
				}
			}
		});
    }

    public static String xml2Style(String xmlcode, String styleName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xmlcode))); 
        Element rootElement=doc.getDocumentElement();

        /*
         if (rootElement.getChildNodes().getLength() > 0) {
         NodeList childNodes=rootElement.getChildNodes();
         for (int i=0;i < childNodes.getLength();i++) {
         Node node=childNodes.item(i);
         if (!node.getNodeName().startsWith("#")) {
         throw new SAXException("此视图不能拥有子节点");
         }
         }
         }
         */

        StringBuilder sb=new StringBuilder();

        if (styleName == null || styleName.length() == 0) {
            styleName = "Style_" + rootElement.getNodeName();
        }
        sb.append(String.format("<style name=\"%s\">", styleName));

        NamedNodeMap attributes=rootElement.getAttributes();
        for (int i=0;i < attributes.getLength() ;i++) {
            Node node=attributes.item(i);
			String nodeName=node.getNodeName();
			if (!"xmlns:android".equals(nodeName))
				sb.append(String.format("\n    <item name=\"%1$s\">%2$s</item>", nodeName, node.getNodeValue()));
        }

        sb.append(String.format("\n</style>", styleName));

        return sb.toString().trim();
    }
}
