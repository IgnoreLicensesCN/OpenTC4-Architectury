package thaumcraft.common.lib.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ChestGenHooks;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Random;

public class WorldGenHilltopStones extends WorldGenerator {
   protected Block[] GetValidSpawnBlocks() {
      return new Block[]{Blocks.stone, Blocks.grass, Blocks.dirt};
   }

   public boolean LocationIsValidSpawn(World world, int i, int j, int k) {
       if (j >= 85) {
           int distanceToAir = 0;

           for (Block checkID = world.getBlock(i, j, k); checkID != Blocks.air; checkID = world.getBlock(i, j + distanceToAir, k)) {
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
       }
       return false;
   }

   public boolean generate(World world, Random rand, int i, int j, int k) {
      if (this.LocationIsValidSpawn(world, i - 2, j, k - 2) && this.LocationIsValidSpawn(world, i, j, k) && this.LocationIsValidSpawn(world, i + 2, j, k) && this.LocationIsValidSpawn(world, i + 2, j, k + 2) && this.LocationIsValidSpawn(world, i, j, k + 2)) {
         Block replaceBlock = world.getBiomeGenForCoords(i, k).topBlock;
         boolean genVines = !world.getBiomeGenForCoords(i, k).getEnableSnow();

         for(int x = i - 3; x <= i + 3; ++x) {
            for(int z = k - 3; z <= k + 3; ++z) {
               if (x != i - 3 && x != i + 3 || z != k - 3 && z != k + 3) {
                  if (rand.nextBoolean()) {
                     world.setBlock(x, j, z, ConfigBlocks.blockCosmeticSolid, 1, 3);
                  } else {
                     world.setBlock(x, j, z, Blocks.obsidian, 0, 3);
                  }

                  boolean stop = false;

                  for(int y = 1; y < 5; ++y) {
                     if (j - y >= 0) {
                        Block blockID = world.getBlock(x, j - y, z);
                        if (replaceBlock != null && blockID == Blocks.snow_layer || blockID == Blocks.red_flower || blockID == Blocks.yellow_flower || blockID == Blocks.tallgrass || blockID == Blocks.air) {
                           world.setBlock(x, j - y, z, replaceBlock, 0, 3);
                        }

                        if (x == i && z == k && y == 1) {
                           world.setBlock(x, j + y, z, ConfigBlocks.blockCosmeticSolid, 1, 3);
                           ChestGenHooks info1 = ChestGenHooks.getInfo("dungeonChest");
                           ChestGenHooks info2 = ChestGenHooks.getInfo("dungeonChest");
                           world.setBlock(x, j + y + 1, z, Blocks.chest, 0, 3);
                           TileEntityChest chest = (TileEntityChest)world.getTileEntity(x, j + y + 1, z);
                           if (chest != null) {
                              WeightedRandomChestContent.generateChestContents(rand, info1.getItems(rand), chest, info1.getCount(rand));
                              WeightedRandomChestContent.generateChestContents(rand, info2.getItems(rand), chest, info2.getCount(rand));
                           }

                           world.setBlock(x, j + y - 1, z, Blocks.mob_spawner, 0, 3);
                           TileEntityMobSpawner var12 = (TileEntityMobSpawner)world.getTileEntity(x, j + y - 1, z);
                           if (var12 != null) {
                              var12.func_145881_a().setEntityName("Thaumcraft.Wisp");
                           }
                        }

                        if (!stop && ((x == i - 3 || x == i + 3) && Math.abs((z - k) % 2) == 1 || (z == k - 3 || z == k + 3) && Math.abs((x - i) % 2) == 1)) {
                           world.setBlock(x, j + y, z, ConfigBlocks.blockCosmeticSolid, 0, 3);
                           if (y >= 2 && rand.nextBoolean()) {
                              stop = true;
                              if (genVines) {
                                 if (rand.nextInt(3) == 0 && world.isAirBlock(x - 1, j + y, z)) {
                                    this.growVines(world, x - 1, j + y, z, 8);
                                 }

                                 if (rand.nextInt(3) == 0 && world.isAirBlock(x + 1, j + y, z)) {
                                    this.growVines(world, x + 1, j + y, z, 2);
                                 }

                                 if (rand.nextInt(3) == 0 && world.isAirBlock(x, j + y, z - 1)) {
                                    this.growVines(world, x, j + y, z - 1, 1);
                                 }

                                 if (rand.nextInt(3) == 0 && world.isAirBlock(x, j + y, z + 1)) {
                                    this.growVines(world, x, j + y, z + 1, 4);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void growVines(World par1World, int par2, int par3, int par4, int par5) {
      this.setBlockAndNotifyAdequately(par1World, par2, par3, par4, Blocks.vine, par5);
      int var6 = 4;

      while(true) {
         --par3;
         if (!par1World.isAirBlock(par2, par3, par4) || var6 <= 0) {
            return;
         }

         this.setBlockAndNotifyAdequately(par1World, par2, par3, par4, Blocks.vine, par5);
         --var6;
      }
   }
}
