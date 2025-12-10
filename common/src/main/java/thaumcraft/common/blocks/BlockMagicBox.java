package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileMagicBox;

import java.util.List;
import java.util.Random;

public class BlockMagicBox extends BlockContainer {
   private Random random = new Random();
   public IIcon icon;

   public BlockMagicBox() {
      super(Material.wood);
      this.setHardness(2.5F);
      this.setStepSound(soundTypeWood);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:woodplain");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return this.icon;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
       return super.renderAsNormalBlock();
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      InventoryUtils.dropItems(par1World, par2, par3, par4);
      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      Object var10 = par1World.getTileEntity(par2, par3, par4);
      if (var10 == null) {
         return true;
      } else if (par1World.isRemote) {
         return true;
      } else {
         par5EntityPlayer.openGui(Thaumcraft.instance, 18, par1World, par2, par3, par4);
         return true;
      }
   }

   public TileEntity createNewTileEntity(World par1World, int m) {
      return new TileMagicBox();
   }
}
