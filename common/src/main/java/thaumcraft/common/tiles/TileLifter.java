package thaumcraft.common.tiles;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class TileLifter extends TileEntity {
   private int counter = 0;
   public int rangeAbove = 0;
   public boolean requiresUpdate = true;
   public boolean lastPowerState = false;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      ++this.counter;
      if (this.requiresUpdate || this.counter % 100 == 0) {
         this.lastPowerState = this.gettingPower();
         this.requiresUpdate = false;
         int max = 10;

         for(int count = 1; this.worldObj.getBlock(this.xCoord, this.yCoord - count, this.zCoord) == ConfigBlocks.blockLifter && !this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord - count, this.zCoord); max += 10) {
            ++count;
         }

         for(this.rangeAbove = 0; this.rangeAbove < max && !this.worldObj.getBlock(this.xCoord, this.yCoord + 1 + this.rangeAbove, this.zCoord).isOpaqueCube(); ++this.rangeAbove) {
         }
      }

      if (this.rangeAbove > 0 && !this.gettingPower()) {
         List<Entity> targets = this.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord + 1, this.zCoord, this.xCoord + 1, this.yCoord + 1 + this.rangeAbove, this.zCoord + 1));
         if (!targets.isEmpty()) {
            for(Entity e : targets) {
               if (e instanceof EntityItem || e.canBePushed() || e instanceof EntityHorse) {
                  if (Thaumcraft.proxy.isShiftKeyDown()) {
                     if (e.motionY < (double)0.0F) {
                        e.motionY *= 0.9F;
                     }
                  } else if (e.motionY < (double)0.35F) {
                     e.motionY += 0.1F;
                  }

                  e.fallDistance = 0.0F;
               }
            }
         }
      }

   }

   public boolean gettingPower() {
      return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord + 1, this.zCoord);
   }
}
