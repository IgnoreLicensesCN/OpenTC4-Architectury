package thaumcraft.common.lib.utils;

import cpw.mods.fml.relauncher.ReflectionHelper;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldServer;
import net.minecraft.core.Direction;
import net.minecraftforge.event.ForgeEventFactory;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.EntityFollowingItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockUtils {
   static HashMap<Integer,ArrayList[]> blockEventCache = new HashMap<>();
   static int lastx = 0;
   static int lasty = 0;
   static int lastz = 0;
   static double lastdistance = 0.0F;

   public static boolean harvestBlock(Level world, Player player, int x, int y, int z) {
      return harvestBlock(world, player, x, y, z, false, 0);
   }

   public static boolean harvestBlock(Level world, Player player, int x, int y, int z, boolean followItem, int color) {
      Block block = world.getBlock(x, y, z);
      int i1 = world.getBlockMetadata(x, y, z);
      if (block.getBlockHardness(world, x, y, z) < 0.0F) {
         return false;
      } else {
         world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (i1 << 12));
         boolean flag = false;
         if (player.capabilities.isCreativeMode) {
            removeBlock(world, x, y, z, player);
         } else {
            boolean flag1 = false;
            if (block != null) {
               flag1 = block.canHarvestBlock(player, i1);
            }

            flag = removeBlock(world, x, y, z, player);
            if (flag && flag1) {
               block.harvestBlock(world, player, x, y, z, i1);
               if (followItem) {
                  ArrayList<Entity> entities = EntityUtils.getEntitiesInRange(world, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, player, EntityItem.class, 2.0F);
                  if (entities != null && !entities.isEmpty()) {
                     for(Entity e : entities) {
                        if (!e.isDead && e instanceof EntityItem && e.ticksExisted == 0 && !(e instanceof EntityFollowingItem)) {
                           EntityFollowingItem fi = new EntityFollowingItem(world, e.posX, e.posY, e.posZ, ((EntityItem)e).getEntityItem().copy(), player, color);
                           fi.motionX = e.motionX;
                           fi.motionY = e.motionY;
                           fi.motionZ = e.motionZ;
                           world.spawnEntityInWorld(fi);
                           e.setDead();
                        }
                     }
                  }
               }
            }
         }

         return true;
      }
   }

   public static ArrayList[] getBlockEventList(WorldServer world) {
      if (!blockEventCache.containsKey(world.dimension())) {
         try {
            blockEventCache.put(world.dimension(),
                    ReflectionHelper.getPrivateValue(WorldServer.class, world, new String[]{"field_147490_S"}));
         } catch (Exception var2) {
            return null;
         }
      }

      return blockEventCache.get(world.dimension());
   }

//   public static ItemStack createStackedBlock(Block block) {
//      ItemStack dropped = null;
//
//      try {
//         Method m = ReflectionHelper.findMethod(Block.class, block, new String[]{"createStackedBlock", "func_149644_j"}, Integer.TYPE);
//         dropped = (ItemStack)m.invoke(block);
//      } catch (Exception var4) {
//         Thaumcraft.log.warn("Could not invoke net.minecraft.world.level.block.Block method createStackedBlock");
//      }
//
//      return dropped;
//   }

   public static void dropBlockAsItem(Level world, int x, int y, int z, ItemStack stack, Block block) {
      try {
         Method m = ReflectionHelper.findMethod(Block.class, block, new String[]{"dropBlockAsItem", "func_149642_a"}, World.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, ItemStack.class);
         m.invoke(block, world, x, y, z, stack);
      } catch (Exception var7) {
         Thaumcraft.log.warn("Could not invoke net.minecraft.world.level.block.Block method createStackedBlock");
      }

   }

   public static void dropBlockAsItemWithChance(Level world, Block block, int x, int y, int z, int meta, float dropchance, int fortune, Player player) {
      if (Platform.getEnvironment() != Env.CLIENT && !world.restoringBlockSnapshots) {
         ArrayList<ItemStack> items = block.getDrops(world, x, y, z, meta, fortune);
         dropchance = ForgeEventFactory.fireBlockHarvesting(items, world, block, x, y, z, meta, fortune, dropchance, false, player);

         for(ItemStack item : items) {
            if (world.getRandom().nextFloat() <= dropchance) {
               dropBlockAsItem(world, x, y, z, item, block);
            }
         }
      }

   }

   public static void destroyBlockPartially(Level world, int par1, int par2, int par3, int par4, int par5) {
      for(ServerPlayer ServerPlayer : (List<ServerPlayer>)MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
         if (ServerPlayer != null && ServerPlayer.level() == MinecraftServer.getServer().getEntityWorld() && ServerPlayer.getEntityId() != par1) {
            double d0 = (double)par2 - ServerPlayer.posX;
            double d1 = (double)par3 - ServerPlayer.posY;
            double d2 = (double)par4 - ServerPlayer.posZ;
            if (d0 * d0 + d1 * d1 + d2 * d2 < (double)1024.0F) {
               ServerPlayer.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(par1, par2, par3, par4, par5));
            }
         }
      }

   }

   public static boolean removeBlock(Level world, int par1, int par2, int par3, Player player) {
      Block block = world.getBlock(par1, par2, par3);
      int l = world.getBlockMetadata(par1, par2, par3);
      if (block != null) {
         block.onBlockHarvested(world, par1, par2, par3, l, player);
      }

      boolean flag = block != null && block.removedByPlayer(world, player, par1, par2, par3);
      if (block != null && flag) {
         block.onBlockDestroyedByPlayer(world, par1, par2, par3, l);
      }

      return flag;
   }

   public static void findBlocks(Level world, int x, int y, int z, Block block) {
      int count = 0;

      for(int xx = -2; xx <= 2; ++xx) {
         for(int yy = 2; yy >= -2; --yy) {
            for(int zz = -2; zz <= 2; ++zz) {
               if (Math.abs(lastx + xx - x) > 24) {
                  return;
               }

               if (Math.abs(lasty + yy - y) > 48) {
                  return;
               }

               if (Math.abs(lastz + zz - z) > 24) {
                  return;
               }

               if (world.getBlock(lastx + xx, lasty + yy, lastz + zz) == block && Utils.isWoodLog(world, lastx + xx, lasty + yy, lastz + zz) && block.getBlockHardness(world, lastx + xx, lasty + yy, lastz + zz) >= 0.0F) {
                  double xd = lastx + xx - x;
                  double yd = lasty + yy - y;
                  double zd = lastz + zz - z;
                  double d = xd * xd + yd * yd + zd * zd;
                  if (d > lastdistance) {
                     lastdistance = d;
                     lastx += xx;
                     lasty += yy;
                     lastz += zz;
                     findBlocks(world, x, y, z, block);
                     return;
                  }
               }
            }
         }
      }

   }

   public static boolean breakFurthestBlock(Level world, int x, int y, int z, Block block, Player player) {
      return breakFurthestBlock(world, x, y, z, block, player, false, 0);
   }

   public static boolean breakFurthestBlock(Level world, int x, int y, int z, Block block, Player player, boolean followitem, int color) {
      lastx = x;
      lasty = y;
      lastz = z;
      lastdistance = 0.0F;
      findBlocks(world, x, y, z, block);
      boolean worked = harvestBlock(world, player, lastx, lasty, lastz, followitem, color);
      world.markBlockForUpdate(x, y, z);
      if (worked) {
         world.markBlockForUpdate(lastx, lasty, lastz);

         for(int xx = -3; xx <= 3; ++xx) {
            for(int yy = -3; yy <= 3; ++yy) {
               for(int zz = -3; zz <= 3; ++zz) {
                  world.scheduleBlockUpdate(lastx + xx, lasty + yy, lastz + zz, world.getBlock(lastx + xx, lasty + yy, lastz + zz), 150 + world.getRandom().nextInt(150));
               }
            }
         }
      }

      return worked;
   }

   public static HitResult getTargetBlock(Level world, double x, double y, double z, float yaw, float pitch, boolean par3, double range) {
      Vec3 var13 = new Vec3(x, y, z);
      float var14 = MathHelper.cos(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
      float var15 = MathHelper.sin(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
      float var16 = -MathHelper.cos(-pitch * ((float)Math.PI / 180F));
      float var17 = MathHelper.sin(-pitch * ((float)Math.PI / 180F));
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      Vec3 var23 = var13.addVector((double)var18 * range, (double)var17 * range, (double)var20 * range);
      return world.func_147447_a(var13, var23, par3, !par3, false);
   }

   public static HitResult getTargetBlock(Level world, Entity entity, boolean par3) {
      float var4 = 1.0F;
      float var5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * var4;
      float var6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * var4;
      double var7 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)var4;
      double var9 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)var4 + 1.62 - (double)entity.yOffset;
      double var11 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)var4;
      Vec3 var13 = new Vec3(var7, var9, var11);
      float var14 = MathHelper.cos(-var6 * ((float)Math.PI / 180F) - (float)Math.PI);
      float var15 = MathHelper.sin(-var6 * ((float)Math.PI / 180F) - (float)Math.PI);
      float var16 = -MathHelper.cos(-var5 * ((float)Math.PI / 180F));
      float var17 = MathHelper.sin(-var5 * ((float)Math.PI / 180F));
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      double var21 = 10.0F;
      Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
      return world.func_147447_a(var13, var23, par3, !par3, false);
   }

   public static boolean isBlockAdjacentToAtleast(IBlockAccess world, int x, int y, int z, Block id, int md, int amount) {
      return isBlockAdjacentToAtleast(world, x, y, z, id, md, amount, 1);
   }

   public static boolean isBlockAdjacentToAtleast(IBlockAccess world, int x, int y, int z, Block id, int md, int amount, int range) {
      int count = 0;

      for(int xx = -range; xx <= range; ++xx) {
         for(int yy = -range; yy <= range; ++yy) {
            for(int zz = -range; zz <= range; ++zz) {
               if (xx != 0 || yy != 0 || zz != 0) {
                  if (world.getBlock(x + xx, y + yy, z + zz) == id && (md == 32767 || world.getBlockMetadata(x + xx, y + yy, z + zz) == md)) {
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

   public static List<EntityItem> getContentsOfBlock(Level world, int x, int y, int z) {
       return (List<EntityItem>)world.getEntitiesWithinAABB(
               EntityItem.class,
               AxisAlignedBB.getBoundingBox(x, y, z, (double)x + (double)1.0F, (double)y + (double)1.0F, (double)z + (double)1.0F)
       );
   }

   public static int countExposedSides(Level world, int x, int y, int z) {
      int count = 0;

      for(Direction dir : Direction.VALID_DIRECTIONS) {
         if (world.isAirBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
            ++count;
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

   public static boolean isAdjacentToSolidBlock(Level world, BlockPos pos) {
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

   //| 旧 side | Direction |
   //| ------ | --------- |
   //| 0      | `DOWN`    |
   //| 1      | `UP`      |
   //| 2      | `NORTH`   |
   //| 3      | `SOUTH`   |
   //| 4      | `WEST`    |
   //| 5      | `EAST`    |
   public static boolean isBlockTouchingOnSide(
           BlockGetter world,
           BlockPos pos,
           Block block,
           Direction side
   ) {
      // 1️⃣ 正对方向
      if (world.getBlockState(pos.relative(side)).getBlock() == block) {
         return true;
      }

      // 2️⃣ 同层的“侧向邻居”
      for (Direction dir : Direction.Plane.HORIZONTAL) {
         if (dir.getAxis() == side.getAxis()) {
            continue; // 跳过同轴方向
         }
         if (world.getBlockState(pos.relative(dir)).getBlock() == block) {
            return true;
         }
      }

      // 3️⃣ 上下两层的侧向邻居
      for (Direction vertical : new Direction[]{Direction.UP, Direction.DOWN}) {
         BlockPos shifted = pos.relative(vertical);

         for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (dir.getAxis() == side.getAxis()) {
               continue;
            }
            if (world.getBlockState(shifted.relative(dir)).getBlock() == block) {
               return true;
            }
         }
      }

      return false;
   }

}
