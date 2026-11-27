package thaumcraft.common.lib.world.dim;

//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntityMobSpawner;
//import net.minecraft.world.level.Level;
//import net.minecraft.core.Direction;
//import thaumcraft.common.config.ConfigBlocks;
//import thaumcraft.common.lib.utils.BlockUtils;
//import thaumcraft.common.lib.utils.Utils;
//import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
//import thaumcraft.common.lib.world.dim.cell.CellState;
//
//import java.util.Random;

public class GenPassage extends GenCommon {
//   static void generateDefaultPassage(Level world, Random random, int cx, int cz, int y, CellState cellState) {
//      int x = cx * 16;
//      int z = cz * 16;
//      generateConnections(world, random, cx, cz, y, cellState, 4, false);
//      int mod = 0;
//      if (cellState.north && cellState.south && cellState.west && cellState.east && random.nextBoolean()) {
//         mod = 1;
//      }
//
//      for(int w = 1; w < 8; ++w) {
//         for(int h = 1; h < 8; ++h) {
//            if (w == 4 && h == 4 && mod == 1) {
//               placeBlock(world, x + 4 + w, y + 2, z + 4 + h, 7, cellState);
//               placeBlock(world, x + 4 + w, y + 8, z + 4 + h, 7, cellState);
//            } else {
//               placeBlock(world, x + 4 + w, y + 2, z + 4 + h, cellState.feature == 11 && random.nextInt(3) == 0 ? 20 : 2, cellState);
//               placeBlock(world, x + 4 + w, y + 8, z + 4 + h, cellState.feature == 11 && random.nextInt(3) == 0 ? 20 : 2, cellState);
//            }
//
//            placeBlock(world, x + 4 + w, y, z + 4 + h, 1, cellState);
//            placeBlock(world, x + 4 + w, y + 10, z + 4 + h, 1, cellState);
//            placeBlock(world, x + 4 + w, y + 1, z + 4 + h, 8, cellState);
//            placeBlock(world, x + 4 + w, y + 9, z + 4 + h, 8, cellState);
//         }
//      }
//
//      if (cellState.north) {
//         for(int w = 2 + mod; w < 9 - mod; ++w) {
//            for(int h = 2 + mod; h < 9 - mod; ++h) {
//               placeBlock(world, x + 3 + w, y + 10 - h, z + 5, PAT_CONNECT[h][w], Direction.NORTH, cellState);
//            }
//         }
//
//         if (mod == 0) {
//            if (cellState.west) {
//               placeBlock(world, x + 6, y + 3, z + 6, 3, Direction.EAST, cellState);
//               placeBlock(world, x + 6, y + 7, z + 6, 5, Direction.EAST, cellState);
//            }
//
//            if (cellState.east) {
//               placeBlock(world, x + 10, y + 3, z + 6, 3, Direction.EAST, cellState);
//               placeBlock(world, x + 10, y + 7, z + 6, 5, Direction.EAST, cellState);
//            }
//         }
//      } else {
//         for(int w = 1; w < 8; ++w) {
//            for(int h = 1; h < 8; ++h) {
//               placeBlock(world, x + 4 + w, y + 9 - h, z + 5, cellState.feature == 11 && random.nextInt(3) == 0 ? 20 : 2, cellState);
//               placeBlock(world, x + 4 + w, y + 9 - h, z + 4, 8, cellState);
//               placeBlock(world, x + 4 + w, y + 9 - h, z + 3, 1, cellState);
//               if (h == 7) {
//                  placeBlock(world, x + 4 + w, y + 1, z + 4, 1, cellState);
//                  placeBlock(world, x + 4 + w, y + 9, z + 4, 1, cellState);
//               }
//
//               if (w == 7) {
//                  placeBlock(world, x + 4, y + 9 - h, z + 4, 1, cellState);
//                  placeBlock(world, x + 12, y + 9 - h, z + 4, 1, cellState);
//               }
//            }
//         }
//
//         for(int w = 2; w < 7; ++w) {
//            placeBlock(world, x + 4 + w, y + 3, z + 6, 3, Direction.EAST, cellState);
//            placeBlock(world, x + 4 + w, y + 7, z + 6, 5, Direction.EAST, cellState);
//         }
//      }
//
//      if (cellState.south) {
//         for(int w = 2 + mod; w < 9 - mod; ++w) {
//            for(int h = 2 + mod; h < 9 - mod; ++h) {
//               placeBlock(world, x + 3 + w, y + 10 - h, z + 11, PAT_CONNECT[h][w], Direction.SOUTH, cellState);
//            }
//         }
//
//         if (mod == 0) {
//            if (cellState.west) {
//               placeBlock(world, x + 6, y + 3, z + 10, 4, Direction.EAST, cellState);
//               placeBlock(world, x + 6, y + 7, z + 10, 6, Direction.EAST, cellState);
//            }
//
//            if (cellState.east) {
//               placeBlock(world, x + 10, y + 3, z + 10, 4, Direction.EAST, cellState);
//               placeBlock(world, x + 10, y + 7, z + 10, 6, Direction.EAST, cellState);
//            }
//         }
//      } else {
//         for(int w = 1; w < 8; ++w) {
//            for(int h = 1; h < 8; ++h) {
//               placeBlock(world, x + 4 + w, y + 9 - h, z + 11, cellState.feature == 11 && random.nextInt(3) == 0 ? 20 : 2, cellState);
//               placeBlock(world, x + 4 + w, y + 9 - h, z + 12, 8, cellState);
//               placeBlock(world, x + 4 + w, y + 9 - h, z + 13, 1, cellState);
//               if (h == 7) {
//                  placeBlock(world, x + 4 + w, y + 1, z + 12, 1, cellState);
//                  placeBlock(world, x + 4 + w, y + 9, z + 12, 1, cellState);
//               }
//
//               if (w == 7) {
//                  placeBlock(world, x + 4, y + 9 - h, z + 12, 1, cellState);
//                  placeBlock(world, x + 12, y + 9 - h, z + 12, 1, cellState);
//               }
//            }
//         }
//
//         for(int w = 2; w < 7; ++w) {
//            placeBlock(world, x + 4 + w, y + 3, z + 10, 4, Direction.EAST, cellState);
//            placeBlock(world, x + 4 + w, y + 7, z + 10, 6, Direction.EAST, cellState);
//         }
//      }
//
//      if (cellState.east) {
//         for(int w = 2 + mod; w < 9 - mod; ++w) {
//            for(int h = 2 + mod; h < 9 - mod; ++h) {
//               placeBlock(world, x + 11, y + 10 - h, z + 3 + w, PAT_CONNECT[h][w], Direction.EAST, cellState);
//            }
//         }
//
//         if (mod == 0) {
//            if (cellState.north) {
//               placeBlock(world, x + 10, y + 3, z + 6, 4, Direction.NORTH, cellState);
//               placeBlock(world, x + 10, y + 7, z + 6, 6, Direction.NORTH, cellState);
//            }
//
//            if (cellState.south) {
//               placeBlock(world, x + 10, y + 3, z + 10, 4, Direction.NORTH, cellState);
//               placeBlock(world, x + 10, y + 7, z + 10, 6, Direction.NORTH, cellState);
//            }
//         }
//      } else {
//         for(int w = 1; w < 8; ++w) {
//            for(int h = 1; h < 8; ++h) {
//               placeBlock(world, x + 11, y + 9 - h, z + 4 + w, cellState.feature == 11 && random.nextInt(3) == 0 ? 20 : 2, cellState);
//               placeBlock(world, x + 12, y + 9 - h, z + 4 + w, 8, cellState);
//               placeBlock(world, x + 13, y + 9 - h, z + 4 + w, 1, cellState);
//               if (h == 7) {
//                  placeBlock(world, x + 12, y + 1, z + 4 + w, 1, cellState);
//                  placeBlock(world, x + 12, y + 9, z + 4 + w, 1, cellState);
//               }
//
//               if (w == 7) {
//                  placeBlock(world, x + 12, y + 9 - h, z + 4, 1, cellState);
//                  placeBlock(world, x + 12, y + 9 - h, z + 12, 1, cellState);
//               }
//            }
//         }
//
//         for(int w = 2; w < 7; ++w) {
//            placeBlock(world, x + 10, y + 3, z + 4 + w, 4, Direction.NORTH, cellState);
//            placeBlock(world, x + 10, y + 7, z + 4 + w, 6, Direction.NORTH, cellState);
//         }
//      }
//
//      if (cellState.west) {
//         for(int w = 2 + mod; w < 9 - mod; ++w) {
//            for(int h = 2 + mod; h < 9 - mod; ++h) {
//               placeBlock(world, x + 5, y + 10 - h, z + 3 + w, PAT_CONNECT[h][w], Direction.WEST, cellState);
//            }
//         }
//
//         if (mod == 0) {
//            if (cellState.north) {
//               placeBlock(world, x + 6, y + 3, z + 6, 3, Direction.NORTH, cellState);
//               placeBlock(world, x + 6, y + 7, z + 6, 5, Direction.NORTH, cellState);
//            }
//
//            if (cellState.south) {
//               placeBlock(world, x + 6, y + 3, z + 10, 3, Direction.NORTH, cellState);
//               placeBlock(world, x + 6, y + 7, z + 10, 5, Direction.NORTH, cellState);
//            }
//         }
//      } else {
//         for(int w = 1; w < 8; ++w) {
//            for(int h = 1; h < 8; ++h) {
//               placeBlock(world, x + 5, y + 9 - h, z + 4 + w, cellState.feature == 11 && random.nextInt(3) == 0 ? 20 : 2, cellState);
//               placeBlock(world, x + 4, y + 9 - h, z + 4 + w, 8, cellState);
//               placeBlock(world, x + 3, y + 9 - h, z + 4 + w, 1, cellState);
//               if (h == 7) {
//                  placeBlock(world, x + 4, y + 1, z + 4 + w, 1, cellState);
//                  placeBlock(world, x + 4, y + 9, z + 4 + w, 1, cellState);
//               }
//
//               if (w == 7) {
//                  placeBlock(world, x + 4, y + 9 - h, z + 4, 1, cellState);
//                  placeBlock(world, x + 4, y + 9 - h, z + 12, 1, cellState);
//               }
//            }
//         }
//
//         for(int w = 2; w < 7; ++w) {
//            placeBlock(world, x + 6, y + 3, z + 4 + w, 3, Direction.NORTH, cellState);
//            placeBlock(world, x + 6, y + 7, z + 4 + w, 5, Direction.NORTH, cellState);
//         }
//      }
//
//      if (mod == 1) {
//         placeBlock(world, x + 5, y + 3, z + 5, 3, Direction.EAST, cellState);
//         placeBlock(world, x + 5, y + 7, z + 5, 5, Direction.EAST, cellState);
//         placeBlock(world, x + 5, y + 3, z + 6, 3, Direction.NORTH, cellState);
//         placeBlock(world, x + 5, y + 7, z + 6, 5, Direction.NORTH, cellState);
//         placeBlock(world, x + 11, y + 3, z + 5, 3, Direction.EAST, cellState);
//         placeBlock(world, x + 11, y + 7, z + 5, 5, Direction.EAST, cellState);
//         placeBlock(world, x + 11, y + 3, z + 6, 4, Direction.NORTH, cellState);
//         placeBlock(world, x + 11, y + 7, z + 6, 6, Direction.NORTH, cellState);
//         placeBlock(world, x + 5, y + 3, z + 11, 3, Direction.NORTH, cellState);
//         placeBlock(world, x + 5, y + 7, z + 11, 5, Direction.NORTH, cellState);
//         placeBlock(world, x + 6, y + 3, z + 11, 4, Direction.EAST, cellState);
//         placeBlock(world, x + 6, y + 7, z + 11, 6, Direction.EAST, cellState);
//         placeBlock(world, x + 11, y + 3, z + 11, 4, Direction.NORTH, cellState);
//         placeBlock(world, x + 11, y + 7, z + 11, 6, Direction.NORTH, cellState);
//         placeBlock(world, x + 10, y + 3, z + 11, 4, Direction.EAST, cellState);
//         placeBlock(world, x + 10, y + 7, z + 11, 6, Direction.EAST, cellState);
//      }
//
//      if (cellState.feature == 12) {
//         for(int w = -4; w <= 4; ++w) {
//            for(int h = -4; h < 5; ++h) {
//               for(int j = -4; j <= 4; ++j) {
//                  if ((world.isAirBlock(x + 8 + w, y + 4 + h, z + 8 + j) || world.getBlock(x + 8 + w, y + 4 + h, z + 8 + j) == ConfigBlocks.blockCosmeticSolid || world.getBlock(x + 8 + w, y + 4 + h, z + 8 + j) == ConfigBlocks.blockStairsEldritch) && random.nextBoolean()) {
//                     placeBlock(world, x + 8 + w, y + 4 + h, z + 8 + j, 21, cellState);
//                  }
//               }
//            }
//         }
//      }
//
//      if (cellState.feature == 13) {
//         for(int w = -4; w <= 4; ++w) {
//            for(int h = -3; h <= 3; ++h) {
//               for(int j = -4; j <= 4; ++j) {
//                  if (world.isAirBlock(x + 8 + w, y + 4 + h, z + 8 + j) && BlockUtils.isAdjacentToSolidBlock(world, x + 8 + w, y + 4 + h, z + 8 + j)) {
//                     if (random.nextInt(3) != 0) {
//                        world.setBlock(x + 8 + w, y + 4 + h, z + 8 + j, ConfigBlocks.blockTaintFibres, random.nextInt(4) == 0 ? 1 : 0, 3);
//                     }
//
//                     Utils.setBiomeAt(world, x + 8 + w, z + 8 + j, ThaumcraftWorldGenerator.biomeTaint);
//                  }
//               }
//            }
//         }
//      }
//
//      if (cellState.feature == 14) {
//         for(int w = -3; w <= 3; ++w) {
//            for(int h = -3; h <= 3; ++h) {
//               for(int j = -3; j <= 3; ++j) {
//                  if (world.isAirBlock(x + 8 + w, y + 4 + h, z + 8 + j) && random.nextFloat() < 0.35F) {
//                     world.setBlock(x + 8 + w, y + 4 + h, z + 8 + j, Blocks.web);
//                  }
//
//                  world.setBlock(x + 8, y + 4, z + 8, Blocks.mob_spawner);
//                  BlockEntityMobSpawner var12 = (BlockEntityMobSpawner)world.getBlockEntity(x + 8, y + 4, z + 8);
//                  if (var12 != null) {
//                     var12.func_145881_a().setEntityName("Thaumcraft.MindSpider");
//                  }
//               }
//            }
//         }
//      }
//
//   }
}
