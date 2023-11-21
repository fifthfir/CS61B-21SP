package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ADTest {
    @Test
    public void testEmptySize() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        assertEquals(0, L.size());
    }

    @Test
    public void testAddAndSize() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        L.addLast(99);
        assertEquals(2, L.size());
    }


    @Test
    public void testAddAndGetLast() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        assert (99 == L.removeLast());
        L.addLast(36);
        assert (36 == L.removeLast());
    }


    @Test
    public void testGet() {
        ArrayDeque<Integer> L = new ArrayDeque<Integer>();
        L.addLast(99);
        assert (99 == L.get(0));
        L.addLast(36);
        assert (99 == L.get(0));
        assert (36 == L.get(1));
    }


    @Test
    public void testRemove() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(99);
        assert (99 == L.get(0));
        L.addLast(36);
        assert (99 == L.get(0));
        assert (36 == L.removeLast());
        L.addLast(100);
        assert (100 == L.removeLast());
        assertEquals(1, L.size());
    }
}
