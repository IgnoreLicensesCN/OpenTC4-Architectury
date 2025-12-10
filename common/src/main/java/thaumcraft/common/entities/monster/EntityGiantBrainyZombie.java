package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;

public class EntityGiantBrainyZombie extends EntityBrainyZombie {
   public EntityGiantBrainyZombie(World world) {
      super(world);
      this.experienceValue = 15;
      this.yOffset *= 1.2F + this.getAnger();
      this.setSize(this.width * (1.2F + this.getAnger()), this.height * (1.2F + this.getAnger()));
      this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.getAnger() > 1.0F) {
         this.setAnger(this.getAnger() - 0.002F);
         this.setSize(0.6F * (1.2F + this.getAnger()), 1.8F * (1.2F + this.getAnger()));
      }

      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0F + (this.getAnger() - 1.0F) * 5.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(20, 1.0F);
   }

   public float getAnger() {
      return this.dataWatcher.getWatchableObjectFloat(20);
   }

   public void setAnger(float par1) {
      this.dataWatcher.updateObject(20, par1);
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      this.setAnger(Math.min(2.0F, this.getAnger() + 0.1F));
      return super.attackEntityFrom(par1DamageSource, par2);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0F);
   }

   protected void dropFewItems(boolean flag, int i) {
      for(int a = 0; a < 6; ++a) {
         if (this.worldObj.rand.nextBoolean()) {
            this.dropItem(Items.rotten_flesh, 2);
         }
      }

      for(int a = 0; a < 6; ++a) {
         if (this.worldObj.rand.nextBoolean()) {
            this.dropItem(Items.rotten_flesh, 2);
         }
      }

      if (this.worldObj.rand.nextInt(10) - i <= 4) {
         this.entityDropItem(new ItemStack(ConfigItems.itemZombieBrain), 2.0F);
      }

   }

   protected void dropRareDrop(int par1) {
      switch (this.rand.nextInt(4)) {
         case 0:
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 2), 2.0F);
            break;
         case 1:
            this.dropItem(Items.carrot, 1);
            break;
         case 2:
            this.dropItem(Items.potato, 1);
            break;
         case 3:
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 6), 2.0F);
      }

   }
}
