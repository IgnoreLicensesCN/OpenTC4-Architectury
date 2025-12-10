package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSpark;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemEldritchObject;
import thaumcraft.common.tiles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockEldritch extends BlockContainer {
   public IIcon icon = null;
   public IIcon[] insIcon = new IIcon[9];
   private Random rand = new Random();

   public BlockEldritch() {
      super(Material.rock);
      this.setResistance(20000.0F);
      this.setHardness(50.0F);
      this.setStepSound(soundTypeStone);
      this.setTickRandomly(true);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setLightOpacity(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:obsidiantile");
      this.insIcon[0] = ir.registerIcon("thaumcraft:es_i_1");
      this.insIcon[1] = ir.registerIcon("thaumcraft:es_i_2");
      this.insIcon[2] = ir.registerIcon("thaumcraft:deco_1");
      this.insIcon[3] = ir.registerIcon("thaumcraft:deco_2");
      this.insIcon[4] = ir.registerIcon("thaumcraft:deco_3");
      this.insIcon[5] = ir.registerIcon("thaumcraft:es_5");
      this.insIcon[6] = ir.registerIcon("thaumcraft:es_6");
      this.insIcon[7] = ir.registerIcon("thaumcraft:es_7");
      this.insIcon[8] = ir.registerIcon("thaumcraft:es_8");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return meta == 4 ? this.insIcon[0] : (meta == 5 ? this.insIcon[1] : (meta == 6 ? this.insIcon[2] : (meta == 7 ? this.insIcon[4] : (meta == 8 ? this.insIcon[3] : (meta == 9 ? ConfigBlocks.blockCosmeticSolid.getIcon(side, 14) : (meta == 10 ? this.insIcon[5] : this.icon))))));
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess ba, int x, int y, int z, int side) {
      int md = ba.getBlockMetadata(x, y, z);
      if (md == 8) {
         TileEntity te = ba.getTileEntity(x, y, z);
         return te instanceof TileEldritchLock && ((TileEldritchLock)te).getFacing() == side ? this.insIcon[3] : this.insIcon[4];
      } else if (md == 10) {
         String l = x + "" + y + z;
         Random r1 = new Random(Math.abs(l.hashCode() * 100) + 1);
         int i = r1.nextInt(12345 + side) % 4;
         return this.insIcon[5 + i];
      } else {
         return super.getIcon(ba, x, y, z, side);
      }
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 4));
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta != 4 && meta != 5 && meta != 7) {
         if (meta != 6 && meta != 8) {
            if (meta == 9) {
               return 4;
            } else {
               return meta == 10 ? 0 : 8;
            }
         } else {
            return 5;
         }
      } else {
         return 12;
      }
   }

   public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
      return false;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.setBlockBoundsBasedOnState(world, i, j, k);
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
   }

   public boolean hasTileEntity(int metadata) {
      return metadata == 0 || metadata == 1 || metadata == 3 || metadata == 8 || metadata == 9 || metadata == 10;
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileEldritchAltar();
      } else if (metadata == 1) {
         return new TileEldritchObelisk();
      } else if (metadata == 3) {
         return new TileEldritchCap();
      } else if (metadata == 8) {
         return new TileEldritchLock();
      } else if (metadata == 9) {
         return new TileEldritchCrabSpawner();
      } else {
         return metadata == 10 ? new TileEldritchTrap() : null;
      }
   }

   public int getRenderType() {
      return ConfigBlocks.blockEldritchRI;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public Item getItemDropped(int md, Random rand, int fortune) {
      return md == 4 ? Item.getItemFromBlock(this) : (md == 5 ? ConfigItems.itemResource : Item.getItemById(0));
   }

   public int damageDropped(int metadata) {
      return metadata == 2 ? 1 : metadata;
   }

   public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
      if (metadata != 5 && metadata != 10) {
         return metadata == 9 ? MathHelper.getRandomIntegerInRange(this.rand, 6, 10) : super.getExpDrop(world, metadata, fortune);
      } else {
         return MathHelper.getRandomIntegerInRange(this.rand, 1, 4);
      }
   }

   public ArrayList getDrops(World world, int x, int y, int z, int md, int fortune) {
      ArrayList<ItemStack> ret = new ArrayList<>();
      if (md == 5) {
         ret.add(new ItemStack(ConfigItems.itemResource, 1, 9));
         return ret;
      } else {
         return super.getDrops(world, x, y, z, md, fortune);
      }
   }

   public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
      if (!world.isRemote && meta < 4) {
         for(int xx = x - 3; xx <= x + 3; ++xx) {
            for(int yy = y - 2; yy <= y + 2; ++yy) {
               for(int zz = z - 3; zz <= z + 3; ++zz) {
                  if (world.getBlock(xx, yy, zz) == this && world.getBlockMetadata(xx, yy, zz) < 4) {
                     world.setBlockToAir(xx, yy, zz);
                  }
               }
            }
         }

         world.createExplosion(null, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 1.0F, false);
      }

      super.breakBlock(world, x, y, z, block, meta);
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta != 4 && meta != 5) {
         if (meta == 6) {
            return 4.0F;
         } else if (meta != 7 && meta != 8) {
            return meta != 9 && meta != 10 ? super.getBlockHardness(world, x, y, z) : 15.0F;
         } else {
            return -1.0F;
         }
      } else {
         return 2.0F;
      }
   }

   public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta != 4 && meta != 5 && meta != 9 && meta != 10) {
         if (meta == 6) {
            return 100.0F;
         } else {
            return meta != 7 && meta != 8 ? super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ) : Float.MAX_VALUE;
         }
      } else {
         return 30.0F;
      }
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 0 && !world.isRemote && !player.isSneaking() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemEldritchObject && player.getHeldItem().getItemDamage() == 0) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileEldritchAltar) {
            TileEldritchAltar tile = (TileEldritchAltar)te;
            if (tile.getEyes() < 4) {
               if (tile.getEyes() >= 2) {
                  tile.setSpawner(true);
                  tile.setSpawnType((byte)1);
               }

               tile.setEyes((byte)(tile.getEyes() + 1));
               tile.checkForMaze();
               --player.getHeldItem().stackSize;
               tile.markDirty();
               world.markBlockForUpdate(x, y, z);
               world.playSoundEffect(x, y, z, "thaumcraft:crystal", 0.2F, 1.0F);
            }
         }
      }

      if (metadata == 8 && player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof ItemEldritchObject && player.inventory.getCurrentItem().getItemDamage() == 2) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileEldritchLock && ((TileEldritchLock) te).count < 0) {
            ((TileEldritchLock)te).count = 0;
            world.markBlockForUpdate(x, y, z);
            te.markDirty();
            --player.getHeldItem().stackSize;
            world.playSoundEffect(x, y, z, "thaumcraft:runicShieldCharge", 1.0F, 1.0F);
         }
      }

      return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World w, int i, int j, int k, Random r) {
      int md = w.getBlockMetadata(i, j, k);
      if (md == 8) {
         TileEntity te = w.getTileEntity(i, j, k);
         if (!(te instanceof TileEldritchLock) || ((TileEldritchLock) te).count < 0) {
            return;
         }

         FXSpark ef = new FXSpark(w, (float)i + w.rand.nextFloat(), (float)j + w.rand.nextFloat(), (float)k + w.rand.nextFloat(), 0.5F);
         ef.setRBGColorF(0.65F + w.rand.nextFloat() * 0.1F, 1.0F, 1.0F);
         ef.setAlphaF(0.8F);
         ParticleEngine.instance.addEffect(w, ef);
      } else if (md == 10) {
         int x = i + r.nextInt(2) - r.nextInt(2);
         int y = j + r.nextInt(2) - r.nextInt(2);
         int z = k + r.nextInt(2) - r.nextInt(2);
         if (w.isAirBlock(x, y, z)) {
            Thaumcraft.proxy.blockRunes(w, (float)x + r.nextFloat(), (float)y + r.nextFloat(), (float)z + r.nextFloat(), 0.5F + r.nextFloat() * 0.5F, r.nextFloat() * 0.3F, 0.9F + r.nextFloat() * 0.1F, 16 + r.nextInt(4), 0.0F);
         }
      }

   }

   public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
      return null;
   }
}
