public class IntList {
    public int first;
    public IntList rest;
    public IntList(int f, IntList r) {
        first = f;
        rest = r;
    }
    public int size() { /** Return the size of the list using... recursion! */
        if (rest == null) {
            return 1;
        }
        return 1 + rest.size();
    }
    public int iterativeSize() {
        int size = 0;
        IntList p = this;
        while (p != null) {
            size++;
            p = p.rest;
        }
        return size;
    }
    public int get(int i) {
        if (i == 0) {
            return first;
        }
        return rest.get(i - 1);
    }
    public IntList incrList(int x) {
        IntList Q = new IntList(0, null);
        Q.first = this.first + x;
        if (this.rest != null) {
            Q.rest = this.rest.incrList(x);
        }
        return Q;
    }
    public IntList dincrList(int x) {
        this.first += x;
        if (this.rest != null) {
            this.rest = this.rest.dincrList(x);
        }
        return this;
    }
    public void printIntList() {
        System.out.print(this.first);
        System.out.print(",");
        if (this.rest != null) {
            this.rest.printIntList();
        }
        System.out.println("");
    }
}
