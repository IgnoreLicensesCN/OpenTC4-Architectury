package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import thaumcraft.common.entities.golems.EntityGolemBase;

public abstract class AITarget extends EntityAIBase {
   protected EntityCreature taskOwner;
   protected float targetDistance;
   protected boolean shouldCheckSight;
   private boolean nearbyOnly;
   private int targetSearchStatus;
   private int targetSearchDelay;
   private int field_75298_g;

   public AITarget(EntityCreature par1EntityLiving, float par2, boolean par3) {
      this(par1EntityLiving, par2, par3, false);
   }

   public AITarget(EntityCreature par1EntityLiving, float par2, boolean par3, boolean par4) {
      this.targetSearchStatus = 0;
      this.targetSearchDelay = 0;
      this.field_75298_g = 0;
      this.taskOwner = par1EntityLiving;
      this.targetDistance = par2;
      this.shouldCheckSight = par3;
      this.nearbyOnly = par4;
   }

   public boolean continueExecuting() {
      EntityLivingBase var1 = this.taskOwner.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else if (this.taskOwner.getDistanceSqToEntity(var1) > (double)(this.targetDistance * this.targetDistance)) {
         return false;
      } else {
         if (this.shouldCheckSight) {
            if (this.taskOwner.getEntitySenses().canSee(var1)) {
               this.field_75298_g = 0;
            } else return ++this.field_75298_g <= 60;
         }

         return true;
      }
   }

   public void startExecuting() {
      this.targetSearchStatus = 0;
      this.targetSearchDelay = 0;
      this.field_75298_g = 0;
   }

   public void resetTask() {
      this.taskOwner.setAttackTarget(null);
   }

   protected boolean isSuitableTarget(EntityLivingBase par1EntityLiving, boolean par2) {
      if (par1EntityLiving == null) {
         return false;
      } else if (par1EntityLiving == this.taskOwner) {
         return false;
      } else if (!par1EntityLiving.isEntityAlive()) {
         return false;
      } else if (!this.taskOwner.canAttackClass(par1EntityLiving.getClass())) {
         return false;
      } else {
         if (this.taskOwner instanceof EntityTameable && ((EntityTameable)this.taskOwner).isTamed()) {
            if (par1EntityLiving instanceof EntityTameable && ((EntityTameable)par1EntityLiving).isTamed()) {
               return false;
            }

            if (par1EntityLiving == ((EntityTameable)this.taskOwner).getOwner()) {
               return false;
            }
         } else {
            if (par1EntityLiving instanceof EntityPlayer && !par2 && ((EntityPlayer)par1EntityLiving).capabilities.disableDamage) {
               return false;
            }

            if (par1EntityLiving instanceof EntityPlayer && par1EntityLiving.getCommandSenderName().equals(((EntityGolemBase)this.taskOwner).getOwnerName())) {
               return false;
            }
         }

         if (!this.taskOwner.isWithinHomeDistance(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ))) {
            return false;
         } else if (this.shouldCheckSight && !this.taskOwner.getEntitySenses().canSee(par1EntityLiving)) {
            return false;
         } else {
            if (this.nearbyOnly) {
               if (--this.targetSearchDelay <= 0) {
                  this.targetSearchStatus = 0;
               }

               if (this.targetSearchStatus == 0) {
                  this.targetSearchStatus = this.canEasilyReach(par1EntityLiving) ? 1 : 2;
               }

                return this.targetSearchStatus != 2;
            }

            return true;
         }
      }
   }

   private boolean canEasilyReach(EntityLivingBase par1EntityLiving) {
      this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
      PathEntity var2 = this.taskOwner.getNavigator().getPathToEntityLiving(par1EntityLiving);
      if (var2 == null) {
         return false;
      } else {
         PathPoint var3 = var2.getFinalPathPoint();
         if (var3 == null) {
            return false;
         } else {
            int var4 = var3.xCoord - MathHelper.floor_double(par1EntityLiving.posX);
            int var5 = var3.zCoord - MathHelper.floor_double(par1EntityLiving.posZ);
            return (double)(var4 * var4 + var5 * var5) <= (double)2.25F;
         }
      }
   }
}
