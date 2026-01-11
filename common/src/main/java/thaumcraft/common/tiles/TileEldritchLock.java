package thaumcraft.common.tiles;

@Deprecated(forRemoval = true)
public class TileEldritchLock /*extends TileThaumcraft*/ {
//   public int count = -1;
//   int[][] ped = new int[][]{{2, 2, 2}, {0, -1, 1}, {3, 3, 3}};
//   byte facing = 0;
//
//   public boolean canUpdate() {
//       return super.canUpdate();
//   }
//
//   public void updateEntity() {
//      super.updateEntity();
//      if (this.count != -1) {
//         ++this.count;
//         if (this.count % 5 == 0) {
//            this.level().playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:pump", 1.0F, 1.0F);
//         }
//
//         if (this.count > 100) {
//            this.doBossSpawn();
//         }
//      }
//
//   }
//
//
//
//   public double getMaxRenderDistanceSquared() {
//      return 9216.0F;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public AxisAlignedBB getRenderBoundingBox() {
//      return AxisAlignedBB.getBoundingBox((double)this.xCoord - (double)2.25F, (double)this.yCoord - (double)2.25F, (double)this.zCoord - (double)2.25F, (double)this.xCoord + (double)3.25F, (double)this.yCoord + (double)3.25F, (double)this.zCoord + (double)3.25F);
//   }
//
//   public byte getFacing() {
//      return this.facing;
//   }
//
//   public void setFacing(byte face) {
//      this.facing = face;
//      this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//      this.markDirty();
//   }
//
//   public void readCustomNBT(NBTTagCompound nbttagcompound) {
//      this.facing = nbttagcompound.getByte("facing");
//      this.count = nbttagcompound.getShort("count");
//   }
//
//   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
//      nbttagcompound.setByte("facing", this.facing);
//      nbttagcompound.setShort("count", (short)this.count);
//   }
}
