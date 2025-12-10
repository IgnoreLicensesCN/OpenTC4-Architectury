package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.init.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class ItemBucketPure extends Item {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemBucketPure() {
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(false);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:bucket_pure");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, Player p_77659_3_) {
      boolean flag = true;
      MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(p_77659_2_, p_77659_3_, flag);
       if (movingobjectposition != null) {
           if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
               int i = movingobjectposition.blockX;
               int j = movingobjectposition.blockY;
               int k = movingobjectposition.blockZ;
               if (movingobjectposition.sideHit == 0) {
                   --j;
               }

               if (movingobjectposition.sideHit == 1) {
                   ++j;
               }

               if (movingobjectposition.sideHit == 2) {
                   --k;
               }

               if (movingobjectposition.sideHit == 3) {
                   ++k;
               }

               if (movingobjectposition.sideHit == 4) {
                   --i;
               }

               if (movingobjectposition.sideHit == 5) {
                   ++i;
               }

               if (!p_77659_3_.canPlayerEdit(i, j, k, movingobjectposition.sideHit, p_77659_1_)) {
                   return p_77659_1_;
               }

               if (this.tryPlaceContainedLiquid(p_77659_2_, i, j, k) && !p_77659_3_.capabilities.isCreativeMode) {
                   return new ItemStack(Items.bucket);
               }
           }

       }
       return p_77659_1_;
   }

   public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
      Material material = world.getBlock(x, y, z).getMaterial();
      boolean flag = !material.isSolid();
      if (!world.isAirBlock(x, y, z) && !flag) {
         return false;
      } else {
         if (!world.isRemote && flag && !material.isLiquid()) {
            world.func_147480_a(x, y, z, true);
         }

         world.setBlock(x, y, z, ConfigBlocks.blockFluidPure, 0, 3);
         return true;
      }
   }
}
