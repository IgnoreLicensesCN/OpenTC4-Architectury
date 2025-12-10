package thaumcraft.common.lib.utils;

import net.minecraft.world.level.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class CropUtils {
   public static ArrayList<String> clickableCrops = new ArrayList<>();
   public static ArrayList<String> standardCrops = new ArrayList<>();
   public static ArrayList<String> stackedCrops = new ArrayList<>();
   public static ArrayList<String> lampBlacklist = new ArrayList<>();

   public static void addStandardCrop(ItemStack stack, int grownMeta) {
      if (Block.getBlockFromItem(stack.getItem()) != null) {
         if (grownMeta == 32767) {
            for(int a = 0; a < 16; ++a) {
               standardCrops.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + a);
            }
         } else {
            standardCrops.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + grownMeta);
         }

         if (Block.getBlockFromItem(stack.getItem()) instanceof BlockCrops && grownMeta != 7) {
            standardCrops.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + "7");
         }

      }
   }

   public static void addClickableCrop(ItemStack stack, int grownMeta) {
      if (Block.getBlockFromItem(stack.getItem()) != null) {
         if (grownMeta == 32767) {
            for(int a = 0; a < 16; ++a) {
               clickableCrops.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + a);
            }
         } else {
            clickableCrops.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + grownMeta);
         }

         if (Block.getBlockFromItem(stack.getItem()) instanceof BlockCrops && grownMeta != 7) {
            clickableCrops.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + "7");
         }

      }
   }

   public static void addStackedCrop(ItemStack stack, int grownMeta) {
      if (Block.getBlockFromItem(stack.getItem()) != null) {
         addStackedCrop(Block.getBlockFromItem(stack.getItem()), grownMeta);
      }
   }

   public static void addStackedCrop(Block block, int grownMeta) {
      if (grownMeta == 32767) {
         for(int a = 0; a < 16; ++a) {
            stackedCrops.add(block.getUnlocalizedName() + a);
         }
      } else {
         stackedCrops.add(block.getUnlocalizedName() + grownMeta);
      }

      if (block instanceof BlockCrops && grownMeta != 7) {
         stackedCrops.add(block.getUnlocalizedName() + "7");
      }

   }

   public static boolean isGrownCrop(Level world, int x, int y, int z) {
      if (world.isAirBlock(x, y, z)) {
         return false;
      } else {
         boolean found = false;
         Block bi = world.getBlock(x, y, z);

         for(int a = 0; a < 16; ++a) {
            if (standardCrops.contains(bi.getUnlocalizedName() + a) || clickableCrops.contains(bi.getUnlocalizedName() + a) || stackedCrops.contains(bi.getUnlocalizedName() + a)) {
               found = true;
               break;
            }
         }

         world.getBlock(x, y + 1, z);
         Block biB = world.getBlock(x, y - 1, z);
         int md = world.getBlockMetadata(x, y, z);
         return bi instanceof IGrowable && !((IGrowable)bi).func_149851_a(world, x, y, z, world.isClientSide()) && !(bi instanceof BlockStem) || bi instanceof BlockCrops && md == 7 && !found || bi == Blocks.nether_wart && md >= 3 || bi == Blocks.cocoa && (md & 12) >> 2 >= 2 || standardCrops.contains(bi.getUnlocalizedName() + md) || clickableCrops.contains(bi.getUnlocalizedName() + md) || stackedCrops.contains(bi.getUnlocalizedName() + md) && biB == bi;
      }
   }

   public static void blacklistLamp(ItemStack stack, int meta) {
      if (Block.getBlockFromItem(stack.getItem()) != null) {
         if (meta == 32767) {
            for(int a = 0; a < 16; ++a) {
               lampBlacklist.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + a);
            }
         } else {
            lampBlacklist.add(Block.getBlockFromItem(stack.getItem()).getUnlocalizedName() + meta);
         }

      }
   }

   public static boolean doesLampGrow(Level world, int x, int y, int z) {
      Block bi = world.getBlock(x, y, z);
      int md = world.getBlockMetadata(x, y, z);
      return !lampBlacklist.contains(bi.getUnlocalizedName() + md);
   }
}
