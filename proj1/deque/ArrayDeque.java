package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }
    // Adds an item of type T to the front of the deque.
    public void resize() {
        T[] a = (T[]) new Object[size * 2];
        System.arraycopy(items, 0, a, 0, nextLast);
        System.arraycopy(items, nextFirst + 1, a, nextLast + size, size - nextLast);
        items = a;
    }
    public void addFirst(T item) {
        if (size == items.length) {
            resize();
        }
        items[nextFirst] = item;
        nextFirst--;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
        size++;
    }
    // Adds an item of type T to the back of the deque.
    public void addLast(T item) {
        if (size == items.length) {
            resize();
        }
        items[nextLast] = item;
        nextLast++;
        if (nextLast == items.length) {
            nextLast = 0;
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

    }
    //Removes and returns the item at the front of the deque.
    // If no such item exists, returns null.
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T ret = items[nextFirst + 1];
        items[nextFirst + 1] = null;
        size--;
        return ret;
    }
    // Removes and returns the item at the back of the deque.
    // If no such item exists, returns null.
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T ret = items[nextLast - 1];
        items[nextLast - 1] = null;
        size--;
        return ret;
    }
    // Gets the item at the given index, where 0 is the front,
    // 1 is the next item, and so forth. If no such item exists,
    // returns null. Must not alter the deque!
    public T get(int index) {
        return items[nextFirst + 1 + index];
    }
    // public Iterator<T> iterator() {
    // }
    // public boolean equals(Object o) {
    // }
}