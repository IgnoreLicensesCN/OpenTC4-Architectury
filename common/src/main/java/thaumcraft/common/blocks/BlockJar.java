package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tc4tweak.network.NetworkedConfiguration;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileJarBrain;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileJarFillableVoid;
import thaumcraft.common.tiles.TileJarNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static tc4tweak.modules.blockJar.EntityCollisionBox.SMALLER_PARAMETERS;
import static tc4tweak.modules.blockJar.EntityCollisionBox.VANILLA_PARAMETERS;

public class BlockJar extends BlockContainer {
   public IIcon iconLiquid;
   public IIcon iconJarBottom;
   public IIcon iconJarSide;
   public IIcon iconJarTop;
   public IIcon iconJarTopVoid;
   public IIcon iconJarSideVoid;

   public BlockJar() {
      super(Material.glass);
      this.setHardness(0.3F);
      this.setStepSound(new CustomStepSound("jar", 1.0F, 1.0F));
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setLightLevel(0.66F);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconLiquid = ir.registerIcon("thaumcraft:animatedglow");
      this.iconJarSide = ir.registerIcon("thaumcraft:jar_side");
      this.iconJarTop = ir.registerIcon("thaumcraft:jar_top");
      this.iconJarTopVoid = ir.registerIcon("thaumcraft:jar_top_void");
      this.iconJarSideVoid = ir.registerIcon("thaumcraft:jar_side_void");
      this.iconJarBottom = ir.registerIcon("thaumcraft:jar_bottom");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      if (meta != 0 && meta != 1 && meta != 2) {
         if (meta == 3) {
            return side == 0 ? this.iconJarBottom : (side == 1 ? this.iconJarTopVoid : this.iconJarSideVoid);
         } else {
            return this.iconJarBottom;
         }
      } else {
         return side == 0 ? this.iconJarBottom : (side == 1 ? this.iconJarTop : this.iconJarSide);
      }
   }

   public int getRenderBlockPass() {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 3));
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileJarFillable();
      } else if (metadata == 1) {
         return new TileJarBrain();
      } else if (metadata == 2) {
         return new TileJarNode();
      } else {
         return metadata == 3 ? new TileJarFillableVoid() : null;
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return ConfigBlocks.blockJarRI;
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      this.dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
      super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
   }

   public static void playJarSound(World world, int x, int y, int z, float p_72980_8_) {
      world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "thaumcraft:jar", p_72980_8_, 1.0F, false);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList<ItemStack> drops = new ArrayList<>();
      int md = world.getBlockMetadata(x, y, z);
      if (md != 0 && md != 3) {
         if (md == 2) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileJarNode && ((TileJarNode) te).drop && ((TileJarNode) te).getAspects() != null) {
               ItemStack drop = new ItemStack(ConfigItems.itemJarNode);
               ((ItemJarNode) drop.getItem()).setAspects(drop, ((TileJarNode) te).getAspects().copy());
               ((ItemJarNode) drop.getItem()).setNodeAttributes(drop, ((TileJarNode) te).getNodeType(), ((TileJarNode) te).getNodeModifier(), ((TileJarNode) te).getId());
               drops.add(drop);
            }

            return drops;
         } else {
            return super.getDrops(world, x, y, z, metadata, fortune);
         }
      } else {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileJarFillable) {
            ItemStack drop = new ItemStack(ConfigItems.itemJarFilled);
            if (((TileJarFillable) te).amount <= 0 && ((TileJarFillable) te).aspectFilter == null) {
               drop = new ItemStack(this);
            }

            if (te instanceof TileJarFillableVoid) {
               drop.setItemDamage(3);
            }

            if (((TileJarFillable) te).amount > 0) {
               ((ItemJarFilled) drop.getItem()).setAspects(drop, (new AspectList()).add(((TileJarFillable) te).aspect, ((TileJarFillable) te).amount));
            }

            if (((TileJarFillable) te).aspectFilter != null) {
               if (!drop.hasTagCompound()) {
                  drop.setTagCompound(new NBTTagCompound());
               }

               drop.stackTagCompound.setString("AspectFilter", ((TileJarFillable) te).aspectFilter.getTag());
            }

            drops.add(drop);
         }

         return drops;
      }
   }

   public int damageDropped(int par1) {
      return par1;
   }

   @SideOnly(Side.CLIENT)
   public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
      return meta != 15;
   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      int md = par1World.getBlockMetadata(par2, par3, par4);
      if (md == 1 && !par1World.isRemote) {
         TileEntity te = par1World.getTileEntity(par2, par3, par4);
         if (te instanceof TileJarBrain) {
            int xp = ((TileJarBrain) te).xp;

            while (xp > 0) {
               int var2 = EntityXPOrb.getXPSplit(xp);
               xp -= var2;
               par1World.spawnEntityInWorld(new EntityXPOrb(par1World, par2, par3, par4, var2));
            }
         }
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public void onBlockPlacedBy(World world, int par2, int par3, int par4, EntityLivingBase ent, ItemStack stack) {
      int l = MathHelper.floor_double((double) (ent.rotationYaw * 4.0F / 360.0F) + (double) 0.5F) & 3;
      TileEntity tile = world.getTileEntity(par2, par3, par4);
      if (tile instanceof TileJarFillable) {
         if (l == 0) {
            ((TileJarFillable) tile).facing = 2;
         }

         if (l == 1) {
            ((TileJarFillable) tile).facing = 5;
         }

         if (l == 2) {
            ((TileJarFillable) tile).facing = 3;
         }

         if (l == 3) {
            ((TileJarFillable) tile).facing = 4;
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z,
                                   EntityPlayer player, int side, float what,
                                   float these, float are) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileJarBrain) {
         ((TileJarBrain) te).eatDelay = 40;
         if (!world.isRemote) {
            int var6 = world.rand.nextInt(Math.min(((TileJarBrain) te).xp + 1, 64));
            if (var6 > 0) {
               ((TileJarBrain) te).xp -= var6;
               int xp = var6;

               while (xp > 0) {
                  int var2 = EntityXPOrb.getXPSplit(xp);
                  xp -= var2;
                  world.spawnEntityInWorld(new EntityXPOrb(world, (double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, var2));
               }

               world.markBlockForUpdate(x, y, z);
               te.markDirty();
            }
         } else {
            playJarSound(world, x, y, z, .2f);
         }
      }
      if (te instanceof TileJarFillable) {
         TileJarFillable fillableJar = (TileJarFillable) te;
         if (player.isSneaking()) {
            if (fillableJar.aspectFilter != null
                    && side == fillableJar.facing) {
               //remove and drop jar filter
               fillableJar.aspectFilter = null;
               world.markBlockForUpdate(x, y, z);
               te.markDirty();

               if (world.isRemote) {
                  playJarSound(world, x, y, z, 1.f);
               } else {
                  ForgeDirection fd = ForgeDirection.getOrientation(side);
                  world.spawnEntityInWorld(new EntityItem(world, (float) x + 0.5F + (float) fd.offsetX / 3.0F, (float) y + 0.5F, (float) z + 0.5F + (float) fd.offsetZ / 3.0F, new ItemStack(ConfigItems.itemResource, 1, 13)));
               }
            } else if (player.getHeldItem() == null) {
               //clear jar
               fillableJar.amount = 0;
               if (((TileJarFillable) te).aspectFilter == null) {
                  fillableJar.aspect = null;
               }

               if (world.isRemote) {
                  playJarSound(world, x, y, z, .4f);
                  world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "game.neutral.swim", 0.5F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.3F, false);
               }
            }
         } else if (player.getHeldItem() != null
                 && fillableJar.aspectFilter == null
                 && player.getHeldItem().getItem() == ConfigItems.itemResource && player.getHeldItem().getItemDamage() == 13) {
            if (((TileJarFillable) te).amount == 0 && ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()) == null) {
               return true;
            }

            if (((TileJarFillable) te).amount == 0 && ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()) != null) {
               fillableJar.aspect = ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()).getAspects()[0];
            }

            --player.getHeldItem().stackSize;
            this.onBlockPlacedBy(world, x, y, z, player, null);

            fillableJar.aspectFilter = fillableJar.aspect;
            world.markBlockForUpdate(x, y, z);
            te.markDirty();

            if (world.isRemote) {
               playJarSound(world, x, y, z, .4f);
            }
         }
      }

      return true;
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      this.setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.75F, 0.8125F);
      super.setBlockBoundsBasedOnState(world, i, j, k);
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      float[] params = NetworkedConfiguration.isSmallerJar() ? SMALLER_PARAMETERS : VANILLA_PARAMETERS;
      this.setBlockBounds(params[0], params[1], params[2], params[3], params[4], params[5]);
      super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
   }

   public float getEnchantPowerBonus(World world, int x, int y, int z) {
      TileEntity te = world.getTileEntity(x, y, z);
      return te instanceof TileJarBrain ? 2.0F : super.getEnchantPowerBonus(world, x, y, z);
   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      return md == 2 ? 11 : super.getLightValue(world, x, y, z);
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileJarBrain && ((TileJarBrain) tile).xp >= ((TileJarBrain) tile).xpMax) {
         double xx = (double) x + 0.3 + (double) (rand.nextFloat() * 0.4F);
         double yy = (double) y + 0.9;
         double zz = (double) z + 0.3 + (double) (rand.nextFloat() * 0.4F);
         EntitySpellParticleFX var21 = new EntitySpellParticleFX(world, xx, yy, zz, 0.0F, 0.0F, 0.0F);
         var21.setAlphaF(0.5F);
         var21.setRBGColorF(0.0F, 0.4F + world.rand.nextFloat() * 0.1F, 0.3F + world.rand.nextFloat() * 0.2F);
         Minecraft.getMinecraft().effectRenderer.addEffect(var21);
      }

   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileJarBrain) {
         float r = (float) ((TileJarBrain) tile).xp / (float) ((TileJarBrain) tile).xpMax;
         return MathHelper.floor_float(r * 14.0F) + (((TileJarBrain) tile).xp > 0 ? 1 : 0);
      } else if (tile instanceof TileJarFillable) {
         float r = (float) ((TileJarFillable) tile).amount / (float) ((TileJarFillable) tile).maxAmount;
         return MathHelper.floor_float(r * 14.0F) + (((TileJarFillable) tile).amount > 0 ? 1 : 0);
      } else {
         return super.getComparatorInputOverride(world, x, y, z, rs);
      }
   }
}
