package thaumcraft.common.entities.ai.misc;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import thaumcraft.common.entities.golems.EntityGolemBase;

public abstract class AIDoorInteract extends EntityAIBase {
   protected EntityGolemBase theEntity;
   protected int entityPosX;
   protected int entityPosY;
   protected int entityPosZ;
   protected Block targetDoor;
   boolean hasStoppedDoorInteraction;
   float entityPositionX;
   float entityPositionZ;
   int count = 0;

   public AIDoorInteract(EntityGolemBase par1EntityLiving) {
      this.theEntity = par1EntityLiving;
   }

   public boolean shouldExecute() {
      if (!this.theEntity.isCollidedHorizontally) {
         return false;
      } else {
         PathNavigate var1 = this.theEntity.getNavigator();
         PathEntity var2 = var1.getPath();
         if (var2 != null && !var2.isFinished() && var1.getCanBreakDoors()) {
            for(int var3 = 0; var3 < Math.min(var2.getCurrentPathIndex() + 2, var2.getCurrentPathLength()); ++var3) {
               PathPoint var4 = var2.getPathPointFromIndex(var3);
               this.entityPosX = var4.xCoord;
               this.entityPosY = var4.yCoord;
               this.entityPosZ = var4.zCoord;
               if (this.theEntity.getDistanceSq(this.entityPosX, this.theEntity.posY, this.entityPosZ) <= (double)2.25F) {
                  this.targetDoor = this.findUsableDoor(this.entityPosX, this.entityPosY, this.entityPosZ);
                  if (this.targetDoor != null && this.targetDoor != Blocks.air) {
                     this.count = 200;
                     return true;
                  }
               }
            }

            this.entityPosX = MathHelper.floor_double(this.theEntity.posX);
            this.entityPosY = MathHelper.floor_double(this.theEntity.posY);
            this.entityPosZ = MathHelper.floor_double(this.theEntity.posZ);
            this.targetDoor = this.findUsableDoor(this.entityPosX, this.entityPosY, this.entityPosZ);
            if (this.targetDoor != null && this.targetDoor != Blocks.air) {
               this.count = 200;
            }

            return this.targetDoor != null && this.targetDoor != Blocks.air;
         } else {
            return false;
         }
      }
   }

   public boolean continueExecuting() {
      return this.count > 0 && !this.hasStoppedDoorInteraction;
   }

   public void startExecuting() {
      this.count = 100;
      this.hasStoppedDoorInteraction = false;
      this.entityPositionX = (float)((double)((float)this.entityPosX + 0.5F) - this.theEntity.posX);
      this.entityPositionZ = (float)((double)((float)this.entityPosZ + 0.5F) - this.theEntity.posZ);
   }

   public void updateTask() {
      --this.count;
      float var1 = (float)((double)((float)this.entityPosX + 0.5F) - this.theEntity.posX);
      float var2 = (float)((double)((float)this.entityPosZ + 0.5F) - this.theEntity.posZ);
      float var3 = this.entityPositionX * var1 + this.entityPositionZ * var2;
      if (var3 < 0.0F) {
         this.hasStoppedDoorInteraction = true;
      }

   }

   private Block findUsableDoor(int par1, int par2, int par3) {
      Block var4 = this.theEntity.worldObj.getBlock(par1, par2, par3);
      return var4 != Blocks.wooden_door && var4 != Blocks.fence_gate ? Block.getBlockById(0) : var4;
   }
}
