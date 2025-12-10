package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;

public class TileBellows extends TileThaumcraft {
   public float inflation = 1.0F;
   boolean direction = false;
   boolean firstrun = true;
   public byte orientation = 0;
   public boolean onVanillaFurnace = false;
   public int delay = 0;

   public void updateEntity() {
      if (this.worldObj.isRemote) {
         if (!this.gettingPower()) {
            if (this.firstrun) {
               this.inflation = 0.35F + this.worldObj.rand.nextFloat() * 0.55F;
            }

            this.firstrun = false;
            if (this.inflation > 0.35F && !this.direction) {
               this.inflation -= 0.075F;
            }

            if (this.inflation <= 0.35F && !this.direction) {
               this.direction = true;
            }

            if (this.inflation < 1.0F && this.direction) {
               this.inflation += 0.025F;
            }

            if (this.inflation >= 1.0F && this.direction) {
               this.direction = false;
               this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "mob.ghast.fireball", 0.01F, 0.5F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F, false);
            }
         }
      } else if (this.onVanillaFurnace && !this.gettingPower()) {
         ++this.delay;
         if (this.delay >= 2) {
            this.delay = 0;
            ForgeDirection dir = ForgeDirection.getOrientation(this.orientation);
            TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord, this.zCoord + dir.offsetZ);
            if (tile instanceof TileEntityFurnace) {
               TileEntityFurnace tf = (TileEntityFurnace)tile;
               if (tf.furnaceCookTime > 0 && tf.furnaceCookTime < 199) {
                  ++tf.furnaceCookTime;
               }
            }
         }
      }

   }

   public boolean gettingPower() {
      return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
   }

   public static int getBellows(World world, int x, int y, int z, ForgeDirection[] directions) {
      int bellows = 0;

      for(ForgeDirection dir : directions) {
         int xx = x + dir.offsetX;
         int yy = y + dir.offsetY;
         int zz = z + dir.offsetZ;
         TileEntity tile = world.getTileEntity(xx, yy, zz);
         if (tile instanceof TileBellows && ((TileBellows) tile).orientation == dir.getOpposite().ordinal() && !world.isBlockIndirectlyGettingPowered(xx, yy, zz)) {
            ++bellows;
         }
      }

      return bellows;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.orientation = nbttagcompound.getByte("orientation");
      this.onVanillaFurnace = nbttagcompound.getBoolean("onVanillaFurnace");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("orientation", this.orientation);
      nbttagcompound.setBoolean("onVanillaFurnace", this.onVanillaFurnace);
   }
}
