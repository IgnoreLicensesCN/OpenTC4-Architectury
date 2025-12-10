package thaumcraft.common.lib.world.dim;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.tiles.TileEldritchLock;

import java.util.Random;

public class GenBossRoom extends GenCommon {
   static final int[][] PAT_DOORWAY = new int[][]{
           {0, 2, 2, 2, 2, 2, 0},
           {2, 2, 9, 9, 9, 2, 2},
           {2, 9, 9, 9, 9, 9, 2},
           {2, 9, 9, 1, 9, 9, 2},
           {2, 9, 9, 9, 9, 9, 2},
           {2, 2, 9, 9, 9, 2, 2},
           {0, 2, 2, 2, 2, 2, 0},
   };

   static void generateRoom(World world, Random random, int cx, int cz, int y, Cell cell) {
      int x = cx * 16;
      int z = cz * 16;
      switch (cell.feature) {
         case 2:
            Gen2x2.generateUpperLeft(world, random, cx, cz, 50, cell);
            break;
         case 3:
            Gen2x2.generateUpperRight(world, random, cx, cz, 50, cell);
            break;
         case 4:
            Gen2x2.generateLowerLeft(world, random, cx, cz, 50, cell);
            break;
         case 5:
            Gen2x2.generateLowerRight(world, random, cx, cz, 50, cell);
      }

      for(int a = 0; a < 7; ++a) {
         for(int b = 0; b < 7; ++b) {
            int xx = 0;
            int zz = 0;
            ForgeDirection dir = ForgeDirection.UNKNOWN;
            if (cell.north) {
               xx = x + 5 + a;
               zz = z + 3;
               dir = ForgeDirection.NORTH;
            }

            if (cell.south) {
               xx = x + 5 + a;
               zz = z + 13;
               dir = ForgeDirection.SOUTH;
            }

            if (cell.east) {
               xx = x + 13;
               zz = z + 5 + a;
               dir = ForgeDirection.EAST;
            }

            if (cell.west) {
               xx = x + 3;
               zz = z + 5 + a;
               dir = ForgeDirection.WEST;
            }

            switch (PAT_DOORWAY[a][b]) {
               case 1:
                  placeBlock(world, xx, y + 2 + b, zz, 16, cell);
                  TileEntity t = world.getTileEntity(xx, y + 2 + b, zz);
                  if (t instanceof TileEldritchLock) {
                     ((TileEldritchLock)t).setFacing((byte)dir.ordinal());
                  }
                  break;
               case 2:
                  placeBlock(world, xx, y + 2 + b, zz, 15, cell);
                  break;
               case 9:
                  placeBlock(world, xx, y + 2 + b, zz, 17, cell);
            }
         }
      }

   }
}
