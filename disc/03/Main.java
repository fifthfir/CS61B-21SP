import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] LL = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0 };
        int[] UR = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
        int[][] S = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        FillGrid.fillGrid(LL, UR, S);
        System.out.println(Arrays.deepToString(S));

        IntList lst = new IntList(0, new IntList(3, new IntList(1, new IntList(4, new IntList(2, new IntList(5, null))))));
        IntList.evenOdd(lst);
    }
}
