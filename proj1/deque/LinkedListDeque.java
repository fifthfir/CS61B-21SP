package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        public T item;
        public Node next;
        public Node prev;
        public Node(T i) {
            item = i;
        }
    }
    private Node first;
    private Node last;
    private int size;
    public LinkedListDeque() {
        first = null;
        last = null;
        size = 0;
    }
    // Adds an item of type T to the front of the deque.
    public void addFirst(T item) {
        Node newNode = new Node(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        size++;
    }
    // Adds an item of type T to the back of the deque.
    public void addLast(T item) {
        Node newNode = new Node(item);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
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
        Node curNode = first;
        while (curNode != null) {
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
        T ret = first.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
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
        T ret = last.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
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
        Node curNode = first;
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
        Node curNode = first;
        return getRecursiveNode(curNode, index);
    }
    private T getRecursiveNode(Node n, int index) {
        if (index == 0) {
            return n.item;
        }
        return getRecursiveNode(n.next, index - 1);
    }
    // public Iterator<T> iterator() {

    // }
    // public boolean equals(Object o) {

    // }
}
