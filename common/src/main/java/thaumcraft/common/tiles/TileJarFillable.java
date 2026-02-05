package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.core.Direction;
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
         nbttagcompound.setString("Aspect", this.aspect.getAspectKey());
      }

      if (this.aspectFilter != null) {
         nbttagcompound.setString("AspectFilter", this.aspectFilter.getAspectKey());
      }

      nbttagcompound.setShort("Amount", (short)this.amount);
      nbttagcompound.setByte("facing", (byte)this.facing);
   }

   public AspectList<Aspect>getAspects() {
      AspectList<Aspect>al = new AspectList();
      if (this.aspect != null && this.amount > 0) {
         al.addAll(this.aspect, this.amount);
      }

      return al;
   }

   public void setAspects(AspectList<Aspect>aspects) {
   }

   public int addToContainer(Aspect tt, int am) {
       if (am != 0) {
           if (this.amount < this.maxAmount && tt == this.aspect || this.amount == 0) {
               this.aspect = tt;
               int added = Math.min(am, this.maxAmount - this.amount);
               this.amount += added;
               am -= added;
           }

           this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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

         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return this.amount >= amt && tag == this.aspect;
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
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

   public boolean isConnectable(Direction face) {
      return face == Direction.UP;
   }

   public boolean canInputFrom(Direction face) {
      return face == Direction.UP;
   }

   public boolean canOutputTo(Direction face) {
      return face == Direction.UP;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return true;
   }

   public int getMinimumSuction() {
      return this.aspectFilter != null ? 64 : 32;
   }

   public Aspect getSuctionType(Direction loc) {
      return this.aspectFilter != null ? this.aspectFilter : this.aspect;
   }

   public int getSuctionAmount(Direction loc) {
      if (this.amount < this.maxAmount) {
         return this.aspectFilter != null ? 64 : 32;
      } else {
         return 0;
      }
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

   public int addEssentia(Aspect aspect, int amount, Direction face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void updateEntity() {
      super.updateEntity();
      if (Platform.getEnvironment() != Env.CLIENT && ++this.count % 5 == 0 && this.amount < this.maxAmount) {
         this.fillJar();
      }

   }

   void fillJar() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, Direction.UP);
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(Direction.DOWN)) {
            return;
         }

         Aspect ta = null;
         if (this.aspectFilter != null) {
            ta = this.aspectFilter;
         } else if (this.aspect != null && this.amount > 0) {
            ta = this.aspect;
         } else if (ic.getEssentiaAmount(Direction.DOWN) > 0 && ic.getSuctionAmount(Direction.DOWN) < this.getSuctionAmount(Direction.UP) && this.getSuctionAmount(Direction.UP) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(Direction.DOWN);
         }

         if (ta != null && ic.getSuctionAmount(Direction.DOWN) < this.getSuctionAmount(Direction.UP)) {
            this.addToContainer(ta, ic.takeEssentia(ta, 1, Direction.DOWN));
         }
      }

   }


   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.level().func_147479_m(this.xCoord, this.yCoord, this.zCoord);
   }
}
