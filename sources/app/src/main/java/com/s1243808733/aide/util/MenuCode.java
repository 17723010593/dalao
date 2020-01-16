package com.s1243808733.aide.util;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MenuCode implements MenuItem.OnMenuItemClickListener {

    private Object object;

    private Node node;

    private OnMenuItemClickListener OnMenuItemClickListener;

    public boolean isMenuItem() {
        return (object instanceof MenuItem);
    }

    public MenuItem getMenuItem() {
        if (isSubMenu()) {
            return getSubMenu().getItem();
        }
        return (MenuItem)object;
    }

    public boolean isSubMenu() {
        return (object instanceof SubMenu);
    }

    public SubMenu getSubMenu() {
        if (isMenuItem()) {
            return getMenuItem().getSubMenu();
        }
        return (SubMenu)object;
    }

    public MenuCode(MenuItem menuItem, Node node, OnMenuItemClickListener OnMenuItemClickListener) {
        this.object = menuItem;
        this.node = node;
        this.OnMenuItemClickListener = OnMenuItemClickListener;
        menuItem.setOnMenuItemClickListener(this);
    }

    public MenuCode(SubMenu subMenu, Node node, OnMenuItemClickListener OnMenuItemClickListener) {
        this.object = subMenu;
        this.node = node;
        this.OnMenuItemClickListener = OnMenuItemClickListener;
        subMenu.getItem().setOnMenuItemClickListener(this);
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuItem menuItem, String content);
    }

    public static MenuCode[] add(Menu menu, InputStream is, String name,  OnMenuItemClickListener OnMenuItemClickListener) throws SAXException, ParserConfigurationException, IOException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        .parse(is); 
        Element element=doc.getDocumentElement();
        NodeList childNodes=element.getChildNodes();
        for (int i=0;i < childNodes.getLength();i++) {
            Node node=childNodes.item(i);
            NamedNodeMap attr=node.getAttributes();
            String regex=getValueByName(attr, "format");
            if (regex != null && Pattern.matches(regex, name)) {
                return add(menu, node.getChildNodes(), OnMenuItemClickListener).toArray(new MenuCode[]{});
            }
        }
        return null;
    }

    public static MenuCode add(Menu menu, Node node, OnMenuItemClickListener OnMenuItemClickListener) {
        String nodeName=node.getNodeName();
        NamedNodeMap attr=node.getAttributes();
        String title=getValueByName(attr, "title");
        if ("item".equals(nodeName)) {
            MenuItem item=menu.add(title);
            return new MenuCode(item, node, OnMenuItemClickListener);
        } else if ("menu".equals(node.getNodeName())) {
            SubMenu subMenu=menu.addSubMenu(title);
            return new MenuCode(subMenu, node, OnMenuItemClickListener);
        }
        return null;
    }

    public static List<MenuCode> add(Menu menu, NodeList childNodes, OnMenuItemClickListener OnMenuItemClickListener) {
        List<MenuCode> list=new ArrayList<>();
        for (int i=0;i < childNodes.getLength();i++) {
            Node node=childNodes.item(i);
            MenuCode item= add(menu, node, OnMenuItemClickListener);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    private static String getValueByName(NamedNodeMap map, String name) {
        for (int i=0;map != null && i < map.getLength();i++) {
            Node node=map.item(i);
            if (name.equals(node.getNodeName())) {
                return node.getNodeValue();
            }
        }
        return "unknown";
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (isSubMenu()) {
            SubMenu subMenu=getSubMenu();
            subMenu.clear();
            add(subMenu, node.getChildNodes(), OnMenuItemClickListener);
        } else if (isMenuItem()) {
            String content=node.getTextContent();
            OnMenuItemClickListener.onMenuItemClick(menuItem, content);
        } else {
            return false;
        }
        return true;
    }

}

