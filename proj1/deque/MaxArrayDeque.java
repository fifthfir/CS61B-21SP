package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> c;
    public MaxArrayDeque(Comparator<T> c) { // constructor is not inherited
        this.c = c;
    }

    public T max() {
        return max(c);
    }
    public T max(Comparator<T> com) {
        if (isEmpty()) {
            return null;
        }
        T maxValue = get(0);
        for (int i = 0; i < size(); i++) {
            if (com.compare(get(i), maxValue) > 0) {
                maxValue = get(i);
            }
        }
        return maxValue;
    }
}
