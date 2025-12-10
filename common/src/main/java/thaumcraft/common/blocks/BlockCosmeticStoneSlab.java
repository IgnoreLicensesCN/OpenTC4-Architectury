package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

import java.util.List;
import java.util.Random;

public class BlockCosmeticStoneSlab extends BlockSlab {
   public static final String[] types = new String[]{"arcane", "eldritch"};

   public BlockCosmeticStoneSlab(boolean p_i45437_1_) {
      super(p_i45437_1_, Material.rock);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setLightOpacity(0);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      return (p_149691_2_ & 7) == 0 ? ConfigBlocks.blockCosmeticSolid.getIcon(p_149691_1_, 7) : ((p_149691_2_ & 7) == 1 ? ConfigBlocks.blockCosmeticSolid.getIcon(p_149691_1_, 11) : super.getIcon(p_149691_1_, p_149691_2_));
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(ConfigBlocks.blockSlabStone);
   }

   protected ItemStack createStackedBlock(int p_149644_1_) {
      return new ItemStack(Item.getItemFromBlock(ConfigBlocks.blockSlabStone), 1, p_149644_1_ & 7);
   }

   public String func_150002_b(int p_150002_1_) {
      if (p_150002_1_ < 0 || p_150002_1_ >= types.length) {
         p_150002_1_ = 0;
      }

      return super.getUnlocalizedName() + "." + types[p_150002_1_];
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
      if (p_149666_1_ != Item.getItemFromBlock(ConfigBlocks.blockDoubleSlabStone)) {
         for(int i = 0; i < types.length; ++i) {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister p_149651_1_) {
   }

   @SideOnly(Side.CLIENT)
   private static boolean func_150003_a(Block p_150003_0_) {
      return p_150003_0_ == ConfigBlocks.blockSlabStone;
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
      return func_150003_a(this) ? Item.getItemFromBlock(this) : (this == ConfigBlocks.blockDoubleSlabStone ? Item.getItemFromBlock(ConfigBlocks.blockSlabStone) : Item.getItemFromBlock(Blocks.stone_slab));
   }
}
