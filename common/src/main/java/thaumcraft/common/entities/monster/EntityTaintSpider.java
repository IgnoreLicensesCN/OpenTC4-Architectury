package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.config.ConfigItems;

public class EntityTaintSpider extends EntitySpider implements ITaintedMob {
   public EntityTaintSpider(World par1World) {
      super(par1World);
      this.setSize(0.4F, 0.3F);
      this.experienceValue = 2;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0F);
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   protected Entity findPlayerToAttack() {
      double d0 = 12.0F;
      return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
   }

   @SideOnly(Side.CLIENT)
   public float spiderScaleAmount() {
      return 0.4F;
   }

   public float getShadowSize() {
      return 0.1F;
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextInt(6) == 0) {
         if (this.worldObj.rand.nextBoolean()) {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
         } else {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
         }
      }

   }
}
