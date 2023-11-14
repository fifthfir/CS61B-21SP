
import org.junit.Test;

import static org.junit.Assert.*;

public class TestIntList {

    /** The IntList that we'll be testing on. */
    static IntList L;
    static int inc = 5;

    @Test
    public void testIncrList() {
        L = new IntList(5, null);
        L.rest = new IntList(7, null);
        L.rest.rest = new IntList(9, null);

        IntList Q = L.incrList(inc);
        assertFalse(Q == L);
        assertTrue(Q.first == L.first + inc);
    }

    @Test
    public void testDincrList() {
        L = new IntList(5, null);
        L.rest = new IntList(7, null);
        L.rest.rest = new IntList(9, null);

        IntList Q = L.dincrList(inc);
        assertTrue(Q == L);
    }
}
