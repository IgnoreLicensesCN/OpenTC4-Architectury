package thaumcraft.common.config;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialTaint extends Material {
   private int mobilityFlag;

   public MaterialTaint(MapColor par1MapColor) {
      super(par1MapColor);
      this.setNoPushMobility();
      this.setRequiresTool();
   }

   public boolean isSolid() {
      return false;
   }

   public boolean isReplaceable() {
      return false;
   }

   public boolean getCanBlockGrass() {
      return false;
   }

   public boolean blocksMovement() {
       return super.blocksMovement();
   }

   protected Material setRequiresTool() {
      return this;
   }

   protected Material setNoPushMobility() {
      this.mobilityFlag = 1;
      return this;
   }

   public int getMaterialMobility() {
      return this.mobilityFlag;
   }
}
