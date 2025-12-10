package thaumcraft.common.lib.world.dim;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Random;

public class GenLibraryRoom extends GenCommon {
   static void generateRoom(World world, Random random, int cx, int cz, int y, Cell cell) {
      int x = cx * 16;
      int z = cz * 16;

      for(int a = 1; a <= 15; ++a) {
         for(int b = 1; b <= 15; ++b) {
            for(int c = 0; c < 13; ++c) {
               if (a == 1 || a == 15 || b == 1 || b == 15) {
                  placeBlock(world, x + a, y + c, z + b, 1, cell);
               }
            }
         }
      }

      for(int a = 2; a <= 14; ++a) {
         for(int b = 2; b <= 14; ++b) {
            for(int c = 1; c < 12; ++c) {
               if ((a == 2 || a == 14 || b == 2 || b == 14) && (a != 2 || b <= 3 || b >= 12 || !cell.west || c >= 10) && (a != 14 || b <= 3 || b >= 12 || !cell.east || c >= 10) && (b != 2 || a <= 3 || a >= 12 || !cell.north || c >= 10) && (b != 14 || a <= 3 || a >= 12 || !cell.south || c >= 10)) {
                  placeBlock(world, x + a, y + c, z + b, 8, cell);
               }
            }
         }
      }

      for(int a = 3; a <= 13; ++a) {
         for(int b = 3; b <= 13; ++b) {
            for(int c = 2; c < 11; ++c) {
               if (a == 3 || a == 13 || b == 3 || b == 13) {
                  placeBlock(world, x + a, y + c, z + b, 2, cell);
               }
            }
         }
      }

      for(int a = 2; a <= 14; ++a) {
         for(int b = 2; b <= 14; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cell);
            placeBlock(world, x + a, y, z + b, 8, cell);
            placeBlock(world, x + a, y + 1, z + b, 2, cell);
            placeBlock(world, x + a, y + 12, z + b, 1, cell);
            placeBlock(world, x + a, y + 11, z + b, 8, cell);
            placeBlock(world, x + a, y + 10, z + b, 2, cell);
            if (a > 3 && a < 13 && b > 3 && b < 13) {
               if (a <= 5 && b <= 5 || a <= 5 && b >= 11 || a >= 11 && b <= 5 || a >= 11 && b >= 11) {
                  placeBlock(world, x + a, y + 2, z + b, 2, cell);
                  placeBlock(world, x + a, y + 9, z + b, 2, cell);
               }

               if (a == 5 && b == 5 || a == 5 && b == 11 || a == 11 && b == 5 || a == 11 && b == 11) {
                  world.setBlock(x + a, y + 3, z + b, ConfigBlocks.blockCosmeticSolid, 15, 3);
                  world.setBlock(x + a, y + 8, z + b, ConfigBlocks.blockCosmeticSolid, 15, 3);
               }
            }
         }
      }

      for(int g = 0; g < 5; ++g) {
         placeBlock(world, x + 6 + g, y + 2, z + 4, 10, ForgeDirection.NORTH, cell);
         placeBlock(world, x + 6 + g, y + 2, z + 12, 10, ForgeDirection.SOUTH, cell);
         placeBlock(world, x + 12, y + 2, z + 6 + g, 10, ForgeDirection.EAST, cell);
         placeBlock(world, x + 4, y + 2, z + 6 + g, 10, ForgeDirection.WEST, cell);
         placeBlock(world, x + 6 + g, y + 9, z + 4, 11, ForgeDirection.NORTH, cell);
         placeBlock(world, x + 6 + g, y + 9, z + 12, 11, ForgeDirection.SOUTH, cell);
         placeBlock(world, x + 12, y + 9, z + 6 + g, 11, ForgeDirection.EAST, cell);
         placeBlock(world, x + 4, y + 9, z + 6 + g, 11, ForgeDirection.WEST, cell);
      }

      world.setBlock(x + 5, y + 4, z + 5, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 5, y + 5, z + 5, ConfigBlocks.blockSlabStone, 1, 3);
      world.setBlock(x + 5, y + 4, z + 11, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 5, y + 5, z + 11, ConfigBlocks.blockSlabStone, 1, 3);
      world.setBlock(x + 11, y + 4, z + 5, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 11, y + 5, z + 5, ConfigBlocks.blockSlabStone, 1, 3);
      world.setBlock(x + 11, y + 4, z + 11, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 11, y + 5, z + 11, ConfigBlocks.blockSlabStone, 1, 3);
      world.setBlock(x + 5, y + 7, z + 5, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 5, y + 6, z + 5, ConfigBlocks.blockSlabStone, 9, 3);
      world.setBlock(x + 5, y + 7, z + 11, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 5, y + 6, z + 11, ConfigBlocks.blockSlabStone, 9, 3);
      world.setBlock(x + 11, y + 7, z + 5, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 11, y + 6, z + 5, ConfigBlocks.blockSlabStone, 9, 3);
      world.setBlock(x + 11, y + 7, z + 11, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 11, y + 6, z + 11, ConfigBlocks.blockSlabStone, 9, 3);
      world.setBlock(x + 8, y + 2, z + 8, ConfigBlocks.blockCosmeticSolid, 15, 3);
      world.setBlock(x + 8, y + 3, z + 8, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 8, y + 4, z + 8, ConfigBlocks.blockSlabStone, 1, 3);
      world.setBlock(x + 8, y + 9, z + 8, ConfigBlocks.blockCosmeticSolid, 15, 3);
      world.setBlock(x + 8, y + 8, z + 8, ConfigBlocks.blockEldritch, 5, 3);
      world.setBlock(x + 8, y + 7, z + 8, ConfigBlocks.blockSlabStone, 9, 3);
      GenCommon.generateConnections(world, random, cx, cz, y, cell, 3, true);
   }
}
