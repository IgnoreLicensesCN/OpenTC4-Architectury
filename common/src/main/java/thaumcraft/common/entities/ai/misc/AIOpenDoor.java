package thaumcraft.common.entities.ai.misc;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIOpenDoor extends AIDoorInteract {
   boolean field_75361_i;
   int field_75360_j;

   public AIOpenDoor(EntityGolemBase par1EntityLiving, boolean par2) {
      super(par1EntityLiving);
      this.theEntity = par1EntityLiving;
      this.field_75361_i = par2;
   }

   public boolean continueExecuting() {
      return this.field_75361_i && this.field_75360_j > 0 && super.continueExecuting();
   }

   public void startExecuting() {
      this.field_75360_j = 20;
      if (this.targetDoor == Blocks.wooden_door) {
         ((BlockDoor)this.targetDoor).func_150014_a(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, true);
      } else {
         int var10 = this.theEntity.worldObj.getBlockMetadata(this.entityPosX, this.entityPosY, this.entityPosZ);
         if (!BlockFenceGate.isFenceGateOpen(var10)) {
            int var11 = (MathHelper.floor_double((double)(this.theEntity.rotationYaw * 4.0F / 360.0F) + (double)0.5F) & 3) % 4;
            int var12 = BlockFenceGate.getDirection(var10);
            if (var12 == (var11 + 2) % 4) {
               var10 = var11;
            }

            this.theEntity.worldObj.setBlock(this.entityPosX, this.entityPosY, this.entityPosZ, this.targetDoor, var10 | 4, 3);
            this.theEntity.worldObj.playAuxSFXAtEntity(null, 1003, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
         }
      }

   }

   public void resetTask() {
      if (this.field_75361_i) {
         if (this.targetDoor == Blocks.wooden_door) {
            ((BlockDoor)this.targetDoor).func_150014_a(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, false);
         } else {
            int var10 = this.theEntity.worldObj.getBlockMetadata(this.entityPosX, this.entityPosY, this.entityPosZ);
            if (BlockFenceGate.isFenceGateOpen(var10)) {
               this.theEntity.worldObj.setBlock(this.entityPosX, this.entityPosY, this.entityPosZ, this.targetDoor, var10 & -5, 3);
               this.theEntity.worldObj.playAuxSFXAtEntity(null, 1003, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
            }
         }
      }

   }

   public void updateTask() {
      --this.field_75360_j;
      super.updateTask();
   }
}
