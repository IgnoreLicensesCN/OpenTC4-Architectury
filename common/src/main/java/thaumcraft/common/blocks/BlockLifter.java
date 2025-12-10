package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileLifter;

import java.util.Random;

public class BlockLifter extends BlockContainer {
   private Random random = new Random();
   public IIcon iconTop;
   public IIcon iconBottom;
   public IIcon iconSide;
   public IIcon iconGlow;

   public BlockLifter() {
      super(Material.wood);
      this.setHardness(2.5F);
      this.setResistance(15.0F);
      this.setStepSound(soundTypeWood);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconTop = ir.registerIcon("thaumcraft:liftertop");
      this.iconBottom = ir.registerIcon("thaumcraft:arcaneearbottom");
      this.iconSide = ir.registerIcon("thaumcraft:lifterside");
      this.iconGlow = ir.registerIcon("thaumcraft:animatedglow");
   }

   public IIcon getIcon(int par1, int par2) {
      if (par1 == 0) {
         return this.iconBottom;
      } else {
         return par1 == 1 ? this.iconTop : this.iconSide;
      }
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return ConfigBlocks.blockLifterRI;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World w, int i, int j, int k, Random r) {
      TileEntity te = w.getTileEntity(i, j, k);
      if (te instanceof TileLifter && !((TileLifter) te).gettingPower() && ((TileLifter) te).rangeAbove > 0) {
         Thaumcraft.proxy.sparkle((float)i + 0.2F + r.nextFloat() * 0.6F, (float)(j + 1), (float)k + 0.2F + r.nextFloat() * 0.6F, 1.0F, 3, -0.3F);
      }

   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return side != ForgeDirection.UP && side != ForgeDirection.DOWN;
   }

   public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
      return side > 1;
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      this.updateLifterStack(world, x, y, z);
      super.onBlockAdded(world, x, y, z);
   }

   public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
      this.updateLifterStack(world, x, y, z);
      super.breakBlock(world, x, y, z, par5, par6);
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileLifter && ((TileLifter) te).gettingPower() != ((TileLifter) te).lastPowerState) {
         this.updateLifterStack(world, x, y, z);
      }

      super.onNeighborBlockChange(world, x, y, z, par5);
   }

   private void updateLifterStack(World worldObj, int xCoord, int yCoord, int zCoord) {
      for(int count = 1; worldObj.getBlock(xCoord, yCoord - count, zCoord) == this; ++count) {
         TileEntity te = worldObj.getTileEntity(xCoord, yCoord - count, zCoord);
         if (te instanceof TileLifter) {
            ((TileLifter)te).requiresUpdate = true;
         }
      }

   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return new TileLifter();
   }
}
