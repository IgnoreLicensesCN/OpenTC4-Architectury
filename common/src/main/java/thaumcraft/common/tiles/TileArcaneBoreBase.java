package thaumcraft.common.tiles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.core.Direction;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;

public class TileArcaneBoreBase extends TileThaumcraft implements IWandable, IEssentiaTransport {
   public Direction orientation = Direction.getOrientation(2);

   public boolean canUpdate() {
      return false;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.orientation = Direction.getOrientation(nbttagcompound.getInteger("orientation"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.orientation.ordinal());
   }

   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
      this.orientation = Direction.getOrientation(side);
      player.level().playSound((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:tool", 0.3F, 1.9F + player.level().rand.nextFloat() * 0.2F, false);
      player.swingItem();
      this.markDirty();
      return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
   }

   boolean drawEssentia() {
      for(Direction facing : Direction.VALID_DIRECTIONS) {
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, facing);
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(facing.getOpposite())) {
               return false;
            }

            if (ic.getSuctionAmount(facing.getOpposite()) < this.getSuctionAmount(facing) && ic.takeEssentia(Aspects.ENTROPY, 1, facing.getOpposite()) == 1) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isConnectable(Direction face) {
      return true;
   }

   public boolean canInputFrom(Direction face) {
      return true;
   }

   public boolean canOutputTo(Direction face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public Aspect getSuctionType(Direction face) {
      return Aspects.ENTROPY;
   }

   public int getSuctionAmount(Direction face) {
      return face != this.orientation ? 128 : 0;
   }

   public int takeEssentia(Aspect aspect, int amount, Direction face) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, Direction face) {
      return 0;
   }

   public Aspect getEssentiaType(Direction face) {
      return null;
   }

   public int getEssentiaAmount(Direction face) {
      return 0;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return true;
   }
}
