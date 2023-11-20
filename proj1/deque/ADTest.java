package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ADTest {
    @Test
    public void testAddAndSize() {
        ArrayDeque<Integer> Ad = new ArrayDeque<>();
        Ad.addLast(2);
        Ad.addFirst(1);
        assertEquals(2, Ad.size());
        assert (Ad.get(0) == 1);
        assert (Ad.removeLast() == 2);
        assertEquals(1, Ad.size());
    }
}
