package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
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
   ForgeDirection fd = null;

   public AspectList getAspects() {
      return this.aspect != null ? (new AspectList()).add(this.aspect, this.amount) : new AspectList();
   }

   public void setAspects(AspectList aspects) {
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
      this.fd = ForgeDirection.getOrientation(this.facing);
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("aspect", this.aspect.getTag());
      }

      if (this.aspectFilter != null) {
         nbttagcompound.setString("AspectFilter", this.aspectFilter.getTag());
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
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean doesContainerContain(AspectList ot) {
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

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public void getAppearance() {
      this.aboveAlembic = false;
       this.aboveFurnace = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockStoneDevice && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0;

      if (this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockMetalDevice && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == this.getBlockMetadata()) {
         this.aboveAlembic = true;
      }

   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.getAppearance();
   }

   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
       if (side > 1) {
           this.facing = side;
           this.fd = ForgeDirection.getOrientation(this.facing);
           this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
           player.swingItem();
           this.markDirty();
       }
       return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {
   }

   public boolean isConnectable(ForgeDirection face) {
      return face != ForgeDirection.getOrientation(this.facing) && face != ForgeDirection.DOWN;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return false;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return face != ForgeDirection.getOrientation(this.facing) && face != ForgeDirection.DOWN;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public Aspect getSuctionType(ForgeDirection loc) {
      return null;
   }

   public int getSuctionAmount(ForgeDirection loc) {
      return 0;
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return this.aspect;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return this.amount;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection loc) {
      return 0;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return true;
   }
}
