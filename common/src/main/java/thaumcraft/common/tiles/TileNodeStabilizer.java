package thaumcraft.common.tiles;

@Deprecated(forRemoval = true)
public class TileNodeStabilizer /*extends TileEntity*/ {
//   public int count = 0;
//   public int lock = 0;
//
//   public TileNodeStabilizer(int metadata) {
//      this.lock = metadata == 9 ? 1 : 2;
//   }
//
//   public TileNodeStabilizer() {
//   }
//
//   public boolean canUpdate() {
//       return super.canUpdate();
//   }
//
//   public void updateEntity() {
//      super.updateEntity();
//      if ((Platform.getEnvironment() == Env.CLIENT) && this.yCoord < this.level().provider.getHeight() - 1) {
//         int md = this.level().getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);
//         if (this.level().getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == ConfigBlocks.blockAiry
//                 && (md == 0 || md == 5)
//                 && !this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {
//            if (this.count < 37) {
//               ++this.count;
//            }
//         } else if (this.count > 0) {
//            --this.count;
//         }
//      }
//   }
//
//   @SideOnly(Side.CLIENT)
//   public AxisAlignedBB getRenderBoundingBox() {
//      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1);
//   }
}
