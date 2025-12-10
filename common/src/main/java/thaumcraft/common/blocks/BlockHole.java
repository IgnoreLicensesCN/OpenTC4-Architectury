package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockHole extends BlockContainer {
   public IIcon icon;
   public IIcon icon2;

   public BlockHole() {
      super(Material.rock);
      this.setBlockUnbreakable();
      this.setResistance(6000000.0F);
      this.setStepSound(Block.soundTypeGlass);
      this.setLightLevel(0.7F);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setTickRandomly(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:blank");
      this.icon2 = ir.registerIcon("thaumcraft:empty");
   }

   public IIcon getIcon(int i, int m) {
      return m == 15 ? this.icon2 : this.icon;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
   }

   public boolean isSideSolid(IBlockAccess world, int i, int j, int k, ForgeDirection o) {
      return false;
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
   }

   public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
      return AxisAlignedBB.getBoundingBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return null;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Item.getItemById(0);
   }
}
