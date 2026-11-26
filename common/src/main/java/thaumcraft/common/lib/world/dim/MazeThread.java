package thaumcraft.common.lib.world.dim;

import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.HashMap;
import java.util.Map;

//TODO:[maybe wont finished]make better
public class MazeThread implements Runnable {
    int x;
    int z;
    int width;
    int height = 0;
    long seed = 0L;

    public MazeThread(int x, int z, int width, int h, long seed) {
        this.x = x;
        this.z = z;
        this.width = width;
        this.height = h;
        this.seed = seed;
    }

    public Map<CellLoc, CellState> labyrinth = new HashMap<>();
    @Override
    public void run() {
        var centerCellLoc = new CellLoc(this.x, this.z);
        var cellNN = new CellLoc(this.x - this.width, this.z - this.height);//N:Negative
        var cellPP = new CellLoc(this.x + this.width, this.z + this.height);//P:Positive
        var cellPN = new CellLoc(this.x + this.width, this.z - this.height);
        var cellNP = new CellLoc(this.x - this.width, this.z + this.height);
        var cellLocs = new CellLoc[]{centerCellLoc,cellNN,cellPP,cellPN,cellNP};
        for (var cell : cellLocs) {
            labyrinth.put(cell,new CellState());
        }
        MazeGenerator gen = new MazeGenerator(this.width, this.height, this.seed++);
        while (!gen.generate()) {
            gen = new MazeGenerator(this.width, this.height, this.seed++);
        }

        int col = this.x - (1 + this.width / 2);
        int row = this.z - (1 + this.height / 2);

        for (int a = 0; a < this.width; ++a) {
            for (int b = 0; b < this.height; ++b) {
                if (gen.grid[b][a] != null) {
                    CellLoc loc = new CellLoc(a + col, b + row);
                    labyrinth.put(loc,  gen.grid[b][a]);
                }
            }
        }

        for (var cellLoc : cellLocs) {
            if (labyrinth.get(cellLoc).isEmpty()) {
                labyrinth.remove(cellLoc);
            }
        }

    }
}
