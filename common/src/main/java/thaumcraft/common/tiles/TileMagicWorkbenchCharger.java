package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TileMagicWorkbenchCharger extends TileVisRelay {
   public short orientation = 0;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord - 1, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
   }

   public boolean isSource() {
       return super.isSource();
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         TileEntity te = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
         if (te instanceof TileMagicWorkbench) {
            TileMagicWorkbench tm = (TileMagicWorkbench)te;
            ItemStack wand = tm.getStackInSlot(10);
            if (wand != null && wand.getItem() instanceof ItemWandCasting) {
               AspectList al = ((ItemWandCasting)wand.getItem()).getAspectsWithRoom(wand);
               if (al.size() > 0) {
                  for(Aspect aspect : al.getAspects()) {
                     int drain = Math.min(5, ((ItemWandCasting)wand.getItem()).getMaxVis(tm.getStackInSlot(10)) - ((ItemWandCasting)wand.getItem()).getVis(tm.getStackInSlot(10), aspect));
                     if (drain > 0) {
                        ((ItemWandCasting)wand.getItem()).addRealVis(tm.getStackInSlot(10), aspect, this.consumeVis(aspect, drain), true);
                     }
                  }
               }
            }
         }
      }

   }
}
