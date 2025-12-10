package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thaumcraft.common.entities.monster.EntityTaintCreeper;

public class AICreeperSwell extends EntityAIBase {
   EntityTaintCreeper swellingCreeper;
   EntityLivingBase creeperAttackTarget;

   public AICreeperSwell(EntityTaintCreeper par1EntityCreeper) {
      this.swellingCreeper = par1EntityCreeper;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
      return this.swellingCreeper.getCreeperState() > 0 || entitylivingbase != null && this.swellingCreeper.getDistanceSqToEntity(entitylivingbase) < (double)9.0F;
   }

   public void startExecuting() {
      this.swellingCreeper.getNavigator().clearPathEntity();
      this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
   }

   public void resetTask() {
      this.creeperAttackTarget = null;
   }

   public void updateTask() {
      if (this.creeperAttackTarget == null) {
         this.swellingCreeper.setCreeperState(-1);
      } else if (this.swellingCreeper.getDistanceSqToEntity(this.creeperAttackTarget) > (double)49.0F) {
         this.swellingCreeper.setCreeperState(-1);
      } else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget)) {
         this.swellingCreeper.setCreeperState(-1);
      } else {
         this.swellingCreeper.setCreeperState(1);
      }

   }
}
