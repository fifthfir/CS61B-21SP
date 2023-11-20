package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LLDCTest {
    @Test
    public void testAddAndSize() {
        LinkedListDequeCircular<Integer> Lldc = new LinkedListDequeCircular<>();
        Lldc.addLast(2);
        Lldc.addFirst(1);
        assertEquals(2, Lldc.size());
        assert (Lldc.get(0) == 1);
        assert (Lldc.removeFirst() == 1);
        assertEquals(1, Lldc.size());
    }
}
