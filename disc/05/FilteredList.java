import java.util.*;
import java.util.function.Predicate;

public class FilteredList<T> implements Iterable<T> {
    List<T> list;
    Predicate<T> pred;
    public FilteredList(List<T> L, Predicate<T> filter) {
        this.list = L;
        this.pred = filter;
    }
    @Override
    public Iterator<T> iterator() {
        return new FilteredListIterator();
    }
    private class FilteredListIterator implements Iterator<T> {
        int index;
        public FilteredListIterator() {
            index = 0;
            filt();
        }
        @Override
        public boolean hasNext() {
            return index < list.size();
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T ret = list.get(index);
            index++;
            filt();
            return ret;
        }
        public void filt() {
            while (hasNext() && !pred.test(list.get(index))) {
                index++;
            }
        }
    }
}
