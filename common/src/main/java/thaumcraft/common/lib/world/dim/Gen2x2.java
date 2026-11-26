package thaumcraft.common.lib.world.dim;

import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.Random;

public class Gen2x2 extends GenCommon {
   static void generateUpperLeft(Level world, Random random, int cx, int cz, int y, CellState cellState) {
      int x = cx * 16;
      int z = cz * 16;

      for(int a = 1; a <= 15; ++a) {
         for(int b = 1; b <= 15; ++b) {
            for(int c = 0; c < 13; ++c) {
               if (a == 1 || b == 1) {
                  placeBlock(world, x + a, y + c, z + b, 1, cellState);
               }
            }
         }
      }

      for(int a = 2; a <= 15; ++a) {
         for(int b = 2; b <= 15; ++b) {
            for(int c = 1; c < 12; ++c) {
               if ((a == 2 || b == 2) && (a != 2 || b <= 4 || b >= 12 || !cellState.west || c >= 10) && (b != 2 || a <= 4 || a >= 12 || !cellState.north || c >= 10)) {
                  placeBlock(world, x + a, y + c, z + b, 8, cellState);
               }
            }
         }
      }

      for(int a = 3; a <= 15; ++a) {
         for(int b = 3; b <= 15; ++b) {
            for(int c = 2; c < 11; ++c) {
               if (a == 3 || b == 3) {
                  placeBlock(world, x + a, y + c, z + b, 18, cellState);
               }
            }
         }
      }

      for(int a = 2; a <= 15; ++a) {
         for(int b = 2; b <= 15; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cellState);
            placeBlock(world, x + a, y, z + b, 8, cellState);
            placeBlock(world, x + a, y + 1, z + b, 19, cellState);
            placeBlock(world, x + a, y + 13, z + b, 1, cellState);
            placeBlock(world, x + a, y + 12, z + b, 8, cellState);
            placeBlock(world, x + a, y + 11, z + b, 2, cellState);
         }
      }

      for(int g = 4; g <= 15; ++g) {
         placeBlock(world, x + g, y + 2, z + 4, 10, Direction.NORTH, cellState);
         placeBlock(world, x + g, y + 10, z + 4, 11, Direction.NORTH, cellState);
      }

      for(int g = 4; g <= 15; ++g) {
         placeBlock(world, x + 4, y + 2, z + g, 10, Direction.WEST, cellState);
         placeBlock(world, x + 4, y + 10, z + g, 11, Direction.WEST, cellState);
      }

      GenCommon.generateConnections(world, random, cx, cz, y, cellState, 3, true);
   }

   static void generateUpperRight(Level world, Random random, int cx, int cz, int y, CellState cellState) {
      int x = cx * 16;
      int z = cz * 16;

      for(int a = 0; a <= 15; ++a) {
         for(int b = 1; b <= 15; ++b) {
            for(int c = 0; c < 13; ++c) {
               if (a == 15 || b == 1) {
                  placeBlock(world, x + a, y + c, z + b, 1, cellState);
               }
            }
         }
      }

      for(int a = 0; a <= 14; ++a) {
         for(int b = 2; b <= 15; ++b) {
            for(int c = 1; c < 12; ++c) {
               if ((a == 14 || b == 2) && (a != 14 || b <= 4 || b >= 12 || !cellState.east || c >= 10) && (b != 2 || a <= 4 || a >= 12 || !cellState.north || c >= 10)) {
                  placeBlock(world, x + a, y + c, z + b, 8, cellState);
               }
            }
         }
      }

      for(int a = 0; a <= 13; ++a) {
         for(int b = 3; b <= 15; ++b) {
            for(int c = 2; c < 11; ++c) {
               if (a == 13 || b == 3) {
                  placeBlock(world, x + a, y + c, z + b, 18, cellState);
               }
            }
         }
      }

      for(int a = 0; a <= 14; ++a) {
         for(int b = 2; b <= 15; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cellState);
            placeBlock(world, x + a, y, z + b, 8, cellState);
            placeBlock(world, x + a, y + 1, z + b, 19, cellState);
            placeBlock(world, x + a, y + 13, z + b, 1, cellState);
            placeBlock(world, x + a, y + 12, z + b, 8, cellState);
            placeBlock(world, x + a, y + 11, z + b, 2, cellState);
         }
      }

      for(int g = 0; g <= 11; ++g) {
         placeBlock(world, x + g, y + 2, z + 4, 10, Direction.NORTH, cellState);
         placeBlock(world, x + g, y + 10, z + 4, 11, Direction.NORTH, cellState);
      }

      for(int g = 4; g <= 15; ++g) {
         placeBlock(world, x + 12, y + 2, z + g, 10, Direction.EAST, cellState);
         placeBlock(world, x + 12, y + 10, z + g, 11, Direction.EAST, cellState);
      }

      GenCommon.generateConnections(world, random, cx, cz, y, cellState, 3, true);
   }

   static void generateLowerLeft(Level world, Random random, int cx, int cz, int y, CellState cellState) {
      int x = cx * 16;
      int z = cz * 16;

      for(int a = 1; a <= 15; ++a) {
         for(int b = 0; b <= 15; ++b) {
            for(int c = 0; c < 13; ++c) {
               if (a == 1 || b == 15) {
                  placeBlock(world, x + a, y + c, z + b, 1, cellState);
               }
            }
         }
      }

      for(int a = 2; a <= 15; ++a) {
         for(int b = 0; b <= 14; ++b) {
            for(int c = 1; c < 12; ++c) {
               if ((a == 2 || b == 14) && (a != 2 || b <= 4 || b >= 12 || !cellState.west || c >= 10) && (b != 14 || a <= 4 || a >= 12 || !cellState.south || c >= 10)) {
                  placeBlock(world, x + a, y + c, z + b, 8, cellState);
               }
            }
         }
      }

      for(int a = 3; a <= 15; ++a) {
         for(int b = 0; b <= 13; ++b) {
            for(int c = 2; c < 11; ++c) {
               if (a == 3 || b == 13) {
                  placeBlock(world, x + a, y + c, z + b, 18, cellState);
               }
            }
         }
      }

      for(int a = 2; a <= 15; ++a) {
         for(int b = 0; b <= 14; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cellState);
            placeBlock(world, x + a, y, z + b, 8, cellState);
            placeBlock(world, x + a, y + 1, z + b, 19, cellState);
            placeBlock(world, x + a, y + 13, z + b, 1, cellState);
            placeBlock(world, x + a, y + 12, z + b, 8, cellState);
            placeBlock(world, x + a, y + 11, z + b, 2, cellState);
         }
      }

      for(int g = 4; g <= 15; ++g) {
         placeBlock(world, x + g, y + 2, z + 12, 10, Direction.SOUTH, cellState);
         placeBlock(world, x + g, y + 10, z + 12, 11, Direction.SOUTH, cellState);
      }

      for(int g = 0; g <= 11; ++g) {
         placeBlock(world, x + 4, y + 2, z + g, 10, Direction.WEST, cellState);
         placeBlock(world, x + 4, y + 10, z + g, 11, Direction.WEST, cellState);
      }

      GenCommon.generateConnections(world, random, cx, cz, y, cellState, 3, true);
   }

   static void generateLowerRight(Level world, Random random, int cx, int cz, int y, CellState cellState) {
      int x = cx * 16;
      int z = cz * 16;

      for(int a = 0; a <= 15; ++a) {
         for(int b = 0; b <= 15; ++b) {
            for(int c = 0; c < 13; ++c) {
               if (a == 15 || b == 15) {
                  placeBlock(world, x + a, y + c, z + b, 1, cellState);
               }
            }
         }
      }

      for(int a = 0; a <= 14; ++a) {
         for(int b = 0; b <= 14; ++b) {
            for(int c = 1; c < 12; ++c) {
               if ((a == 14 || b == 14) && (a != 14 || b <= 4 || b >= 12 || !cellState.east || c >= 10) && (b != 14 || a <= 4 || a >= 12 || !cellState.south || c >= 10)) {
                  placeBlock(world, x + a, y + c, z + b, 8, cellState);
               }
            }
         }
      }

      for(int a = 0; a <= 13; ++a) {
         for(int b = 0; b <= 13; ++b) {
            for(int c = 2; c < 11; ++c) {
               if (a == 13 || b == 13) {
                  placeBlock(world, x + a, y + c, z + b, 18, cellState);
               }
            }
         }
      }

      for(int a = 0; a <= 14; ++a) {
         for(int b = 0; b <= 14; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cellState);
            placeBlock(world, x + a, y, z + b, 8, cellState);
            placeBlock(world, x + a, y + 1, z + b, 19, cellState);
            placeBlock(world, x + a, y + 13, z + b, 1, cellState);
            placeBlock(world, x + a, y + 12, z + b, 8, cellState);
            placeBlock(world, x + a, y + 11, z + b, 2, cellState);
         }
      }

      for(int g = 0; g <= 11; ++g) {
         placeBlock(world, x + g, y + 2, z + 12, 10, Direction.SOUTH, cellState);
         placeBlock(world, x + g, y + 10, z + 12, 11, Direction.SOUTH, cellState);
      }

      for(int g = 0; g <= 12; ++g) {
         placeBlock(world, x + 12, y + 2, z + g, 10, Direction.EAST, cellState);
         placeBlock(world, x + 12, y + 10, z + g, 11, Direction.EAST, cellState);
      }

      GenCommon.generateConnections(world, random, cx, cz, y, cellState, 3, true);
   }
}
