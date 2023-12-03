package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    public T[] items;
    public int size;
    public int nextFirst;
    public int nextLast;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }
    public int getLength() {
        return items.length;
    }
    public void resizeUp() {
        T[] a = (T[]) new Object[items.length * 2];
        System.arraycopy(items, 0, a, 0, nextLast);
        System.arraycopy(items, nextLast, a, nextLast + items.length, items.length - nextLast);
        items = a;
        nextLast = nextFirst + 1;
        nextFirst = nextLast + size - 1;
    }
    public void resizeDown() {
        T[] a = (T[]) new Object[items.length / 2];
        if (nextFirst < nextLast) {
            System.arraycopy(items, nextFirst, a, 0, size + 1);
            nextFirst = 0;
            nextLast = size + 1;
        } else {
            System.arraycopy(items, 0, a, 0, nextLast);
            System.arraycopy(items, nextFirst, a, a.length - (items.length - nextFirst), items.length - nextFirst);
            nextFirst = a.length - (items.length - nextFirst);
        }
        items = a;
    }
    // Adds an item of type T to the front of the deque.
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resizeUp();
        }
        items[nextFirst] = item;
        nextFirst--;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
        size++;
    }
    // Adds an item of type T to the back of the deque.
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resizeUp();
        }
        items[nextLast] = item;
        nextLast++;
        if (nextLast == items.length) {
            nextLast = 0;
        }
        size++;
    }
//     Returns true if deque is empty, false otherwise.
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
        for (int i = 0; i < size-1; i++) {
            System.out.print(items[i]);
            System.out.print(" ");
        }
        System.out.print(items[size]);
        System.out.println();
    }
    //Removes and returns the item at the front of the deque.
    // If no such item exists, returns null.
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = (nextFirst+1) % items.length;
        T ret = items[nextFirst];
        items[nextFirst] = null;
        size--;

        if (size <= items.length / 4) {
            resizeDown();
        }

        return ret;
    }
    // Removes and returns the item at the back of the deque.
    // If no such item exists, returns null.
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast =  ((nextLast-1) % items.length + items.length) % items.length;
        T ret = items[nextLast];
        items[nextLast] = null;
        size--;
        if (size <= items.length / 4) {
            resizeDown();
        }
        return ret;
    }
    // Gets the item at the given index, where 0 is the front,
    // 1 is the next item, and so forth. If no such item exists,
    // returns null. Must not alter the deque!
    @Override
    public T get(int index) {
        return items[(nextFirst + 1 + index) % items.length];
    }
     public Iterator<T> iterator() {
        return new ArrayDequeIterator();
     }
     private class ArrayDequeIterator implements Iterator<T> {
        private int position;
        public ArrayDequeIterator() {
            position = 0;
        }
         @Override
         public boolean hasNext() {
             return position < size;
         }
         @Override
         public T next() {
             T retuanItem = items[position];
             position += 1;
             return retuanItem;
         }
     }
     public boolean equals(Object o) {
         if (o instanceof ArrayDeque) {
             ArrayDeque odq = (ArrayDeque) o;
             if (odq.size() != this.size()) {
                 return false;
             }
             for (int i = 0; i < odq.size(); i++) {
                 if (odq.get(i) != this.get(i)) {
                     return false;
                 }
             }
             return true;
         }
         return false;
     }
}