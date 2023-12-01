public class FillGrid {
    public static void fillGrid(int[] LL, int[] UR, int[][] S) {
        int N = S.length;
        int kL, kR;
        kL = kR = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i > j) {
                    S[i][j] = LL[kL];
                    kL++;
                } else if (i < j) {
                    S[i][j] = UR[kR];
                    kR++;
                }
            }
        }
    }
}
