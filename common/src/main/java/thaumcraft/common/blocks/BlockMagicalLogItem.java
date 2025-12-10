package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockMagicalLogItem extends ItemBlock {
   public BlockMagicalLogItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      int var2 = par1ItemStack.getItemDamage();
      if (var2 < 0 || var2 >= BlockMagicalLog.woodType.length) {
         var2 = 0;
      }

      return super.getUnlocalizedName() + "." + BlockMagicalLog.woodType[var2];
   }
}
