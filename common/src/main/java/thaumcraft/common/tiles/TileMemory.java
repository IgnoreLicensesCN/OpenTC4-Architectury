package thaumcraft.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileMemory extends TileEntity {
   public Block oldblock;
   public int oldmeta;
   public NBTTagCompound tileEntityCompound;

   public TileMemory() {
   }

   public TileMemory(Block bi, int md, TileEntity te) {
      this.oldblock = bi;
      this.oldmeta = md;
      if (te != null) {
         this.tileEntityCompound = new NBTTagCompound();
         te.writeToNBT(this.tileEntityCompound);
      }

   }

   public boolean canUpdate() {
      return false;
   }

   public void recreateTileEntity() {
      if (this.tileEntityCompound != null && this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != null) {
         this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.oldblock, this.oldmeta, 0);
         this.tileEntityCompound.setInteger("x", this.xCoord);
         this.tileEntityCompound.setInteger("y", this.yCoord);
         this.tileEntityCompound.setInteger("z", this.zCoord);
         this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord).readFromNBT(this.tileEntityCompound);
      }

      this.markDirty();
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.oldblock = Block.getBlockById(nbttagcompound.getInteger("oldblock"));
      this.oldmeta = nbttagcompound.getInteger("oldmeta");
      if (nbttagcompound.hasKey("TileEntity")) {
         this.tileEntityCompound = nbttagcompound.getCompoundTag("TileEntity");
      }

   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("oldblock", Block.getIdFromBlock(this.oldblock));
      nbttagcompound.setInteger("oldmeta", this.oldmeta);
      if (this.tileEntityCompound != null) {
         nbttagcompound.setTag("TileEntity", this.tileEntityCompound);
      }

   }
}
