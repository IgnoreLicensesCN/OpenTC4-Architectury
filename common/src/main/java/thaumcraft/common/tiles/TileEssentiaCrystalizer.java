package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemCrystalEssence;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.awt.*;

public class TileEssentiaCrystalizer extends TileThaumcraft implements IAspectContainer, IEssentiaTransport {
   public Aspect aspect = null;
   public ForgeDirection facing;
   int count;
   int progress;
   final int progMax;
   public float spin;
   public float spinInc;
   float tr;
   float tg;
   float tb;
   public float cr;
   public float cg;
   public float cb;
   int venting;

   public TileEssentiaCrystalizer() {
      this.facing = ForgeDirection.DOWN;
      this.count = 0;
      this.progress = 0;
      this.progMax = 200;
      this.spin = 0.0F;
      this.spinInc = 0.0F;
      this.tr = 1.0F;
      this.tg = 1.0F;
      this.tb = 1.0F;
      this.cr = 1.0F;
      this.cg = 1.0F;
      this.cb = 1.0F;
      this.venting = 0;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspect = Aspect.getAspect(nbttagcompound.getString("Aspect"));
      this.facing = ForgeDirection.getOrientation(nbttagcompound.getByte("face"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("Aspect", this.aspect.getTag());
      }

      nbttagcompound.setByte("face", (byte)this.facing.ordinal());
   }

   public AspectList getAspects() {
      AspectList al = new AspectList();
      if (this.aspect != null) {
         al.add(this.aspect, 1);
      }

      return al;
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tt, int am) {
       if (am != 0) {
           if (this.aspect == null) {
               --am;
               this.aspect = tt;
               this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
               this.markDirty();
           }

       }
       return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.aspect != null && am == 1) {
         this.aspect = null;
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
      return amt == 1 && this.aspect != null && tag == this.aspect;
   }

   public boolean doesContainerContain(AspectList ot) {
      for(Aspect tt : ot.getAspects()) {
         if (this.aspect == null || this.aspect != tt || ot.getAmount(tt) != 1) {
            return false;
         }
      }

      return true;
   }

   public int containerContains(Aspect tag) {
      return this.aspect != null && tag == this.aspect ? 1 : 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(ForgeDirection face) {
      return face == this.facing;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return face == this.facing;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(ForgeDirection loc) {
      return null;
   }

   public int getSuctionAmount(ForgeDirection loc) {
      return this.gettingPower() ? 0 : (loc == this.facing && this.aspect == null ? 128 : 64);
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return this.aspect;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return this.aspect == null ? 0 : 1;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         if (++this.count % 5 == 0 && !this.gettingPower()) {
            if (this.aspect == null) {
               this.fillReservoir();
               this.progress = 0;
            } else {
               this.progress += 1 + VisNetHandler.drainVis(this.worldObj, this.xCoord, this.yCoord, this.zCoord, Aspect.EARTH, Math.min(20, Math.max(1, (200 - this.progress) / 2))) * 2;
            }
         }

         if (this.aspect != null && this.progress >= 200) {
            this.eject();
            this.aspect = null;
            this.progress = 0;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
         }
      } else {
         if (this.aspect == null) {
            this.tr = this.tg = this.tb = 1.0F;
         } else {
            Color c = new Color(this.aspect.getColor());
            this.tr = (float)c.getRed() / 220.0F;
            this.tg = (float)c.getGreen() / 220.0F;
            this.tb = (float)c.getBlue() / 220.0F;
         }

         if (this.cr < this.tr) {
            this.cr += 0.05F;
         }

         if (this.cr > this.tr) {
            this.cr -= 0.05F;
         }

         if (this.cg < this.tg) {
            this.cg += 0.05F;
         }

         if (this.cg > this.tg) {
            this.cg -= 0.05F;
         }

         if (this.cb < this.tb) {
            this.cb += 0.05F;
         }

         if (this.cb > this.tb) {
            this.cb -= 0.05F;
         }

         this.spin += this.spinInc;
         if (this.spin > 360.0F) {
            this.spin -= 360.0F;
         }

         if (this.aspect != null && this.spinInc < 20.0F && !this.gettingPower()) {
            this.spinInc += 0.1F;
            if (this.spinInc > 20.0F) {
               this.spinInc = 20.0F;
            }
         } else if ((this.aspect == null || this.gettingPower()) && this.spinInc > 0.0F) {
            this.spinInc -= 0.2F;
            if (this.spinInc < 0.0F) {
               this.spinInc = 0.0F;
            }
         }

         if (this.venting > 0) {
            --this.venting;
            float fx = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
            float fz = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
            float fy = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
            float fx2 = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
            float fz2 = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
            float fy2 = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
            int color = 16777215;
            Thaumcraft.proxy.drawVentParticles(this.worldObj, (float)this.xCoord + 0.5F + fx + (float)this.facing.getOpposite().offsetX / 2.1F, (float)this.yCoord + 0.5F + fy + (float)this.facing.getOpposite().offsetY / 2.1F, (float)this.zCoord + 0.5F + fz + (float)this.facing.getOpposite().offsetZ / 2.1F, (float)this.facing.getOpposite().offsetX / 4.0F + fx2, (float)this.facing.getOpposite().offsetY / 4.0F + fy2, (float)this.facing.getOpposite().offsetZ / 4.0F + fz2, color);
         }
      }

   }

   public boolean receiveClientEvent(int i, int j) {
      if (i >= 0) {
         if (this.worldObj.isRemote) {
            this.venting = 7;
         }

         return true;
      } else {
         return super.receiveClientEvent(i, j);
      }
   }

   public void eject() {
      ItemStack stack = new ItemStack(ConfigItems.itemCrystalEssence, 1, 0);
      ((ItemCrystalEssence)stack.getItem()).setAspects(stack, (new AspectList()).add(this.aspect, 1));
      TileEntity inventory = this.worldObj.getTileEntity(this.xCoord + this.facing.getOpposite().offsetX, this.yCoord + this.facing.getOpposite().offsetY, this.zCoord + this.facing.getOpposite().offsetZ);
      if (inventory instanceof IInventory) {
         stack = InventoryUtils.placeItemStackIntoInventory(stack, (IInventory)inventory, this.facing.ordinal(), true);
      }

      if (stack != null) {
         this.spawnItem(stack);
      }

      this.worldObj.playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "random.fizz", 0.25F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);
   }

   public boolean spawnItem(ItemStack stack) {
      EntityItem ie2 = new EntityItem(this.worldObj, (double)this.xCoord + (double)0.5F + (double)this.facing.getOpposite().offsetX * 0.65, (double)this.yCoord + (double)0.5F + (double)this.facing.getOpposite().offsetY * 0.65, (double)this.zCoord + (double)0.5F + (double)this.facing.getOpposite().offsetZ * 0.65, stack);
      ie2.motionX = (float)this.facing.getOpposite().offsetX * 0.04F;
      ie2.motionY = (float)this.facing.getOpposite().offsetY * 0.04F;
      ie2.motionZ = (float)this.facing.getOpposite().offsetZ * 0.04F;
      this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 0, 0);
      return this.worldObj.spawnEntityInWorld(ie2);
   }

   void fillReservoir() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.facing);
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

   public boolean gettingPower() {
      return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
   }
}
