package thaumcraft.common.items.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.init.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.HitResult.MovingObjectType;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class ItemBucketDeath extends Item {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemBucketDeath() {
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(false);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:bucket_death");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, Player p_77659_3_) {
      boolean flag = true;
      HitResult HitResult = this.getHitResultFromPlayer(p_77659_2_, p_77659_3_, flag);
       if (HitResult != null) {
           if (HitResult.typeOfHit == MovingObjectType.BLOCK) {
               int i = HitResult.blockX;
               int j = HitResult.blockY;
               int k = HitResult.blockZ;
               if (HitResult.sideHit == 0) {
                   --j;
               }

               if (HitResult.sideHit == 1) {
                   ++j;
               }

               if (HitResult.sideHit == 2) {
                   --k;
               }

               if (HitResult.sideHit == 3) {
                   ++k;
               }

               if (HitResult.sideHit == 4) {
                   --i;
               }

               if (HitResult.sideHit == 5) {
                   ++i;
               }

               if (!p_77659_3_.canPlayerEdit(i, j, k, HitResult.sideHit, p_77659_1_)) {
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
         if (Platform.getEnvironment() != Env.CLIENT && flag && !material.isLiquid()) {
            world.func_147480_a(x, y, z, true);
         }

         world.setBlock(x, y, z, ConfigBlocks.blockFluidDeath, 3, 3);
         return true;
      }
   }
}
