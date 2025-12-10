package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityFallingTaint;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;
import thaumcraft.common.lib.CustomSoundType;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.List;
import java.util.Random;

public class BlockTaint extends Block {
   private IIcon iconCrust;
   private IIcon iconSoil;
   private IIcon iconFlesh;

   public BlockTaint() {
      super(Config.taintMaterial);
      this.setHardness(2.0F);
      this.setResistance(10.0F);
      this.setStepSound(new CustomSoundType("gore", 0.5F, 0.8F));
      this.setTickRandomly(true);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int var4 = 0; var4 <= 2; ++var4) {
         par3List.add(new ItemStack(par1, 1, var4));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      this.iconCrust = par1IconRegister.registerIcon("thaumcraft:taint_crust");
      this.iconSoil = par1IconRegister.registerIcon("thaumcraft:taint_soil");
      this.iconFlesh = par1IconRegister.registerIcon("thaumcraft:fleshblock");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess ba, int x, int y, int z, int side) {
      int md = ba.getBlockMetadata(x, y, z);
      if (md == 0) {
         return this.iconCrust;
      } else if (md == 1) {
         return this.iconSoil;
      } else {
         return md == 2 ? this.iconFlesh : this.iconCrust;
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int md) {
      if (md == 0) {
         return this.iconCrust;
      } else if (md == 1) {
         return this.iconSoil;
      } else {
         return md == 2 ? this.iconFlesh : this.iconCrust;
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
      return par1 == 1 ? ThaumcraftWorldGenerator.biomeTaint.color : super.getBlockColor();
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int md = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
      if (md != 1) {
         return super.colorMultiplier(par1IBlockAccess, par2, par3, par4);
      } else {
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
   }

   public void updateTick(World world, int x, int y, int z, Random random) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         int md = world.getBlockMetadata(x, y, z);
         if (md == 2) {
            return;
         }

         BlockTaintFibres.taintBiomeSpread(world, x, y, z, random, this);
         if (md == 0) {
            if (this.tryToFall(world, x, y, z, x, y, z)) {
               return;
            }

            if (world.isAirBlock(x, y + 1, z)) {
               boolean doIt = true;
               ForgeDirection dir = ForgeDirection.getOrientation(2 + random.nextInt(4));

               for(int a = 0; a < 4; ++a) {
                  if (!world.isAirBlock(x + dir.offsetX, y - a, z + dir.offsetZ)) {
                     doIt = false;
                     break;
                  }

                  if (world.getBlock(x, y - a, z) != this) {
                     doIt = false;
                     break;
                  }
               }

               if (doIt && this.tryToFall(world, x, y, z, x + dir.offsetX, y, z + dir.offsetZ)) {
                  return;
               }
            }
         }

         int xx = x + random.nextInt(3) - 1;
         int yy = y + random.nextInt(5) - 3;
         int zz = z + random.nextInt(3) - 1;
         if (world.getBiomeGenForCoords(xx, zz).biomeID == Config.biomeTaintID) {
            world.getBlock(xx, yy, zz);
            if (BlockTaintFibres.spreadFibres(world, xx, yy, zz)) {
            }

            if (md == 0) {
               if (Config.spawnTaintSpore && world.isAirBlock(x, y + 1, z) && random.nextInt(200) == 0) {
                  List<Entity> targets = world.getEntitiesWithinAABB(EntityTaintSporeSwarmer.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(16.0F, 16.0F, 16.0F));
                  if (targets.isEmpty()) {
                     world.setBlockToAir(x, y, z);
                     EntityTaintSporeSwarmer spore = new EntityTaintSporeSwarmer(world);
                     spore.setLocationAndAngles((float)x + 0.5F, y, (float)z + 0.5F, 0.0F, 0.0F);
                     world.spawnEntityInWorld(spore);
                     world.playSoundAtEntity(spore, "thaumcraft:roots", 0.1F, 0.9F + world.getRandom().nextFloat() * 0.2F);
                  }
               } else {
                  boolean doIt = world.getBlock(x, y + 1, z) == this;
                  if (doIt) {
                     for(int a = 2; a < 6; ++a) {
                        ForgeDirection dir = ForgeDirection.getOrientation(a);
                        if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != this) {
                           doIt = false;
                           break;
                        }
                     }
                  }

                  if (doIt) {
                     world.setBlock(x, y, z, ConfigBlocks.blockFluxGoo, ((FluxGooBlock)ConfigBlocks.blockFluxGoo).getQuanta(), 3);
                  }
               }
            }
         } else if (md == 0 && random.nextInt(20) == 0) {
            world.setBlock(x, y, z, ConfigBlocks.blockFluxGoo, ((FluxGooBlock)ConfigBlocks.blockFluxGoo).getQuanta(), 3);
         } else if (md == 1 && random.nextInt(10) == 0) {
            world.setBlock(x, y, z, Blocks.dirt, 0, 3);
         }
      }

   }

   public Item getItemDropped(int md, Random rand, int fortune) {
      return md == 1 ? Blocks.dirt.getItemDropped(0, rand, fortune) : (md == 2 ? Items.rotten_flesh : Item.getItemById(0));
   }

   public int damageDropped(int par1) {
      return 0;
   }

   public ItemStack getPickBlock(HitResult target, World world, int x, int y, int z) {
      return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
   }

   public boolean canSilkHarvest(World world, Player player, int x, int y, int z, int metadata) {
      return metadata == 2 || super.canSilkHarvest(world, player, x, y, z, metadata);
   }

   public int quantityDropped(int meta, int fortune, Random random) {
      return meta == 2 ? 9 : super.quantityDropped(meta, fortune, random);
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 0) {
         return 1.75F;
      } else if (md == 1) {
         return 1.5F;
      } else {
         return md == 2 ? 0.2F : super.getBlockHardness(world, x, y, z);
      }
   }

   public static boolean canFallBelow(World par0World, int par1, int par2, int par3) {
      Block l = par0World.getBlock(par1, par2, par3);
      int md = par0World.getBlockMetadata(par1, par2, par3);

      for(int xx = -1; xx <= 1; ++xx) {
         for(int zz = -1; zz <= 1; ++zz) {
            for(int yy = -1; yy <= 1; ++yy) {
               if (Utils.isWoodLog(par0World, par1 + xx, par2 + yy, par3 + zz)) {
                  return false;
               }
            }
         }
      }

      if (l.isAir(par0World, par1, par2, par3)) {
         return true;
      } else if (l == ConfigBlocks.blockFluxGoo && md >= 4) {
         return false;
      } else if (l != Blocks.fire && l != ConfigBlocks.blockTaintFibres) {
         if (l.isReplaceable(par0World, par1, par2, par3)) {
            return true;
         } else {
            return l.getMaterial() == Material.water || l.getMaterial() == Material.lava;
         }
      } else {
         return true;
      }
   }

   private boolean tryToFall(Level par1World, int x, int y, int z, int x2, int y2, int z2) {
      int md = par1World.getBlockMetadata(x, y, z);
      if (canFallBelow(par1World, x2, y2 - 1, z2) && y2 >= 0) {
         byte b0 = 32;
         if (par1World.checkChunksExist(x2 - b0, y2 - b0, z2 - b0, x2 + b0, y2 + b0, z2 + b0)) {
            if (!(Platform.getEnvironment() == Env.CLIENT)) {
               EntityFallingTaint entityfalling = new EntityFallingTaint(par1World, (float)x2 + 0.5F, (float)y2 + 0.5F, (float)z2 + 0.5F, this, md, x, y, z);
               this.onStartFalling(entityfalling);
               par1World.spawnEntityInWorld(entityfalling);
               return true;
            }
         } else {
            par1World.setBlockToAir(x, y, z);

            while(canFallBelow(par1World, x2, y2 - 1, z2) && y2 > 0) {
               --y2;
            }

            if (y2 > 0) {
               par1World.setBlock(x, y, z, this, md, 3);
            }
         }
      }

      return false;
   }

   public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
      int md = world.getBlockMetadata(i, j, k);
      if (md != 2) {
         if (Platform.getEnvironment() != Env.CLIENT && entity instanceof EntityLivingBase && !((EntityLivingBase)entity).isEntityUndead()) {
            if (entity instanceof Player && world.getRandom().nextInt(100) == 0) {
               ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 80, 0, false));
            } else if (!(entity instanceof Player) && world.getRandom().nextInt(20) == 0) {
               ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 160, 0, false));
            }
         }

         super.onEntityWalking(world, i, j, k, entity);
      }
   }

   protected void onStartFalling(EntityFallingTaint entityfalling) {
   }

   public void onFinishFalling(Level par1World, int par2, int par3, int par4, int par5) {
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      int md = world.getBlockMetadata(i, j, k);
      if (md == 0 && world.isAirBlock(i, j - 1, k) && random.nextInt(10) == 0) {
         Thaumcraft.proxy.dropletFX(world, (float)i + 0.1F + world.getRandom().nextFloat() * 0.8F, (float)j, (float)k + 0.1F + world.getRandom().nextFloat() * 0.8F, 0.3F, 0.1F, 0.8F);
      }

   }

   public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int cd) {
      if (id == 1) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            world.playSound(x, y, z, "thaumcraft:roots", 0.1F, 0.9F + world.getRandom().nextFloat() * 0.2F, false);
         }

         return true;
      } else {
         return super.onBlockEventReceived(world, x, y, z, id, cd);
      }
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return true;
   }
}
