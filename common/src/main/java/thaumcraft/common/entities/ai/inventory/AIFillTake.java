package thaumcraft.common.entities.ai.inventory;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

public class AIFillTake extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int countChest = 0;
   private IInventory inv;
   int count = 0;

   public AIFillTake(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.getCarried() == null && this.theGolem.itemWatched != null && this.theGolem.getNavigator().noPath() && this.theGolem.hasSomething()) {
         ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
         ChunkCoordinates home = this.theGolem.getHomePosition();
         int cX = home.posX - facing.offsetX;
         int cY = home.posY - facing.offsetY;
         int cZ = home.posZ - facing.offsetZ;

         for(IInventory te : GolemHelper.getMarkedContainersAdjacentToGolem(this.theGolem.worldObj, this.theGolem)) {
            TileEntity tile = (TileEntity)te;
            if (tile != null && (tile.xCoord != cX || tile.yCoord != cY || tile.zCoord != cZ)) {
               for(byte color : this.theGolem.getColorsMatching(this.theGolem.itemWatched)) {
                  for(Integer side : GolemHelper.getMarkedSides(this.theGolem, tile, color)) {
                     ItemStack target = this.theGolem.itemWatched.copy();
                     target.stackSize = this.theGolem.getToggles()[0] ? this.theGolem.getCarrySpace() : Math.min(target.stackSize, this.theGolem.getCarrySpace());
                     ItemStack result = InventoryUtils.extractStack(te, target, side, this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT(), true);
                     if (result == null && InventoryUtils.getDoubleChest(tile) != null) {
                        result = InventoryUtils.extractStack(InventoryUtils.getDoubleChest(tile), target, side, this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT(), true);
                     }

                     if (result != null) {
                        this.theGolem.setCarried(result);

                        try {
                           if (Config.golemChestInteract) {
                              this.inv.openInventory();
                           }
                        } catch (Exception ignored) {
                        }

                        this.countChest = 5;
                        this.count = 200;
                        this.theGolem.itemWatched = null;
                        this.theGolem.updateCarried();
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return this.count > 0 && (!this.theGolem.getNavigator().noPath() || this.countChest > 0);
   }

   public void updateTask() {
      --this.count;
      --this.countChest;
      super.updateTask();
   }

   public void resetTask() {
      try {
         if (this.inv != null && Config.golemChestInteract) {
            this.inv.closeInventory();
         }
      } catch (Exception ignored) {
      }

   }

   public void startExecuting() {
       super.startExecuting();
   }
}
