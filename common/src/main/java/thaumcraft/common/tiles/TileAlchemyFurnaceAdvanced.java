package thaumcraft.common.tiles;

@Deprecated(forRemoval = true)
public class TileAlchemyFurnaceAdvanced /*extends TileThaumcraft*/ {
//   public AspectList<Aspect>aspects = new AspectList<>();
//   public int visSize;
//   public static int maxVis = 500;
//   int bellows = -1;
//   public int heat = 0;
//   public int aspectAmountEntropy = 0;
//   public int aspectAmountWater = 0;
//   public int maxPower = 500;
//   public boolean destroy = false;
//   int tickCount = 0;
//   int processed = 0;
//
//   @SideOnly(Side.CLIENT)
//   public AxisAlignedBB getRenderBoundingBox() {
//      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
//   }
//
//   public void readCustomNBT(NBTTagCompound nbttagcompound) {
//      this.visSize = nbttagcompound.getShort("vis");
//      this.heat = nbttagcompound.getShort("heat");
//   }
//
//   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
//      nbttagcompound.setShort("vis", (short)this.visSize);
//      nbttagcompound.setShort("heat", (short)this.heat);
//   }
//
//   public void readFromNBT(NBTTagCompound nbtCompound) {
//      super.readFromNBT(nbtCompound);
//      this.aspects.readFromNBT(nbtCompound);
//      this.aspectAmountEntropy = nbtCompound.getShort("power1");
//      this.aspectAmountWater = nbtCompound.getShort("power2");
//   }
//
//   public void writeToNBT(NBTTagCompound nbtCompound) {
//      super.writeToNBT(nbtCompound);
//      this.aspects.writeToNBT(nbtCompound);
//      nbtCompound.setShort("power1", (short)this.aspectAmountEntropy);
//      nbtCompound.setShort("power2", (short)this.aspectAmountWater);
//   }
//
//   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
//      super.onDataPacket(net, pkt);
//      if (this.level() != null) {
//         this.level().updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
//      }
//
//   }
//
//   public boolean canUpdate() {
//       return super.canUpdate();
//   }
//
//   public void updateEntity() {
//      ++this.tickCount;
//      if (Platform.getEnvironment() != Env.CLIENT) {
//         if (this.destroy) {
//            for(int a = -1; a <= 1; ++a) {
//               for(int b = 0; b <= 1; ++b) {
//                  for(int c = -1; c <= 1; ++c) {
//                     if ((a != 0 || b != 0 || c != 0) && this.level().getBlock(this.xCoord + a, this.yCoord + b, this.zCoord + c) == this.getBlockType()) {
//                        int m = this.level().getBlockMetadata(this.xCoord + a, this.yCoord + b, this.zCoord + c);
//                        this.level().setBlock(this.xCoord + a, this.yCoord + b, this.zCoord + c, Block.getBlockFromItem(this.getBlockType().getItemDropped(m, this.level().rand, 0)), this.getBlockType().damageDropped(m), 3);
//                     }
//                  }
//               }
//            }
//
//            this.level().setBlock(this.xCoord, this.yCoord, this.zCoord, Block.getBlockFromItem(this.getBlockType().getItemDropped(0, this.level().rand, 0)), this.getBlockType().damageDropped(0), 3);
//            return;
//         }
//
//         if (this.processed > 0) {
//            --this.processed;
//         }
//
//         if (this.tickCount % 5 == 0) {
//            int pt = this.heat--;
//            if (this.heat <= this.maxPower) {
//               this.heat += VisNetHandler.drainVis(this.level(), this.xCoord, this.yCoord, this.zCoord, Aspects.FIRE, 50);
//            }
//
//            if (this.aspectAmountEntropy <= this.maxPower) {
//               this.aspectAmountEntropy += VisNetHandler.drainVis(this.level(), this.xCoord, this.yCoord, this.zCoord, Aspects.ENTROPY, 50);
//            }
//
//            if (this.aspectAmountWater <= this.maxPower) {
//               this.aspectAmountWater += VisNetHandler.drainVis(this.level(), this.xCoord, this.yCoord, this.zCoord, Aspects.WATER, 50);
//            }
//
//            if (pt / 50 != this.heat / 50) {
//               this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//            }
//         }
//      }
//
//   }
//
//   public boolean process(ItemStack stack) {
//      if (this.processed == 0 && this.canSmelt(stack)) {
//         AspectList<Aspect>al = ThaumcraftCraftingManager.getObjectTags(stack);
//         al = ThaumcraftCraftingManager.getBonusAspects(stack, al);
//         int aa = al.visSize();
//         if (aa * 2 <= this.heat && aa <= this.aspectAmountEntropy && aa <= this.aspectAmountWater) {
//            this.heat -= aa * 2;
//            this.aspectAmountEntropy -= aa;
//            this.aspectAmountWater -= aa;
//            this.processed = (int)((float)this.processed + 5.0F + Math.max(0.0F, (1.0F - (float)this.heat / (float)this.maxPower) * 100.0F));
//            this.aspects.addAll(al);
//            this.visSize = this.aspects.visSize();
//            this.markDirty();
//            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//            return true;
//         } else {
//            return false;
//         }
//      } else {
//         return false;
//      }
//   }
//
//   private boolean canSmelt(ItemStack stack) {
//      if (stack == null) {
//         return false;
//      } else {
//         AspectList<Aspect>al = ThaumcraftCraftingManager.getObjectTags(stack);
//         al = ThaumcraftCraftingManager.getBonusAspects(stack, al);
//         if (al != null && al.size() != 0) {
//            int vs = al.visSize();
//            return vs + this.aspects.visSize() <= maxVis;
//         } else {
//            return false;
//         }
//      }
//   }
}
