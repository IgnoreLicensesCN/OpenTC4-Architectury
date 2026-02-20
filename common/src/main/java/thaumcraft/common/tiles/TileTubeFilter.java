package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainerBlockEntity;

public class TileTubeFilter extends TileTube implements IAspectContainerBlockEntity {
   public Aspect aspectFilter = null;

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      super.readCustomNBT(nbttagcompound);
      this.aspectFilter = Aspect.getAspect(nbttagcompound.getString("AspectFilter"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      super.writeCustomNBT(nbttagcompound);
      if (this.aspectFilter != null) {
         nbttagcompound.setString("AspectFilter", this.aspectFilter.getAspectKey());
      }

   }

   void calculateSuction(Aspect filter, boolean restrict, boolean dir) {
      super.calculateSuction(this.aspectFilter, restrict, dir);
   }

   public @NotNull AspectList<Aspect>getAspects() {
      return this.aspectFilter != null ? (new AspectList<>()).addAll(this.aspectFilter, -1) : null;
   }

   public void setAspects(AspectList<Aspect>aspects) {
   }

   public boolean doesContainerAccept(Aspect tag) {
      return false;
   }

   public int addIntoContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }
}
