package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.TileArcaneFurnace;
import thaumcraft.common.tiles.TileArcaneFurnaceNozzle;

import java.util.List;
import java.util.Random;

public class BlockArcaneFurnace extends BlockContainer {
   public IIcon[] icon = new IIcon[27];

   public BlockArcaneFurnace() {
      super(Material.rock);
      this.setHardness(10.0F);
      this.setResistance(500.0F);
      this.setLightLevel(0.2F);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void registerBlockIcons(IIconRegister ir) {
      for(int a = 0; a < 27; ++a) {
         if (a != 8 && a != 24) {
            this.icon[a] = ir.registerIcon("thaumcraft:furnace" + a);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      return this.calculateTexture(world, x, y, z, side);
   }

   @SideOnly(Side.CLIENT)
   public IIcon calculateTexture(IBlockAccess world, int x, int y, int z, int side) {
      int meta = world.getBlockMetadata(x, y, z);
      int level = this.calculateLevel(world, x, y, z);
      int add = 0;
      if (BlockUtils.isBlockTouchingOnSide(world, x, y, z, this, 10, side)) {
         add = 3;
      }

      switch (side) {
         case 0:
         case 1:
            if (side == 1 && level == 18) {
               switch (meta) {
                  case 2:
                     return this.icon[16];
                  case 4:
                     return this.icon[17];
                  case 6:
                     return this.icon[26];
                  case 8:
                     return this.icon[25];
                  default:
                     break;
               }
            }

            if (add != 3) {
               if (meta == 5) {
                  return this.icon[10];
               } else {
                  if ((meta - 1) % 3 + (meta - 1) / 3 * 9 < 0) {
                     return null;
                  }

                  return this.icon[(meta - 1) % 3 + (meta - 1) / 3 * 9];
               }
            }
         case 2:
            switch (meta) {
               case 1:
                  return this.icon[2 + level + add];
               case 2:
                  return this.icon[1 + level + add];
               case 3:
                  return this.icon[level + add];
               default:
                  if (level != 9) {
                     return this.icon[7];
                  }

                  return this.icon[6];
            }
         case 3:
            switch (meta) {
               case 7:
                  return this.icon[level + add];
               case 8:
                  return this.icon[1 + level + add];
               case 9:
                  return this.icon[2 + level + add];
               default:
                  if (level != 9) {
                     return this.icon[7];
                  }

                  return this.icon[6];
            }
         case 4:
            switch (meta) {
               case 1:
                  return this.icon[level + add];
               case 4:
                  return this.icon[1 + level + add];
               case 7:
                  return this.icon[2 + level + add];
               default:
                  if (level != 9) {
                     return this.icon[7];
                  }

                  return this.icon[6];
            }
         case 5:
            switch (meta) {
               case 3:
                  return this.icon[2 + level + add];
               case 6:
                  return this.icon[1 + level + add];
               case 9:
                  return this.icon[level + add];
               default:
                  return level != 9 ? this.icon[7] : this.icon[6];
            }
         default:
            return add == 0 ? this.icon[7] : this.icon[6];
      }
   }

   public int calculateLevel(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      int metaA = world.getBlockMetadata(x, y + 1, z);
      if (metaA == 10 || metaA == 0) {
         metaA = meta;
      }

      int metaB = world.getBlockMetadata(x, y - 1, z);
      if (metaB == 10 || metaB == 0) {
         metaB = meta;
      }

      Block blockA = world.getBlock(x, y + 1, z);
      Block blockB = world.getBlock(x, y - 1, z);
      if (meta == metaA && meta == metaB && this == blockA && this == blockB) {
         return 9;
      } else {
         return meta != metaA || this != blockA || meta == metaB && this == blockB ? 0 : 18;
      }
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta != 0 && meta != 10 ? super.getLightValue(world, x, y, z) : 13;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
      int meta = w.getBlockMetadata(i, j, k);
      return meta == 0 ? AxisAlignedBB.getBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F) : AxisAlignedBB.getBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      int md = world.getBlockMetadata(i, j, k);
      if (md == 10) {
         if (world.getBlock(i - 1, j, k) == this && world.getBlockMetadata(i - 1, j, k) == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
         } else if (world.getBlock(i + 1, j, k) == this && world.getBlockMetadata(i + 1, j, k) == 0) {
            this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         } else if (world.getBlock(i, j, k - 1) == this && world.getBlockMetadata(i, j, k - 1) == 0) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
         }

         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else if (md != 0) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 0) {
         if (entity.posX < (double)((float)x + 0.3F)) {
            entity.motionX += 1.0E-4F;
         }

         if (entity.posX > (double)((float)x + 0.7F)) {
            entity.motionX -= 1.0E-4F;
         }

         if (entity.posZ < (double)((float)z + 0.3F)) {
            entity.motionZ += 1.0E-4F;
         }

         if (entity.posZ > (double)((float)z + 0.7F)) {
            entity.motionZ -= 1.0E-4F;
         }

         if (entity instanceof EntityItem) {
            entity.motionY = 0.025F;
            if (entity.onGround) {
               TileArcaneFurnace taf = (TileArcaneFurnace)world.getTileEntity(x, y, z);
               if (taf.addItemsToInventory(((EntityItem)entity).getEntityItem())) {
                  entity.setDead();
               }
            }
         } else if (entity instanceof EntityLivingBase && !entity.isImmuneToFire()) {
            entity.attackEntityFrom(DamageSource.lava, 3.0F);
            entity.setFire(10);
         }
      }

   }

   private void restoreBlocks(World par1World, int par2, int par3, int par4) {
      for(int yy = -1; yy <= 1; ++yy) {
         for(int xx = -1; xx <= 1; ++xx) {
            for(int zz = -1; zz <= 1; ++zz) {
               Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
               int md = par1World.getBlockMetadata(par2 + xx, par3 + yy, par4 + zz);
               if (block == this) {
                  block = Block.getBlockFromItem(this.getItemDropped(md, new Random(), 0));
                  par1World.setBlock(par2 + xx, par3 + yy, par4 + zz, block, 0, 3);
                  par1World.notifyBlocksOfNeighborChange(par2 + xx, par3 + yy, par4 + zz, par1World.getBlock(par2 + xx, par3 + yy, par4 + zz));
                  par1World.markBlockForUpdate(par2 + xx, par3 + yy, par4 + zz);
               }
            }
         }
      }

   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      int meta = par1World.getBlockMetadata(par2, par3, par4);
      if (meta == 0) {
         for(int yy = -1; yy <= 1; ++yy) {
            for(int xx = -1; xx <= 1; ++xx) {
               for(int zz = -1; zz <= 1 && (yy != 1 && yy != 0 || zz != 0 || xx != 0); ++zz) {
                  Block block = par1World.getBlock(par2 + xx, par3 + yy, par4 + zz);
                  if (block != this) {
                     this.restoreBlocks(par1World, par2, par3, par4);
                     par1World.setBlockToAir(par2, par3, par4);
                     par1World.notifyBlocksOfNeighborChange(par2, par3, par4, par1World.getBlock(par2, par3, par4));
                     par1World.markBlockForUpdate(par2, par3, par4);
                     return;
                  }
               }
            }
         }
      }

      super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
   }

   public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
      if (meta == 0 && !world.isRemote) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileArcaneFurnace) {
            Entity newentity = EntityList.createEntityByName("Blaze", world);
            newentity.setLocationAndAngles((float)x + 0.5F, (float)y + 1.0F, (float)z + 0.5F, 0.0F, 0.0F);
            ((EntityLivingBase)newentity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 6000, 2));
            ((EntityLivingBase)newentity).addPotionEffect(new PotionEffect(Potion.resistance.id, 12000, 0));
            world.spawnEntityInWorld(newentity);
         }
      }

      super.onBlockPreDestroy(world, x, y, z, meta);
   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
         this.restoreBlocks(par1World, par2, par3, par4);
      }

      for(int yy = -1; yy <= 1; ++yy) {
         for(int xx = -1; xx <= 1; ++xx) {
            for(int zz = -1; zz <= 1; ++zz) {
               par1World.notifyBlocksOfNeighborChange(par2 + xx, par3 + yy, par4 + zz, this);
            }
         }
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public Item getItemDropped(int meta, Random par2Random, int par3) {
      return meta == 0 ? Item.getItemById(0) : (meta == 10 ? Item.getItemFromBlock(Blocks.iron_bars) : (meta % 2 != 0 && meta != 5 ? Item.getItemFromBlock(Blocks.nether_brick) : Item.getItemFromBlock(Blocks.obsidian)));
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return world.getBlockMetadata(x, y, z) != 0;
   }

   public int getRenderType() {
      return ConfigBlocks.blockArcaneFurnaceRI;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (par1World.getBlockMetadata(par2, par3, par4) == 0 && par1World.getBlock(par2, par3 + 1, par4).getMaterial() == Material.air && !par1World.getBlock(par2, par3 + 1, par4).isOpaqueCube()) {
         for(int a = 0; a < 3; ++a) {
            double var7 = (float)par2 + par5Random.nextFloat();
            double var8 = (float)par3 + 1.0F + par5Random.nextFloat() * 0.5F;
            double var9 = (float)par4 + par5Random.nextFloat();
            par1World.spawnParticle("largesmoke", var7, var8, var9, 0.0F, 0.0F, 0.0F);
         }
      }

   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileArcaneFurnace();
      } else {
         return metadata != 2 && metadata != 4 && metadata != 5 && metadata != 6 && metadata != 8 ? super.createTileEntity(world, metadata) : new TileArcaneFurnaceNozzle();
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
      if (par5 == 1) {
         if (par1World.isRemote) {
            Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, 16736256, 5);
         }

         return true;
      } else {
         return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
      }
   }
}
