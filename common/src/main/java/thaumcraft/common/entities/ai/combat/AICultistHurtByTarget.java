package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityCultistCleric;

import java.util.List;

public class AICultistHurtByTarget extends EntityAITarget {
   boolean entityCallsForHelp;
   private int field_142052_b;
   private static final String __OBFID = "CL_00001619";

   public AICultistHurtByTarget(EntityCreature p_i1660_1_, boolean p_i1660_2_) {
      super(p_i1660_1_, false);
      this.entityCallsForHelp = p_i1660_2_;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      int i = this.taskOwner.func_142015_aE();
      return i != this.field_142052_b && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
      this.field_142052_b = this.taskOwner.func_142015_aE();
      if (this.entityCallsForHelp) {
         double d0 = this.getTargetDistance();

         for(EntityCreature entitycreature : (List<EntityCreature>)this.taskOwner.worldObj.getEntitiesWithinAABB(EntityCultist.class, AxisAlignedBB.getBoundingBox(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + (double)1.0F, this.taskOwner.posY + (double)1.0F, this.taskOwner.posZ + (double)1.0F).expand(d0, 10.0F, d0))) {
            if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAITarget())) {
               if (entitycreature instanceof EntityCultistCleric && ((EntityCultistCleric)entitycreature).getIsRitualist()) {
                  if (this.taskOwner.worldObj.rand.nextInt(3) == 0) {
                     ((EntityCultistCleric)entitycreature).setIsRitualist(false);
                     entitycreature.setAttackTarget(this.taskOwner.getAITarget());
                  }
               } else {
                  entitycreature.setAttackTarget(this.taskOwner.getAITarget());
               }
            }
         }
      }

      super.startExecuting();
   }
}
