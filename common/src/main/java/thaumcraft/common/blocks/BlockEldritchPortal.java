package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileEldritchPortal;

import java.util.List;
import java.util.Random;

public class BlockEldritchPortal extends Block {
   public IIcon blankIcon;

   public BlockEldritchPortal() {
      super(Config.airyMaterial);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.blankIcon = ir.registerIcon("thaumcraft:blank");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return this.blankIcon;
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      return -1.0F;
   }

   public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
      return 200000.0F;
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      return 15;
   }

   public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return AxisAlignedBB.getBoundingBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return false;
   }

   public int getRenderType() {
      return -1;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Item.getItemById(0);
   }

   public TileEntity createTileEntity(World world, int metadata) {
      return new TileEldritchPortal();
   }

   public boolean hasTileEntity(int metadata) {
      return true;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
      if (world.getBlock(x, y + 1, z) != ConfigBlocks.blockEldritch || world.getBlock(x, y - 1, z) != ConfigBlocks.blockEldritch) {
         world.setBlockToAir(x, y, z);
      }

      super.onNeighborBlockChange(world, x, y, z, p_149695_5_);
   }
}
