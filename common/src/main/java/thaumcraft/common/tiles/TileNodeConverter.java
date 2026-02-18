package thaumcraft.common.tiles;

@Deprecated(forRemoval = true)
public class TileNodeConverter /*extends TileThaumcraft*/ {
//   public int count = -1;
//   public int status = 0;
//
//   public boolean canUpdate() {
//       return super.canUpdate();
//   }
//
//   public void updateEntity() {
//      super.updateEntity();
//      if (this.count == -1) {
//         this.checkStatus();
//      }
//
//      if (this.status == 1 && Platform.getEnvironment() != Env.CLIENT && this.count >= 1000) {
//         TileEntity tile = this.level().getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
//         if (tile instanceof AbstractNodeBlockEntity) {
//            AspectList<Aspect>base = ((AbstractNodeBlockEntity)tile).getAspectsBase();
//            NodeType type = ((AbstractNodeBlockEntity)tile).getNodeType();
//            NodeModifier mod = ((AbstractNodeBlockEntity)tile).getNodeModifier();
//            this.level().setBlock(this.xCoord, this.yCoord - 1, this.zCoord, ConfigBlocks.blockAiry, 5, 3);
//            TileEntity tilenew = this.level().getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
//            if (tilenew instanceof TileNodeEnergized) {
//               ((TileNodeEnergized)tilenew).setNodeModifier(mod);
//               ((TileNodeEnergized)tilenew).setNodeType(type);
//               ((TileNodeEnergized)tilenew).setAspects(base.copy());
//               ((TileNodeEnergized)tilenew).setupNode();
//            }
//
//            this.checkStatus();
//            this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 10, 10);
//            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//            this.markDirty();
//         }
//      }
//
//      if (this.status == 2 && Platform.getEnvironment() != Env.CLIENT && this.count <= 50) {
//         TileEntity tile = this.level().getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
//         if (tile instanceof TileNodeEnergized) {
//            AspectList<Aspect>base = ((TileNodeEnergized)tile).getAuraBase();
//            NodeType type = ((TileNodeEnergized)tile).getNodeType();
//            NodeModifier mod = ((TileNodeEnergized)tile).getNodeModifier();
//            this.level().setBlock(this.xCoord, this.yCoord - 1, this.zCoord, ConfigBlocks.blockAiry, 0, 3);
//            TileEntity tilenew = this.level().getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
//            if (tilenew instanceof AbstractNodeBlockEntity) {
//               ((AbstractNodeBlockEntity)tilenew).setNodeModifier(mod);
//               ((AbstractNodeBlockEntity)tilenew).setNodeType(type);
//               ((AbstractNodeBlockEntity)tilenew).setAspectsWithBase(base.copy());
//
//               for(Aspect a : ((AbstractNodeBlockEntity)tilenew).getAspects().getAspects()) {
//                  ((AbstractNodeBlockEntity)tilenew).takeAspectFromContainer(a, ((AbstractNodeBlockEntity)tilenew).getAspects().getAmount(a));
//               }
//            }
//
//            this.level().addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 10, 10);
//            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//            this.markDirty();
//            this.status = 0;
//         }
//      }
//
//      if (this.status != 0 && this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {
//         if (this.count < 1000) {
//            ++this.count;
//            if (Platform.getEnvironment() != Env.CLIENT) {
//               TileEntity tilenew = this.level().getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
//               if (tilenew instanceof AbstractNodeBlockEntity) {
//                  AbstractNodeBlockEntity nd = (AbstractNodeBlockEntity)tilenew;
//                  AspectList<Aspect>al = nd.getAspects();
//                  if (al.getAspects().length > 0) {
//                     nd.takeAspectFromContainer(al.getAspects()[this.level().rand.nextInt(al.getAspects().length)], 1);
//                     if (this.count % 5 == 0 || nd.getAspects().visSize() == 0) {
//                        this.level().markBlockForUpdate(this.xCoord, this.yCoord - 1, this.zCoord);
//                     }
//                  }
//               }
//            }
//
//            if (this.count > 50 && (Platform.getEnvironment() == Env.CLIENT)) {
//               if (this.level().rand.nextBoolean()) {
//                  Thaumcraft.proxy.nodeBolt(this.level(), (float)this.xCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
//               }
//
//               if (this.level().rand.nextBoolean() && this.hasStabilizer()) {
//                  Thaumcraft.proxy.nodeBolt(this.level(), (float)this.xCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.yCoord - 1.5F, (float)this.zCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
//               }
//            }
//         }
//      } else if (this.count > 0) {
//         --this.count;
//         if (this.count > 50 && (Platform.getEnvironment() == Env.CLIENT)) {
//            if (this.level().rand.nextBoolean()) {
//               Thaumcraft.proxy.nodeBolt(this.level(), (float)this.xCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
//            }
//
//            if (this.level().rand.nextBoolean() && this.hasStabilizer()) {
//               Thaumcraft.proxy.nodeBolt(this.level(), (float)this.xCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.yCoord - 1.5F, (float)this.zCoord + 0.25F + this.level().rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
//            }
//         }
//      }
//
//      if (this.count > 1000) {
//         this.count = 1000;
//      }
//
//   }
//
//   private boolean hasStabilizer() {
//      TileEntity te = this.level().getTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
//      return !this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord - 2, this.zCoord) && te instanceof TileNodeStabilizer;
//   }
//
//   public void checkStatus() {
//      if (this.count == -1) {
//         this.count = 0;
//      }
//
//      if (this.status != 2 || this.count <= 50 || this.hasStabilizer() && this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 5) {
//         if (this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0 && this.hasStabilizer()) {
//            this.status = 1;
//            this.markDirty();
//            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//         } else if (this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 5) {
//            this.status = 2;
//            this.count = 1000;
//            this.markDirty();
//            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//         } else {
//            this.status = 0;
//         }
//      } else {
//         BlockAiry.explodify(this.getLevel(), this.xCoord, this.yCoord - 1, this.zCoord);
//         this.status = 0;
//         this.count = 50;
//         this.markDirty();
//         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//      }
//
//   }
//
//   public void readCustomNBT(NBTTagCompound nbttagcompound) {
//      super.readCustomNBT(nbttagcompound);
//      this.status = nbttagcompound.getInteger("status");
//      this.count = nbttagcompound.getInteger("count");
//   }
//
//   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
//      super.writeCustomNBT(nbttagcompound);
//      nbttagcompound.setInteger("status", this.status);
//      nbttagcompound.setInteger("count", this.count);
//   }
//
//   public boolean receiveClientEvent(int i, int j) {
//      if (i == 10 && j == 10) {
//         if ((Platform.getEnvironment() == Env.CLIENT)) {
//            Thaumcraft.proxy.burst(this.level(), (double)this.xCoord + (double)0.5F, (double)this.yCoord - (double)0.5F, (double)this.zCoord + (double)0.5F, 1.0F);
//            this.level().playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord - (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:craftfail", 0.5F, 1.0F, false);
//         }
//
//         return true;
//      } else {
//         return super.receiveClientEvent(i, j);
//      }
//   }
}
