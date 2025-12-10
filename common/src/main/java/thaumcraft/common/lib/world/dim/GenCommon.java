package thaumcraft.common.lib.world.dim;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.TileCrystal;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;

import java.util.ArrayList;
import java.util.Random;

public class GenCommon {
   static ArrayList<ChunkCoordinates> decoCommon = new ArrayList<>();
   static ArrayList<ChunkCoordinates> crabSpawner = new ArrayList<>();
   static ArrayList<ChunkCoordinates> decoUrn = new ArrayList<>();
   static final int BEDROCK = 1;
   static final int BEDROCK_REPL = 99;
   static final int STONE = 2;
   static final int VOID = 8;
   static final int AIR_REPL = 9;
   static final int STAIR_DIRECTIONAL = 10;
   static final int STAIR_DIRECTIONAL_INV = 11;
   static final int SLAB = 12;
   static final int DOOR_BLOCK = 15;
   static final int DOOR_LOCK = 16;
   static final int VOID_DOOR = 17;
   static final int ROCK = 18;
   static final int STONE_NOSPAWN = 19;
   static final int STONE_TRAPPED = 20;
   static final int CRUST = 21;
   static final int[][] PAT_CONNECT = new int[][]{
           {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
           {1, 8, 8, 8, 8, 8, 8, 8, 8, 8, 1},
           {1, 8, 8, 2, 2, 2, 2, 2, 8, 8, 1},
           {1, 8, 2, 5, 9, 9, 9, 6, 2, 8, 1},
           {1, 8, 2, 9, 9, 9, 9, 9, 2, 8, 1},
           {1, 8, 2, 9, 9, 9, 9, 9, 2, 8, 1},
           {1, 8, 2, 9, 9, 9, 9, 9, 2, 8, 1},
           {1, 8, 2, 3, 9, 9, 9, 4, 2, 8, 1},
           {1, 8, 8, 2, 2, 2, 2, 2, 8, 8, 1},
           {1, 8, 8, 8, 8, 8, 8, 8, 8, 8, 1},
           {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
   };

   static void placeBlock(World world, int i, int j, int k, int l, Cell cell) {
      placeBlock(world, i, j, k, l, ForgeDirection.UNKNOWN, cell);
   }

   static void placeBlock(World world, int x, int y, int z, int b, ForgeDirection dir, Cell cell) {
      Block block;
      int meta;
      label233: {
         block = null;
         meta = 0;
         switch (b) {
            case 1:
               if (world.isAirBlock(x, y, z)) {
                  block = Blocks.bedrock;
               }
               break label233;
            case 2:
               if (cell.feature == 7 && world.rand.nextInt(3) == 0) {
                  break;
               }

               if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                  if (world.rand.nextInt(25) == 0) {
                     boolean crab = cell.feature == 7 || world.rand.nextInt(50) == 0;
                     if ((!crab || cell.feature != 0) && (!crab || cell.feature != 7)) {
                        decoCommon.add(new ChunkCoordinates(x, y, z));
                     } else {
                        crabSpawner.add(new ChunkCoordinates(x, y, z));
                     }
                  }

                  block = ConfigBlocks.blockCosmeticSolid;
                  meta = 11;
               }
               break label233;
            case 3:
               if ((double)world.rand.nextFloat() < 0.005) {
                  decoUrn.add(new ChunkCoordinates(x, y, z));
               }

               block = ConfigBlocks.blockStairsEldritch;
               switch (dir.ordinal()) {
                  case 2:
                  case 3:
                     meta = 1;
                     break label233;
                  case 4:
                  case 5:
                     meta = 3;
                  default:
                     break label233;
               }
            case 4:
               if ((double)world.rand.nextFloat() < 0.005) {
                  decoUrn.add(new ChunkCoordinates(x, y, z));
               }

               block = ConfigBlocks.blockStairsEldritch;
               switch (dir.ordinal()) {
                  case 2:
                  case 3:
                     meta = 0;
                     break label233;
                  case 4:
                  case 5:
                     meta = 2;
                  default:
                     break label233;
               }
            case 5:
               block = ConfigBlocks.blockStairsEldritch;
               switch (dir.ordinal()) {
                  case 2:
                  case 3:
                     meta = 5;
                     break label233;
                  case 4:
                  case 5:
                     meta = 7;
                  default:
                     break label233;
               }
            case 6:
               block = ConfigBlocks.blockStairsEldritch;
               switch (dir.ordinal()) {
                  case 2:
                  case 3:
                     meta = 4;
                     break label233;
                  case 4:
                  case 5:
                     meta = 6;
                  default:
                     break label233;
               }
            case 7:
               block = ConfigBlocks.blockEldritch;
               meta = 4;
               break label233;
            case 8:
               block = ConfigBlocks.blockEldritchNothing;
               break label233;
            case 9:
               block = Blocks.air;
               decoCommon.remove(new ChunkCoordinates(x, y, z));
               crabSpawner.remove(new ChunkCoordinates(x, y, z));
               decoUrn.remove(new ChunkCoordinates(x, y, z));
               break label233;
            case 10:
               block = ConfigBlocks.blockStairsEldritch;
               switch (dir) {
                  case NORTH:
                     meta = 3;
                     break label233;
                  case SOUTH:
                     meta = 2;
                     break label233;
                  case EAST:
                     meta = 0;
                     break label233;
                  case WEST:
                     meta = 1;
                  default:
                     break label233;
               }
            case 11:
               block = ConfigBlocks.blockStairsEldritch;
               switch (dir) {
                  case NORTH:
                     meta = 7;
                     break label233;
                  case SOUTH:
                     meta = 6;
                     break label233;
                  case EAST:
                     meta = 4;
                     break label233;
                  case WEST:
                     meta = 5;
                  default:
                     break label233;
               }
            case 15:
               block = ConfigBlocks.blockEldritch;
               meta = 7;
               decoCommon.remove(new ChunkCoordinates(x, y, z));
               crabSpawner.remove(new ChunkCoordinates(x, y, z));
               decoUrn.remove(new ChunkCoordinates(x, y, z));
               break label233;
            case 16:
               block = ConfigBlocks.blockEldritch;
               meta = 8;
               decoCommon.remove(new ChunkCoordinates(x, y, z));
               crabSpawner.remove(new ChunkCoordinates(x, y, z));
               decoUrn.remove(new ChunkCoordinates(x, y, z));
               break label233;
            case 17:
               block = ConfigBlocks.blockAiry;
               meta = 12;
               break label233;
            case 18:
               if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                  block = ConfigBlocks.blockCosmeticSolid;
                  meta = 12;
               }
               break label233;
            case 19:
               if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                  block = ConfigBlocks.blockCosmeticSolid;
                  meta = 13;
               }
               break label233;
            case 20:
               if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                  block = ConfigBlocks.blockEldritch;
                  meta = 10;
               }
               break label233;
            case 21:
               break;
            case 99:
               block = Blocks.bedrock;
            default:
               break label233;
         }

         if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
            block = ConfigBlocks.blockCosmeticSolid;
            meta = 14;
            if (world.rand.nextInt(25) == 0) {
               block = ConfigBlocks.blockEldritch;
               meta = 4;
            } else if (world.rand.nextInt(25) == 0) {
               boolean crab = cell.feature == 7 || (cell.feature == 12 && world.rand.nextBoolean() || world.rand.nextInt(25) == 0);
               if (crab && cell.feature == 0 || crab && cell.feature == 7 || crab && cell.feature == 12) {
                  crabSpawner.add(new ChunkCoordinates(x, y, z));
               }
            }
         }
      }

      if (block != null) {
         world.setBlock(x, y, z, block, meta, block != ConfigBlocks.blockEldritchNothing && block != Blocks.bedrock && block != Blocks.air ? 3 : 0);
      }

   }

   public static void genObelisk(World world, int x, int y, int z) {
      world.setBlock(x, y, z, ConfigBlocks.blockEldritch, 1, 3);
      world.setBlock(x, y + 1, z, ConfigBlocks.blockEldritch, 2, 3);
      world.setBlock(x, y + 2, z, ConfigBlocks.blockEldritch, 2, 3);
      world.setBlock(x, y + 3, z, ConfigBlocks.blockEldritch, 2, 3);
      world.setBlock(x, y + 4, z, ConfigBlocks.blockEldritch, 2, 3);
   }

   static void processDecorations(World world) {
      for(ChunkCoordinates cc : decoUrn) {
         if (world.isAirBlock(cc.posX, cc.posY + 1, cc.posZ)) {
            world.setBlock(cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockCosmeticSolid, 15, 3);
            float rr = world.rand.nextFloat();
            int meta = rr < 0.025F ? 2 : (rr < 0.1F ? 1 : 0);
            world.setBlock(cc.posX, cc.posY + 1, cc.posZ, ConfigBlocks.blockLootUrn, meta, 3);
         }
      }

      for(ChunkCoordinates cc : decoCommon) {
         int exp = BlockUtils.countExposedSides(world, cc.posX, cc.posY, cc.posZ);
         if (exp > 0 && (exp == 1 || !isBedrockShowing(world, cc.posX, cc.posY, cc.posZ)) && !BlockUtils.isBlockAdjacentToAtleast(world, cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockEldritch, 32767, 1)) {
            int meta = world.rand.nextInt(3) != 0 ? 4 : (world.rand.nextInt(8) != 0 ? 5 : 10);
            world.setBlock(cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockEldritch, meta, 3);
            if (meta == 4 && world.rand.nextInt(12) == 0) {
               for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                  if (world.isAirBlock(cc.posX + dir.offsetX, cc.posY + dir.offsetY, cc.posZ + dir.offsetZ)) {
                     world.setBlock(cc.posX + dir.offsetX, cc.posY + dir.offsetY, cc.posZ + dir.offsetZ, ConfigBlocks.blockCrystal, 7, 3);
                     TileCrystal te = (TileCrystal)world.getTileEntity(cc.posX + dir.offsetX, cc.posY + dir.offsetY, cc.posZ + dir.offsetZ);
                     te.orientation = (short)dir.ordinal();
                     break;
                  }
               }
            }
         }
      }

      for(ChunkCoordinates cc : crabSpawner) {
         int exp = BlockUtils.countExposedSides(world, cc.posX, cc.posY, cc.posZ);
         if (exp == 1 && !BlockUtils.isBlockAdjacentToAtleast(world, cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockEldritch, 32767, 1)) {
            world.setBlock(cc.posX, cc.posY, cc.posZ, ConfigBlocks.blockEldritch, 9, 3);
            TileEntity te = world.getTileEntity(cc.posX, cc.posY, cc.posZ);
            if (te instanceof TileEldritchCrabSpawner) {
               for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                  if (world.isAirBlock(cc.posX + dir.offsetX, cc.posY + dir.offsetY, cc.posZ + dir.offsetZ)) {
                     ((TileEldritchCrabSpawner)te).setFacing((byte)dir.ordinal());
                     break;
                  }
               }
            }
         }
      }

      decoCommon.clear();
      crabSpawner.clear();
      decoUrn.clear();
   }

   static boolean isBedrockShowing(World world, int x, int y, int z) {
      for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
         if (!world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).isOpaqueCube() && (world.getBlock(x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ) == Blocks.bedrock || world.getBlock(x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ) == ConfigBlocks.blockEldritchNothing)) {
            return true;
         }
      }

      return false;
   }

   static void generateConnections(World world, Random random, int cx, int cz, int y, Cell cell, int depth, boolean justthetip) {
      int x = cx * 16;
      int z = cz * 16;
      if (cell.north) {
         for(int d = 0; d <= depth; ++d) {
            for(int w = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); w < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++w) {
               for(int h = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); h < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++h) {
                  if (d != depth || !justthetip || PAT_CONNECT[h][w] != 8) {
                     placeBlock(world, x + 3 + w, y + 10 - h, z + d, PAT_CONNECT[h][w], ForgeDirection.NORTH, cell);
                  }
               }
            }
         }
      }

      if (cell.south) {
         for(int d = 0; d <= depth; ++d) {
            for(int w = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); w < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++w) {
               for(int h = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); h < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++h) {
                  if (d != depth || !justthetip || PAT_CONNECT[h][w] != 8) {
                     placeBlock(world, x + 3 + w, y + 10 - h, z + 16 - d, PAT_CONNECT[h][w], ForgeDirection.SOUTH, cell);
                  }
               }
            }
         }
      }

      if (cell.east) {
         for(int d = 0; d <= depth; ++d) {
            for(int w = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); w < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++w) {
               for(int h = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); h < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++h) {
                  if (d != depth || !justthetip || PAT_CONNECT[h][w] != 8) {
                     placeBlock(world, x + 16 - d, y + 10 - h, z + 3 + w, PAT_CONNECT[h][w], ForgeDirection.EAST, cell);
                  }
               }
            }
         }
      }

      if (cell.west) {
         for(int d = 0; d <= depth; ++d) {
            for(int w = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); w < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++w) {
               for(int h = d == depth && justthetip ? 2 : (d == depth - 1 && justthetip ? 1 : 0); h < (d == depth && justthetip ? 9 : (d == depth - 1 && justthetip ? 10 : 11)); ++h) {
                  if (d != depth || !justthetip || PAT_CONNECT[h][w] != 8) {
                     placeBlock(world, x + d, y + 10 - h, z + 3 + w, PAT_CONNECT[h][w], ForgeDirection.WEST, cell);
                  }
               }
            }
         }
      }

   }
}
