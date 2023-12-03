package gh2;

import deque.ArrayDeque;
import deque.Deque;

public class Drum implements Instrument{
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = 1.0; // energy decay factor
    private Deque<Double> buffer;
    public Drum(double frequency) {
        long capacity = Math.round(SR / frequency) * 2;
        buffer = new ArrayDeque<>();

        for (int i = 0; i < capacity; i++) {
            buffer.addLast(0.0);
        }
    }
    @Override
    public void pluck() {
        for (int i = 0; i < buffer.size(); i++) {
            buffer.removeFirst();
            buffer.addLast(Math.random() - 0.5);
        }
    }
    @Override
    public void tic() {
        double front = buffer.removeFirst();
        double toAddLast = (front + buffer.get(0)) * DECAY * 0.5;
        if (Math.random() < 0.5) {
            toAddLast = -toAddLast;
        }
        buffer.addLast(toAddLast);
    }
    @Override
    public double sample() {
        return buffer.get(0);
    }
}
