package org.ar4k.agent.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

public class SymbolicLinksTree<V extends AbstractHomunculusConfiguration> {

  private V head;

  private ArrayList<SymbolicLinksTree<V>> leafs = new ArrayList<SymbolicLinksTree<V>>();

  private transient SymbolicLinksTree<V> parent = null;

  private transient HashMap<V, SymbolicLinksTree<V>> locate = new HashMap<V, SymbolicLinksTree<V>>();

  public SymbolicLinksTree(V head) {
    this.head = head;
    locate.put(head, this);
  }

  public void changeHead(V newHead) {
    locate.remove(head);
    head = newHead;
    locate.put(head, this);
  }

  public void addLeaf(V root, V leaf) {
    if (locate.containsKey(root)) {
      locate.get(root).addLeaf(leaf);
    } else {
      addLeaf(root).addLeaf(leaf);
    }
  }

  public SymbolicLinksTree<V> addLeaf(V leaf) {
    SymbolicLinksTree<V> t = new SymbolicLinksTree<V>(leaf);
    leafs.add(t);
    t.parent = this;
    t.locate = this.locate;
    locate.put(leaf, t);
    return t;
  }

  public SymbolicLinksTree<V> setAsParent(V parentRoot) {
    SymbolicLinksTree<V> t = new SymbolicLinksTree<V>(parentRoot);
    t.leafs.add(this);
    this.parent = t;
    t.locate = this.locate;
    t.locate.put(head, this);
    t.locate.put(parentRoot, t);
    return t;
  }

  public void setAsFather(V parentRoot) {
    SymbolicLinksTree<V> t = new SymbolicLinksTree<V>(parentRoot);
    t.leafs.add(this);
    this.parent = t;
    t.locate = this.locate;
    t.locate.put(head, this);
    t.locate.put(parentRoot, t);
  }

  public V getHead() {
    return head;
  }

  public Set<V> getAllHomunculusConfigurations() {
    return locate.keySet();
  }

  public SymbolicLinksTree<V> getTree(V element) {
    return locate.get(element);
  }

  public SymbolicLinksTree<V> getParent() {
    return parent;
  }

  public Collection<V> getSuccessors(V root) {
    Collection<V> successors = new ArrayList<V>();
    SymbolicLinksTree<V> tree = getTree(root);
    for (SymbolicLinksTree<V> leaf : tree.leafs) {
      successors.add(leaf.head);
    }
    return successors;
  }

  public Collection<V> getDescent() {
    Collection<V> discendenza = new ArrayList<V>();
    discendenza.add(getHead());
    for (SymbolicLinksTree<V> p : getSubTrees()) {
      discendenza.addAll(p.getDescent());
    }
    return discendenza;
  }

  public Collection<SymbolicLinksTree<V>> getSubTrees() {
    return leafs;
  }

  /*
   * public static SymbolicLinksTree<? extends AbstractHomunculusConfiguration>
   * newInstance(Object head) throws InstantiationException,
   * IllegalAccessException, ClassNotFoundException, IllegalArgumentException,
   * InvocationTargetException, NoSuchMethodException, SecurityException { Type
   * sooper = head.getClass(); Type t = ((ParameterizedType)
   * sooper).getActualTypeArguments()[0]; return (SymbolicLinksTree<?>)
   * Class.forName(t.toString()).getDeclaredConstructor(Object.class).newInstance(
   * head); }
   */
  @Override
  public String toString() {
    String ritorno = null;
    if (head != null) {
      ritorno = head.toString();
    }
    return ritorno;
  }
}