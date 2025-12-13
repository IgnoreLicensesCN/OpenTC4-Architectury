package thaumcraft.common.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.HitResult.MovingObjectType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ItemSpawnerEgg extends Item {
   static ArrayList spawnList = new ArrayList<>();
   @SideOnly(Side.CLIENT)
   private IIcon theIcon;

   public static void addMapping(String name, int c1, int c2) {
      spawnList.add(new EntityEggStuff("Thaumcraft." + name, c1, c2));
   }

   public ItemSpawnerEgg() {
      this.setHasSubtypes(true);
      this.setCreativeTab(CreativeTabs.tabMisc);
   }

   public String getItemStackDisplayName(ItemStack par1ItemStack) {
      String s = (StatCollector.translateToLocal("item.monsterPlacer.name")).trim();
      String s1 = ((EntityEggStuff)spawnList.get(par1ItemStack.getItemDamage())).name;
      if (s1 != null) {
         s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
      }

      return s;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int layer) {
      EntityEggStuff entityegginfo = (EntityEggStuff)spawnList.get(stack.getItemDamage());
      return entityegginfo != null ? (layer == 0 ? entityegginfo.color1 : entityegginfo.color2) : 16777215;
   }

   public boolean onItemUse(ItemStack stack, Player player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
       if (Platform.getEnvironment() != Env.CLIENT) {
           Block block = world.getBlock(x, y, z);
           x += Facing.offsetsXForSide[side];
           y += Facing.offsetsYForSide[side];
           z += Facing.offsetsZForSide[side];
           double d0 = 0.0F;
           if (side == 1 && block.getRenderType() == 11) {
               d0 = 0.5F;
           }

           Entity entity = spawnCreature(world, stack.getItemDamage(), (double) x + (double) 0.5F, (double) y + d0, (double) z + (double) 0.5F);
           if (entity != null) {
               if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                   ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
               }

               if (!player.capabilities.isCreativeMode) {
                   --stack.stackSize;
               }
           }

       }
       return true;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, Player player) {
       if (Platform.getEnvironment() != Env.CLIENT) {
           HitResult HitResult = this.getHitResultFromPlayer(world, player, true);
           if (HitResult != null) {
               if (HitResult.typeOfHit == MovingObjectType.BLOCK) {
                   int i = HitResult.blockX;
                   int j = HitResult.blockY;
                   int k = HitResult.blockZ;
                   if (!world.canMineBlock(player, i, j, k)) {
                       return stack;
                   }

                   if (!player.canPlayerEdit(i, j, k, HitResult.sideHit, stack)) {
                       return stack;
                   }

                   if (world.getBlock(i, j, k) instanceof BlockLiquid) {
                       Entity entity = spawnCreature(world, stack.getItemDamage(), i, j, k);
                       if (entity != null) {
                           if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                               ((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());
                           }

                           if (!player.capabilities.isCreativeMode) {
                               --stack.stackSize;
                           }
                       }
                   }
               }

           }
       }
       return stack;
   }

   public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6) {
      if (spawnList.get(par1) == null) {
         return null;
      } else {
         Entity entity = null;

         for(int j = 0; j < 1; ++j) {
            entity = EntityList.createEntityByName(((EntityEggStuff)spawnList.get(par1)).name, par0World);
            if (entity instanceof EntityLivingBase) {
               EntityLiving entityliving = (EntityLiving)entity;
               entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0world.getRandom().nextFloat() * 360.0F), 0.0F);
               entityliving.rotationYawHead = entityliving.rotationYaw;
               entityliving.renderYawOffset = entityliving.rotationYaw;
               entityliving.onSpawnWithEgg(null);
               par0World.spawnEntityInWorld(entity);
               entityliving.playLivingSound();
            }
         }

         return entity;
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(par1, par2);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
      for(int a = 0; a < spawnList.size(); ++a) {
         p_150895_3_.add(new ItemStack(p_150895_1_, 1, a));
      }

   }

   protected String getIconString() {
      return "spawn_egg";
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      this.theIcon = par1IconRegister.registerIcon(this.getIconString() + "_overlay");
   }

   static class EntityEggStuff {
      String name;
      int color1;
      int color2;

      public EntityEggStuff(String name, int color1, int color2) {
         this.name = name;
         this.color1 = color1;
         this.color2 = color2;
      }
   }
}
