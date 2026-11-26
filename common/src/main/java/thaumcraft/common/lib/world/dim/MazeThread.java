package thaumcraft.common.lib.world.dim;

import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.HashMap;
import java.util.Map;

//TODO:[maybe wont finished]make better
public class MazeThread implements Runnable {
    int chunkX;
    int chunkZ;
    int width;
    int height = 0;
    long seed = 0L;

    public MazeThread(int chunkX, int chunkZ, int width/*15 + level.random.nextInt(8) * 2*/, int h, long seed) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.width = width;
        this.height = h;
        this.seed = seed;
    }

    public Map<CellLoc, CellState> labyrinth = new HashMap<>();
    @Override
    public void run() {
        var centerCellLoc = new CellLoc(this.chunkX, this.chunkZ);
        var cellNN = new CellLoc(this.chunkX - this.width, this.chunkZ - this.height);//N:Negative
        var cellPP = new CellLoc(this.chunkX + this.width, this.chunkZ + this.height);//P:Positive
        var cellPN = new CellLoc(this.chunkX + this.width, this.chunkZ - this.height);
        var cellNP = new CellLoc(this.chunkX - this.width, this.chunkZ + this.height);
        var cellLocs = new CellLoc[]{centerCellLoc,cellNN,cellPP,cellPN,cellNP};
        for (var cell : cellLocs) {
            labyrinth.put(cell,new CellState());
        }
        MazeGenerator gen = new MazeGenerator(this.width, this.height, this.seed++);
        while (!gen.generate()) {
            gen = new MazeGenerator(this.width, this.height, this.seed++);
        }

        int col = this.chunkX - (1 + this.width / 2);
        int row = this.chunkZ - (1 + this.height / 2);

        for (int a = 0; a < this.width; ++a) {
            for (int b = 0; b < this.height; ++b) {
                if (gen.cellGrid[b][a] != null) {
                    CellLoc loc = new CellLoc(a + col, b + row);
                    labyrinth.put(loc,  gen.cellGrid[b][a]);
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
