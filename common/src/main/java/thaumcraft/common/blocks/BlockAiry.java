package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.nodes.INode;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSpark;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.projectile.EntityShockOrb;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.*;

import java.util.List;
import java.util.Random;

public class BlockAiry extends BlockContainer {
   public IIcon blankIcon;

   public BlockAiry() {
      super(Config.airyMaterial);
      this.setStepSound(new Block.SoundType("cloth", 0.0F, 1.0F));
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setTickRandomly(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.blankIcon = ir.registerIcon("thaumcraft:blank");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return this.blankIcon;
   }

   @SideOnly(Side.CLIENT)
   public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
      int md = worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ);
      if ((md == 0 || md == 5) && worldObj.rand.nextBoolean()) {
         UtilsFX.infusedStoneSparkle(worldObj, target.blockX, target.blockY, target.blockZ, 0);
      }

      return super.addHitEffects(worldObj, target, effectRenderer);
   }

   public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
      if (meta == 0 || meta == 5) {
         Thaumcraft.proxy.burst(world, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 1.0F);
         world.playSound((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:craftfail", 1.0F, 1.0F, false);
      }

      return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 0 && md != 5) {
         if (md != 10 && md != 11) {
            return md == 12 ? -1.0F : super.getBlockHardness(world, x, y, z);
         } else {
            return 100.0F;
         }
      } else {
         return 2.0F;
      }
   }

   public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 0 && md != 5) {
         if (md != 10 && md != 11) {
            return md == 12 ? Float.MAX_VALUE : super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
         } else {
            return 50.0F;
         }
      } else {
         return 200.0F;
      }
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 1 && md != 2 && md != 3) {
         if (md != 4 && md != 12) {
            return md != 0 && md != 5 && md != 10 && md != 11 ? super.getLightValue(world, x, y, z) : 8;
         } else {
            return 0;
         }
      } else {
         return 15;
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess ba, int x, int y, int z) {
      int md = ba.getBlockMetadata(x, y, z);
      if (md != 3 && md != 4 && md != 10 && md != 11 && md != 12) {
         this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      }

      super.setBlockBoundsBasedOnState(ba, x, y, z);
   }

   public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 2 || md == 3 || md == 4 || md == 10 || md == 11;
   }

   public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 2 || md == 3 || md == 4 || super.canBeReplacedByLeaves(world, x, y, z);
   }

   public boolean isLeaves(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 2 || md == 3 || super.isLeaves(world, x, y, z);
   }

   public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 4 && par7Entity instanceof EntityLivingBase && !(par7Entity instanceof EntityPlayer)) {
         int a = 1;
         if (world.getBlock(x, y - a, z) != ConfigBlocks.blockCosmeticSolid) {
            ++a;
         }

         if (!world.isBlockIndirectlyGettingPowered(x, y - a, z)) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, par6List, par7Entity);
         }
      } else if (metadata == 12) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, par6List, par7Entity);
      }

   }

   public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 4) {
         for(int a = 1; a < 3; ++a) {
            TileEntity te = world.getTileEntity(x, y - a, z);
            if (te instanceof TileWardingStone) {
               return te.getWorldObj().isBlockIndirectlyGettingPowered(x, y - a, z);
            }
         }
      }

      return true;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      return metadata != 4 && metadata != 12 ? null : super.getCollisionBoundingBoxFromPool(world, x, y, z);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      int md = par1World.getBlockMetadata(par2, par3, par4);
      return md != 0 && md != 2 && md != 3 && md != 4 && md != 5 && md != 10 && md != 11 && md != 12 ? super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4) : AxisAlignedBB.getBoundingBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
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
      return par1 == 1 ? ConfigItems.itemResource : Item.getItemById(0);
   }

   public Item getItem(World world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 1 ? ConfigItems.itemResource : Item.getItemById(0);
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      if (par5 == 0 && !par1World.isRemote) {
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

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World w, int i, int j, int k, Random r) {
      int md = w.getBlockMetadata(i, j, k);
      if (md == 1) {
         FXSparkle ef2 = new FXSparkle(w, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (float)i + 0.5F + (r.nextFloat() - r.nextFloat()) / 3.0F, (float)j + 0.5F + (r.nextFloat() - r.nextFloat()) / 3.0F, (float)k + 0.5F + (r.nextFloat() - r.nextFloat()) / 3.0F, 1.0F, 6, 3);
         ef2.setGravity(0.05F);
         ef2.noClip = true;
         ParticleEngine.instance.addEffect(w, ef2);
      } else if (md == 2 && r.nextInt(500) == 0) {
         int x1 = i + r.nextInt(3) - r.nextInt(3);
         int y1 = j + r.nextInt(3) - r.nextInt(3);
         int z1 = k + r.nextInt(3) - r.nextInt(3);
         int x2 = x1 + r.nextInt(3) - r.nextInt(3);
         int y2 = y1 + r.nextInt(3) - r.nextInt(3);
         int z2 = z1 + r.nextInt(3) - r.nextInt(3);
         Thaumcraft.proxy.wispFX3(w, x1, y1, z1, x2, y2, z2, 0.1F + r.nextFloat() * 0.1F, 7, false, r.nextBoolean() ? -0.033F : 0.033F);
      } else if (md == 10 || md == 11) {
         float h = r.nextFloat() * 0.33F;
         FXSpark ef = new FXSpark(w, (float)i + w.rand.nextFloat(), (float)j + 0.1515F + h / 2.0F, (float)k + w.rand.nextFloat(), 0.33F + h);
         if (md == 10) {
            ef.setRBGColorF(0.65F + w.rand.nextFloat() * 0.1F, 1.0F, 1.0F);
            ef.setAlphaF(0.8F);
         } else {
            ef.setRBGColorF(0.3F - w.rand.nextFloat() * 0.1F, 0.0F, 0.5F + w.rand.nextFloat() * 0.2F);
         }

         ParticleEngine.instance.addEffect(w, ef);
         if (r.nextInt(50) == 0) {
            w.playSound(i, j, k, "thaumcraft:jacobs", 0.5F, 1.0F + (r.nextFloat() - r.nextFloat()) * 0.2F, false);
         }
      }

   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileNode();
      } else if (metadata == 1) {
         return new TileNitor();
      } else if (metadata == 4) {
         return new TileWardingStoneFence();
      } else {
         return metadata == 5 ? new TileNodeEnergized() : super.createTileEntity(world, metadata);
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      if (stack.getItemDamage() == 0 && entity instanceof EntityPlayer) {
         ThaumcraftWorldGenerator.createRandomNodeAt(world, x, y, z, world.rand, false, false, false);
      }

      super.onBlockPlacedBy(world, x, y, z, entity, stack);
   }

   public boolean isAir(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 2 || md == 3 || md == 10 || md == 11;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 5) {
         TileEntity te = world.getTileEntity(x, y - 1, z);
         if (!world.isBlockIndirectlyGettingPowered(x, y - 1, z) && te instanceof TileNodeStabilizer) {
            te = world.getTileEntity(x, y + 1, z);
            if (!(te instanceof TileNodeConverter)) {
               explodify(world, x, y, z);
            }
         } else {
            explodify(world, x, y, z);
         }
      }

      super.onNeighborBlockChange(world, x, y, z, block);
   }

   public static void explodify(World world, int x, int y, int z) {
      if (!world.isRemote) {
         world.setBlockToAir(x, y, z);
         world.createExplosion(null, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 3.0F, false);

         for(int a = 0; a < 50; ++a) {
            int xx = x + world.rand.nextInt(8) - world.rand.nextInt(8);
            int yy = y + world.rand.nextInt(8) - world.rand.nextInt(8);
            int zz = z + world.rand.nextInt(8) - world.rand.nextInt(8);
            if (world.isAirBlock(xx, yy, zz)) {
               if (yy < y) {
                  world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGoo, 8, 3);
               } else {
                  world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGas, 8, 3);
               }
            }
         }
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 10 && entity instanceof EntityLivingBase && EntityShockOrb.canEarthShockHurt(entity)) {
         entity.attackEntityFrom(DamageSource.magic, (float)(1 + world.rand.nextInt(2)));
         entity.motionX *= 0.8;
         entity.motionZ *= 0.8;
         if (!world.isRemote && world.rand.nextInt(100) == 0) {
            world.setBlockToAir(x, y, z);
         }
      } else if (md == 11 && !(entity instanceof IEldritchMob)) {
         if (world.rand.nextInt(100) == 0) {
            entity.attackEntityFrom(DamageSource.wither, 1.0F);
         }

         entity.motionX *= 0.66;
         entity.motionZ *= 0.66;
         if (entity instanceof EntityPlayer) {
            ((EntityPlayer)entity).addExhaustion(0.05F);
         }

         if (entity instanceof EntityLivingBase) {
            PotionEffect pe = new PotionEffect(Potion.weakness.id, 100, 1, true);
            ((EntityLivingBase)entity).addPotionEffect(pe);
         }
      }

   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      super.updateTick(world, x, y, z, rand);
      int md = world.getBlockMetadata(x, y, z);
      if ((md == 10 || md == 11) && !world.isRemote) {
         world.setBlockToAir(x, y, z);
      }

   }
}
