package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import thaumcraft.common.config.ConfigBlocks;

public class BlockCosmeticWoodSlabItem extends ItemSlab {
   public BlockCosmeticWoodSlabItem(Block par1) {
      super(par1, (BlockSlab)ConfigBlocks.blockSlabWood, (BlockSlab)ConfigBlocks.blockDoubleSlabWood, false);
   }
}
