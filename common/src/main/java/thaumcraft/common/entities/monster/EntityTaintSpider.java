package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.config.ConfigItems;

public class EntityTaintSpider extends EntitySpider implements ITaintedMob {
   public EntityTaintSpider(Level par1World) {
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
      return this.level().getClosestVulnerablePlayerToEntity(this, d0);
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
      if (this.level().rand.nextInt(6) == 0) {
         if (this.level().rand.nextBoolean()) {
            this.entityDropItem(new ItemStack(ThaumcraftItems.TAINTED_GOO,1), this.height / 2.0F);
         } else {
            this.entityDropItem(new ItemStack(ThaumcraftItems.TAINT_TENDRIL,1), this.height / 2.0F);
         }
      }

   }
}
