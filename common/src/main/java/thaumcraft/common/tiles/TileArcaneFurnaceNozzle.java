package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.core.Direction;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileArcaneFurnaceNozzle extends TileThaumcraft implements IEssentiaTransport {
   Direction facing;
   TileArcaneFurnace furnace;
   int drawDelay;

   public TileArcaneFurnaceNozzle() {
      this.facing = Direction.UNKNOWN;
      this.furnace = null;
      this.drawDelay = 0;
   }

   public boolean canUpdate() {
      return this.facing != null;
   }

   public void updateEntity() {
      if (this.facing == Direction.UNKNOWN && this.furnace == null) {
         this.facing = null;

         for(Direction dir : Direction.VALID_DIRECTIONS) {
            TileEntity tile = this.level().getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
            if (tile instanceof TileArcaneFurnace) {
               this.facing = dir.getOpposite();
               this.furnace = (TileArcaneFurnace)tile;
               break;
            }
         }
      }

      if (Platform.getEnvironment() != Env.CLIENT) {
         try {
            if (this.furnace != null && this.furnace.speedyTime < 60 && this.drawEssentia()) {
               TileArcaneFurnace var10000 = this.furnace;
               var10000.speedyTime += 600;
            }
         } catch (Exception ignored) {
         }
      }

   }

   boolean drawEssentia() {
      if (++this.drawDelay % 5 != 0) {
         return false;
      } else {
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.level(), this.xCoord, this.yCoord, this.zCoord, this.facing);
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(this.facing.getOpposite())) {
               return false;
            }

             return ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && ic.takeEssentia(
                     Aspects.FIRE, 1, this.facing.getOpposite()) == 1;
         }

         return false;
      }
   }

   public boolean isConnectable(Direction face) {
      return this.facing != null;
   }

   public boolean canInputFrom(Direction face) {
      return this.facing != null;
   }

   public boolean canOutputTo(Direction face) {
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

   public Aspect getSuctionType(Direction face) {
      return Aspects.FIRE;
   }

   public int getSuctionAmount(Direction face) {
      try {
         if (this.furnace != null && this.furnace.speedyTime < 40) {
            return 128;
         }
      } catch (Exception ignored) {
      }

      return 0;
   }

   public Aspect getEssentiaType(Direction loc) {
      return null;
   }

   public int getEssentiaAmount(Direction loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, Direction facing) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, Direction facing) {
      return 0;
   }
}
