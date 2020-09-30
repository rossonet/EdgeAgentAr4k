package org.ar4k.agent.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataTree<T extends EdgeChannel> {

	private static final int indent = 2;

	private T head;

	private ArrayList<DataTree<T>> leafs = new ArrayList<>();

	private DataTree<T> parent = null;

	private HashMap<T, DataTree<T>> locate = new HashMap<>();

	public DataTree(T head) {
		this.head = head;
		locate.put(head, this);
	}

	public void addLeaf(T root, T leaf) {
		if (locate.containsKey(root)) {
			locate.get(root).addLeaf(leaf);
		} else {
			addLeaf(root).addLeaf(leaf);
		}
	}

	public DataTree<T> addLeaf(T leaf) {
		final DataTree<T> t = new DataTree<>(leaf);
		leafs.add(t);
		t.parent = this;
		t.locate = this.locate;
		locate.put(leaf, t);
		return t;
	}

	public DataTree<T> setAsParent(T parentRoot) {
		final DataTree<T> t = new DataTree<>(parentRoot);
		t.leafs.add(this);
		this.parent = t;
		t.locate = this.locate;
		t.locate.put(head, this);
		t.locate.put(parentRoot, t);
		return t;
	}

	public T getHead() {
		return head;
	}

	public DataTree<T> getTree(T element) {
		return locate.get(element);
	}

	public DataTree<T> getParent() {
		return parent;
	}

	public Collection<T> getSuccessors(T root) {
		final Collection<T> successors = new ArrayList<>();
		final DataTree<T> tree = getTree(root);
		if (null != tree) {
			for (final DataTree<T> leaf : tree.leafs) {
				successors.add(leaf.head);
			}
		}
		return successors;
	}

	public Collection<DataTree<T>> getSubTrees() {
		return leafs;
	}

	@Override
	public String toString() {
		return printTree(0);
	}

	public JSONObject toJson() {
		final JSONObject json = new JSONObject();
		json.put("nodeId", head.getNodeId());
		json.put("name", head.getBrowseName());
		json.put("description", head.getDescription());
		json.put("type", head.getDataType());
		json.put("data-address", head.getDataAddress());
		json.put("namespace", head.getNameSpace());
		json.put("domain", head.getDomainId());
		json.put("tags", head.getTags());
		json.put("documentation", head.getDocumentation());
		json.put("dictionaryRef", head.getDictionaryRef());
		json.put("create-data", head.getCreateData());
		final JSONArray array = new JSONArray();
		for (final DataTree<T> child : leafs) {
			array.put(child.toJson());
		}
		json.put("sub-tree", array);
		return json;
	}

	private String printTree(int increment) {
		String s = "";
		String inc = "";
		for (int i = 0; i < increment; ++i) {
			inc = inc + " ";
		}
		s = inc + head;
		for (final DataTree<T> child : leafs) {
			s += "\n" + child.printTree(increment + indent);
		}
		return s;
	}

	public void addTree(DataTree<T> scopeTreeChildren) {
		if (scopeTreeChildren != null) {
			leafs.add(scopeTreeChildren);
		}
	}

}
