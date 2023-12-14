package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;
        private int size;
        public BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }
    private BSTNode root;

    public void printInOrder() {
        printInOrder(root);
    }
    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.println(node.key.toString() + " -> " + node.value.toString());
        printInOrder(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }
    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        } else {
            int compare = key.compareTo(node.key);
            if (compare > 0) {
                return containsKey(node.right, key);
            } else if (compare < 0) {
                return  containsKey(node.left, key);
            } else {
                return true;
            }
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }
    private V get(BSTNode node, K key) {
        if (key == null || node == null) {
            return null;
        } else {
            int compare = key.compareTo(node.key);
            if (compare > 0) {
                return get(node.right, key);
            } else if (compare < 0) {
                return get(node.left, key);
            } else {
                return node.value;
            }
        }
    }

    @Override
    public int size() {
        return size(root);
    }
    private int size(BSTNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }
    }

    @Override
    public void put(K key, V value) {
        root = put(key, value, root);

    }
    private BSTNode put(K key, V value, BSTNode node) {
        if (node == null) {
            return new BSTNode(key, value, 1);
        } else {
            int compare = key.compareTo(node.key);
            if (compare > 0) {
                node.right = put(key, value, node.right);
            } else if (compare < 0) {
                node.left = put(key, value, node.left);
            }
            node.size = 1 + size(node.left) + size(node.right);
            return node;
        }
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        addKeys(root, set);
        return set;
    }
    private void addKeys(BSTNode node, Set<K> set) {
        if (node != null) {
            addKeys(node.left, set);
            set.add(node.key);
            addKeys(node.right, set);
        }
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V valueToRemove = get(key);
            root = remove(root, key);
            return valueToRemove;
        }
        return null;
    }
    private BSTNode remove(BSTNode node, K key) {
        if (node == null) {
            return null;
        } else {
            int compare = key.compareTo(node.key);
            if (compare > 0) {
                node.right = remove(node.right, key);
            } else if (compare < 0) {
                node.left = remove(node.left, key);
            } else {
                if (node.left == null) {
                    return node.right;
                }
                if (node.right == null) {
                    return node.left;
                }
                BSTNode targetNode = node;
                node = getMin(node.right);
                node.left = targetNode.left;
                node.right = remove(targetNode.right, node.key);
            }
            node.size--;
            return node;
        }
    }
    private BSTNode getMin(BSTNode node) {
        if (node.left == null) {
            return node;
        } else {
            return getMin(node.left);
        }
    }

    @Override
    public V remove(K key, V value) {
        if (containsKey(key)) {
            V targetValue = get(key);
            if (targetValue == value) {
                root = remove(root, key);
                return value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
