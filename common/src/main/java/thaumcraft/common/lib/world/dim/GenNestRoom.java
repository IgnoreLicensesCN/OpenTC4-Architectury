package thaumcraft.common.lib.world.dim;

import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.dim.cell.CellState;
import thaumcraft.common.tiles.junkbox.TileCrystal;

import java.util.Random;

public class GenNestRoom extends GenCommon {
   static void generateRoom(Level world, Random random, int cx, int cz, int y, CellState cellState) {
      int x = cx * 16;
      int z = cz * 16;

      for(int a = 1; a <= 15; ++a) {
         for(int b = 1; b <= 15; ++b) {
            for(int c = 0; c < 11; ++c) {
               if (a == 1 || a == 15 || b == 1 || b == 15) {
                  placeBlock(world, x + a, y + c, z + b, 1, cellState);
               }
            }
         }
      }

      for(int a = 2; a <= 14; ++a) {
         for(int b = 2; b <= 14; ++b) {
            for(int c = 1; c < 10; ++c) {
               if ((a == 2 || a == 14 || b == 2 || b == 14) && (a != 2 || b <= 3 || b >= 12 || !cellState.west || c >= 10) && (a != 14 || b <= 3 || b >= 12 || !cellState.east || c >= 10) && (b != 2 || a <= 3 || a >= 12 || !cellState.north || c >= 10) && (b != 14 || a <= 3 || a >= 12 || !cellState.south || c >= 10)) {
                  placeBlock(world, x + a, y + c, z + b, 8, cellState);
               }
            }
         }
      }

      for(int a = 3; a <= 13; ++a) {
         for(int b = 3; b <= 13; ++b) {
            for(int c = 2; c < 9; ++c) {
               if (a == 3 || a == 13 || b == 3 || b == 13) {
                  placeBlock(world, x + a, y + c, z + b, 21, cellState);
               }

               if ((a == 4 && !cellState.west || a == 12 && !cellState.east || b == 4 && !cellState.north || b == 12 && !cellState.south) && random.nextBoolean()) {
                  placeBlock(world, x + a, y + c, z + b, 21, cellState);
               }
            }
         }
      }

      for(int a = 2; a <= 14; ++a) {
         for(int b = 2; b <= 14; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cellState);
            placeBlock(world, x + a, y, z + b, 8, cellState);
            placeBlock(world, x + a, y + 1, z + b, 21, cellState);
            placeBlock(world, x + a, y + 11, z + b, 1, cellState);
            placeBlock(world, x + a, y + 10, z + b, 8, cellState);
            placeBlock(world, x + a, y + 9, z + b, 21, cellState);
            if (random.nextBoolean()) {
               placeBlock(world, x + a, y + 8, z + b, 21, cellState);
            } else if (random.nextBoolean() && world.isAirBlock(x + a, y + 8, z + b)) {
               world.setBlock(x + a, y + 8, z + b, ConfigBlocks.blockCrystal, 7, 3);
               TileCrystal te = (TileCrystal)world.getBlockEntity(x + a, y + 8, z + b);
               te.orientation = (short)Direction.DOWN.ordinal();
            }
         }
      }

      placeBlock(world, x + 8, y + 2, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 3, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 4, z + 8, 21, cellState);
      placeBlock(world, x + 7, y + 2, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 2, z + 7, 21, cellState);
      placeBlock(world, x + 9, y + 2, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 2, z + 9, 21, cellState);
      if (random.nextBoolean()) {
         placeBlock(world, x + 7, y + 3, z + 8, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 8, y + 3, z + 7, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 9, y + 3, z + 8, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 8, y + 3, z + 9, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 8, y + 5, z + 8, 7, cellState);
      }

      placeBlock(world, x + 8, y + 8, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 7, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 6, z + 8, 21, cellState);
      placeBlock(world, x + 7, y + 8, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 8, z + 7, 21, cellState);
      placeBlock(world, x + 9, y + 8, z + 8, 21, cellState);
      placeBlock(world, x + 8, y + 8, z + 9, 21, cellState);
      if (random.nextBoolean()) {
         placeBlock(world, x + 7, y + 7, z + 8, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 8, y + 7, z + 7, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 9, y + 7, z + 8, 21, cellState);
      }

      if (random.nextBoolean()) {
         placeBlock(world, x + 8, y + 7, z + 9, 21, cellState);
      }

      GenCommon.generateConnections(world, random, cx, cz, y, cellState, 3, true);

      for(int a = -5; a <= 5; ++a) {
         for(int b = -5; b <= 5; ++b) {
            if (random.nextFloat() < 0.15F && world.isAirBlock(x + 8 + a, y + 2, z + 8 + b)) {
               float rr = random.nextFloat();
               int md = rr < 0.15F ? 2 : (rr < 0.4F ? 1 : 0);
               world.setBlock(x + 8 + a, y + 2, z + 8 + b, random.nextFloat() < 0.2F ? ConfigBlocks.blockLootCrate : ConfigBlocks.blockLootUrn, md, 3);
            }
         }
      }

   }
}
