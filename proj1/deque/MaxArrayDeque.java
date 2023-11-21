package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    public MaxArrayDeque(Comparator<T> c) { // constructor is not inherited
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }
    /*
    public T max() {
        if (size == 0) {
            return null;
        }

    }
    public T max(Comparator<T> c) {
        if (size == 0) {
            return null;
        }

    }

     */
}
