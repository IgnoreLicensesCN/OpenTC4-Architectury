package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class AIAttackOnCollide extends EntityAIBase {
   World worldObj;
   EntityCreature attacker;
   int attackTick;
   double speedTowardsTarget;
   boolean longMemory;
   PathEntity entityPathEntity;
   Class<?> classTarget;
   private int delayCounter;
   private double targetX;
   private double targetY;
   private double targetZ;
   private static final String __OBFID = "CL_00001595";
   private int failedPathFindingPenalty;

   public AIAttackOnCollide(EntityCreature attacker, Class<?> classTarget, double speedTowardsTarget, boolean longMemory) {
      this(attacker, speedTowardsTarget, longMemory);
      this.classTarget = classTarget;
   }

   public AIAttackOnCollide(EntityCreature attacker, double speedTowardsTarget, boolean longMemory) {
      this.attacker = attacker;
      this.worldObj = attacker.worldObj;
      this.speedTowardsTarget = speedTowardsTarget;
      this.longMemory = longMemory;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
      if (entitylivingbase == null) {
         return false;
      } else if (!entitylivingbase.isEntityAlive()) {
         return false;
      } else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass())) {
         return false;
      } else if (--this.delayCounter <= 0) {
         this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
         this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
         return this.entityPathEntity != null;
      } else {
         return true;
      }
   }

   public boolean continueExecuting() {
      EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
      return entitylivingbase != null
              && (entitylivingbase.isEntityAlive()
              && (
                      !this.longMemory
                              ? !this.attacker.getNavigator().noPath()
                              : this.attacker.isWithinHomeDistance(
                                      MathHelper.floor_double(entitylivingbase.posX),
                              MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ))
      ));
   }

   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
      this.delayCounter = 0;
   }

   public void resetTask() {
      this.attacker.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
      this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
      double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ);
      double d1 = this.attacker.width * 2.0F * this.attacker.width * 2.0F + entitylivingbase.width;
      --this.delayCounter;
      if (this.attackTick > 0) {
         --this.attackTick;
      }

      if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && (this.targetX == (double)0.0F && this.targetY == (double)0.0F && this.targetZ == (double)0.0F || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= (double)1.0F || this.attacker.getRNG().nextFloat() < 0.05F)) {
         this.targetX = entitylivingbase.posX;
         this.targetY = entitylivingbase.boundingBox.minY;
         this.targetZ = entitylivingbase.posZ;
         this.delayCounter = this.failedPathFindingPenalty + 4 + this.attacker.getRNG().nextInt(7);
         if (this.attacker.getNavigator().getPath() != null) {
            PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
            if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < (double)1.0F) {
               this.failedPathFindingPenalty = 0;
            } else {
               this.failedPathFindingPenalty += 10;
            }
         } else {
            this.failedPathFindingPenalty += 10;
         }

         if (d0 > (double)1024.0F) {
            this.delayCounter += 10;
         } else if (d0 > (double)256.0F) {
            this.delayCounter += 5;
         }

         if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
            this.delayCounter += 15;
         }
      }

      if (d0 <= d1 && this.attackTick <= 0) {
         this.attackTick = 10;
         if (this.attacker.getHeldItem() != null) {
            this.attacker.swingItem();
         }

         this.attacker.attackEntityAsMob(entitylivingbase);
      }

   }
}
