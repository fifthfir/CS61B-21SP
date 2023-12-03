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
        L.addLast(92);
        L.addLast(93);
        L.addLast(94);
        L.addLast(95);
        L.addLast(96);
        L.addLast(97);
        assert (97 == L.get(7));
    }
    @Test
    public void testRemove() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addFirst(0);
        L.addFirst(1);
        L.addLast(2);
        L.addLast(3);
        L.removeFirst();
        L.addLast(5);
        L.addLast(6);
        L.removeFirst();
        L.get(0);
        L.get(2);
        L.removeLast();
        L.removeFirst();
        L.removeLast();
        L.addFirst(13);
        L.get(1);
        L.addFirst(15);
        assert L.removeLast() == 3;

    }
    @Test
    public void testResize() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            L.addLast(i);
        }
        assert (L.size() == 10);

        for (int i = 0; i < 8; i++) {
            L.removeLast();
        }
        assert (L.size() == 2);
    }
    @Test
    public void testResize2() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            L.addLast(i);
        }
        assert (L.size() == 10);

        for (int i = 0; i < 4; i++) {
            L.removeLast();
            L.removeFirst();
        }
        assert (L.size() == 2);
    }
    @Test
    public void testEquals() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            L.addLast(i);
        }

        ArrayDeque<Integer> M = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            M.addLast(i);
        }
        assert (L.equals(M));
    }
    @Test
    public void testRemove2() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(0);
        assert L.removeFirst() == 0;
        L.addLast(2);
        assert L.removeLast() == 2;
        L.addFirst(4);
        L.addFirst(5);
        assert L.removeFirst() == 5;
        assert L.removeFirst() == 4;
        L.addFirst(8);
        assert L.removeLast() == 8;
        L.addFirst(10);
        assert L.removeFirst() == 10;
        L.addLast(12);
        L.removeFirst();
        L.addLast(14);
        L.addLast(15);
        L.addFirst(16);
        L.addLast(17);
        L.addLast(18);
        L.addLast(19);
        L.addFirst(20);
        assert L.get(6) == 19;
    }
    @Test
    public void testRemove3() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(0);
        L.removeFirst();
        L.addLast(2);
        L.removeLast();
        L.addFirst(4);
        L.addFirst(5);
        L.removeFirst();
        L.removeFirst();
        L.addFirst(8);
        L.removeLast();
        L.addFirst(10);
        L.removeFirst();
        L.addLast(12);
        L.removeFirst();
        L.addLast(14);
        L.addLast(15);
        L.addFirst(16);
        L.addLast(17);
        L.addLast(18);
        L.addLast(19);
        L.addFirst(20);
        assert L.get(6) == 19;
    }
    @Test
    public void testRemove4() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addFirst(0);
        assert L.get(0) == 0;
        L.addFirst(2);
        assert L.get(1) == 0;
        L.removeFirst();
        assert L.get(0) == 0;
        L.addFirst(6);
        assert L.removeFirst() == 6;
        assert L.removeLast() == 0;
        L.addFirst(9);
        L.addFirst(10);
        L.addFirst(11);
        assert L.removeLast() == 9;
        L.addFirst(13);
        L.addLast(14);
        assert L.removeLast() == 14;
        L.addLast(16);
        assert L.removeFirst() == 13;
    }
    @Test
    public void testRemove5() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addFirst(0);
        assert L.get(0) == 0;
        L.addFirst(2);
        assert L.get(1) == 0;
        assert L.removeFirst() == 2;
        assert L.get(0) == 0;
        L.addFirst(6);
        assert L.removeFirst() == 6;
        assert L.removeLast() == 0;
        L.addFirst(9);
        L.addFirst(10);
        L.addFirst(11);
        assert L.removeLast() == 9;
        L.addFirst(13);
        L.addLast(14);
        assert L.removeLast() == 14;
        L.addLast(16);
        assert L.removeFirst() == 13;
    }
    @Test
    public void testRemove6() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(0);
        L.addLast(1);
        L.addLast(2);
        assert L.removeFirst() == 0;
        assert L.removeFirst() == 1;
        L.addLast(5);
    }
    @Test
    public void testRemove7() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(0);
        assert L.removeFirst() == 0;
        L.addFirst(2);
        assert L.get(0) == 2;
        assert L.get(0) == 2;
        assert L.get(0) == 2;
        assert L.removeFirst() == 2;
        L.addLast(7);
        L.addFirst(8);
        assert L.removeFirst() == 8;
        assert L.get(0) == 7;
        assert L.removeLast() == 7;
        L.addLast(12);
        L.addLast(13);
        assert L.removeFirst() == 12;
        assert L.get(0) == 13;
        L.addLast(16);
    }
    @Test
    public void testRemove8() {
        ArrayDeque<Integer> L = new ArrayDeque<>();
        L.addLast(0);
        L.addLast(1);
        assert L.removeFirst() == 0;
        L.addFirst(3);
        assert L.removeLast() == 1;
        assertFalse(L.isEmpty());
        L.addLast(6);
    }
}
