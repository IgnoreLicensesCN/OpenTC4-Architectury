package thaumcraft.common.entities.ai.misc;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIReturnHome extends EntityAIBase {
   private EntityGolemBase theGolem;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private int pathingDelay = 0;
   private int pathingDelayInc = 5;
   int count = 0;
   int prevX = 0;
   int prevY = 0;
   int prevZ = 0;

   public AIReturnHome(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      if (this.pathingDelay > 0) {
         --this.pathingDelay;
      }

      if (this.pathingDelay <= 0 && !(this.theGolem.getDistanceSq((float)home.posX + 0.5F, (float)home.posY + 0.5F, (float)home.posZ + 0.5F) < (double)3.0F)) {
         this.movePosX = (double)home.posX + (double)0.5F;
         this.movePosY = (double)home.posY + (double)0.5F;
         this.movePosZ = (double)home.posZ + (double)0.5F;
         return true;
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      return this.pathingDelay <= 0 && this.count > 0 && !this.theGolem.getNavigator().noPath() && this.theGolem.getDistanceSq((float)home.posX + 0.5F, (float)home.posY + 0.5F, (float)home.posZ + 0.5F) >= (double)3.0F;
   }

   public void resetTask() {
       super.resetTask();
   }

   public void updateTask() {
      --this.count;
      if (this.count == 0 && this.prevX == MathHelper.floor_double(this.theGolem.posX) && this.prevY == MathHelper.floor_double(this.theGolem.posY) && this.prevZ == MathHelper.floor_double(this.theGolem.posZ)) {
         Vec3 var2 = RandomPositionGenerator.findRandomTarget(this.theGolem, 2, 1);
         if (var2 != null) {
            this.count = 20;
            boolean path = this.theGolem.getNavigator().tryMoveToXYZ(var2.xCoord + (double)0.5F, var2.yCoord + (double)0.5F, var2.zCoord + (double)0.5F, this.theGolem.getAIMoveSpeed());
            if (!path) {
               this.pathingDelay = this.pathingDelayInc;
               if (this.pathingDelayInc < 50) {
                  this.pathingDelayInc += 5;
               }
            } else {
               this.pathingDelayInc = 5;
            }
         }
      }

      super.updateTask();
   }

   public void startExecuting() {
      this.count = 20;
      this.prevX = MathHelper.floor_double(this.theGolem.posX);
      this.prevY = MathHelper.floor_double(this.theGolem.posY);
      this.prevZ = MathHelper.floor_double(this.theGolem.posZ);
      boolean path = this.theGolem.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.theGolem.getAIMoveSpeed());
      if (!path) {
         this.pathingDelay = this.pathingDelayInc;
         if (this.pathingDelayInc < 50) {
            this.pathingDelayInc += 5;
         }
      } else {
         this.pathingDelayInc = 5;
      }

   }
}
