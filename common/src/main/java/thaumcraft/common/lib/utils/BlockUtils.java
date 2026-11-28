package thaumcraft.common.lib.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;

public class BlockUtils {
   public static boolean isBlockAdjacentToAtleast(BlockGetter world, BlockPos pos, TagKey<Block> tag, int amount) {
      return isBlockAdjacentToAtleast(world, pos, tag/*, md*/, amount, 1);
   }

   public static boolean isBlockAdjacentToAtleast(BlockGetter world, BlockPos pos, TagKey<Block> tag, int amount, int range) {
      int count = 0;
      var mutable = new BlockPos.MutableBlockPos();
      for(int xx = -range; xx <= range; ++xx) {
         for(int yy = -range; yy <= range; ++yy) {
            for(int zz = -range; zz <= range; ++zz) {
               if (xx != 0 || yy != 0 || zz != 0) {
                  if (world.getBlockState(mutable.setWithOffset(pos,xx,yy,zz)).is(tag)) {
                     ++count;
                  }

                  if (count >= amount) {
                     return true;
                  }
               }
            }
         }
      }

      return count >= amount;
   }

   public static int countExposedSides(BlockGetter world, BlockPos pos) {
      int count = 0;

      var mutablePos = new BlockPos.MutableBlockPos().set(pos);
      for(Direction dir : Direction.values()) {
         if (world.getBlockState(mutablePos.setWithOffset(pos,dir)).isAir()){
            count += 1;
         }
      }

      return count;
   }

   public static boolean isBlockExposed(Level world, BlockPos pos) {
      return !world.getBlockState(pos.above()).isCollisionShapeFullBlock(world, pos)
              || !world.getBlockState(pos.below()).isCollisionShapeFullBlock(world, pos)
              || !world.getBlockState(pos.north()).isCollisionShapeFullBlock(world, pos)
              || !world.getBlockState(pos.west()).isCollisionShapeFullBlock(world, pos)
              || !world.getBlockState(pos.south()).isCollisionShapeFullBlock(world, pos)
              || !world.getBlockState(pos.east()).isCollisionShapeFullBlock(world, pos);
   }

   public static boolean isAdjacentToSolidBlock(BlockGetter world, BlockPos pos) {
      for(int a = 0; a < 6; ++a) {
         Direction d = Direction.values()[a];
         var consideringPos = pos.relative(d);
         if (world.getBlockState(consideringPos).isFaceSturdy(world,consideringPos,d.getOpposite())) {
            return true;
         }
      }
      return false;
   }

   private static final int[][] DIRS_6 = {
           { 0,  0,  1},
           { 0,  0, -1},
           { 1,  0,  0},
           {-1,  0,  0},
           { 0,  1,  0},
           { 0, -1,  0}
   };


   public static boolean isBlockTouching(BlockGetter world, BlockPos pos, Block block) {
      return world.getBlockState(pos.north()).getBlock() == block
              || world.getBlockState(pos.south()).getBlock() == block
              || world.getBlockState(pos.east()).getBlock()  == block
              || world.getBlockState(pos.west()).getBlock()  == block
              || world.getBlockState(pos.above()).getBlock() == block
              || world.getBlockState(pos.below()).getBlock() == block;
   }

}
