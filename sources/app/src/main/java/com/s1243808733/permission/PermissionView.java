package com.s1243808733.permission;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import android.os.AsyncTask;

public class PermissionView extends RelativeLayout
implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

	private LinearLayout mHeaderView;

	private EditText mSearchView;

	private ListView mListView;

	private PermissionAdapter adapter;

	private boolean selectionMode;

	private onItemClick onItemClickListener;

	private onItemLongClick onItemLongClickListener;

	public interface onItemClick {
		void onItemClick(Permission item, View view, int position);
	}

	public interface onItemLongClick {
		boolean onItemLongClick(Permission item, View view, int position) 
	}

	public PermissionView(Context context) {
		super(context);
		init(context, -1, -1);
	}

	public PermissionView(Context context, int width, int height) {
		super(context);
		init(context, width, height);
	}

	private void init(Context context, int width, int height) {
		adapter = new PermissionAdapter(context);

		mSearchView = new EditText(context);
		mSearchView.setSingleLine(true);
		mSearchView.addTextChangedListener(new SearchTextWatcher());
		mSearchView.setHint("输入搜索内容...");

		mHeaderView = new LinearLayout(context);
		mHeaderView.addView(mSearchView, -1, -2);
		mListView = new ListView(context);
		mListView.setFastScrollEnabled(true);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);

		LinearLayout ll=new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.addView(mHeaderView, -1, -2);
		ll.addView(mListView, -1, -2);

		addView(ll, width, height);
	}

	public void load(String xmlCode) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		load(db.parse(new InputSource(new StringReader(xmlCode))));
	}

	public void load(File manifest) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		load(db.parse(manifest));
	}

	public void load(Document doc) throws SAXException  {
		NodeList nodeList=doc.getElementsByTagName("manifest");
		if (nodeList.getLength() == 0) {
			throw new SAXException("找不到manifest节点");
		} 
		Node manifestNode =nodeList.item(0);
		NodeList manifestChildNodes=manifestNode.getChildNodes();
		load(manifestChildNodes);
	}

	public void load(NodeList nodeList) {
		List<Permission> list=new ArrayList<>();
		for (int i=0;i < nodeList.getLength();i++) {
			Node node=nodeList.item(i);
			NamedNodeMap attributes=node.getAttributes();
			if ("uses-permission".equals(node.getNodeName()) && attributes.getLength() == 1) {
				label1:
				for (int j=0;j < attributes.getLength();j++) {
					Node attributesNode=attributes.item(j);
					if ("android:name".equals(attributesNode.getNodeName())) {
						String permission =attributesNode.getNodeValue();
						if (!TextUtils.isEmpty(permission.trim())) {
							if (!PermissionUtils.hasAdded(list, permission)) {
								Permission item=new Permission(permission);
								list.add(item);
							}
						}
						break label1;
					}
				}
			}
		}

		load(list);

	}

	public void load(List<Permission> list) {
		adapter.setSourceList(list);
		adapter.setList(list);
		adapter.notifyDataSetChanged();
	}

	public boolean isSelectionMode() {
		return selectionMode;
	}

	public List<String> getSelection() {
		return adapter.getSelectionList();
	}

	public void selectionAll(boolean selection) {
		for (int i=0;i < adapter.getCount();i++) {
			if (!(adapter.getItem(i) instanceof CommonItem))
				adapter.setSelection(i, selection);
		}
	}
	public void invertSelection() {
		for (int i=0;i < adapter.getCount();i++) {
			if (!(adapter.getItem(i) instanceof CommonItem))
				adapter.setSelection(i, !adapter.isSelection(adapter.getItem(i).getPermission()));
		}
	}

	public void setSelectionMode(boolean selectionMode) {
		this.selectionMode = selectionMode;
		adapter.setSelectionMode(selectionMode);
		adapter.notifyDataSetChanged();
	}

	public void setOnItemClickListener(onItemClick listener) {
		this.onItemClickListener = listener;
	}

	public void setOnItemLongClickListener(onItemLongClick listener) {
		this.onItemLongClickListener = listener;
	}

	public void setOnSelectionListener(PermissionAdapter.OnSelectionListener listener) {
		adapter.setOnSelectionListener(listener);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
		Permission item=adapter.getItem(position);

		if (isSelectionMode() && !(item instanceof CommonItem)) {
			PermissionAdapter.ViewHolder holder=((PermissionAdapter.ItemView)view).holder;
			adapter.setSelection(position, holder.invertSetSelection());
		} 

		if (onItemClickListener != null) {
			onItemClickListener.onItemClick(adapter.getItem(position), view, position);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long itemId) {
		if (onItemLongClickListener != null) {
			return onItemLongClickListener.onItemLongClick(adapter.getItem(position), view, position);
		}
		return false;
	}

    private SearchTesk searchTesk;
	public void search(String search) {
        if (searchTesk != null) {
            searchTesk.setCancel(false);
        }
        searchTesk = new SearchTesk();
        searchTesk.execute(search);
	}

    private class SearchTesk extends AsyncTask<String,Void,List<Permission>> {

        private boolean cancel;

        public void setCancel(boolean cancel) {
            this.cancel = cancel;
        }
        @Override
        protected List<Permission> doInBackground(String[] p1) {
            String search=p1[0];
            List<Permission> list=new ArrayList<>();
            if (search == null || search.length() == 0) {
                list = adapter.getSourceList();
            } else {
                search = search.toLowerCase(Locale.ENGLISH);
                Iterator<Permission> it=adapter.getSourceList().iterator();
                while (!cancel && it.hasNext()) {
                    Permission permissionItem=it.next();
                    if (!(permissionItem instanceof CommonItem)) {
                        String name=permissionItem.getName().toLowerCase(Locale.ENGLISH);
                        String permission=permissionItem.getPermission().toLowerCase(Locale.ENGLISH);

                        int indexName=name.indexOf(search);
                        int indexPermission = permission.indexOf(search);

                        if (indexName != -1 || indexPermission != -1) {
                            Permission item=new Permission(permissionItem.getPermission());

                            if (indexName != -1) {
                                item.highlight.title.start = indexName;
                                item.highlight.title.end = indexName + search.length();
                            }
                            if (indexPermission != -1) {
                                item.highlight.subtitle.start = indexPermission;
                                item.highlight.subtitle.end = indexPermission + search.length();
                            }
                            list.add(item);
                        }
                    }
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Permission> result) {
            super.onPostExecute(result);
            if (cancel)return;
            adapter.setList(result);
            adapter.notifyDataSetChanged();
        }




    }

	private class SearchTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			search(s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

	public PermissionAdapter getAdapter() {
		return adapter;
	}

	public ListView getListView() {
		return mListView;
	}

	public EditText getSearchView() {
		return mSearchView;
	}

}
