package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileOwned;

import java.util.List;
import java.util.Random;

public class BlockCosmeticOpaque extends BlockContainer {
   public IIcon[] icon = new IIcon[3];
   public static IIcon[] wardedGlassIcon = new IIcon[47];
   public int currentPass;

   public BlockCosmeticOpaque() {
      super(Material.rock);
      this.setResistance(5.0F);
      this.setHardness(1.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:amberblock");
      this.icon[1] = ir.registerIcon("thaumcraft:amberbrick");
      this.icon[2] = ir.registerIcon("thaumcraft:amberblock_top");

      for(int a = 0; a < 47; ++a) {
         wardedGlassIcon[a] = ir.registerIcon("thaumcraft:warded_glass_" + (a + 1));
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      if (par2 == 0 && par1 < 2) {
         return this.icon[2];
      } else {
         return par2 == 2 ? wardedGlassIcon[0] : this.icon[par2];
      }
   }

   public int getRenderType() {
      return ConfigBlocks.blockCosmeticOpaqueRI;
   }

   public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
      int md = worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ);
      if (md == 2) {
         float f = (float)target.hitVec.xCoord - (float)target.blockX;
         float f1 = (float)target.hitVec.yCoord - (float)target.blockY;
         float f2 = (float)target.hitVec.zCoord - (float)target.blockZ;
         Thaumcraft.proxy.blockWard(worldObj, target.blockX, target.blockY, target.blockZ, ForgeDirection.getOrientation(target.sideHit), f, f1, f2);
         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 2) {
         return super.getIcon(world, x, y, z, side);
      } else {
         boolean[] bitMatrix = new boolean[8];
         if (side == 0 || side == 1) {
            bitMatrix[0] = world.getBlock(x - 1, y, z - 1) == this && world.getBlockMetadata(x - 1, y, z - 1) == 2;
            bitMatrix[1] = world.getBlock(x, y, z - 1) == this && world.getBlockMetadata(x, y, z - 1) == 2;
            bitMatrix[2] = world.getBlock(x + 1, y, z - 1) == this && world.getBlockMetadata(x + 1, y, z - 1) == 2;
            bitMatrix[3] = world.getBlock(x - 1, y, z) == this && world.getBlockMetadata(x - 1, y, z) == 2;
            bitMatrix[4] = world.getBlock(x + 1, y, z) == this && world.getBlockMetadata(x + 1, y, z) == 2;
            bitMatrix[5] = world.getBlock(x - 1, y, z + 1) == this && world.getBlockMetadata(x - 1, y, z + 1) == 2;
            bitMatrix[6] = world.getBlock(x, y, z + 1) == this && world.getBlockMetadata(x, y, z + 1) == 2;
            bitMatrix[7] = world.getBlock(x + 1, y, z + 1) == this && world.getBlockMetadata(x + 1, y, z + 1) == 2;
         }

         if (side == 2 || side == 3) {
            bitMatrix[0] = world.getBlock(x + (side == 2 ? 1 : -1), y + 1, z) == this && world.getBlockMetadata(x + (side == 2 ? 1 : -1), y + 1, z) == 2;
            bitMatrix[1] = world.getBlock(x, y + 1, z) == this && world.getBlockMetadata(x, y + 1, z) == 2;
            bitMatrix[2] = world.getBlock(x + (side == 3 ? 1 : -1), y + 1, z) == this && world.getBlockMetadata(x + (side == 3 ? 1 : -1), y + 1, z) == 2;
            bitMatrix[3] = world.getBlock(x + (side == 2 ? 1 : -1), y, z) == this && world.getBlockMetadata(x + (side == 2 ? 1 : -1), y, z) == 2;
            bitMatrix[4] = world.getBlock(x + (side == 3 ? 1 : -1), y, z) == this && world.getBlockMetadata(x + (side == 3 ? 1 : -1), y, z) == 2;
            bitMatrix[5] = world.getBlock(x + (side == 2 ? 1 : -1), y - 1, z) == this && world.getBlockMetadata(x + (side == 2 ? 1 : -1), y - 1, z) == 2;
            bitMatrix[6] = world.getBlock(x, y - 1, z) == this && world.getBlockMetadata(x, y - 1, z) == 2;
            bitMatrix[7] = world.getBlock(x + (side == 3 ? 1 : -1), y - 1, z) == this && world.getBlockMetadata(x + (side == 3 ? 1 : -1), y - 1, z) == 2;
         }

         if (side == 4 || side == 5) {
            bitMatrix[0] = world.getBlock(x, y + 1, z + (side == 5 ? 1 : -1)) == this && world.getBlockMetadata(x, y + 1, z + (side == 5 ? 1 : -1)) == 2;
            bitMatrix[1] = world.getBlock(x, y + 1, z) == this && world.getBlockMetadata(x, y + 1, z) == 2;
            bitMatrix[2] = world.getBlock(x, y + 1, z + (side == 4 ? 1 : -1)) == this && world.getBlockMetadata(x, y + 1, z + (side == 4 ? 1 : -1)) == 2;
            bitMatrix[3] = world.getBlock(x, y, z + (side == 5 ? 1 : -1)) == this && world.getBlockMetadata(x, y, z + (side == 5 ? 1 : -1)) == 2;
            bitMatrix[4] = world.getBlock(x, y, z + (side == 4 ? 1 : -1)) == this && world.getBlockMetadata(x, y, z + (side == 4 ? 1 : -1)) == 2;
            bitMatrix[5] = world.getBlock(x, y - 1, z + (side == 5 ? 1 : -1)) == this && world.getBlockMetadata(x, y - 1, z + (side == 5 ? 1 : -1)) == 2;
            bitMatrix[6] = world.getBlock(x, y - 1, z) == this && world.getBlockMetadata(x, y - 1, z) == 2;
            bitMatrix[7] = world.getBlock(x, y - 1, z + (side == 4 ? 1 : -1)) == this && world.getBlockMetadata(x, y - 1, z + (side == 4 ? 1 : -1)) == 2;
         }

         int idBuilder = 0;

         for(int i = 0; i <= 7; ++i) {
            idBuilder += bitMatrix[i] ? (i == 0 ? 1 : (i == 1 ? 2 : (i == 2 ? 4 : (i == 3 ? 8 : (i == 4 ? 16 : (i == 5 ? 32 : (i == 6 ? 64 : 128))))))) : 0;
         }

         return idBuilder <= 255 && idBuilder >= 0 ? wardedGlassIcon[UtilsFX.connectedTextureRefByID[idBuilder]] : wardedGlassIcon[0];
      }
   }

   public int getRenderBlockPass() {
      return 1;
   }

   public boolean canRenderInPass(int pass) {
      this.currentPass = pass;
      return pass == 1 || pass == 0;
   }

   public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md <= 1 ? 3 : super.getLightOpacity(world, x, y, z);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
   }

   public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      Block block = world.getBlock(x, y, z);
      if (world.getBlockMetadata(x, y, z) != world.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side])) {
         return true;
      } else {
         return block != this && super.shouldSideBeRendered(world, x, y, z, side);
      }
   }

   public int quantityDropped(Random par1Random) {
      return 1;
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public TileEntity createTileEntity(World world, int metadata) {
      return metadata == 2 ? new TileOwned() : super.createTileEntity(world, metadata);
   }

   public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
      int md = world.getBlockMetadata(x, y, z);
      return md != 2 && super.canEntityDestroy(world, x, y, z, entity);
   }

   public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 2) {
         super.onBlockExploded(world, x, y, z, explosion);
      }

   }

   public boolean canDropFromExplosion(Explosion explosion) {
      return false;
   }

   public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase p, ItemStack is) {
      TileEntity tile = w.getTileEntity(x, y, z);
      if (tile instanceof TileOwned && p instanceof EntityPlayer) {
         ((TileOwned)tile).owner = p.getCommandSenderName();
         tile.markDirty();
      }

      super.onBlockPlacedBy(w, x, y, z, p, is);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return null;
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 2) {
         return Config.wardedStone ? -1.0F : 5.0F;
      } else {
         return super.getBlockHardness(world, x, y, z);
      }
   }

   public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 2 ? 999.0F : super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
   }
}
