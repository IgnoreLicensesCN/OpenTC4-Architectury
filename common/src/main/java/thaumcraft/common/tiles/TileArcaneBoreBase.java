package thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;

public class TileArcaneBoreBase extends TileThaumcraft implements IWandable, IEssentiaTransport {
   public ForgeDirection orientation = ForgeDirection.getOrientation(2);

   public boolean canUpdate() {
      return false;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.orientation = ForgeDirection.getOrientation(nbttagcompound.getInteger("orientation"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.orientation.ordinal());
   }

   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
      this.orientation = ForgeDirection.getOrientation(side);
      player.worldObj.playSound((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:tool", 0.3F, 1.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
      player.swingItem();
      this.markDirty();
      return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {
   }

   boolean drawEssentia() {
      for(ForgeDirection facing : ForgeDirection.VALID_DIRECTIONS) {
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, facing);
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(facing.getOpposite())) {
               return false;
            }

            if (ic.getSuctionAmount(facing.getOpposite()) < this.getSuctionAmount(facing) && ic.takeEssentia(Aspect.ENTROPY, 1, facing.getOpposite()) == 1) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isConnectable(ForgeDirection face) {
      return true;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return true;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public Aspect getSuctionType(ForgeDirection face) {
      return Aspect.ENTROPY;
   }

   public int getSuctionAmount(ForgeDirection face) {
      return face != this.orientation ? 128 : 0;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return 0;
   }

   public Aspect getEssentiaType(ForgeDirection face) {
      return null;
   }

   public int getEssentiaAmount(ForgeDirection face) {
      return 0;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return true;
   }
}
