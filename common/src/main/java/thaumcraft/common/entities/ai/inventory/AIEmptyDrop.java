package thaumcraft.common.entities.ai.inventory;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ChunkCoordinates;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;

public class AIEmptyDrop extends EntityAIBase {
   private EntityGolemBase theGolem;
   int count = 0;

   public AIEmptyDrop(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.itemCarried != null && this.theGolem.getNavigator().noPath()) {
         ChunkCoordinates home = this.theGolem.getHomePosition();

         for(byte color : this.theGolem.getColorsMatching(this.theGolem.itemCarried)) {
            for(ChunkCoordinates cc : GolemHelper.getMarkedBlocksAdjacentToGolem(this.theGolem.worldObj, this.theGolem, color)) {
               if (cc != home) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.count > 0 && this.shouldExecute();
   }

   public void resetTask() {
       super.resetTask();
   }

   public void updateTask() {
      --this.count;
      super.updateTask();
   }

   public void startExecuting() {
      this.count = 200;
      ChunkCoordinates home = this.theGolem.getHomePosition();

      label24:
      for(byte color : this.theGolem.getColorsMatching(this.theGolem.itemCarried)) {
         for(ChunkCoordinates cc : GolemHelper.getMarkedBlocksAdjacentToGolem(this.theGolem.worldObj, this.theGolem, color)) {
            if (cc != home) {
               EntityItem item = new EntityItem(this.theGolem.worldObj, this.theGolem.posX, this.theGolem.posY + (double)(this.theGolem.height / 2.0F), this.theGolem.posZ, this.theGolem.itemCarried.copy());
               if (item != null) {
                  double distance = this.theGolem.getDistance((double)cc.posX + (double)0.5F, (double)cc.posY + (double)0.5F, (double)cc.posZ + (double)0.5F);
                  item.motionX = ((double)cc.posX + (double)0.5F - this.theGolem.posX) * (distance / (double)3.0F);
                  item.motionY = 0.1 + ((double)cc.posY + (double)0.5F - (this.theGolem.posY + (double)(this.theGolem.height / 2.0F))) * (distance / (double)3.0F);
                  item.motionZ = ((double)cc.posZ + (double)0.5F - this.theGolem.posZ) * (distance / (double)3.0F);
                  item.delayBeforeCanPickup = 10;
                  this.theGolem.worldObj.spawnEntityInWorld(item);
                  this.theGolem.itemCarried = null;
                  this.theGolem.startActionTimer();
                  break label24;
               }
            }
         }
      }

      this.theGolem.updateCarried();
   }
}
