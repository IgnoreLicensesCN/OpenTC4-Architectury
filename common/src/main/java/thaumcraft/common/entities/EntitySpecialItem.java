package thaumcraft.common.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySpecialItem extends EntityItem {
   public EntitySpecialItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
      super(par1World);
      this.setSize(0.25F, 0.25F);
      this.yOffset = this.height / 2.0F;
      this.setPosition(par2, par4, par6);
      this.setEntityItemStack(par8ItemStack);
      this.rotationYaw = (float)(Math.random() * (double)360.0F);
      this.motionX = (float)(Math.random() * (double)0.2F - (double)0.1F);
      this.motionY = 0.2F;
      this.motionZ = (float)(Math.random() * (double)0.2F - (double)0.1F);
   }

   public EntitySpecialItem(World par1World) {
      super(par1World);
      this.setSize(0.25F, 0.25F);
      this.yOffset = this.height / 2.0F;
   }

   public void onUpdate() {
      if (this.motionY > (double)0.0F) {
         this.motionY *= 0.9F;
      }

      this.motionY += 0.04F;
      super.onUpdate();
   }

   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
      return !p_70097_1_.isExplosion() && super.attackEntityFrom(p_70097_1_, p_70097_2_);
   }
}
