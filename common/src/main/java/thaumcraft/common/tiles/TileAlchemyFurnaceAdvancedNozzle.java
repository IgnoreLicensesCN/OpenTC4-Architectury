package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileAlchemyFurnaceAdvancedNozzle extends TileThaumcraft implements IAspectContainer, IEssentiaTransport {
   ForgeDirection facing;
   public TileAlchemyFurnaceAdvanced furnace;

   public TileAlchemyFurnaceAdvancedNozzle() {
      this.facing = ForgeDirection.UNKNOWN;
      this.furnace = null;
   }

   public boolean canUpdate() {
      return this.facing != null;
   }

   public void updateEntity() {
      if (this.facing == ForgeDirection.UNKNOWN && this.furnace == null) {
         this.facing = null;

         for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
            if (tile instanceof TileAlchemyFurnaceAdvanced) {
               this.facing = dir.getOpposite();
               this.furnace = (TileAlchemyFurnaceAdvanced)tile;
               break;
            }
         }
      }

   }

   public AspectList getAspects() {
      return this.furnace != null ? this.furnace.aspects : null;
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tt, int am) {
      return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.furnace == null) {
         return false;
      } else if (this.furnace.aspects.getAmount(tt) >= am) {
         this.furnace.aspects.remove(tt, am);
         this.furnace.markDirty();
         this.furnace.vis = this.furnace.aspects.visSize();
         this.worldObj.markBlockForUpdate(this.furnace.xCoord, this.furnace.yCoord, this.furnace.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      if (this.furnace == null) {
         return false;
      } else {
         return this.furnace.aspects.getAmount(tt) >= am;
      }
   }

   public int containerContains(Aspect tt) {
      return this.furnace == null ? 0 : this.furnace.aspects.getAmount(tt);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return false;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean isConnectable(ForgeDirection face) {
      return face == this.facing;
   }

   public boolean canInputFrom(ForgeDirection face) {
      return false;
   }

   public boolean canOutputTo(ForgeDirection face) {
      return face == this.facing;
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
      return 0;
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return this.furnace != null ? this.furnace.aspects.getAspects()[0] : null;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return this.furnace != null ? this.furnace.aspects.getAmount(this.furnace.aspects.getAspects()[0]) : null;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection facing) {
      return this.canOutputTo(facing) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection facing) {
      return 0;
   }
}
