package thaumcraft.common.entities.ai.inventory;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.core.Direction;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;

public class AIHomeTakeSorting extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int countChest = 0;
   private IInventory inv;
   int count = 0;

   public AIHomeTakeSorting(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      if (this.theGolem.getCarried() == null && this.theGolem.ticksExisted % Config.golemDelay <= 0 && this.theGolem.getNavigator().noPath() && !(this.theGolem.getDistanceSq((float)home.posX + 0.5F, (float)home.posY + 0.5F, (float)home.posZ + 0.5F) > (double)5.0F)) {
         Direction facing = Direction.getOrientation(this.theGolem.homeFacing);
         int cX = home.posX - facing.offsetX;
         int cY = home.posY - facing.offsetY;
         int cZ = home.posZ - facing.offsetZ;
         TileEntity tile = this.theGolem.level().getTileEntity(cX, cY, cZ);
         boolean repeat = true;
         boolean didRepeat = false;

         while(repeat) {
            if (didRepeat) {
               repeat = false;
            }

            if (tile instanceof IInventory) {
               ArrayList<ItemStack> neededList = GolemHelper.getItemsNeeded(this.theGolem, this.theGolem.getUpgradeAmount(5) > 0);
               if (neededList != null && !neededList.isEmpty()) {
                  for(ItemStack stack : neededList) {
                     ItemStack needed = stack.copy();
                     needed.stackSize = this.theGolem.getCarrySpace();
                     if (InventoryUtils.extractStack((IInventory)tile, needed, facing.ordinal(), this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT(), false) != null) {
                        return true;
                     }
                  }
               }
            }

            if (!didRepeat && InventoryUtils.getDoubleChest(tile) != null) {
               tile = InventoryUtils.getDoubleChest(tile);
               didRepeat = true;
            } else {
               repeat = false;
            }
         }

         return false;
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
      Direction facing = Direction.getOrientation(this.theGolem.homeFacing);
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity tile = this.theGolem.level().getTileEntity(cX, cY, cZ);
      boolean repeat = true;
      boolean didRepeat = false;

      while(repeat) {
         if (didRepeat) {
            repeat = false;
         }

         if (tile instanceof IInventory) {
            ArrayList<ItemStack> neededList = GolemHelper.getItemsNeeded(this.theGolem, this.theGolem.getUpgradeAmount(5) > 0);
            if (neededList != null && !neededList.isEmpty()) {
               for(ItemStack stack : neededList) {
                  ItemStack needed = stack.copy();
                  needed.stackSize = this.theGolem.getCarrySpace();
                  ItemStack result = InventoryUtils.extractStack((IInventory)tile, needed, facing.ordinal(), this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT(), true);
                  if (result != null) {
                     this.theGolem.setCarried(result);

                     try {
                        if (Config.golemChestInteract) {
                           ((IInventory)tile).openInventory();
                        }
                     } catch (Exception ignored) {
                     }

                     this.countChest = 5;
                     this.inv = (IInventory)tile;
                     break;
                  }
               }
            }

            if (this.theGolem.getCarried() != null) {
               break;
            }
         }

         if (!didRepeat && InventoryUtils.getDoubleChest(tile) != null) {
            tile = InventoryUtils.getDoubleChest(tile);
            didRepeat = true;
         } else {
            repeat = false;
         }
      }

   }
}
