package byow.lab12;

import byow.TileEngine.TETile;

import java.awt.*;
import java.util.ArrayList;

public class Hexagon {
    private int startX;
    private int startY;
    private ArrayList<Point> range;

    public Hexagon(int x, int y, int size) {
        this.startX = x;
        this.startY = y;
        createHex(size);
    }

    private void createHex(int size) {
        this.range = new ArrayList<>();

        for (int j = 0; j < size; j++) {
            int addFromX = startX + size-1-j;
            int lineY = startY + j;
            int lineYup = startY + (size*2-1 - j);
            for (int add = 0; add < size + j * 2; add++) {
                Point point = new Point(addFromX+add, lineY);
                Point pointOp = new Point(addFromX+add, lineYup);
                this.range.add(point);
                this.range.add(pointOp);
            }
        }
    }
    public ArrayList<Point> getRange() {
        return this.range;
    }
}
