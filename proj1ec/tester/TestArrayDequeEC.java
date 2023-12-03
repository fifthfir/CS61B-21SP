package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void test1() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();
        String errString = "";

        for (int i = 0; i < 10; i++) {
            double num = StdRandom.uniform();

            if (num < 0.5) {
                sad1.addLast(i);
                sad2.addLast(i);
                errString += ("addLast(" + i + ")\n");
            } else {
                sad1.addFirst(i);
                sad2.addFirst(i);
                errString += ("addFirst(" + i + ")\n");
            }
        }

        errString += ("size()\n");
        assertEquals(errString, sad1.size(), sad2.size());

        for (int i = 0; i < 10; i ++) {
            double num = StdRandom.uniform();

            if (num < 0.5) {
                Integer item1 = sad1.removeLast();
                Integer item2 = sad2.removeLast();
                errString += ("removeLast()\n");
                assertEquals(errString, item2, item1);
            } else {
                Integer item1 = sad1.removeFirst();
                Integer item2 = sad2.removeFirst();
                errString += ("removeFirst()\n");
                assertEquals(errString, item2, item1);
            }
            errString += ("size()\n");
            assertEquals(errString, sad2.size(), sad1.size());
        }
    }
}
