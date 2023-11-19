package timingtest;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.print("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }
    public static double timeEveryAList(int n) {
        // every N
        Stopwatch sw = new Stopwatch();
        AList lst = new AList();
        for (int i = 0; i < n; i++) {
            lst.addLast(1);
        }
        return sw.elapsedTime();
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // arrays to store the data
        AList lstN = new AList();
        AList lstTimes = new AList();
        AList lstOps = new AList();

        for (int i = 1000; i <= 10000000; i *= 2) {
            lstN.addLast(i);
            lstTimes.addLast(timeEveryAList(i));
            lstOps.addLast(i);
        }

        printTimingTable(lstN, lstTimes, lstOps);
    }
}
