package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.config.ConfigBlocks;

public class TileWardingStoneFence extends TileEntity {
   int count = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         if (this.count == 0) {
            this.count = this.worldObj.rand.nextInt(100);
         }

         if (++this.count % 100 == 0 && (this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) != ConfigBlocks.blockCosmeticSolid || this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) != 3) && (this.worldObj.getBlock(this.xCoord, this.yCoord - 2, this.zCoord) != ConfigBlocks.blockCosmeticSolid || this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 2, this.zCoord) != 3)) {
            this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
         }
      }

   }
}
