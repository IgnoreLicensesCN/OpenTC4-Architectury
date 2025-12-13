package thaumcraft.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;

public class BlockTaintItem extends ItemBlock {
   public BlockTaintItem(Block par1) {
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
