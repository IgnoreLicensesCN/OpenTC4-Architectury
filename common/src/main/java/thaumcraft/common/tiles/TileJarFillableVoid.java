package thaumcraft.common.tiles;

import net.minecraft.core.Direction;
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
               this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
               this.markDirty();
           }

       }
       return am;
   }

   public int getMinimumSuctionToDrainOut() {
      return this.aspectFilter != null ? 48 : 32;
   }

   public int getSuctionAmount(Direction loc) {
      return this.aspectFilter != null && this.amount < this.maxAmount ? 48 : 32;
   }

   public void updateEntity() {
      if (Platform.getEnvironment() != Env.CLIENT && ++this.count % 5 == 0) {
         this.fillJar();
      }

   }
}
