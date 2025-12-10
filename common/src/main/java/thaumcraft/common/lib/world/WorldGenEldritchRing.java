package thaumcraft.common.lib.world;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.core.Direction;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.tiles.TileBanner;
import thaumcraft.common.tiles.TileEldritchAltar;

import java.util.Random;

public class WorldGenEldritchRing extends WorldGenerator {
   public int chunkX;
   public int chunkZ;
   public int width;
   public int height = 0;

   protected Block[] GetValidSpawnBlocks() {
      return new Block[]{Blocks.stone, Blocks.sand, Blocks.packed_ice, Blocks.grass, Blocks.gravel, Blocks.dirt};
   }

   public boolean LocationIsValidSpawn(Level world, int i, int j, int k) {
      int distanceToAir = 0;

      for(Block checkID = world.getBlock(i, j, k); checkID != Blocks.air; checkID = world.getBlock(i, j + distanceToAir, k)) {
         ++distanceToAir;
      }

       if (distanceToAir <= 2) {
           j += distanceToAir - 1;
           Block blockID = world.getBlock(i, j, k);
           Block blockIDAbove = world.getBlock(i, j + 1, k);
           Block blockIDBelow = world.getBlock(i, j - 1, k);

           for (Block x : this.GetValidSpawnBlocks()) {
               if (blockIDAbove != Blocks.air) {
                   return false;
               }

               if (blockID == x) {
                   return true;
               }

               if ((blockID == Blocks.snow_layer || blockID == Blocks.tallgrass) && blockIDBelow == x) {
                   return true;
               }
           }

       }
       return false;
   }

   public boolean generate(Level world, Random rand, int i, int j, int k) {
      if (this.LocationIsValidSpawn(world, i - 3, j, k - 3)
              && this.LocationIsValidSpawn(world, i, j, k)
              && this.LocationIsValidSpawn(world, i + 3, j, k)
              && this.LocationIsValidSpawn(world, i + 3, j, k + 3)
              && this.LocationIsValidSpawn(world, i, j, k + 3)
              && !MazeHandler.mazesInRange(this.chunkX, this.chunkZ, this.width, this.height)
      ) {
         Block replaceBlock = world.getBiomeGenForCoords(i, k).topBlock;

         for(int x = i - 3; x <= i + 3; ++x) {
            for(int z = k - 3; z <= k + 3; ++z) {
               if (x != i - 3 && x != i + 3 || z != k - 3 && z != k + 3) {
                  for(int q = -4; q < 5; ++q) {
                     Block bb = world.getBlock(x, j + q, z);
                     if (q <= 0 || bb.isReplaceable(world, x, j + q, z) || !bb.getMaterial().blocksMovement() || bb.isFoliage(world, x, j + q, z)) {
                        if (rand.nextInt(4) == 0) {
                           world.setBlock(x, j + q, z, Blocks.obsidian);
                        } else {
                           world.setBlock(x, j + q, z, ConfigBlocks.blockCosmeticSolid, 1, 3);
                        }
                     }

                     if (q > 0) {
                        world.setBlockToAir(x, j + q, z);
                     }
                  }

                  if (x == i && z == k) {
                     world.setBlock(x, j + 1, z, ConfigBlocks.blockEldritch, 0, 3);
                     world.setBlock(x, j, z, ConfigBlocks.blockCosmeticSolid, 1, 3);
                     int r = rand.nextInt(10);
                     BlockEntity te = world.getBlockEntity(x, j + 1, z);
                     if (te instanceof TileEldritchAltar) {
                        TileEldritchAltar altar = (TileEldritchAltar) te;
                        switch (r) {
                           case 1:
                           case 2:
                           case 3:
                           case 4:
                              altar.setSpawner(true);
                              altar.setSpawnType((byte)0);

                              for(int a = 2; a < 6; ++a) {
                                 Direction dir = Direction.getOrientation(a);
                                 world.setBlock(x - dir.offsetX * 3, j + 1, z + dir.offsetZ * 3, ConfigBlocks.blockWoodenDevice, 8, 3);
                                 BlockEntity probablyBanner = world.getBlockEntity(x - dir.offsetX * 3, j + 1, z + dir.offsetZ * 3);
                                 if (probablyBanner instanceof TileBanner) {
                                    TileBanner banner = (TileBanner) probablyBanner;
                                    banner.setFacing(bannerFaceFromDirection(a));
                                 }
                              }
                           case 5:
                           default:
                              break;
                           case 6:
                           case 7:
                              altar.setSpawner(true);
                              altar.setSpawnType((byte)1);
                        }
                     }

                     world.setBlock(x, j + 3, z, ConfigBlocks.blockEldritch, 1, 3);
                     world.setBlock(x, j + 4, z, ConfigBlocks.blockEldritch, 2, 3);
                     world.setBlock(x, j + 5, z, ConfigBlocks.blockEldritch, 2, 3);
                     world.setBlock(x, j + 6, z, ConfigBlocks.blockEldritch, 2, 3);
                     world.setBlock(x, j + 7, z, ConfigBlocks.blockEldritch, 2, 3);
                  } else if (((x == i - 3 || x == i + 3) && Math.abs((z - k) % 2) == 1 || (z == k - 3 || z == k + 3) && Math.abs((x - i) % 2) == 1) && Math.abs(x - i) != Math.abs(z - k)) {
                     world.setBlock(x, j, z, ConfigBlocks.blockCosmeticSolid, 1, 3);
                     world.setBlock(x, j + 1, z, ConfigBlocks.blockEldritch, 3, 3);
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static byte bannerFaceFromDirection(int a) {
      switch (a) {
         case 2:
            return 8;
         case 3:
            return 0;
         case 4:
            return 12;
         case 5:
            return 4;
         default:
            return 0;
      }
   }
}
