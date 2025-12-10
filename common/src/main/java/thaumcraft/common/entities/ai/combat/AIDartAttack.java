package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIDartAttack extends EntityAIBase {
   private final EntityGolemBase theGolem;
   private EntityLivingBase attackTarget;
   private int rangedAttackTime = 0;
   private int maxRangedAttackTime;

   public AIDartAttack(EntityGolemBase par1IRangedAttackMob) {
      this.theGolem = par1IRangedAttackMob;
      this.maxRangedAttackTime = 30 - this.theGolem.getUpgradeAmount(0) * 8;
      this.rangedAttackTime = this.maxRangedAttackTime / 2;
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
         double ra = this.theGolem.getDistanceSq(var1.posX, var1.boundingBox.minY, var1.posZ);
         if (ra < (double)9.0F) {
            return false;
         } else {
            this.attackTarget = var1;
            return true;
         }
      }
   }

   public boolean continueExecuting() {
      return this.shouldExecute() && !this.theGolem.getNavigator().noPath();
   }

   public void resetTask() {
      this.attackTarget = null;
      this.rangedAttackTime = this.maxRangedAttackTime / 2;
   }

   public void updateTask() {
      double var1 = this.theGolem.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
      boolean var3 = this.theGolem.getEntitySenses().canSee(this.attackTarget);
      this.theGolem.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.theGolem.getAIMoveSpeed());
      if (var3) {
         this.theGolem.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
         this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
         if (this.rangedAttackTime == 0) {
            float r = this.theGolem.getRange() * 0.8F;
            r *= r;
            if (var1 <= (double)r) {
               this.theGolem.attackEntityWithRangedAttack(this.attackTarget);
               this.rangedAttackTime = this.maxRangedAttackTime;
            }
         }
      }

   }
}
