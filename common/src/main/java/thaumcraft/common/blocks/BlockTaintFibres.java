package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityTaintSpore;
import thaumcraft.common.lib.CustomSoundType;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.List;
import java.util.Random;

public class BlockTaintFibres extends Block {
   public IIcon[] iconOver = new IIcon[4];
   public IIcon[] icon = new IIcon[5];
   protected int currentPass;

   public BlockTaintFibres() {
      super(Config.taintMaterial);
      this.setHardness(1.0F);
      this.setResistance(5.0F);
      this.setStepSound(new CustomSoundType("gore", 0.5F, 0.8F));
      this.setTickRandomly(true);
      this.setCreativeTab(Thaumcraft.tabTC);
      float f = 0.2F;
      this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
      this.currentPass = 1;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:taint_fibres");
      this.icon[1] = ir.registerIcon("thaumcraft:taintgrass1");
      this.icon[2] = ir.registerIcon("thaumcraft:taintgrass2");
      this.icon[3] = ir.registerIcon("thaumcraft:taint_spore_stalk_1");
      this.icon[4] = ir.registerIcon("thaumcraft:taint_spore_stalk_2");
      this.iconOver[0] = ir.registerIcon("thaumcraft:blank");

      for(int a = 1; a < 4; ++a) {
         this.iconOver[a] = ir.registerIcon("thaumcraft:taint_over_" + a);
      }

   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int var4 = 0; var4 <= 3; ++var4) {
         par3List.add(new ItemStack(par1, 1, var4));
      }

   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 2) {
         return 8;
      } else {
         return md == 4 ? 10 : super.getLightValue(world, x, y, z);
      }
   }

   @SideOnly(Side.CLIENT)
   public int getBlockColor() {
      double d0 = 0.5F;
      double d1 = 1.0F;
      return ColorizerGrass.getGrassColor(d0, d1);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(int par1) {
      return this.getBlockColor();
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int l = 0;
      int i1 = 0;
      int j1 = 0;

      for(int k1 = -1; k1 <= 1; ++k1) {
         for(int l1 = -1; l1 <= 1; ++l1) {
            int i2 = par1IBlockAccess.getBiomeGenForCoords(par2 + l1, par4 + k1).getBiomeGrassColor(par2, par3, par4);
            l += (i2 & 16711680) >> 16;
            i1 += (i2 & '\uff00') >> 8;
            j1 += i2 & 255;
         }
      }

      return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getOverlayBlockTexture(int x, int y, int z, int side) {
      Random r = new Random(side + y + (long) x * z);
      return r.nextInt(100) < 95 ? this.iconOver[0] : this.iconOver[r.nextInt(3) + 1];
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      return this.icon[par2];
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
      if (isOnlyAdjacentToTaint(world, x, y, z)) {
         world.setBlock(x, y, z, Blocks.air);
      }

      super.onNeighborBlockChange(world, x, y, z, par5);
   }

   public void updateTick(World world, int x, int y, int z, Random random) {
      if (!world.isRemote) {
         int md = world.getBlockMetadata(x, y, z);
         taintBiomeSpread(world, x, y, z, random, this);
         if (md == 0 && isOnlyAdjacentToTaint(world, x, y, z) || world.getBiomeGenForCoords(x, z).biomeID != Config.biomeTaintID) {
            world.setBlock(x, y, z, Blocks.air);
            return;
         }

         int xx = x + random.nextInt(3) - 1;
         int yy = y + random.nextInt(5) - 3;
         int zz = z + random.nextInt(3) - 1;
         if (world.getBiomeGenForCoords(xx, zz).biomeID == Config.biomeTaintID) {
            Block bi = world.getBlock(xx, yy, zz);
            if (!spreadFibres(world, xx, yy, zz)) {
               int adjacentTaint = getAdjacentTaint(world, xx, yy, zz);
               Material bm = world.getBlock(xx, yy, zz).getMaterial();
               if (adjacentTaint >= 2 && (Utils.isWoodLog(world, xx, yy, zz) || bm == Material.gourd || bm == Material.cactus)) {
                  world.setBlock(xx, yy, zz, ConfigBlocks.blockTaint, 0, 3);
                  world.addBlockEvent(xx, yy, zz, ConfigBlocks.blockTaint, 1, 0);
               }

               if (adjacentTaint >= 3 && bi != Blocks.air && (bm == Material.sand || bm == Material.ground || bm == Material.grass || bm == Material.clay)) {
                  world.setBlock(xx, yy, zz, ConfigBlocks.blockTaint, 1, 3);
                  world.addBlockEvent(xx, yy, zz, ConfigBlocks.blockTaint, 1, 0);
               }

               if (md == 3 && Config.spawnTaintSpore && random.nextInt(10) == 0 && world.isAirBlock(x, y + 1, z)) {
                  world.setBlockMetadataWithNotify(x, y, z, 4, 3);
                  EntityTaintSpore spore = new EntityTaintSpore(world);
                  spore.setLocationAndAngles((float)x + 0.5F, y + 1, (float)z + 0.5F, 0.0F, 0.0F);
                  world.spawnEntityInWorld(spore);
               } else if (md == 4) {
                  List<Entity> targets = world.getEntitiesWithinAABB(EntityTaintSpore.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));
                  if (targets.isEmpty()) {
                     world.setBlockMetadataWithNotify(x, y, z, 3, 3);
                  }
               }
            }
         }
      }

   }

   public static boolean spreadFibres(World world, int x, int y, int z) {
      Block bi = world.getBlock(x, y, z);
      if (BlockUtils.isAdjacentToSolidBlock(world, x, y, z) && !isOnlyAdjacentToTaint(world, x, y, z) && !world.getBlock(x, y, z).getMaterial().isLiquid() && (world.isAirBlock(x, y, z) || bi.isReplaceable(world, x, y, z) || bi instanceof BlockFlower || bi.isLeaves(world, x, y, z))) {
         if (world.rand.nextInt(10) == 0 && world.isAirBlock(x, y + 1, z) && world.isSideSolid(x, y - 1, z, ForgeDirection.UP)) {
            if (world.rand.nextInt(10) < 9) {
               world.setBlock(x, y, z, ConfigBlocks.blockTaintFibres, 1, 3);
            } else if (world.rand.nextInt(12) < 10) {
               world.setBlock(x, y, z, ConfigBlocks.blockTaintFibres, 2, 3);
            } else {
               world.setBlock(x, y, z, ConfigBlocks.blockTaintFibres, 3, 3);
            }
         } else {
            world.setBlock(x, y, z, ConfigBlocks.blockTaintFibres, 0, 3);
         }

         world.addBlockEvent(x, y, z, ConfigBlocks.blockTaintFibres, 1, 0);
         return true;
      } else {
         return false;
      }
   }

   public static void taintBiomeSpread(World world, int x, int y, int z, Random rand, Block block) {
      if (Config.taintSpreadRate > 0) {
         int xx = rand.nextInt(3) - 1;
         int zz = rand.nextInt(3) - 1;
         if (world.getBiomeGenForCoords(x + xx, z + zz).biomeID != Config.biomeTaintID && rand.nextInt(Config.taintSpreadRate * 5) == 0 && getAdjacentTaint(world, x, y, z) >= 2) {
            Utils.setBiomeAt(world, x + xx, z + zz, ThaumcraftWorldGenerator.biomeTaint);
            world.addBlockEvent(x, y, z, block, 1, 0);
         }
      }

   }

   public static int getAdjacentTaint(IBlockAccess world, int x, int y, int z) {
      int count = 0;

      for(int a = 0; a < 6; ++a) {
         ForgeDirection d = ForgeDirection.getOrientation(a);
         int xx = x + d.offsetX;
         int yy = y + d.offsetY;
         int zz = z + d.offsetZ;
         Block bi = world.getBlock(xx, yy, zz);
         if (bi == ConfigBlocks.blockTaint || bi == ConfigBlocks.blockTaintFibres) {
            ++count;
         }
      }

      return count;
   }

   public static boolean isOnlyAdjacentToTaint(World world, int x, int y, int z) {
      for(int a = 0; a < 6; ++a) {
         ForgeDirection d = ForgeDirection.getOrientation(a);
         int xx = x + d.offsetX;
         int yy = y + d.offsetY;
         int zz = z + d.offsetZ;
         world.getBlock(xx, yy, zz);
         if (!world.isAirBlock(xx, yy, zz) && world.getBlock(xx, yy, zz).getMaterial() != Config.taintMaterial) {
            return false;
         }
      }

      return true;
   }

   public Item getItemDropped(int md, Random rand, int fortune) {
      return Item.getItemById(0);
   }

   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      world.getBlockMetadata(i, j, k);
      if (!world.isRemote && entity instanceof EntityLivingBase && !((EntityLivingBase)entity).isEntityUndead()) {
         if (entity instanceof EntityPlayer && world.rand.nextInt(1000) == 0) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 80, 0, false));
         } else if (!(entity instanceof EntityPlayer) && world.rand.nextInt(500) == 0) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 160, 0, false));
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int cd) {
      if (id == 1) {
         if (world.isRemote) {
            world.playSound(x, y, z, "thaumcraft:roots", 0.1F, 0.9F + world.rand.nextFloat() * 0.2F, false);
         }

         return true;
      } else {
         return super.onBlockEventReceived(world, x, y, z, id, cd);
      }
   }

   public int getRenderType() {
      return ConfigBlocks.blockTaintFibreRI;
   }

   public boolean canRenderInPass(int pass) {
      return pass == 1;
   }

   public int getRenderBlockPass() {
      return 1;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
      return true;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 0) {
         float f = 0.0625F;

         try {
            for(int a = 0; a < 6; ++a) {
               ForgeDirection side = ForgeDirection.getOrientation(a);
               if (world.isSideSolid(x + side.offsetX, y + side.offsetY, z + side.offsetZ, side.getOpposite(), false)) {
                  switch (a) {
                     case 0:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
                        break;
                     case 1:
                        this.setBlockBounds(0.0F, 1.0F - f, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;
                     case 2:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
                        break;
                     case 3:
                        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
                        break;
                     case 4:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
                        break;
                     case 5:
                        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         } catch (Throwable ignored) {
         }

         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
      } else {
         this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.8F, 0.8F);
      }

   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return false;
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      boolean biome = par1World.getBiomeGenForCoords(par2, par4).biomeID == Config.biomeTaintID;
      return biome && super.canPlaceBlockAt(par1World, par2, par3, par4);
   }
}
