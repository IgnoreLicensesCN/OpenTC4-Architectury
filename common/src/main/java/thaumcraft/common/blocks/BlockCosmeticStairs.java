package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class BlockCosmeticStairs extends BlockStairs {
   Block refBlock;
   int refMeta;

   public BlockCosmeticStairs(Block p_i45428_1_, int p_i45428_2_) {
      super(p_i45428_1_, p_i45428_2_);
      this.refBlock = p_i45428_1_;
      this.refMeta = p_i45428_2_;
      this.setLightOpacity(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess ba, int x, int y, int z, int side) {
      return this.refBlock == ConfigBlocks.blockCosmeticSolid && this.refMeta == 11 ? this.refBlock.getIcon(ba, x, y, z, side + 100) : this.refBlock.getIcon(side, this.refMeta);
   }
}
