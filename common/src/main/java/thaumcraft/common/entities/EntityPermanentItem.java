package thaumcraft.common.entities;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityPermanentItem extends EntitySpecialItem {
   public EntityPermanentItem(World par1World) {
      super(par1World);
   }

   public EntityPermanentItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
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

   public void onUpdate() {
      super.onUpdate();
      if (this.age + 5 >= this.lifespan) {
         this.age = 0;
      }

   }
}
