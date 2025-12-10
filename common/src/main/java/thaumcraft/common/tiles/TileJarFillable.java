package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileJarFillable extends TileJar implements IAspectSource, IEssentiaTransport {
   public Aspect aspect = null;
   public Aspect aspectFilter = null;
   public int amount = 0;
   public int maxAmount = 64;
   public int facing = 2;
   public boolean forgeLiquid = false;
   public int lid = 0;
   int count = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspect = Aspect.getAspect(nbttagcompound.getString("Aspect"));
      this.aspectFilter = Aspect.getAspect(nbttagcompound.getString("AspectFilter"));
      this.amount = nbttagcompound.getShort("Amount");
      this.facing = nbttagcompound.getByte("facing");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("Aspect", this.aspect.getTag());
      }

      if (this.aspectFilter != null) {
         nbttagcompound.setString("AspectFilter", this.aspectFilter.getTag());
      }

      nbttagcompound.setShort("Amount", (short)this.amount);
      nbttagcompound.setByte("facing", (byte)this.facing);
   }

   public AspectList getAspects() {
      AspectList al = new AspectList();
      if (this.aspect != null && this.amount > 0) {
         al.add(this.aspect, this.amount);
      }

      return al;
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tt, int am) {
       if (am != 0) {
           if (this.amount < this.maxAmount && tt == this.aspect || this.amount == 0) {
               this.aspect = tt;
               int added = Math.min(am, this.maxAmount - this.amount);
               this.amount += added;
               am -= added;
           }

           this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
           this.markDirty();
       }
       return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.amount >= am && tt == this.aspect) {
         this.amount -= am;
         if (this.amount <= 0) {
            this.aspect = null;
            this.amount = 0;
         }

         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return this.amount >= amt && tag == this.aspect;
   }

   public boolean doesContainerContain(AspectList ot) {
      for(Aspect tt : ot.getAspects()) {
         if (this.amount > 0 && tt == this.aspect) {
            return true;
         }
      }

      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return this.aspectFilter == null || tag.equals(this.aspectFilter);
   }

   public boolean isConnectable(ForgeDirection face) {
      return face == ForgeDirection.UP;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return face == ForgeDirection.UP;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return face == ForgeDirection.UP;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return true;
   }

   public int getMinimumSuction() {
      return this.aspectFilter != null ? 64 : 32;
   }

   public Aspect getSuctionType(ForgeDirection loc) {
      return this.aspectFilter != null ? this.aspectFilter : this.aspect;
   }

   public int getSuctionAmount(ForgeDirection loc) {
      if (this.amount < this.maxAmount) {
         return this.aspectFilter != null ? 64 : 32;
      } else {
         return 0;
      }
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

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote && ++this.count % 5 == 0 && this.amount < this.maxAmount) {
         this.fillJar();
      }

   }

   void fillJar() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.UP);
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(ForgeDirection.DOWN)) {
            return;
         }

         Aspect ta = null;
         if (this.aspectFilter != null) {
            ta = this.aspectFilter;
         } else if (this.aspect != null && this.amount > 0) {
            ta = this.aspect;
         } else if (ic.getEssentiaAmount(ForgeDirection.DOWN) > 0 && ic.getSuctionAmount(ForgeDirection.DOWN) < this.getSuctionAmount(ForgeDirection.UP) && this.getSuctionAmount(ForgeDirection.UP) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(ForgeDirection.DOWN);
         }

         if (ta != null && ic.getSuctionAmount(ForgeDirection.DOWN) < this.getSuctionAmount(ForgeDirection.UP)) {
            this.addToContainer(ta, ic.takeEssentia(ta, 1, ForgeDirection.DOWN));
         }
      }

   }


   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
   }
}
