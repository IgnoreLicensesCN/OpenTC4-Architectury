package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockTableItem extends ItemBlock {
   public BlockTableItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return par1ItemStack.getItemDamage() >= 2 && par1ItemStack.getItemDamage() <= 9 ? super.getUnlocalizedName() + ".research" : super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
