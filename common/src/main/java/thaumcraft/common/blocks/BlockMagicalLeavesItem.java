package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockMagicalLeavesItem extends ItemBlock {
   public BlockMagicalLeavesItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1 | 4;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      int var2 = par1ItemStack.getItemDamage();
      return super.getUnlocalizedName() + "." + BlockMagicalLeaves.leafType[var2 & 1];
   }
}
