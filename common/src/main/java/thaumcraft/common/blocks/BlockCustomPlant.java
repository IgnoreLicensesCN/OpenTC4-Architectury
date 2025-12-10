package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.world.WorldGenGreatwoodTrees;
import thaumcraft.common.lib.world.WorldGenSilverwoodTrees;
import thaumcraft.common.tiles.TileEtherealBloom;

import java.util.List;
import java.util.Random;

public class BlockCustomPlant extends BlockBush {
   public IIcon[] icon = new IIcon[6];
   public IIcon iconLeaves;
   public IIcon iconStalk;
   IIcon blank;

   public BlockCustomPlant() {
      super(Material.plants);
      this.setStepSound(soundTypeGrass);
      float var3 = 0.4F;
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:greatwoodsapling");
      this.icon[1] = ir.registerIcon("thaumcraft:silverwoodsapling");
      this.icon[2] = ir.registerIcon("thaumcraft:shimmerleaf");
      this.icon[3] = ir.registerIcon("thaumcraft:cinderpearl");
      this.icon[4] = ir.registerIcon("thaumcraft:purifier_seed");
      this.icon[5] = ir.registerIcon("thaumcraft:manashroom");
      this.iconLeaves = ir.registerIcon("thaumcraft:purifier_leaves");
      this.iconStalk = ir.registerIcon("thaumcraft:purifier_stalk");
      this.blank = ir.registerIcon("thaumcraft:blank");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      if (par2 == 4 && par1 == 0) {
         return this.blank;
      } else {
         return par2 < this.icon.length ? this.icon[par2] : null;
      }
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int var4 = 0; var4 <= 5; ++var4) {
         par3List.add(new ItemStack(par1, 1, var4));
      }

   }

   public boolean hasTileEntity(int metadata) {
      return metadata == 4 || super.hasTileEntity(metadata);
   }

   public TileEntity createTileEntity(World world, int metadata) {
      return metadata == 4 ? new TileEtherealBloom() : super.createTileEntity(world, metadata);
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      return Item.getItemFromBlock(this);
   }

   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 3) {
         return EnumPlantType.Desert;
      } else {
         return md == 4 ? EnumPlantType.Cave : EnumPlantType.Plains;
      }
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return true;
   }

   public void updateTick(World world, int i, int j, int k, Random random) {
      if (!world.isRemote) {
         super.updateTick(world, i, j, k, random);
         int l = world.getBlockMetadata(i, j, k);
         if (l == 0 && world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(25) == 0) {
            this.growGreatTree(world, i, j, k, random);
         } else if (l == 1 && world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(50) == 0) {
            this.growSilverTree(world, i, j, k, random);
         }
      }

   }

   public void growGreatTree(World world, int i, int j, int k, Random random) {
      if (world != null && world.provider != null) {
         if (!world.isRemote) {
            world.setBlockToAir(i, j, k);
            WorldGenGreatwoodTrees obj = new WorldGenGreatwoodTrees(true);
            if (!obj.generate(world, random, i, j, k, false)) {
               world.setBlock(i, j, k, this, 0, 0);
            }

         }
      }
   }

   public void growSilverTree(World world, int i, int j, int k, Random random) {
      if (world != null && world.provider != null) {
         if (!world.isRemote) {
            world.setBlockToAir(i, j, k);
            WorldGenSilverwoodTrees obj = new WorldGenSilverwoodTrees(true, 7, 5);
            if (!obj.generate(world, random, i, j, k)) {
               world.setBlock(i, j, k, this, 1, 0);
            }

         }
      }
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 1 && md != 2 && md != 3 && md != 5) {
         return md == 4 ? 15 : super.getLightValue(world, x, y, z);
      } else {
         return 8;
      }
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 5 && entity instanceof EntityLivingBase) {
         ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
      }

      super.onEntityCollidedWithBlock(world, x, y, z, entity);
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      int md = world.getBlockMetadata(i, j, k);
      if (md == 2 && random.nextInt(3) == 0) {
         float cr = 0.3F + world.rand.nextFloat() * 0.3F;
         float cg = 0.7F + world.rand.nextFloat() * 0.3F;
         float cb = 0.7F + world.rand.nextFloat() * 0.3F;
         float xr = (float)i + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
         float yr = (float)j + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.15F;
         float zr = (float)k + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
         FXWisp ef = new FXWisp(world, xr, yr, zr, 0.2F, cr, cg, cb);
         ef.tinkle = false;
         ParticleEngine.instance.addEffect(world, ef);
      } else if (md == 3 && random.nextBoolean()) {
         float xr = (float)i + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
         float yr = (float)j + 0.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
         float zr = (float)k + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
         world.spawnParticle("smoke", xr, yr, zr, 0.0F, 0.0F, 0.0F);
         world.spawnParticle("flame", xr, yr, zr, 0.0F, 0.0F, 0.0F);
      } else if (md == 5 && random.nextInt(3) == 0) {
         float xr = (float)i + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F;
         float yr = (float)j + 0.3F;
         float zr = (float)k + 0.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F;
         FXWisp ef = new FXWisp(world, xr, yr, zr, 0.1F, 0.5F, 0.3F, 0.8F);
         ef.tinkle = false;
         ef.shrink = true;
         ef.setGravity(0.015F);
         ParticleEngine.instance.addEffect(world, ef);
      }

   }

   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return 100;
   }

   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return 60;
   }
}
