package thaumcraft.common.entities.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;

public class EntityBrainyZombie extends EntityZombie {
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0F);
      this.getEntityAttribute(field_110186_bp).setBaseValue(0.0F);
   }

   public EntityBrainyZombie(World world) {
      super(world);
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      return super.attackEntityFrom(par1DamageSource, par2);
   }

   public void onKillEntity(EntityLivingBase par1EntityLivingBase) {
      super.onKillEntity(par1EntityLivingBase);
   }

   public int getTotalArmorValue() {
      int var1 = super.getTotalArmorValue() + 3;
      if (var1 > 20) {
         var1 = 20;
      }

      return var1;
   }

   protected void dropFewItems(boolean flag, int i) {
      for(int a = 0; a < 3; ++a) {
         if (this.worldObj.rand.nextBoolean()) {
            this.dropItem(Items.rotten_flesh, 1);
         }
      }

      if (this.worldObj.rand.nextInt(10) - i <= 4) {
         this.entityDropItem(new ItemStack(ConfigItems.itemZombieBrain), 1.5F);
      }

   }
}
