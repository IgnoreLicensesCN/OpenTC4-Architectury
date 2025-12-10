package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileCentrifuge extends TileThaumcraft implements IAspectContainer, IEssentiaTransport {
   public Aspect aspectOut = null;
   public Aspect aspectIn = null;
   public ForgeDirection facing;
   int count;
   int process;
   float rotationSpeed;
   public float rotation;

   public TileCentrifuge() {
      this.facing = ForgeDirection.NORTH;
      this.count = 0;
      this.process = 0;
      this.rotationSpeed = 0.0F;
      this.rotation = 0.0F;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspectIn = Aspect.getAspect(nbttagcompound.getString("aspectIn"));
      this.aspectOut = Aspect.getAspect(nbttagcompound.getString("aspectOut"));
      this.facing = ForgeDirection.getOrientation(nbttagcompound.getInteger("facing"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspectIn != null) {
         nbttagcompound.setString("aspectIn", this.aspectIn.getTag());
      }

      if (this.aspectOut != null) {
         nbttagcompound.setString("aspectOut", this.aspectOut.getTag());
      }

      nbttagcompound.setInteger("facing", this.facing.ordinal());
   }

   public AspectList getAspects() {
      AspectList al = new AspectList();
      if (this.aspectOut != null) {
         al.add(this.aspectOut, 1);
      }

      return al;
   }

   public int addToContainer(Aspect tt, int am) {
      if (am > 0 && this.aspectOut == null) {
         this.aspectOut = tt;
         this.markDirty();
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         --am;
      }

      return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.aspectOut != null && tt == this.aspectOut) {
         this.aspectOut = null;
         this.markDirty();
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return amt == 1 && tag == this.aspectOut;
   }

   public boolean doesContainerContain(AspectList ot) {
      for(Aspect tt : ot.getAspects()) {
         if (tt == this.aspectOut) {
            return true;
         }
      }

      return false;
   }

   public int containerContains(Aspect tag) {
      return tag == this.aspectOut ? 1 : 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(ForgeDirection face) {
      return face == ForgeDirection.UP || face == ForgeDirection.DOWN;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return face == ForgeDirection.DOWN;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return face == ForgeDirection.UP;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(ForgeDirection face) {
      return null;
   }

   public int getSuctionAmount(ForgeDirection face) {
      return face == ForgeDirection.DOWN ? (this.gettingPower() ? 0 : (this.aspectIn == null ? 128 : 64)) : 0;
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return this.aspectOut;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return this.aspectOut != null ? 1 : 0;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      if (this.aspectIn == null && !aspect.isPrimal()) {
         this.aspectIn = aspect;
         this.process = 39;
         this.markDirty();
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return 1;
      } else {
         return 0;
      }
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         if (!this.gettingPower()) {
            if (this.aspectOut == null && this.aspectIn == null && ++this.count % 5 == 0) {
               this.drawEssentia();
            }

            if (this.process > 0) {
               --this.process;
            }

            if (this.aspectOut == null && this.aspectIn != null && this.process == 0) {
               this.processEssentia();
            }
         }
      } else {
         if (this.aspectIn != null && !this.gettingPower() && this.rotationSpeed < 20.0F) {
            this.rotationSpeed += 2.0F;
         }

         if ((this.aspectIn == null || this.gettingPower()) && this.rotationSpeed > 0.0F) {
            this.rotationSpeed -= 0.5F;
         }

         int pr = (int)this.rotation;
         this.rotation += this.rotationSpeed;
         if (this.rotation % 180.0F <= 20.0F && pr % 180 >= 160 && this.rotationSpeed > 0.0F) {
            this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:pump", 1.0F, 1.0F, false);
         }
      }

   }

   void processEssentia() {
      Aspect[] comps = this.aspectIn.getComponents();
      this.aspectOut = comps[this.worldObj.rand.nextInt(2)];
      this.aspectIn = null;
      this.markDirty();
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   void drawEssentia() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.DOWN);
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(ForgeDirection.UP)) {
            return;
         }

         Aspect ta = null;
         if (ic.getEssentiaAmount(ForgeDirection.UP) > 0 && ic.getSuctionAmount(ForgeDirection.UP) < this.getSuctionAmount(ForgeDirection.DOWN) && this.getSuctionAmount(ForgeDirection.DOWN) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(ForgeDirection.UP);
         }

         if (ta != null && !ta.isPrimal() && ic.getSuctionAmount(ForgeDirection.UP) < this.getSuctionAmount(ForgeDirection.DOWN) && ic.takeEssentia(ta, 1, ForgeDirection.UP) == 1) {
            this.aspectIn = ta;
            this.process = 39;
            this.markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      }

   }

   public void setAspects(AspectList aspects) {
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
   }

   public boolean gettingPower() {
      return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
   }
}
