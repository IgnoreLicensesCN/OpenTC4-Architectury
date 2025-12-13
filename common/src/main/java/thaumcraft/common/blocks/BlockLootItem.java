package thaumcraft.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.ItemBlock;
import net.minecraft.world.item.ItemStack;

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

   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
      super.addInformation(stack, player, list, par4);
      list.add(this.getRarity(stack).rarityName);
   }
}
