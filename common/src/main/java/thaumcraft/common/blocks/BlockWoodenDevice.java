package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWoodenDevice extends BlockContainer {
   private Random random = new Random();
   public IIcon iconDefault;
   public IIcon iconSilverwood;
   public IIcon iconGreatwood;
   public IIcon[] iconAPPlate = new IIcon[3];
   public IIcon[] iconAEar = new IIcon[7];
   public int renderState = 0;

   public BlockWoodenDevice() {
      super(Material.wood);
      this.setHardness(2.5F);
      this.setResistance(10.0F);
      this.setStepSound(soundTypeWood);
      this.setTickRandomly(true);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconDefault = ir.registerIcon("thaumcraft:woodplain");
      this.iconSilverwood = ir.registerIcon("thaumcraft:planks_silverwood");
      this.iconGreatwood = ir.registerIcon("thaumcraft:planks_greatwood");
      this.iconAPPlate[0] = ir.registerIcon("thaumcraft:applate1");
      this.iconAPPlate[1] = ir.registerIcon("thaumcraft:applate2");
      this.iconAPPlate[2] = ir.registerIcon("thaumcraft:applate3");
      this.iconAEar[0] = ir.registerIcon("thaumcraft:arcaneearsideon");
      this.iconAEar[1] = ir.registerIcon("thaumcraft:arcaneearsideoff");
      this.iconAEar[2] = ir.registerIcon("thaumcraft:arcaneearbottom");
      this.iconAEar[3] = ir.registerIcon("thaumcraft:arcaneeartopon");
      this.iconAEar[4] = ir.registerIcon("thaumcraft:arcaneeartopoff");
      this.iconAEar[5] = ir.registerIcon("thaumcraft:arcaneearbellside");
      this.iconAEar[6] = ir.registerIcon("thaumcraft:arcaneearbelltop");
   }

   public int tickRate() {
      return 20;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
      par3List.add(new ItemStack(par1, 1, 6));
      par3List.add(new ItemStack(par1, 1, 7));
      par3List.add(new ItemStack(par1, 1, 8));

      for(int a = 0; a < 16; ++a) {
         ItemStack banner = new ItemStack(par1, 1, 8);
         banner.setTagCompound(new NBTTagCompound());
         banner.stackTagCompound.setByte("color", (byte)a);
         par3List.add(banner);
      }

   }

   public IIcon getIcon(int par1, int par2) {
      if (par2 == 0) {
         return this.iconDefault;
      } else if (par2 == 6) {
         return this.iconGreatwood;
      } else if (par2 == 7) {
         return this.iconSilverwood;
      } else if (par2 != 2 && par2 != 3) {
         if (this.renderState == 0) {
            switch (par1) {
               case 0:
                  return this.iconAEar[2];
               case 1:
                  return this.iconAEar[4];
            }
         } else {
            if (this.renderState != 1) {
               if (par1 <= 1) {
                  return this.iconAEar[6];
               }

               return this.iconAEar[5];
            }

            switch (par1) {
               case 0:
                  return this.iconAEar[2];
               case 1:
                  return this.iconAEar[3];
            }
         }

         return this.iconAEar[0];
      } else {
         return this.iconAPPlate[0];
      }
   }

   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 2 || meta == 3) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileArcanePressurePlate) {
            return this.iconAPPlate[((TileArcanePressurePlate)tile).setting];
         }
      }

      return super.getIcon(world, x, y, z, side);
   }

   public int damageDropped(int par1) {
      return par1 == 3 ? 2 : par1;
   }

   public Item getItemDropped(int par1, Random par2Random, int par3) {
      if (!Config.wardedStone || par1 != 2 && par1 != 3) {
         return par1 == 8 ? Item.getItemById(0) : super.getItemDropped(par1, par2Random, par3);
      } else {
         return Item.getItemById(0);
      }
   }

   public float getBlockHardness(World world, int x, int y, int z) {
      if (world.getBlock(x, y, z) != this) {
         return super.getBlockHardness(world, x, y, z);
      } else {
         int md = world.getBlockMetadata(x, y, z);
         if (md != 2 && md != 3) {
            return super.getBlockHardness(world, x, y, z);
         } else {
            return Config.wardedStone ? -1.0F : 2.0F;
         }
      }
   }

   public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
      if (world.getBlock(x, y, z) != this) {
         return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
      } else {
         int md = world.getBlockMetadata(x, y, z);
         return md != 2 && md != 3 ? super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ) : 999.0F;
      }
   }

   public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
      if (world.getBlock(x, y, z) == this) {
         int md = world.getBlockMetadata(x, y, z);
         if (md != 2 && md != 3) {
            super.onBlockExploded(world, x, y, z, explosion);
         }
      } else {
         super.onBlockExploded(world, x, y, z, explosion);
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return ConfigBlocks.blockWoodenDeviceRI;
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_) {
      return p_149633_1_.getBlock(p_149633_2_, p_149633_3_, p_149633_4_) != this ? AxisAlignedBB.getBoundingBox(p_149633_2_, p_149633_3_, p_149633_4_, (double)p_149633_2_ + (double)1.0F, (double)p_149633_3_ + (double)1.0F, (double)p_149633_4_ + (double)1.0F) : super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
      if (par1iBlockAccess.getBlock(par2, par3, par4) != this) {
         super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
      } else {
         int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
         if (meta == 0) {
            this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
         } else if (meta == 2) {
            float var6 = 0.0625F;
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
         } else if (meta == 3) {
            float var6 = 0.0625F;
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
         } else if (meta == 5) {
            ForgeDirection dir = ForgeDirection.UNKNOWN;
            TileEntity tile = par1iBlockAccess.getTileEntity(par2, par3, par4);
            if (tile instanceof TileArcaneBore) {
               dir = ((TileArcaneBore)tile).orientation;
            }

            this.setBlockBounds((float)((dir.offsetX < 0 ? -1 : 0)), (float)((dir.offsetY < 0 ? -1 : 0)), (float)((dir.offsetZ < 0 ? -1 : 0)), (float)(1 + (dir.offsetX > 0 ? 1 : 0)), (float)(1 + (dir.offsetY > 0 ? 1 : 0)), (float)(1 + (dir.offsetZ > 0 ? 1 : 0)));
         } else if (meta == 8) {
            TileEntity tile = par1iBlockAccess.getTileEntity(par2, par3, par4);
            if (tile instanceof TileBanner) {
               if (((TileBanner)tile).getWall()) {
                  switch (((TileBanner)tile).getFacing()) {
                     case 0:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.25F);
                        break;
                     case 4:
                        this.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
                        break;
                     case 8:
                        this.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 2.0F, 1.0F);
                        break;
                     case 12:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 2.0F, 1.0F);
                  }
               } else {
                  this.setBlockBounds(0.33F, 0.0F, 0.33F, 0.66F, 2.0F, 0.66F);
               }
            } else {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         }

         super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
      }
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      if (world.getBlock(i, j, k) != this) {
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else {
         int meta = world.getBlockMetadata(i, j, k);
         if (meta == 0) {
            this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
         } else if (meta != 2 && meta != 3 && meta != 8) {
            if (meta == 5) {
               ForgeDirection dir = ForgeDirection.UNKNOWN;
               TileEntity tile = world.getTileEntity(i, j, k);
               if (tile instanceof TileArcaneBore) {
                  dir = ((TileArcaneBore)tile).orientation;
               }

               this.setBlockBounds((float)((dir.offsetX < 0 ? -1 : 0)), (float)((dir.offsetY < 0 ? -1 : 0)), (float)((dir.offsetZ < 0 ? -1 : 0)), (float)(1 + (dir.offsetX > 0 ? 1 : 0)), (float)(1 + (dir.offsetY > 0 ? 1 : 0)), (float)(1 + (dir.offsetZ > 0 ? 1 : 0)));
            } else {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      }
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 1) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileSensor) {
            ((TileSensor)tile).updateTone();
         }
      } else if (meta == 5) {
         TileArcaneBore tile = (TileArcaneBore)world.getTileEntity(x, y, z);
         if (tile instanceof TileArcaneBore) {
            ForgeDirection d = tile.baseOrientation.getOpposite();
            Block block = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
            if (block != ConfigBlocks.blockWoodenDevice || !block.isSideSolid(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, tile.baseOrientation)) {
               InventoryUtils.dropItems(world, x, y, z);
               this.dropBlockAsItem(world, x, y, z, 5, 0);
               world.setBlockToAir(x, y, z);
            }
         }
      }

      super.onNeighborBlockChange(world, x, y, z, par5);
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta == 4 || meta == 6 || meta == 7 || super.isSideSolid(world, x, y, z, side);
   }

   public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int par6, float par7, float par8, float par9) {
      if (w.getBlock(x, y, z) != this) {
         return false;
      } else {
         int meta = w.getBlockMetadata(x, y, z);
         if (meta != 4 && meta != 6 && meta != 7) {
            if (w.isRemote) {
               return true;
            } else if (meta != 5 || p.inventory.getCurrentItem() != null && p.inventory.getCurrentItem() != null && p.inventory.getCurrentItem().getItem() instanceof ItemWandCasting) {
               if (meta == 1) {
                  TileSensor var6 = (TileSensor)w.getTileEntity(x, y, z);
                  if (var6 != null) {
                     var6.changePitch();
                     var6.triggerNote(w, x, y, z, true);
                  }
               } else if (meta != 2 && meta != 3) {
                  if (meta == 8 && (p.isSneaking() || p.inventory.getCurrentItem() != null && p.inventory.getCurrentItem().getItem() instanceof ItemEssence)) {
                     TileBanner te = (TileBanner)w.getTileEntity(x, y, z);
                     if (te != null && te.getColor() >= 0) {
                        if (p.isSneaking()) {
                           te.setAspect(null);
                        } else if (((IEssentiaContainerItem) p.getHeldItem().getItem()).getAspects(p.getHeldItem()) != null) {
                           te.setAspect(((IEssentiaContainerItem) p.getHeldItem().getItem()).getAspects(p.getHeldItem()).getAspects()[0]);
                           --p.getHeldItem().stackSize;
                        }

                        w.markBlockForUpdate(x, y, z);
                        te.markDirty();
                        w.playSoundEffect(x, y, z, "step.cloth", 1.0F, 1.0F);
                     }
                  }
               } else {
                  TileArcanePressurePlate var6 = (TileArcanePressurePlate)w.getTileEntity(x, y, z);
                  if (var6 != null && (var6.owner.equals(p.getCommandSenderName()) || var6.accessList.contains("1" + p.getCommandSenderName()))) {
                     ++var6.setting;
                     if (var6.setting > 2) {
                        var6.setting = 0;
                     }

                     switch (var6.setting) {
                        case 0:
                           p.addChatMessage(new ChatComponentTranslation("It will now trigger on everything."));
                           break;
                        case 1:
                           p.addChatMessage(new ChatComponentTranslation("It will now trigger on everything except you."));
                           break;
                        case 2:
                           p.addChatMessage(new ChatComponentTranslation("It will now trigger on just you."));
                     }

                     w.playSoundEffect((double)x + (double)0.5F, (double)y + 0.1, (double)z + (double)0.5F, "random.click", 0.1F, 0.9F);
                     w.markBlockForUpdate(x, y, z);
                     var6.markDirty();
                  }
               }

               return true;
            } else {
               p.openGui(Thaumcraft.instance, 15, w, x, y, z);
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      int md = par1World.getBlockMetadata(par2, par3, par4);
      if (md == 8) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
      }

      super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      int md = world.getBlockMetadata(x, y, z);
      if (md != 8) {
         return super.getDrops(world, x, y, z, metadata, fortune);
      } else {
         ArrayList<ItemStack> drops = new ArrayList<>();
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileBanner) {
            ItemStack drop = new ItemStack(this, 1, 8);
            if (((TileBanner)te).getColor() >= 0 || ((TileBanner)te).getAspect() != null) {
               drop.setTagCompound(new NBTTagCompound());
               if (((TileBanner)te).getAspect() != null) {
                  drop.stackTagCompound.setString("aspect", ((TileBanner)te).getAspect().getTag());
               }

               drop.stackTagCompound.setByte("color", ((TileBanner)te).getColor());
            }

            drops.add(drop);
         }

         return drops;
      }
   }

   public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase p, ItemStack s) {
      TileEntity tile = w.getTileEntity(x, y, z);
      if (tile instanceof TileOwned && p instanceof EntityPlayer) {
         ((TileOwned)tile).owner = p.getCommandSenderName();
         tile.markDirty();
      }

      super.onBlockPlacedBy(w, x, y, z, p, s);
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      super.onBlockAdded(world, x, y, z);
      if (world.getBlock(x, y, z) == this) {
         int meta = world.getBlockMetadata(x, y, z);
         if (meta == 1) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileSensor) {
               ((TileSensor)tile).updateTone();
               tile.markDirty();
            }
         }

      }
   }

   public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 0) {
         return false;
      } else {
         return meta == 1 || meta == 2 || meta == 3 || meta == 4 || meta == 5 || super.canConnectRedstone(world, x, y, z, side);
      }
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileBellows();
      } else if (metadata == 1) {
         return new TileSensor();
      } else if (metadata == 2) {
         return new TileArcanePressurePlate();
      } else if (metadata == 3) {
         return new TileArcanePressurePlate();
      } else if (metadata == 4) {
         return new TileArcaneBoreBase();
      } else if (metadata == 5) {
         return new TileArcaneBore();
      } else {
         return metadata == 8 ? new TileBanner() : super.createTileEntity(world, metadata);
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
      float var7 = (float)Math.pow(2.0F, (double)(par6 - 12) / (double)12.0F);
      if (par5 <= 4) {
         if (par5 >= 0) {
            String var8 = "harp";
            if (par5 == 1) {
               var8 = "bd";
            }

            if (par5 == 2) {
               var8 = "snare";
            }

            if (par5 == 3) {
               var8 = "hat";
            }

            if (par5 == 4) {
               var8 = "bassattack";
            }

            par1World.playSoundEffect((double)par2 + (double)0.5F, (double)par3 + (double)0.5F, (double)par4 + (double)0.5F, "note." + var8, 3.0F, var7);
         }

         par1World.spawnParticle("note", (double)par2 + (double)0.5F, (double)par3 + 1.2, (double)par4 + (double)0.5F, (double)par6 / (double)24.0F, 0.0F, 0.0F);
         return true;
      } else {
         return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
      }
   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if (par1World.getBlock(par2, par3, par4) == this) {
         if (!par1World.isRemote && par1World.getBlockMetadata(par2, par3, par4) == 3) {
            this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
         }

      }
   }

   public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
      if (par1World.getBlock(par2, par3, par4) == this) {
         if (!par1World.isRemote && par1World.getBlockMetadata(par2, par3, par4) == 2) {
            this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
         }

      }
   }

   private void setStateIfMobInteractsWithPlate(World world, int x, int y, int z) {
      boolean var5 = world.getBlockMetadata(x, y, z) == 3;
      boolean var6 = false;
      float var7 = 0.125F;
      List<Entity> var8 = null;
      String username = "";
      byte setting = 0;
      ArrayList<String> accessList = new ArrayList<>();
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof TileArcanePressurePlate) {
         setting = ((TileArcanePressurePlate)tile).setting;
         username = ((TileArcanePressurePlate)tile).owner;
         accessList = ((TileArcanePressurePlate)tile).accessList;
      }

      if (setting == 0) {
         var8 = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox((float)x + var7, y, (float)z + var7, (float)(x + 1) - var7, (double)y + (double)0.25F, (float)(z + 1) - var7));
      }

      if (setting == 1) {
         var8 = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox((float)x + var7, y, (float)z + var7, (float)(x + 1) - var7, (double)y + (double)0.25F, (float)(z + 1) - var7));
      }

      if (setting == 2) {
         var8 = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((float)x + var7, y, (float)z + var7, (float)(x + 1) - var7, (double)y + (double)0.25F, (float)(z + 1) - var7));
      }

      if (!var8.isEmpty()) {
         for(Entity var10 : var8) {
            if (!var10.doesEntityNotTriggerPressurePlate() && (setting != 1 || !(var10 instanceof EntityPlayer) || !var10.getCommandSenderName().equals(username) && !accessList.contains("0" + var10.getCommandSenderName()) && !accessList.contains("1" + var10.getCommandSenderName())) && (setting != 2 || !(var10 instanceof EntityPlayer) || var10.getCommandSenderName().equals(username) || accessList.contains("0" + var10.getCommandSenderName()) || accessList.contains("1" + var10.getCommandSenderName()))) {
               var6 = true;
               break;
            }
         }
      }

      if (var6 && !var5) {
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);
         world.notifyBlocksOfNeighborChange(x, y, z, this);
         world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
         world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
         world.playSoundEffect((double)x + (double)0.5F, (double)y + 0.1, (double)z + (double)0.5F, "random.click", 0.2F, 0.6F);
      }

      if (!var6 && var5) {
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);
         world.notifyBlocksOfNeighborChange(x, y, z, this);
         world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
         world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
         world.playSoundEffect((double)x + (double)0.5F, (double)y + 0.1, (double)z + (double)0.5F, "random.click", 0.2F, 0.5F);
      }

      if (var6) {
         world.scheduleBlockUpdate(x, y, z, this, this.tickRate());
      }

   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      if (par6 == 3) {
         par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this);
         par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this);
      } else if (par6 == 5) {
         InventoryUtils.dropItems(par1World, par2, par3, par4);
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 1) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileSensor) {
            return ((TileSensor)tile).redstoneSignal > 0 ? 15 : 0;
         } else {
            return super.isProvidingStrongPower(world, x, y, z, side);
         }
      } else {
         return world.getBlockMetadata(x, y, z) == 2 ? 0 : (side == 1 && world.getBlockMetadata(x, y, z) == 3 ? 15 : 0);
      }
   }

   public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 1) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile instanceof TileSensor) {
            return ((TileSensor)tile).redstoneSignal > 0 ? 15 : 0;
         }
      } else if (meta == 3) {
         return 15;
      }

      return super.isProvidingStrongPower(world, x, y, z, side);
   }

   public boolean canProvidePower() {
      return true;
   }

   public int getMobilityFlag() {
      return 1;
   }

   public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta != 6 && meta != 7 ? super.getLightOpacity(world, x, y, z) : 255;
   }

   public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta != 2 && meta != 3 && super.canEntityDestroy(world, x, y, z, entity);
   }

   public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta != 6 && meta != 7 ? 0 : 20;
   }

   public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta != 6 && meta != 7 ? 0 : 5;
   }
}
