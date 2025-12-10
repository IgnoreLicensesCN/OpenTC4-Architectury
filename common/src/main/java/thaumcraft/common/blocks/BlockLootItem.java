package thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockLootItem extends ItemBlock {
   public BlockLootItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public EnumRarity getRarity(ItemStack stack) {
      switch (stack.getItemDamage()) {
         case 1:
            return EnumRarity.uncommon;
         case 2:
            return EnumRarity.rare;
         default:
            return EnumRarity.common;
      }
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.addInformation(stack, player, list, par4);
      list.add(this.getRarity(stack).rarityName);
   }
}
