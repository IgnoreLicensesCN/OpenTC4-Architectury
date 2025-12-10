package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class BlockTaintFibresItem extends ItemBlock {
   public BlockTaintFibresItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      return ThaumcraftWorldGenerator.biomeTaint.color;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return ((BlockTaintFibres)ConfigBlocks.blockTaintFibres).icon[meta];
   }
}
