package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockFluxGooItem extends ItemBlock {
   public BlockFluxGooItem(Block i) {
      super(i);
      this.maxStackSize = 64;
      this.setHasSubtypes(true);
   }

   public boolean isFull3D() {
      return true;
   }

   public int getMetadata(int i) {
      return 8;
   }
}
