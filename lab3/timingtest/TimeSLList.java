package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }
    public static double timeEverySLList(int n) {
        SLList lst = new SLList();

        for (int i = 0; i < n; i++) {
            lst.addLast(1);
        }

        Stopwatch sw = new Stopwatch();

        int ops = 10000;
        for (int i = 0; i < ops; i++) {
            if (lst != null) {
                lst.getLast();
            }
        }
        return sw.elapsedTime();
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList lstN = new AList();
        AList lstTimes = new AList();
        AList lstOps = new AList();
        int ops = 10000;

        for (int i = 1000; i <= 128000; i *= 2) {
            lstN.addLast(i);
            lstTimes.addLast(timeEverySLList(i));
            lstOps.addLast(ops);
        }

        printTimingTable(lstN, lstTimes, lstOps);
    }
}
