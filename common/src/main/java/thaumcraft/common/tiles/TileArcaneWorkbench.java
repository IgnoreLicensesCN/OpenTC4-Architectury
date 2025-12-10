package thaumcraft.common.tiles;

public class TileArcaneWorkbench extends TileMagicWorkbench {
   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
   }

   public String getInventoryName() {
      return "container.arcaneworkbench";
   }
}
