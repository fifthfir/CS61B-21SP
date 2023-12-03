package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    public static class MyComparator implements Comparator<String> {
        public int compare(String a, String b) {
            if (a.compareTo(b) > 0) {
                return 1;
            } else if (a.compareTo(b) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    @Test
    public void maxADTest() {
        MyComparator myComparator = new MyComparator();
        MaxArrayDeque<String> mad1 = new MaxArrayDeque<>(myComparator);
        assertTrue ("A new list should be empty", mad1.isEmpty());

        mad1.addFirst("first");
        assertEquals(1, mad1.size());
        assertFalse("The list should contain one item now", mad1.isEmpty());

        mad1.addLast("middle");
        assertEquals(2, mad1.size());

        mad1.addLast("last2");
        assertEquals(3, mad1.size());

        assertEquals("The max string should be middle", "middle", mad1.max());
    }
}
