package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockEldritchItem extends ItemBlock {
   public BlockEldritchItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
