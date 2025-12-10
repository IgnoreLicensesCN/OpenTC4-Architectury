package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.common.config.ConfigBlocks;

public class TileNodeStabilizer extends TileEntity {
   public int count = 0;
   public int lock = 0;

   public TileNodeStabilizer(int metadata) {
      this.lock = metadata == 9 ? 1 : 2;
   }

   public TileNodeStabilizer() {
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.worldObj.isRemote && this.yCoord < this.worldObj.provider.getHeight() - 1) {
         int md = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);
         if (this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == ConfigBlocks.blockAiry && (md == 0 || md == 5) && !this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {
            if (this.count < 37) {
               ++this.count;
            }
         } else if (this.count > 0) {
            --this.count;
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1);
   }
}
