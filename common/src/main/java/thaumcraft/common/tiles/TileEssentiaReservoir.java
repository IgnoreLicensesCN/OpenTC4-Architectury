package thaumcraft.common.tiles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.core.Direction;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;

import java.awt.*;

public class TileEssentiaReservoir extends TileThaumcraft implements IAspectSource, IWandable, IEssentiaTransport {
   public AspectList essentia = new AspectList();
   public int maxAmount = 256;
   public Direction facing;
   int count;
   float tr;
   float tri;
   float tg;
   float tgi;
   float tb;
   float tbi;
   public float cr;
   public float cg;
   public float cb;
   public Aspect displayAspect;

   public TileEssentiaReservoir() {
      this.facing = Direction.DOWN;
      this.count = 0;
      this.tr = 1.0F;
      this.tri = 0.0F;
      this.tg = 1.0F;
      this.tgi = 0.0F;
      this.tb = 1.0F;
      this.tbi = 0.0F;
      this.cr = 1.0F;
      this.cg = 1.0F;
      this.cb = 1.0F;
      this.displayAspect = null;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.essentia.readFromNBT(nbttagcompound);
      if (this.essentia.visSize() > this.maxAmount) {
         this.essentia = new AspectList();
      }

      this.facing = Direction.getOrientation(nbttagcompound.getByte("face"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      this.essentia.writeToNBT(nbttagcompound);
      nbttagcompound.setByte("face", (byte)this.facing.ordinal());
   }

   public AspectList getAspects() {
      return this.essentia;
   }

   public void setAspects(AspectList aspects) {
      this.essentia = aspects.copy();
   }

   public int addToContainer(Aspect tt, int am) {
       if (am != 0) {
           int space = this.maxAmount - this.essentia.visSize();
           if (space >= am) {
               this.essentia.addAll(tt, am);
               am = 0;
           } else {
               this.essentia.addAll(tt, space);
               am -= space;
           }

           if (space > 0) {
               this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
               this.markDirty();
           }

       }
       return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.essentia.getAmount(tt) >= am) {
         this.essentia.reduceAndRemoveIfNegative(tt, am);
         this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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
      return this.essentia.getAmount(tag) >= amt;
   }

   public boolean doesContainerContain(AspectList ot) {
      for(Aspect tt : ot.getAspects()) {
         if (this.essentia.getAmount(tt) < ot.getAmount(tt)) {
            return false;
         }
      }

      return true;
   }

   public int containerContains(Aspect tag) {
      return this.essentia.getAmount(tag);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(Direction face) {
      return face == this.facing;
   }

   public boolean canInputFrom(Direction face) {
      return face == this.facing;
   }

   public boolean canOutputTo(Direction face) {
      return face == this.facing;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuction() {
      return 24;
   }

   public Aspect getSuctionType(Direction loc) {
      return null;
   }

   public int getSuctionAmount(Direction loc) {
      return this.essentia.visSize() < this.maxAmount ? 24 : 0;
   }

   public Aspect getEssentiaType(Direction loc) {
      return this.essentia.visSize() > 0 && loc == Direction.UNKNOWN ? this.essentia.getAspects()[0] : null;
   }

   public int getEssentiaAmount(Direction loc) {
      return this.essentia.visSize();
   }

   public int takeEssentia(Aspect aspect, int amount, Direction face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, Direction face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void updateEntity() {
      super.updateEntity();
      ++this.count;
      if (Platform.getEnvironment() != Env.CLIENT && this.count % 5 == 0 && this.essentia.visSize() < this.maxAmount) {
         this.fillReservoir();
      }

      if ((Platform.getEnvironment() == Env.CLIENT)) {
         int vs = this.essentia.visSize();
         if (vs > 0) {
            if (this.level().rand.nextInt(500 - vs) == 0) {
               this.level().playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:creak", 1.0F, 1.4F + this.level().rand.nextFloat() * 0.2F, false);
            }

            if (this.count % 20 == 0 && this.essentia.size() > 0) {
               this.displayAspect = this.essentia.getAspects()[this.count / 20 % this.essentia.size()];
               Color c = new Color(this.displayAspect.getColor());
               this.tr = (float)c.getRed() / 255.0F;
               this.tg = (float)c.getGreen() / 255.0F;
               this.tb = (float)c.getBlue() / 255.0F;
               this.tri = (this.cr - this.tr) / 20.0F;
               this.tgi = (this.cg - this.tg) / 20.0F;
               this.tbi = (this.cb - this.tb) / 20.0F;
            }

            if (this.displayAspect == null) {
               this.tr = this.tg = this.tb = 1.0F;
               this.tri = this.tgi = this.tbi = 0.0F;
            } else {
               this.cr -= this.tri;
               this.cg -= this.tgi;
               this.cb -= this.tbi;
            }
         }
      }

   }

   void fillReservoir() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, this.facing);
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(this.facing.getOpposite())) {
            return;
         }

         Aspect ta = null;
         if (ic.getEssentiaAmount(this.facing.getOpposite()) > 0 && ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && this.getSuctionAmount(this.facing) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(this.facing.getOpposite());
         }

         if (ta != null && ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing)) {
            this.addToContainer(ta, ic.takeEssentia(ta, 1, this.facing.getOpposite()));
         }
      }

   }

   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
      if (player.isSneaking()) {
         this.facing = Direction.getOrientation(side);
      } else {
         this.facing = Direction.getOrientation(side).getOpposite();
      }

      this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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
}
