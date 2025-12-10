package thaumcraft.common.lib.world.dim;

import net.minecraft.world.Difficulty;
import net.minecraft.world.item.ItemStack;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.Random;

public class GenKeyRoom extends GenCommon {
   static void generateRoom(Level world, Random random, int cx, int cz, int y, Cell cell) {
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
                  if (c > 3 && c < 9 && (a == 8 || b == 8) || c > 4 && c < 8 && (a == 7 || b == 7 || a == 9 || b == 9)) {
                     if (a != 8 && b != 8 || c != 6) {
                        placeBlock(world, x + a, y + c, z + b, 19, cell);
                     }
                  } else {
                     placeBlock(world, x + a, y + c, z + b, 18, cell);
                  }
               }
            }
         }
      }

      for(int a = 2; a <= 14; ++a) {
         for(int b = 2; b <= 14; ++b) {
            placeBlock(world, x + a, y - 1, z + b, 1, cell);
            placeBlock(world, x + a, y, z + b, 8, cell);
            placeBlock(world, x + a, y + 1, z + b, 2, cell);
            placeBlock(world, x + a, y + 13, z + b, 1, cell);
            placeBlock(world, x + a, y + 12, z + b, 8, cell);
            placeBlock(world, x + a, y + 11, z + b, 2, cell);
            if (a > 1 && a < 15 && b > 1 && b < 15) {
               int q = Math.min(Math.abs(8 - a), Math.abs(8 - b));

               for(int g = 0; g < q - 1; ++g) {
                  placeBlock(world, x + a, y + 1 + g, z + b, 2, cell);
               }
            }

            if (a > 3 && a < 13 && b > 3 && b < 13) {
               int q = Math.min(Math.abs(8 - a), Math.abs(8 - b));

               for(int g = 0; g < q; ++g) {
                  placeBlock(world, x + a, y + 11 - g, z + b, 2, cell);
               }
            }
         }
      }

      for(int g = 0; g < 5; ++g) {
         placeBlock(world, x + 6 + g, y + 2, z + 4, 10, Direction.NORTH, cell);
         placeBlock(world, x + 6 + g, y + 2, z + 12, 10, Direction.SOUTH, cell);
         placeBlock(world, x + 12, y + 2, z + 6 + g, 10, Direction.EAST, cell);
         placeBlock(world, x + 4, y + 2, z + 6 + g, 10, Direction.WEST, cell);
      }

      GenCommon.generateConnections(world, random, cx, cz, y, cell, 3, true);
      world.setBlock(x + 8, y + 2, z + 8, ConfigBlocks.blockEldritch, 3, 3);
      EntityPermanentItem entityitem = new EntityPermanentItem(world, (double)x + (double)8.5F, (double)y + (double)3.5F, (double)z + (double)8.5F, new ItemStack(ThaumcraftItems.RUNED_TABLET));
      entityitem.motionY = 0.0F;
      entityitem.motionX = 0.0F;
      entityitem.motionZ = 0.0F;
      world.spawnEntityInWorld(entityitem);
      int zz = 2 + (world.getDifficulty() == Difficulty.HARD ? 2 : (world.getDifficulty() == Difficulty.NORMAL ? 1 : 0));

      for(int qq = 0; qq < zz; ++qq) {
         EntityEldritchGuardian eg = new EntityEldritchGuardian(world);
         double i1 = (double)x + (double)8.5F + (double)(MathHelper.getRandomIntegerInRange(world.getRandom(), 1, 3) * MathHelper.getRandomIntegerInRange(world.getRandom(), -1, 1));
         double j1 = y + 2;
         double k1 = (double)z + (double)8.5F + (double)(MathHelper.getRandomIntegerInRange(world.getRandom(), 1, 3) * MathHelper.getRandomIntegerInRange(world.getRandom(), -1, 1));
         eg.setPosition(i1, j1, k1);
         eg.onSpawnWithEgg(null);
         eg.setHomeArea(x + 8, y + 2, z + 8, 16);
         if (qq == 0 && zz >= 4) {
            EntityUtils.makeChampion(eg, true);
         }

         world.spawnEntityInWorld(eg);
      }

   }
}
