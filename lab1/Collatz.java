/** Class that prints the Collatz sequence starting from a given number.
 *  @author Rain Zhang
 */
public class Collatz {
    /** next number if 1 return 0 */
    public static int nextNumber(int n) {
        if (n % 2 == 0) {
            return n / 2;
        } else {
            return n * 3 + 1;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        while (n != 1) {
            System.out.print(n);
            System.out.print(' ');
            n = nextNumber(n);
        }
    }
}

