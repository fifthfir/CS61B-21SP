/** Class that prints the Collatz sequence starting from a given number.
 *  @author Rain Zhang
 */
public class Collatz {

    /** Buggy implementation of nextNumber! */
    public static int nextNumber(int n) {
        int ret = 0;
        if (n % 2 == 0) {
            ret = n / 2;
        }else if (n % 2 == 1 && n != 1) {
            ret = 3 * n + 1;
        }
        return ret;
    }

    public static void main(String[] args) {
        int n = 5;
        while (n != 0) {
            System.out.print(n);
            System.out.print(' ');
            n = nextNumber(n);
        }
    }
}

