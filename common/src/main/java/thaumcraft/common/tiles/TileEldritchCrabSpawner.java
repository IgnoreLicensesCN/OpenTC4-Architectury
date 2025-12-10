package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityEldritchCrab;

import java.util.List;

public class TileEldritchCrabSpawner extends TileThaumcraft {
   public int count = 150;
   public int ticks = 0;
   int venting = 0;
   byte facing = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.ticks == 0) {
         this.ticks = this.worldObj.rand.nextInt(500);
      }

      ++this.ticks;
      if (!this.worldObj.isRemote) {
         --this.count;
         if (this.count < 0) {
            this.count = 50 + this.worldObj.rand.nextInt(50);
         } else {
            if (this.count == 15 && this.isActivated() && !this.maxEntitiesReached()) {
               this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 0);
               this.worldObj.playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "random.fizz", 0.5F, 1.0F);
            }

            if (this.count <= 0 && this.isActivated() && !this.maxEntitiesReached()) {
               this.count = 150 + this.worldObj.rand.nextInt(100);
               this.spawnCrab();
               this.worldObj.playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:gore", 0.5F, 1.0F);
            }
         }
      } else if (this.venting > 0) {
         --this.venting;

         for(int a = 0; a < 3; ++a) {
            this.drawVent();
         }
      } else if (this.worldObj.rand.nextInt(20) == 0) {
         this.drawVent();
      }

   }

   void drawVent() {
      ForgeDirection dir = ForgeDirection.getOrientation(this.facing);
      float fx = 0.15F - this.worldObj.rand.nextFloat() * 0.3F;
      float fz = 0.15F - this.worldObj.rand.nextFloat() * 0.3F;
      float fy = 0.15F - this.worldObj.rand.nextFloat() * 0.3F;
      float fx2 = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
      float fz2 = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
      float fy2 = 0.1F - this.worldObj.rand.nextFloat() * 0.2F;
      Thaumcraft.proxy.drawVentParticles(this.worldObj, (float)this.xCoord + 0.5F + fx + (float)dir.offsetX / 2.1F, (float)this.yCoord + 0.5F + fy + (float)dir.offsetY / 2.1F, (float)this.zCoord + 0.5F + fz + (float)dir.offsetZ / 2.1F, (float)dir.offsetX / 3.0F + fx2, (float)dir.offsetY / 3.0F + fy2, (float)dir.offsetZ / 3.0F + fz2, 10061994, 2.0F);
   }

   public boolean receiveClientEvent(int i, int j) {
      if (i == 1) {
         this.venting = 20;
         return true;
      } else {
         return super.receiveClientEvent(i, j);
      }
   }

   private boolean maxEntitiesReached() {
      List ents = this.worldObj.getEntitiesWithinAABB(EntityEldritchCrab.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (double)this.xCoord + (double)1.0F, (double)this.yCoord + (double)1.0F, (double)this.zCoord + (double)1.0F).expand(32.0F, 32.0F, 32.0F));
      return ents.size() > 5;
   }

   public boolean isActivated() {
      return this.worldObj.getClosestPlayer((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, 16.0F) != null;
   }

   private void spawnCrab() {
      ForgeDirection dir = ForgeDirection.getOrientation(this.facing);
      EntityEldritchCrab crab = new EntityEldritchCrab(this.worldObj);
      double x = this.xCoord + dir.offsetX;
      double y = this.yCoord + dir.offsetY;
      double z = this.zCoord + dir.offsetZ;
      crab.setLocationAndAngles(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F, 0.0F, 0.0F);
      crab.onSpawnWithEgg(null);
      crab.setHelm(false);
      crab.motionX = (float)dir.offsetX * 0.2F;
      crab.motionY = (float)dir.offsetY * 0.2F;
      crab.motionZ = (float)dir.offsetZ * 0.2F;
      this.worldObj.spawnEntityInWorld(crab);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2);
   }

   public byte getFacing() {
      return this.facing;
   }

   public void setFacing(byte face) {
      this.facing = face;
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      this.markDirty();
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = nbttagcompound.getByte("facing");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("facing", this.facing);
   }
}
