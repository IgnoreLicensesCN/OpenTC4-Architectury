package thaumcraft.common.tiles;


@Deprecated(forRemoval = true)
public class TileArcaneLampGrowth /*extends TileThaumcraft implements IEssentiaTransportBlockEntity*/ {
//   public Direction facing = Direction.getOrientation(0);
//   private boolean owningEssentia = false;
//   public int charges = -1;
//   int lx = 0;
//   int ly = 0;
//   int lz = 0;
//   Block lid;//lastBlockID
//   int lmd;//lastBlockMetadata bad naming.
//   ArrayList checklist;
//   int drawDelay;
//
//   public TileArcaneLampGrowth() {
//      this.lid = Blocks.air;
//      this.lmd = 0;
//      this.checklist = new ArrayList<>();
//      this.drawDelay = 0;
//   }
//
//   public boolean canUpdate() {
//       return super.canUpdate();
//   }
//
//   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
//      super.onDataPacket(net, pkt);
//      if (this.level() != null && (Platform.getEnvironment() == Env.CLIENT)) {
//         this.level().updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
//      }
//
//   }
//
//   public void updateEntity() {
//      if (Platform.getEnvironment() != Env.CLIENT) {
//         if (this.charges <= 0) {
//            if (this.owningEssentia) {
//               this.charges = 100;
//               this.owningEssentia = false;
//               this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//            } else if (this.drawEssentia()) {
//               this.charges = 100;
//               this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//            }
//         }
//
//         if (!this.owningEssentia && this.drawEssentia()) {
//            this.owningEssentia = true;
//         }
//
//         if (this.charges == 0) {
//            this.charges = -1;
//            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
//         }
//
//         if (this.charges > 0) {
//            this.updatePlant();
//         }
//      }
//
//   }
//
//   boolean isPlant(int x, int y, int z) {
//      boolean flag = this.level().getBlock(x, y, z) instanceof IGrowable;
//      Material mat = this.level().getBlock(x, y, z).getMaterial();
//      return (flag || mat == Material.cactus || mat == Material.plants) && mat != Material.grass;
//   }
//
//   private void updatePlant() {
//      if (this.lid != this.level().getBlock(this.lx, this.ly, this.lz)
//              || this.lmd != this.level().getBlockMetadata(this.lx, this.ly, this.lz)
//      ) {
//         Player p = this.level().getClosestPlayer(this.lx, this.ly, this.lz, 32.0F);
//         if (p != null) {
//            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkleS2C(this.lx, this.ly, this.lz, 4259648), new NetworkRegistry.TargetPoint(this.level().dimension(), this.lx, this.ly, this.lz, 32.0F));
//         }
//
//         this.lid = this.level().getBlock(this.lx, this.ly, this.lz);
//         this.lmd = this.level().getBlockMetadata(this.lx, this.ly, this.lz);
//      }
//
//      int distance = 6;
//      if (this.checklist.isEmpty()) {
//         for(int a = -distance; a <= distance; ++a) {
//            for(int b = -distance; b <= distance; ++b) {
//               this.checklist.add(new BlockCoordinates(this.xCoord + a, this.yCoord + distance, this.zCoord + b));
//            }
//         }
//
//         Collections.shuffle(this.checklist, this.level().rand);
//      }
//
//      int x = ((BlockCoordinates)this.checklist.get(0)).x;
//      int y = ((BlockCoordinates)this.checklist.get(0)).y;
//      int z = ((BlockCoordinates)this.checklist.get(0)).z;
//      this.checklist.remove(0);
//
//      while(y >= this.yCoord - distance) {
//         if (!this.level().isAirBlock(x, y, z) && this.isPlant(x, y, z) && this.getDistanceFrom((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F) < (double)(distance * distance) && !CropUtils.isGrownCrop(this.level(), x, y, z) && CropUtils.doesLampGrow(this.level(), x, y, z)) {
//            --this.charges;
//            this.lx = x;
//            this.ly = y;
//            this.lz = z;
//            this.lid = this.level().getBlock(x, y, z);
//            this.lmd = this.level().getBlockMetadata(x, y, z);
//            this.level().scheduleBlockUpdate(x, y, z, this.level().getBlock(x, y, z), 1);
//            return;
//         }
//
//         --y;
//      }
//
//   }
//
//   public void readCustomNBT(NBTTagCompound nbttagcompound) {
//      this.facing = Direction.getOrientation(nbttagcompound.getInteger("orientation"));
//      this.owningEssentia = nbttagcompound.getBoolean("reserve");
//      this.charges = nbttagcompound.getInteger("charges");
//   }
//
//   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
//      nbttagcompound.setInteger("orientation", this.facing.ordinal());
//      nbttagcompound.setBoolean("reserve", this.owningEssentia);
//      nbttagcompound.setInteger("charges", this.charges);
//   }
//
//   boolean drawEssentia() {
//      if (++this.drawDelay % 5 != 0) {
//         return false;
//      } else {
//         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, this.facing);
//         if (te != null) {
//            IEssentiaTransportBlockEntity ic = (IEssentiaTransportBlockEntity)te;
//            if (!ic.canOutputTo(this.facing.getOpposite())) {
//               return false;
//            }
//
//             return ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && ic.takeEssentia(
//                     Aspects.PLANT, 1, this.facing.getOpposite()) == 1;
//         }
//
//         return false;
//      }
//   }
//
//   public boolean isConnectable(Direction face) {
//      return face == this.facing;
//   }
//
//   public boolean canInputFrom(Direction face) {
//      return face == this.facing;
//   }
//
//   public boolean canOutputTo(Direction face) {
//      return false;
//   }
//
//   public void setSuction(Aspect aspect, int amount) {
//   }
//
//   public boolean renderExtendedTube() {
//      return false;
//   }
//
//   public int getMinimumSuctionToDrainOut() {
//      return 0;
//   }
//
//   public Aspect getSuctionType(Direction face) {
//      return Aspects.PLANT;
//   }
//
//   public int getSuctionAmount(Direction face) {
//      return face != this.facing || this.owningEssentia && this.charges > 0 ? 0 : 128;
//   }
//
//   public @NotNull Aspect getEssentiaType(Direction loc) {
//      return null;
//   }
//
//   public int getEssentiaAmount(Direction loc) {
//      return 0;
//   }
//
//   public int takeEssentia(Aspect aspect, int amount, Direction outputToDirection) {
//      return 0;
//   }
//
//   public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
//      return 0;
//   }
}
