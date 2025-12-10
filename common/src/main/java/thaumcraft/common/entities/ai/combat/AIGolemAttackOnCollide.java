package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIGolemAttackOnCollide extends EntityAIBase {
   World worldObj;
   EntityGolemBase theGolem;
   EntityLivingBase entityTarget;
   int attackTick = 0;
   PathEntity entityPathEntity;
   private int counter;

   public AIGolemAttackOnCollide(EntityGolemBase par1EntityLiving) {
      this.theGolem = par1EntityLiving;
      this.worldObj = par1EntityLiving.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityLivingBase var1 = this.theGolem.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!this.theGolem.isValidTarget(var1)) {
         this.theGolem.setAttackTarget(null);
         return false;
      } else {
         this.entityTarget = var1;
         this.entityPathEntity = this.theGolem.getNavigator().getPathToEntityLiving(this.entityTarget);
         return this.entityPathEntity != null;
      }
   }

   public boolean continueExecuting() {
      return this.shouldExecute() && !this.theGolem.getNavigator().noPath();
   }

   public void startExecuting() {
      this.theGolem.getNavigator().setPath(this.entityPathEntity, this.theGolem.getAIMoveSpeed());
      this.counter = 0;
   }

   public void resetTask() {
      this.entityTarget = null;
      this.theGolem.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      this.theGolem.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
      if (this.theGolem.getEntitySenses().canSee(this.entityTarget) && --this.counter <= 0) {
         this.counter = 4 + this.theGolem.getRNG().nextInt(7);
         this.theGolem.getNavigator().tryMoveToEntityLiving(this.entityTarget, this.theGolem.getAIMoveSpeed());
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      double attackRange = (double)(this.entityTarget.width * 2.0F * this.entityTarget.width * 2.0F) + (double)1.0F;
      if (this.theGolem.getDistanceSq(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ) <= attackRange && this.attackTick <= 0) {
         this.attackTick = this.theGolem.getAttackSpeed();
         if (this.theGolem.getHeldItem() != null) {
            this.theGolem.swingItem();
         } else {
            this.theGolem.startActionTimer();
         }

         this.theGolem.attackEntityAsMob(this.entityTarget);
      }

   }
}
