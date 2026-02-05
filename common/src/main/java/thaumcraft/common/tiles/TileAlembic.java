package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.core.Direction;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.config.ConfigBlocks;

public class TileAlembic extends TileThaumcraft implements IAspectContainer, IWandable, IEssentiaTransport {
   public Aspect aspect;
   public Aspect aspectFilter = null;
   public int amount = 0;
   public int maxAmount = 32;
   public int facing = 2;
   public boolean aboveAlembic = false;
   public boolean aboveFurnace = false;
   Direction fd = null;

   public AspectList<Aspect>getAspects() {
      return this.aspect != null ? (new AspectList()).addAll(this.aspect, this.amount) : new AspectList();
   }

   public void setAspects(AspectList<Aspect>aspects) {
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 2, this.yCoord + 1, this.zCoord + 2);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = nbttagcompound.getByte("facing");
      this.aspectFilter = Aspect.getAspect(nbttagcompound.getString("AspectFilter"));
      String tag = nbttagcompound.getString("aspect");
      if (tag != null) {
         this.aspect = Aspect.getAspect(tag);
      }

      this.amount = nbttagcompound.getShort("amount");
      this.fd = Direction.getOrientation(this.facing);
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("aspect", this.aspect.getAspectKey());
      }

      if (this.aspectFilter != null) {
         nbttagcompound.setString("AspectFilter", this.aspectFilter.getAspectKey());
      }

      nbttagcompound.setShort("amount", (short)this.amount);
      nbttagcompound.setByte("facing", (byte)this.facing);
   }

   public boolean canUpdate() {
      return false;
   }

   public int addToContainer(Aspect tt, int am) {
      if (this.amount < this.maxAmount && tt == this.aspect || this.amount == 0) {
         this.aspect = tt;
         int added = Math.min(am, this.maxAmount - this.amount);
         this.amount += added;
         am -= added;
      }

      this.markDirty();
      this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.amount == 0 || this.aspect == null) {
         this.aspect = null;
         this.amount = 0;
      }

      if (this.aspect != null && this.amount >= am && tt == this.aspect) {
         this.amount -= am;
         if (this.amount <= 0) {
            this.aspect = null;
            this.amount = 0;
         }

         this.markDirty();
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
      return this.amount > 0 && this.aspect != null && ot.getAmount(this.aspect) > 0;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      return this.amount >= am && tt == this.aspect;
   }

   public int containerContains(Aspect tt) {
      return tt == this.aspect ? this.amount : 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public void getAppearance() {
      this.aboveAlembic = false;
       this.aboveFurnace = this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockStoneDevice && this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0;

      if (this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockMetalDevice && this.level().getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == this.getBlockMetadata()) {
         this.aboveAlembic = true;
      }

   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.getAppearance();
   }

   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
       if (side > 1) {
           this.facing = side;
           this.fd = Direction.getOrientation(this.facing);
           this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
           player.swingItem();
           this.markDirty();
       }
       return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, Player player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, Player player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, Player player, int count) {
   }

   public boolean isConnectable(Direction face) {
      return face != Direction.getOrientation(this.facing) && face != Direction.DOWN;
   }

   public boolean canInputFrom(Direction face) {
      return false;
   }

   public boolean canOutputTo(Direction face) {
      return face != Direction.getOrientation(this.facing) && face != Direction.DOWN;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public Aspect getSuctionType(Direction loc) {
      return null;
   }

   public int getSuctionAmount(Direction loc) {
      return 0;
   }

   public Aspect getEssentiaType(Direction loc) {
      return this.aspect;
   }

   public int getEssentiaAmount(Direction loc) {
      return this.amount;
   }

   public int takeEssentia(Aspect aspect, int amount, Direction face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, Direction loc) {
      return 0;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return true;
   }
}
