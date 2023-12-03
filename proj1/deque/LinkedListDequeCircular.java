package deque;

public class LinkedListDequeCircular<T> implements Deque<T> {
    private class Node {
        private T item;
        private Node next;
        private Node prev;
        Node(T i) {
            item = i;
        }
    }
    private Node sentinel;
    private int size;
    public LinkedListDequeCircular() {
        sentinel = new Node(null);
        size = 0;
    }
    // Adds an item of type T to the front of the deque.
    public void addFirst(T item) {
        Node newNode = new Node(item);
        if (size == 0) {
            sentinel.next = newNode;
            sentinel.prev = newNode;
        } else {
            Node oldFrontNode = sentinel.next;
            oldFrontNode.prev = newNode;
            newNode.next = oldFrontNode;
            newNode.prev = sentinel;
            sentinel.next = newNode;
        }
        size++;
    }
    // Adds an item of type T to the back of the deque.
    public void addLast(T item) {
        Node newNode = new Node(item);
        if (size == 0) {
            sentinel.next = newNode;
            sentinel.prev = newNode;
        } else {
            Node oldLastNode = sentinel.prev;
            oldLastNode.next = newNode;
            newNode.prev = oldLastNode;
            sentinel.prev = newNode;
            newNode.next = sentinel;
        }
        size++;
    }
    // Returns true if deque is empty, false otherwise.
    public boolean isEmpty() {
        return size == 0;
    }
    // Returns the number of items in the deque.
    public int size() {
        return size;
    }
    // Prints the items in the deque from first to last,
    // separated by a space. Once all the items have been printed,
    // print out a new line.
    public void printDeque() {
        Node curNode = sentinel.next;
        while (curNode.item != null) {
            System.out.print(curNode.item + " ");
            curNode = curNode.next;
        }
        System.out.println();
    }
    //Removes and returns the item at the front of the deque.
    // If no such item exists, returns null.
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T ret = sentinel.next.item;
        if (sentinel.next == sentinel.prev) {
            sentinel.prev = null;
            sentinel.next = null;
        } else {
            sentinel.next.next.prev = sentinel;
            sentinel.next = sentinel.next.next;
        }
        size--;
        return ret;
    }
    // Removes and returns the item at the back of the deque.
    // If no such item exists, returns null.
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T ret = sentinel.prev.item;
        if (sentinel.next == sentinel.prev) {
            sentinel.prev = null;
            sentinel.next = null;
        } else {
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
        }
        size--;
        return ret;
    }
    // Gets the item at the given index, where 0 is the front,
    // 1 is the next item, and so forth. If no such item exists,
    // returns null. Must not alter the deque!
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }
        Node curNode = sentinel.next;
        while (index != 0) {
            curNode = curNode.next;
            index--;
        }
        return curNode.item;
    }
    // Same as get, but uses recursion.
    public T getRecursive(int index) {
        if (isEmpty()) {
            return null;
        }
        Node curNode = sentinel.next;
        return getRecursiveNode(curNode, index);
    }
    private T getRecursiveNode(Node n, int index) {
        if (index == 0) {
            return n.item;
        }
        return getRecursiveNode(n.next, index - 1);
    }
}
