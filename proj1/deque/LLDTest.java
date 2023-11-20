package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LLDTest {
    @Test
    public void testAddAndSize() {
        LinkedListDeque<Integer> Lld = new LinkedListDeque<>();
        Lld.addLast(2);
        Lld.addFirst(1);
        assertEquals(2, Lld.size());
        assert (Lld.get(0) == 1);
        assert (Lld.removeLast() == 2);
        assertEquals(1, Lld.size());
    }
}
