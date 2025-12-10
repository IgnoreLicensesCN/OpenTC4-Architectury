package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class AIHurtByTarget extends AITarget {
   boolean entityCallsForHelp;
   EntityCreature entityPathNavigate;

   public AIHurtByTarget(EntityCreature par1EntityLiving, boolean par2) {
      super(par1EntityLiving, 16.0F, false);
      this.entityCallsForHelp = par2;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      return this.isSuitableTarget(this.taskOwner.getAITarget(), false);
   }

   public boolean continueExecuting() {
      return this.taskOwner.getAITarget() != null && this.taskOwner.getAITarget() != this.entityPathNavigate;
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
      if (this.entityCallsForHelp) {
         for(EntityLiving var3 : (List<EntityLiving>)
                 this.taskOwner
                         .worldObj
                         .getEntitiesWithinAABB(
                                 this.taskOwner.getClass(),
                                 AxisAlignedBB.getBoundingBox(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + (double)1.0F, this.taskOwner.posY + (double)1.0F, this.taskOwner.posZ + (double)1.0F)
                                         .expand(this.targetDistance, 4.0F, this.targetDistance))
         ) {
            if (this.taskOwner != var3 && var3.getAttackTarget() == null) {
               var3.setAttackTarget(this.taskOwner.getAITarget());
            }
         }
      }

      super.startExecuting();
   }

   public void resetTask() {
      if (this.taskOwner.getAttackTarget() != null && this.taskOwner.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)this.taskOwner.getAttackTarget()).capabilities.disableDamage) {
         this.taskOwner.setAttackTarget(null);
         super.resetTask();
      }

   }
}
