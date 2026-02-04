package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.core.Direction;
import thaumcraft.api.tile.TileThaumcraft;

public class TileBellows extends TileThaumcraft {
   public float renderInflation = 1.0F;
   boolean renderMovingDirection = false;
   boolean firstrun = true;
   public byte orientation = 0;
   public boolean onVanillaFurnace = false;
   public int delay = 0;

   public void updateEntity() {
      if ((Platform.getEnvironment() == Env.CLIENT)) {

         if (!this.gettingPower()) {
            if (this.firstrun) {
               this.renderInflation = 0.35F + this.level.rand.nextFloat() * 0.55F;
            }

            this.firstrun = false;

            if (this.renderInflation > 0.35F && !this.renderMovingDirection) {
               this.renderInflation -= 0.075F;
            }

            if (this.renderInflation <= 0.35F && !this.renderMovingDirection) {
               this.renderMovingDirection = true;
            }

            if (this.renderInflation < 1.0F && this.renderMovingDirection) {
               this.renderInflation += 0.025F;
            }

            if (this.renderInflation >= 1.0F && this.renderMovingDirection) {
               this.renderMovingDirection = false;
               this.level.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "mob.ghast.fireball", 0.01F, 0.5F + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.2F, false);
            }
         }
      } else if (this.onVanillaFurnace && !this.gettingPower()) {
         ++this.delay;
         if (this.delay >= 2) {
            this.delay = 0;
            Direction dir = Direction.getOrientation(this.orientation);
            TileEntity tile = this.level().getTileEntity(this.xCoord + dir.offsetX, this.yCoord, this.zCoord + dir.offsetZ);
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
      return this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
   }

   public static int getBellows(World world, int x, int y, int z, Direction[] directions) {
      int bellows = 0;

      for(Direction dir : directions) {
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
