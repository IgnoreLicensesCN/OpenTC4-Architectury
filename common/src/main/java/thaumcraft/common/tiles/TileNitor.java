package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.Thaumcraft;

public class TileNitor extends TileEntity {
   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.worldObj.isRemote) {
         if (this.worldObj.rand.nextInt(9 - Thaumcraft.proxy.particleCount(2)) == 0) {
            Thaumcraft.proxy.wispFX3(this.worldObj, (float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F, (float)this.xCoord + 0.3F + this.worldObj.rand.nextFloat() * 0.4F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.3F + this.worldObj.rand.nextFloat() * 0.4F, 0.5F, 4, true, -0.025F);
         }

         if (this.worldObj.rand.nextInt(15 - Thaumcraft.proxy.particleCount(4)) == 0) {
            Thaumcraft.proxy.wispFX3(this.worldObj, (float)this.xCoord + 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.5F, (float)this.xCoord + 0.4F + this.worldObj.rand.nextFloat() * 0.2F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.4F + this.worldObj.rand.nextFloat() * 0.2F, 0.25F, 1, true, -0.02F);
         }
      }

   }
}
