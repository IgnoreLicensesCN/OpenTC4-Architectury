package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileArcaneFurnaceNozzle extends TileThaumcraft implements IEssentiaTransport {
   ForgeDirection facing;
   TileArcaneFurnace furnace;
   int drawDelay;

   public TileArcaneFurnaceNozzle() {
      this.facing = ForgeDirection.UNKNOWN;
      this.furnace = null;
      this.drawDelay = 0;
   }

   public boolean canUpdate() {
      return this.facing != null;
   }

   public void updateEntity() {
      if (this.facing == ForgeDirection.UNKNOWN && this.furnace == null) {
         this.facing = null;

         for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
            if (tile instanceof TileArcaneFurnace) {
               this.facing = dir.getOpposite();
               this.furnace = (TileArcaneFurnace)tile;
               break;
            }
         }
      }

      if (!this.worldObj.isRemote) {
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
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.facing);
         if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(this.facing.getOpposite())) {
               return false;
            }

             return ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && ic.takeEssentia(Aspect.FIRE, 1, this.facing.getOpposite()) == 1;
         }

         return false;
      }
   }

   public boolean isConnectable(ForgeDirection face) {
      return this.facing != null;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return this.facing != null;
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

   public Aspect getSuctionType(ForgeDirection face) {
      return Aspect.FIRE;
   }

   public int getSuctionAmount(ForgeDirection face) {
      try {
         if (this.furnace != null && this.furnace.speedyTime < 40) {
            return 128;
         }
      } catch (Exception ignored) {
      }

      return 0;
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return null;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection facing) {
      return 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection facing) {
      return 0;
   }
}
