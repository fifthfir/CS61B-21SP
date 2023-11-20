package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ADTest {
    @Test
    public void testAddAndSize() {
        ArrayDeque<Integer> Ad = new ArrayDeque<>();
        Ad.addLast(2);
        Ad.addLast(3);
        Ad.addLast(4);
        Ad.addLast(5);
        Ad.addLast(6);
        Ad.addLast(7);
        Ad.addFirst(1);
        Ad.addFirst(0);
        assertEquals(8, Ad.size());
        assert (Ad.get(0) == 0);
        assert (Ad.removeLast() == 7);
        Ad.addLast(7);
        Ad.addLast(8);
        assert (Ad.get(8) == 8);
    }
}
