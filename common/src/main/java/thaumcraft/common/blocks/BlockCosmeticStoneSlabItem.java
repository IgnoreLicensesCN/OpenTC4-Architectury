package thaumcraft.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockSlab;
import net.minecraft.world.item.ItemSlab;
import thaumcraft.common.config.ConfigBlocks;

public class BlockCosmeticStoneSlabItem extends ItemSlab {
   public BlockCosmeticStoneSlabItem(Block par1) {
      super(par1, (BlockSlab)ConfigBlocks.blockSlabStone, (BlockSlab)ConfigBlocks.blockDoubleSlabStone, false);
   }
}
