package thaumcraft.common.entities.ai.inventory;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIHomeDrop extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int countChest = 0;
   private IInventory inv;
   int count = 0;

   public AIHomeDrop(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      if (this.theGolem.getCarried() != null && this.theGolem.getNavigator().noPath() && !(this.theGolem.getDistanceSq((float)home.posX + 0.5F, (float)home.posY + 0.5F, (float)home.posZ + 0.5F) > (double)5.0F)) {
         ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
         int cX = home.posX - facing.offsetX;
         int cY = home.posY - facing.offsetY;
         int cZ = home.posZ - facing.offsetZ;
         TileEntity tile = this.theGolem.worldObj.getTileEntity(cX, cY, cZ);
         return !(tile instanceof IInventory);
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.count > 0 && (this.shouldExecute() || this.countChest > 0);
   }

   public void resetTask() {
      try {
         if (this.inv != null && Config.golemChestInteract) {
            this.inv.closeInventory();
         }
      } catch (Exception ignored) {
      }

   }

   public void updateTask() {
      --this.countChest;
      --this.count;
      super.updateTask();
   }

   public void startExecuting() {
      this.count = 200;
      ChunkCoordinates home = this.theGolem.getHomePosition();
      ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      EntityItem item = new EntityItem(this.theGolem.worldObj, this.theGolem.posX, this.theGolem.posY + (double)(this.theGolem.height / 2.0F), this.theGolem.posZ, this.theGolem.itemCarried.copy());
      if (item != null) {
         double distance = this.theGolem.getDistance((double)cX + (double)0.5F, (double)cY + (double)0.5F, (double)cZ + (double)0.5F);
         item.motionX = ((double)cX + (double)0.5F - this.theGolem.posX) * (distance / (double)3.0F);
         item.motionY = 0.1 + ((double)cY + (double)0.5F - (this.theGolem.posY + (double)(this.theGolem.height / 2.0F))) * (distance / (double)3.0F);
         item.motionZ = ((double)cZ + (double)0.5F - this.theGolem.posZ) * (distance / (double)3.0F);
         item.delayBeforeCanPickup = 10;
         this.theGolem.worldObj.spawnEntityInWorld(item);
         this.theGolem.itemCarried = null;
         this.theGolem.startActionTimer();
         this.theGolem.updateCarried();
      }

   }
}
