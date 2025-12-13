package thaumcraft.common.tiles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.codechicken.lib.raytracer.RayTracer;

public class TileTubeValve extends TileTube {
   public boolean allowFlow = true;
   boolean wasPoweredLastTick = false;
   public float rotation = 0.0F;

   public void updateEntity() {
      if (Platform.getEnvironment() != Env.CLIENT && this.count % 5 == 0) {
         boolean gettingPower = this.gettingPower();
         if (this.wasPoweredLastTick && !gettingPower && !this.allowFlow) {
            this.allowFlow = true;
            this.level().playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:squeek", 0.7F, 0.9F + this.level().rand.nextFloat() * 0.2F);
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
         }

         if (!this.wasPoweredLastTick && gettingPower && this.allowFlow) {
            this.allowFlow = false;
            this.level().playSoundEffect((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:squeek", 0.7F, 0.9F + this.level().rand.nextFloat() * 0.2F);
            this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
         }

         this.wasPoweredLastTick = gettingPower;
      }

      if ((Platform.getEnvironment() == Env.CLIENT)) {
         if (!this.allowFlow && this.rotation < 360.0F) {
            this.rotation += 20.0F;
         } else if (this.allowFlow && this.rotation > 0.0F) {
            this.rotation -= 20.0F;
         }
      }

      super.updateEntity();
   }

   public int onWandRightClick(World world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md) {
      HitResult hit = RayTracer.retraceBlock(world, player, x, y, z);
       if (hit != null) {
           if (hit.subHit >= 0 && hit.subHit < 6) {
               player.level().playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.level().rand.nextFloat() * 0.2F, false);
               player.swingItem();
               this.markDirty();
               world.markBlockForUpdate(x, y, z);
               this.openSides[hit.subHit] = !this.openSides[hit.subHit];
               Direction dir = Direction.getOrientation(hit.subHit);
               TileEntity tile = this.level().getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
               if (tile instanceof TileTube) {
                   ((TileTube) tile).openSides[dir.getOpposite().ordinal()] = this.openSides[hit.subHit];
                   world.markBlockForUpdate(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                   tile.markDirty();
               }
           }

           if (hit.subHit == 6) {
               player.level().playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.level().rand.nextFloat() * 0.2F, false);
               player.swingItem();
               int a = this.facing.ordinal();
               this.markDirty();

               while (true) {
                   ++a;
                   if (a >= 20) {
                       break;
                   }

                   if (!this.canConnectSide(Direction.getOrientation(a % 6).ordinal())) {
                       a %= 6;
                       this.facing = Direction.getOrientation(a);
                       world.markBlockForUpdate(x, y, z);
                       break;
                   }
               }
           }

       }
       return 0;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      super.readCustomNBT(nbttagcompound);
      this.allowFlow = nbttagcompound.getBoolean("flow");
      this.wasPoweredLastTick = nbttagcompound.getBoolean("hadpower");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      super.writeCustomNBT(nbttagcompound);
      nbttagcompound.setBoolean("flow", this.allowFlow);
      nbttagcompound.setBoolean("hadpower", this.wasPoweredLastTick);
   }

   public boolean isConnectable(Direction face) {
      return face != this.facing && super.isConnectable(face);
   }

   public void setSuction(Aspect aspect, int amount) {
      if (this.allowFlow) {
         super.setSuction(aspect, amount);
      }

   }

   public boolean gettingPower() {
      return this.level().isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
   }
}
