package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        private T item;
        private Node next;
        private Node prev;
        Node(T i) {
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
    @Override
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
    @Override
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
//    // Returns true if deque is empty, false otherwise.
//    @Override
//    public boolean isEmpty() {
//        return size == 0;
//    }
    // Returns the number of items in the deque.
    @Override
    public int size() {
        return size;
    }
    // Prints the items in the deque from first to last,
    // separated by a space. Once all the items have been printed,
    // print out a new line.
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    public Iterator<T> iterator() {
        return new LLDIterator();
    }
    private class LLDIterator implements Iterator<T> {
        private int position;
        LLDIterator() {
            position = 0;
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public T next() {
            T returnItem = (T) get(position);
            position++;
            return returnItem;
        }
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Deque) {
            Deque<T> odq = (Deque<T>) o;
            if (odq.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < size(); i++) {
                T item1 = get(i);
                T item2 = odq.get(i);
                if (!item1.equals(item2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
