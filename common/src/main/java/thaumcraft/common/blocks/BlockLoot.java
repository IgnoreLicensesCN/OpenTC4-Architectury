package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BlockLoot extends Block {
   String iconPre = "urn";
   int renderType = 0;
   public IIcon[] icon = new IIcon[4];

   public BlockLoot(Material mat, String ip, int rt) {
      super(mat);
      this.setHardness(0.15F);
      this.setResistance(0.0F);
      this.iconPre = ip;
      this.renderType = rt;
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:" + this.iconPre + "_top");
      this.icon[1] = ir.registerIcon("thaumcraft:" + this.iconPre + "_side_0");
      this.icon[2] = ir.registerIcon("thaumcraft:" + this.iconPre + "_side_1");
      this.icon[3] = ir.registerIcon("thaumcraft:" + this.iconPre + "_side_2");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side <= 1 ? this.icon[0] : this.icon[meta + 1];
   }

   public int getRenderType() {
      return this.renderType == 1 ? ConfigBlocks.blockLootUrnRI : ConfigBlocks.blockLootCrateRI;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
      if (this.renderType == 1) {
         this.setBlockBounds(BlockRenderer.W2, BlockRenderer.W1, BlockRenderer.W2, BlockRenderer.W14, BlockRenderer.W13, BlockRenderer.W14);
      } else {
         this.setBlockBounds(BlockRenderer.W1, 0.0F, BlockRenderer.W1, BlockRenderer.W15, BlockRenderer.W14, BlockRenderer.W15);
      }

      return super.getSelectedBoundingBoxFromPool(w, i, j, k);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public ArrayList getDrops(World world, int x, int y, int z, int md, int fortune) {
      ArrayList<ItemStack> ret = new ArrayList<>();
      int q = 1 + md + world.rand.nextInt(3);

      for(int a = 0; a < q; ++a) {
         ItemStack is = Utils.generateLoot(md, world.rand);
         if (is != null) {
            ret.add(is.copy());
         }
      }

      return ret;
   }
}
