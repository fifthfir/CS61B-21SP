package byow.lab12;
import org.junit.Test;

import static byow.TileEngine.TETile.colorVariant;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int FRAME = 2;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private static int size;
    private static int diffX;
    private static int width;
    private static int height;
    private static TETile[][] world;
    public static TETile[][] initWorld(int width, int height) {
        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }

    private static void setOneHex(ArrayList<Point> range, TETile pattern) {
        for (Point point : range) {
            TETile newPattern = colorVariant(pattern, 50, 50, 50, RANDOM);
            world[point.x][point.y] = newPattern;
        }
    }

    private static void addPoint(int num, int x, int y, ArrayList<Point> points) {
        points.add(new Point(x, y));
        for (int j = num; j < 5; j++) {
            y += size * 2;
            points.add(new Point(x, y));
        }
    }
    private static ArrayList<Point> addPoints() {
        ArrayList<Point> starts = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            int x1 = FRAME + diffX * (3-i);
            int y1 = FRAME + size * (i-1);
            addPoint(i, x1, y1, starts);

            if (i / 2 == 1) {  // i == 2 or i == 3
                addPoint(i, x1 + (i-1) * 2 * diffX, y1, starts);
            }
        }
        return starts;
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        size = Integer.parseInt(args[0]);
        diffX = size * 2 - 1;
        width = FRAME + diffX * 5 + size-1 + FRAME;
        height = FRAME * 2 + size * 2 * 5;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        world = initWorld(width, height);

        // all start points
        ArrayList<Point> starts = addPoints();

        // random pattern
        ArrayList<TETile> patterns = new ArrayList<>();
        patterns.add(Tileset.GRASS);
        patterns.add(Tileset.WATER);
        patterns.add(Tileset.FLOWER);
        patterns.add(Tileset.SAND);
        patterns.add(Tileset.MOUNTAIN);
        patterns.add(Tileset.TREE);

        for (Point point : starts) {
            Hexagon hex = new Hexagon(point.x, point.y, size);

            Random random = new Random();
            int randomIndex = random.nextInt(patterns.size());
            TETile randomPattern = patterns.get(randomIndex);

            setOneHex(hex.getRange(), randomPattern);
        }

        ter.renderFrame(world);
    }
}
