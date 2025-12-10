package thaumcraft.common.lib.world.dim;

import java.util.Arrays;
import java.util.List;

public class MazeThread implements Runnable {
    int x;
    int z;
    int w;
    int h = 0;
    long seed = 0L;

    public MazeThread(int x, int z, int w, int h, long seed) {
        this.x = x;
        this.z = z;
        this.w = w;
        this.h = h;
        this.seed = seed;
    }

    @Override
    public void run() {
        MazeHandler.putToHashMapRaw(new CellLoc(this.x, this.z), (short) 0);
        MazeHandler.putToHashMapRaw(new CellLoc(this.x - this.w, this.z - this.h), (short) 0);
        MazeHandler.putToHashMapRaw(new CellLoc(this.x + this.w, this.z + this.h), (short) 0);
        MazeHandler.putToHashMapRaw(new CellLoc(this.x - this.w, this.z + this.h), (short) 0);
        MazeHandler.putToHashMapRaw(new CellLoc(this.x + this.w, this.z - this.h), (short) 0);

        MazeGenerator gen;
        for (gen = new MazeGenerator(this.w, this.h, this.seed++); !gen.generate(); gen = new MazeGenerator(this.w, this.h, this.seed++)) {
        }

        int col = this.x - (1 + this.w / 2);
        int row = this.z - (1 + this.h / 2);
        List<Integer> directions = Arrays.asList(1, 2, 4, 8);

        for (int a = 0; a < this.w; ++a) {
            for (int b = 0; b < this.h; ++b) {
                if (gen.grid[b][a] > 0) {
                    CellLoc loc = new CellLoc(a + col, b + row);
                    MazeHandler.putToHashMapRaw(loc, (short) gen.grid[b][a]);
                }
            }
        }

        if (MazeHandler.getFromHashMapRaw(new CellLoc(this.x, this.z)) == 0) {
            MazeHandler.removeFromHashMap(new CellLoc(this.x, this.z));
        }

        if (MazeHandler.getFromHashMapRaw(new CellLoc(this.x - this.w, this.z - this.h)) == 0) {
            MazeHandler.removeFromHashMap(new CellLoc(this.x - this.w, this.z - this.h));
        }

        if (MazeHandler.getFromHashMapRaw(new CellLoc(this.x + this.w, this.z + this.h)) == 0) {
            MazeHandler.removeFromHashMap(new CellLoc(this.x + this.w, this.z + this.h));
        }

        if (MazeHandler.getFromHashMapRaw(new CellLoc(this.x - this.w, this.z + this.h)) == 0) {
            MazeHandler.removeFromHashMap(new CellLoc(this.x - this.w, this.z + this.h));
        }

        if (MazeHandler.getFromHashMapRaw(new CellLoc(this.x + this.w, this.z - this.h)) == 0) {
            MazeHandler.removeFromHashMap(new CellLoc(this.x + this.w, this.z - this.h));
        }

    }
}
