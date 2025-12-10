package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.tiles.TileNode;

import java.util.List;
import java.util.Random;

public class BlockMagicalLog extends BlockRotatedPillar {
   public static final String[] woodType = new String[]{"greatwood", "silverwood", "silverwoodknot"};
   @SideOnly(Side.CLIENT)
   private IIcon[] tree_side;
   @SideOnly(Side.CLIENT)
   private IIcon[] tree_top;

   public BlockMagicalLog() {
      super(Material.wood);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHardness(2.5F);
      this.setStepSound(soundTypeWood);
   }

   @SideOnly(Side.CLIENT)
   protected IIcon getTopIcon(int par1) {
      return this.tree_top[par1 % this.tree_top.length];
   }

   @SideOnly(Side.CLIENT)
   protected IIcon getSideIcon(int i) {
      return this.tree_side[i % this.tree_side.length];
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.tree_side = new IIcon[woodType.length];
      this.tree_top = new IIcon[woodType.length];

      for(int i = 0; i < this.tree_side.length; ++i) {
         this.tree_side[i] = ir.registerIcon("thaumcraft:" + woodType[i] + "side");
         this.tree_top[i] = ir.registerIcon("thaumcraft:" + woodType[i] + "top");
      }

   }

   public int quantityDropped(Random par1Random) {
      return 1;
   }

//   public void breakBlock(World par1World, int x, int y, int z, int par5, int par6) {
//      byte b0 = 4;
//      int j1 = b0 + 1;
//      if (par1World.checkChunksExist(x - j1, y - j1, z - j1, x + j1, y + j1, z + j1)) {
//         for(int k1 = -b0; k1 <= b0; ++k1) {
//            for(int l1 = -b0; l1 <= b0; ++l1) {
//               for(int i2 = -b0; i2 <= b0; ++i2) {
//                  Block j2 = par1World.getBlock(x + k1, y + l1, z + i2);
//                  if (!j2.isAir(par1World, x + k1, y + l1, z + i2)) {
//                     j2.beginLeavesDecay(par1World, x + k1, y + l1, z + i2);
//                  }
//               }
//            }
//         }
//      }
//
//   }

   /**
    * Old breakBlock() does not override the correct method.
    */
   @Override
   public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
      byte b0 = 4;
      int i1 = b0 + 1;

      if (worldIn.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
         for (int j1 = -b0; j1 <= b0; ++j1) {
            for (int k1 = -b0; k1 <= b0; ++k1) {
               for (int l1 = -b0; l1 <= b0; ++l1) {
                  Block block = worldIn.getBlock(x + j1, y + k1, z + l1);
                  if (block.isLeaves(worldIn, x + j1, y + k1, z + l1)) {
                     block.beginLeavesDecay(worldIn, x + j1, y + k1, z + l1);
                  }
               }
            }
         }
      }
   }
   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      if (limitToValidMetadata(par5) == 2 && !par1World.isRemote) {
         TileEntity te = par1World.getTileEntity(par2, par3, par4);
         if (te instanceof INode && ((INode) te).getAspects().size() > 0) {
            for(Aspect aspect : ((INode)te).getAspects().getAspects()) {
               for(int a = 0; a <= ((INode)te).getAspects().getAmount(aspect) / 10; ++a) {
                  if (((INode)te).getAspects().getAmount(aspect) >= 5) {
                     ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
                     new AspectList();
                     ((ItemWispEssence)ess.getItem()).setAspects(ess, (new AspectList()).add(aspect, 2));
                     this.dropBlockAsItem(par1World, par2, par3, par4, ess);
                  }
               }
            }
         }
      }

      super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
   }

   public int damageDropped(int par1) {
      return (par1 & 3) == 2 ? 1 : par1 & 3;
   }

   public static int limitToValidMetadata(int par0) {
      return par0 & 3;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
   }

   public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public boolean isWood(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
//      if ((world.getBlockMetadata(x, y, z) & 2) == 1) {
//         return 7;
//      }
      return (world.getBlockMetadata(x, y, z) & 2) == 2 ? 7 : super.getLightValue(world, x, y, z);
   }

   public boolean hasTileEntity(int metadata) {
      return limitToValidMetadata(metadata) == 2 || super.hasTileEntity(metadata);
   }

   public TileEntity createTileEntity(World world, int metadata) {
      return limitToValidMetadata(metadata) == 2 ? new TileNode() : super.createTileEntity(world, metadata);
   }

   public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
      if (limitToValidMetadata(meta) == 2) {
         Thaumcraft.proxy.burst(world, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 1.0F);
         world.playSound((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:craftfail", 1.0F, 1.0F, false);
      }

      return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
   }

   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return 5;
   }

   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return 5;
   }
}
