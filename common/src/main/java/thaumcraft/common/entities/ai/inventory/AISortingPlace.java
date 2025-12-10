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

public class AISortingPlace extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int countChest = 0;
   private IInventory inv;
   private int xx;
   private int yy;
   private int zz;
   int count = 0;

   public AISortingPlace(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.theGolem.itemCarried != null && this.theGolem.getNavigator().noPath()) {
         ChunkCoordinates home = this.theGolem.getHomePosition();
         ForgeDirection facing = ForgeDirection.getOrientation(this.theGolem.homeFacing);
         int cX = home.posX - facing.offsetX;
         int cY = home.posY - facing.offsetY;
         int cZ = home.posZ - facing.offsetZ;

         for(IInventory te : GolemHelper.getMarkedContainersAdjacentToGolem(this.theGolem.worldObj, this.theGolem)) {
            TileEntity tile = (TileEntity)te;
            if (tile != null && (tile.xCoord != cX || tile.yCoord != cY || tile.zCoord != cZ)) {
               for(Integer side : GolemHelper.getMarkedSides(this.theGolem, tile, (byte)-1)) {
                  ItemStack is = InventoryUtils.placeItemStackIntoInventory(this.theGolem.itemCarried, te, side, false);
                  if (!ItemStack.areItemStacksEqual(is, this.theGolem.itemCarried) && InventoryUtils.inventoryContains(te, this.theGolem.itemCarried, side, this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT())) {
                     this.xx = tile.xCoord;
                     this.yy = tile.yCoord;
                     this.zz = tile.zCoord;
                     return true;
                  }
               }

               if (InventoryUtils.getDoubleChest(tile) != null) {
                  for(Integer side : GolemHelper.getMarkedSides(this.theGolem, tile, (byte)-1)) {
                     ItemStack is = InventoryUtils.placeItemStackIntoInventory(this.theGolem.itemCarried, InventoryUtils.getDoubleChest(tile), side, false);
                     if (!ItemStack.areItemStacksEqual(is, this.theGolem.itemCarried) && InventoryUtils.inventoryContains(te, this.theGolem.itemCarried, side, this.theGolem.checkOreDict(), this.theGolem.ignoreDamage(), this.theGolem.ignoreNBT())) {
                        this.xx = tile.xCoord;
                        this.yy = tile.yCoord;
                        this.zz = tile.zCoord;
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
      TileEntity tile = this.theGolem.worldObj.getTileEntity(this.xx, this.yy, this.zz);
      if (tile != null && (tile.xCoord != cX || tile.yCoord != cY || tile.zCoord != cZ)) {
         IInventory te = (IInventory)tile;

         for(byte color : this.theGolem.getColorsMatching(this.theGolem.itemCarried)) {
            for(Integer side : GolemHelper.getMarkedSides(this.theGolem, tile, color)) {
               this.theGolem.itemCarried = InventoryUtils.placeItemStackIntoInventory(this.theGolem.itemCarried, te, side, true);
               this.countChest = 5;
               this.inv = (IInventory)tile;
               if (this.theGolem.itemCarried == null) {
                  break;
               }
            }

            if (InventoryUtils.getDoubleChest(tile) != null && this.theGolem.itemCarried != null) {
               for(Integer side : GolemHelper.getMarkedSides(this.theGolem, tile, color)) {
                  ItemStack is = InventoryUtils.placeItemStackIntoInventory(this.theGolem.itemCarried, InventoryUtils.getDoubleChest(tile), side, false);
                  if (!ItemStack.areItemStacksEqual(is, this.theGolem.itemCarried)) {
                     this.theGolem.itemCarried = InventoryUtils.placeItemStackIntoInventory(this.theGolem.itemCarried, InventoryUtils.getDoubleChest(tile), side, true);
                     this.countChest = 5;
                     this.inv = InventoryUtils.getDoubleChest(tile);
                     if (this.theGolem.itemCarried == null) {
                        break;
                     }
                  }
               }
            }

            if (this.countChest == 5) {
               try {
                  if (Config.golemChestInteract) {
                     ((IInventory)tile).openInventory();
                  }
               } catch (Exception ignored) {
               }
               break;
            }
         }
      }

      this.theGolem.updateCarried();
   }
}
