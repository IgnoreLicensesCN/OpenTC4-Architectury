package thaumcraft.common.tiles;

import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;

public class TileJarFillableVoid extends TileJarFillable {
   int count = 0;

   public int addToContainer(Aspect tt, int am) {
      boolean up = this.amount < this.maxAmount;
       if (am != 0) {
           if (tt == this.aspect || this.amount == 0) {
               this.aspect = tt;
               this.amount += am;
               am = 0;
               if (this.amount > this.maxAmount) {
                   this.amount = this.maxAmount;
               }
           }

           if (up) {
               this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
               this.markDirty();
           }

       }
       return am;
   }

   public int getMinimumSuction() {
      return this.aspectFilter != null ? 48 : 32;
   }

   public int getSuctionAmount(ForgeDirection loc) {
      return this.aspectFilter != null && this.amount < this.maxAmount ? 48 : 32;
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote && ++this.count % 5 == 0) {
         this.fillJar();
      }

   }
}
