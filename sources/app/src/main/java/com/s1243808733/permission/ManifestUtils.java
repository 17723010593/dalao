package com.s1243808733.permission;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManifestUtils {
	private static int indent=4;

	private StringBuffer sb=new StringBuffer();

	private List<Permission> permission;

	private boolean isAnnotation;

	public ManifestUtils(List<Permission> permission, Document doc, boolean isAnnotation) {
		this.permission = permission;
		this.isAnnotation = isAnnotation;
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		loadNodeList(sb, doc.getChildNodes(), null, 0);
	}

	public static String annotationPermission(List<Permission> permission, Document document, boolean isAnnotation) {
		return new ManifestUtils(permission, document, isAnnotation).result();
	}

	private boolean isAdded=false;

	private void loadNodeList(StringBuffer sb, NodeList nodeList, Node parentNode, int depth) {
		int length=nodeList.getLength();

		for (int i=0;i < length;i++) {

			Node node=nodeList.item(i);
			String nodeName=node.getNodeName();

			NamedNodeMap attributes=node.getAttributes();

			if (!nodeName.startsWith("#") && (isAdded ?true
				: parentNode != null && !"manifest".equals(parentNode.getNodeName()) ?true
				: !"uses-permission".equals(nodeName) ?true: attributes.getLength() > 1)) {

				NodeList childNodes=node.getChildNodes();

				String indent1 = getSpace(depth);
				String indent2 = getSpace(depth + indent);

				if (depth > 0) {
					sb.append("\n\n");
				} else {
					sb.append("\n");
				}

				if (permission.size() != 0 && !isAdded && parentNode != null
					&& "manifest".equals(parentNode.getNodeName())) {

					for (Permission item:permission) {
						if (!(item instanceof CommonItem)) {
							if (isAnnotation) {
								sb.append(indent1);
								sb.append("<!--");
								sb.append(item.getName());
								sb.append("-->\n");
							}
							sb.append(indent1);
							sb.append("<uses-permission android:name=\"");
							sb.append(item.getPermission());
							sb.append("\"/>");

							sb.append("\n\n");
						}
					}

					isAdded = true;
				}

				if (depth > 0) {
					sb.append(indent1);
				}

				sb.append("<");
				sb.append(nodeName);

				loadNodeAttributes(sb, attributes, indent2);

				if (childNodes.getLength() == 0) {
					sb.append("/>");
				} else {
					sb.append(">");
					loadNodeList(sb, childNodes, node, depth + indent);
					sb.append("\n\n");
					if (depth > 0) {
						sb.append(indent1);
					}
					sb.append("</");
					sb.append(nodeName);
					sb.append(">");
				}
			}

		}
	}


	private void loadNodeAttributes(StringBuffer sb, NamedNodeMap namedNodeMap, String indent) {
		if (namedNodeMap != null) {
			int length=namedNodeMap.getLength();
			for (int i=0;i < length;i++) {
				Node node=namedNodeMap.item(i);
				String nodeName=node.getNodeName();
				String nodeValue=node.getNodeValue();

				if (length > 1) {
					sb.append("\n");
					sb.append(indent);
				} else if (length > 0) {
					sb.append(" ");
				}

				sb.append(nodeName);
				sb.append("=\"");
				sb.append(nodeValue);
				sb.append("\"");
			}
		}
	}

	private String getSpace(int depth) {
		StringBuffer sb=new StringBuffer();
		for (int i=0; i < depth;i++) {
			sb.append(" ");
		}
		return sb.toString();
	}


	public String result() {
		return sb.toString();
	}


}

