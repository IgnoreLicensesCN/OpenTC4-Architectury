package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSpark;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileCrystal;
import thaumcraft.common.tiles.TileEldritchCrystal;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCrystal extends BlockContainer implements IInfusionStabiliser {
   private Random random = new Random();
   public IIcon icon;

   public BlockCrystal() {
      super(Material.glass);
      this.setHardness(0.7F);
      this.setResistance(1.0F);
      this.setLightLevel(0.5F);
      this.setStepSound(new CustomStepSound("crystal", 1.0F, 1.0F));
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int var4 = 0; var4 <= 6; ++var4) {
         par3List.add(new ItemStack(par1, 1, var4));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:crystal");
   }

   public IIcon getIcon(int par1, int par2) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      int md = world.getBlockMetadata(i, j, k);
      if (md <= 6 && random.nextInt(17) == 0) {
         FXSpark ef = new FXSpark(world, (double)i + 0.3 + (double)(world.rand.nextFloat() * 0.4F), (double)j + 0.3 + (double)(world.rand.nextFloat() * 0.4F), (double)k + 0.3 + (double)(world.rand.nextFloat() * 0.4F), 0.2F + random.nextFloat() * 0.1F);
         Color c = new Color(md == 6 ? BlockCustomOreItem.colors[random.nextInt(6) + 1] : BlockCustomOreItem.colors[md + 1]);
         ef.setRBGColorF((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
         ef.setAlphaF(0.8F);
         ParticleEngine.instance.addEffect(world, ef);
      }

   }

   public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
      int md = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
      if (md < 6) {
         return BlockCustomOreItem.colors[md + 1];
      } else {
         return md == 6 ? BlockCustomOreItem.colors[(new Random()).nextInt(6) + 1] : super.colorMultiplier(par1iBlockAccess, par2, par3, par4);
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return ConfigBlocks.blockCrystalRI;
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata <= 6) {
         return new TileCrystal();
      } else {
         return metadata == 7 ? new TileEldritchCrystal() : super.createTileEntity(world, metadata);
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public ArrayList getDrops(World world, int x, int y, int z, int md, int fortune) {
      ArrayList<ItemStack> ret = new ArrayList<>();
      if (md < 6) {
         for(int t = 0; t < 6; ++t) {
            ret.add(new ItemStack(ConfigItems.itemShard, 1, md));
         }

         return ret;
      } else if (md != 6) {
         if (md == 7) {
            ret.add(new ItemStack(ConfigItems.itemShard, 1, 6));
            return ret;
         } else {
            return super.getDrops(world, x, y, z, md, fortune);
         }
      } else {
         for(int t = 0; t < 6; ++t) {
            ret.add(new ItemStack(ConfigItems.itemShard, 1, t));
         }

         return ret;
      }
   }

   public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
      super.onNeighborBlockChange(world, i, j, k, l);
      int md = world.getBlockMetadata(i, j, k);
      if (md <= 6 && this.checkIfAttachedToBlock(world, i, j, k)) {
         TileCrystal tes = (TileCrystal)world.getTileEntity(i, j, k);
         int i1 = tes.orientation;
         boolean flag = !world.isSideSolid(i - 1, j, k, ForgeDirection.getOrientation(5)) && i1 == 5;

          if (!world.isSideSolid(i + 1, j, k, ForgeDirection.getOrientation(4)) && i1 == 4) {
            flag = true;
         }

         if (!world.isSideSolid(i, j, k - 1, ForgeDirection.getOrientation(3)) && i1 == 3) {
            flag = true;
         }

         if (!world.isSideSolid(i, j, k + 1, ForgeDirection.getOrientation(2)) && i1 == 2) {
            flag = true;
         }

         if (!world.isSideSolid(i, j - 1, k, ForgeDirection.getOrientation(1)) && i1 == 1) {
            flag = true;
         }

         if (!world.isSideSolid(i, j + 1, k, ForgeDirection.getOrientation(0)) && i1 == 0) {
            flag = true;
         }

         if (flag) {
            this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
         }

      } else if (md == 7) {
         TileCrystal tes = (TileCrystal)world.getTileEntity(i, j, k);
         ForgeDirection fd = ForgeDirection.getOrientation(tes.orientation).getOpposite();
         if (world.isAirBlock(i + fd.offsetX, j + fd.offsetY, k + fd.offsetZ)) {
            this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockToAir(i, j, k);
         }

      }
   }

   private boolean checkIfAttachedToBlock(World world, int i, int j, int k) {
      if (!this.canPlaceBlockAt(world, i, j, k)) {
         this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
         return false;
      } else {
         return true;
      }
   }

   public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l) {
      if (l == 0 && world.isSideSolid(i, j + 1, k, ForgeDirection.getOrientation(0))) {
         return true;
      } else if (l == 1 && world.isSideSolid(i, j - 1, k, ForgeDirection.getOrientation(1))) {
         return true;
      } else if (l == 2 && world.isSideSolid(i, j, k + 1, ForgeDirection.getOrientation(2))) {
         return true;
      } else if (l == 3 && world.isSideSolid(i, j, k - 1, ForgeDirection.getOrientation(3))) {
         return true;
      } else if (l == 4 && world.isSideSolid(i + 1, j, k, ForgeDirection.getOrientation(4))) {
         return true;
      } else {
         return l == 5 && world.isSideSolid(i - 1, j, k, ForgeDirection.getOrientation(5));
      }
   }

   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      if (world.isSideSolid(i - 1, j, k, ForgeDirection.getOrientation(5))) {
         return true;
      } else if (world.isSideSolid(i + 1, j, k, ForgeDirection.getOrientation(4))) {
         return true;
      } else if (world.isSideSolid(i, j, k - 1, ForgeDirection.getOrientation(3))) {
         return true;
      } else if (world.isSideSolid(i, j, k + 1, ForgeDirection.getOrientation(2))) {
         return true;
      } else {
         return world.isSideSolid(i, j - 1, k, ForgeDirection.getOrientation(1)) || world.isSideSolid(i, j + 1, k, ForgeDirection.getOrientation(0));
      }
   }

   public boolean canStabaliseInfusion(World world, int x, int y, int z) {
      return true;
   }
}
