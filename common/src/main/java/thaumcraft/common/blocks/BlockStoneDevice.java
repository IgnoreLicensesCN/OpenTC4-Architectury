package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockContainer;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.*;

import java.util.List;
import java.util.Random;

public class BlockStoneDevice extends BlockContainer {
   public IIcon[] iconFurnace = new IIcon[5];
   public IIcon[] iconPedestal = new IIcon[2];
   public IIcon[] iconWandPedestal = new IIcon[2];
   public IIcon[] iconWandPedestalFocus = new IIcon[3];
   public IIcon[] iconSpa = new IIcon[2];

   public BlockStoneDevice() {
      super(Material.rock);
      this.setHardness(3.0F);
      this.setResistance(25.0F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconPedestal[0] = ir.registerIcon("thaumcraft:pedestal_side");
      this.iconPedestal[1] = ir.registerIcon("thaumcraft:pedestal_top");
      this.iconWandPedestal[0] = ir.registerIcon("thaumcraft:wandpedestal_side");
      this.iconWandPedestal[1] = ir.registerIcon("thaumcraft:wandpedestal_top");
      this.iconWandPedestalFocus[0] = ir.registerIcon("thaumcraft:wandpedestal_focus_side");
      this.iconWandPedestalFocus[1] = ir.registerIcon("thaumcraft:wandpedestal_focus_top");
      this.iconWandPedestalFocus[2] = ir.registerIcon("thaumcraft:wandpedestal_focus_bot");
      this.iconFurnace[0] = ir.registerIcon("thaumcraft:al_furnace_side");
      this.iconFurnace[1] = ir.registerIcon("thaumcraft:al_furnace_top");
      this.iconFurnace[2] = ir.registerIcon("thaumcraft:al_furnace_front_off");
      this.iconFurnace[3] = ir.registerIcon("thaumcraft:al_furnace_front_on");
      this.iconFurnace[4] = ir.registerIcon("thaumcraft:al_furnace_top_filled");
      this.iconSpa[0] = ir.registerIcon("thaumcraft:spa_side");
      this.iconSpa[1] = ir.registerIcon("thaumcraft:spa_top");
   }

   public int getRenderType() {
      return ConfigBlocks.blockStoneDeviceRI;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public IIcon getIcon(int side, int md) {
      if (md == 0) {
         if (side == 1) {
            return this.iconFurnace[1];
         }

         if (side > 1) {
            return this.iconFurnace[2];
         }
      } else if (md == 1) {
         if (side <= 1) {
            return this.iconPedestal[1];
         }

         if (side > 1) {
            return this.iconPedestal[0];
         }
      } else if (md == 5) {
         if (side == 0) {
            return this.iconPedestal[1];
         }

         if (side == 1) {
            return this.iconWandPedestal[1];
         }

         if (side > 1) {
            return this.iconWandPedestal[0];
         }
      } else if (md == 8) {
         if (side == 0) {
            return this.iconWandPedestalFocus[2];
         }

         if (side == 1) {
            return this.iconWandPedestalFocus[1];
         }

         if (side > 1) {
            return this.iconWandPedestalFocus[0];
         }
      } else if (md == 12) {
         if (side == 0) {
            return this.iconPedestal[1];
         }

         if (side == 1) {
            return this.iconSpa[1];
         }

         if (side > 1) {
            return this.iconSpa[0];
         }
      }

      return this.iconPedestal[1];
   }

   public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int side) {
      int metadata = iblockaccess.getBlockMetadata(i, j, k);
      if (metadata == 0) {
         TileEntity te = iblockaccess.getTileEntity(i, j, k);
         if (side == 1) {
            if (te instanceof TileAlchemyFurnace && ((TileAlchemyFurnace) te).vis > 0) {
               return this.iconFurnace[4];
            }

            return this.iconFurnace[1];
         }

         if (side > 1) {
            if (te instanceof TileAlchemyFurnace && ((TileAlchemyFurnace) te).isBurning()) {
               return this.iconFurnace[3];
            }

            return this.iconFurnace[2];
         }
      } else if (metadata == 1 || metadata == 5 || metadata == 8 || metadata == 12) {
         return super.getIcon(iblockaccess, i, j, k, side);
      }

      return this.iconFurnace[0];
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 5));
      par3List.add(new ItemStack(par1, 1, 8));
      par3List.add(new ItemStack(par1, 1, 9));
      par3List.add(new ItemStack(par1, 1, 10));
      par3List.add(new ItemStack(par1, 1, 11));
      par3List.add(new ItemStack(par1, 1, 12));
      par3List.add(new ItemStack(par1, 1, 13));
      par3List.add(new ItemStack(par1, 1, 14));
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World w, int i, int j, int k, Random r) {
      TileEntity te = w.getTileEntity(i, j, k);
      if (te instanceof TileAlchemyFurnace && ((TileAlchemyFurnace) te).isBurning()) {
         float f = (float)i + 0.5F;
         float f1 = (float)j + 0.2F + r.nextFloat() * 5.0F / 16.0F;
         float f2 = (float)k + 0.5F;
         float f3 = 0.52F;
         float f4 = r.nextFloat() * 0.5F - 0.25F;
         w.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0F, 0.0F, 0.0F);
         w.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0F, 0.0F, 0.0F);
      }

   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 0) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileAlchemyFurnace && ((TileAlchemyFurnace) te).isBurning()) {
            return 12;
         }
      } else if (meta == 2) {
         return 10;
      }

      return super.getLightValue(world, x, y, z);
   }

   public int damageDropped(int metadata) {
      return metadata == 3 ? 7 : (metadata == 4 ? 6 : metadata);
   }

   public Item getItemDropped(int metadata, Random par2Random, int par3) {
      return metadata != 3 && metadata != 4 ? super.getItemDropped(metadata, par2Random, par3) : Item.getItemFromBlock(ConfigBlocks.blockCosmeticSolid);
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileAlchemyFurnace();
      } else if (metadata == 1) {
         return new TilePedestal();
      } else if (metadata == 2) {
         return new TileInfusionMatrix();
      } else if (metadata == 3) {
         return new TileInfusionPillar();
      } else if (metadata == 5) {
         return new TileWandPedestal();
      } else if (metadata != 9 && metadata != 10) {
         if (metadata == 11) {
            return new TileNodeConverter();
         } else if (metadata == 12) {
            return new TileSpa();
         } else if (metadata == 13) {
            return new TileFocalManipulator();
         } else {
            return metadata == 14 ? new TileFluxScrubber() : super.createTileEntity(world, metadata);
         }
      } else {
         return new TileNodeStabilizer();
      }
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (!(te instanceof TilePedestal) && !(te instanceof TileAlchemyFurnace)) {
         if (te instanceof TileWandPedestal && ((TileWandPedestal) te).getAspects() != null && ((TileWandPedestal) te).getStackInSlot(0) != null && ((TileWandPedestal) te).getStackInSlot(0).getItem() instanceof WandCastingItem) {
            WandCastingItem wand = (WandCastingItem)((TileWandPedestal)te).getStackInSlot(0).getItem();
            float r = (float)wand.getAllVis(((TileWandPedestal)te).getStackInSlot(0)).visSize() / ((float)wand.getMaxVis(((TileWandPedestal)te).getStackInSlot(0)) * 6.0F);
            return MathHelper.floor_float(r * 14.0F) + 1;
         } else {
            return 0;
         }
      } else {
         return Container.calcRedstoneFromInventory((IInventory)te);
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public void breakBlock(Level par1World, int par2, int par3, int par4, Block par5, int par6) {
      InventoryUtils.dropItems(par1World, par2, par3, par4);
      TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
      if (tileEntity instanceof TileInfusionMatrix && ((TileInfusionMatrix) tileEntity).crafting) {
         par1World.createExplosion(null, (double)par2 + (double)0.5F, (double)par3 + (double)0.5F, (double)par4 + (double)0.5F, 2.0F, true);
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileAlchemyFurnace) {
         ((TileAlchemyFurnace)te).getBellows();
      } else if (te instanceof TileNodeConverter) {
         ((TileNodeConverter)te).checkStatus();
      } else {
         int metadata = world.getBlockMetadata(x, y, z);
         if (metadata == 1) {
            if (!world.isAirBlock(x, y + 1, z)) {
               InventoryUtils.dropItems(world, x, y, z);
            }
         } else if (metadata == 5) {
            if (!world.isAirBlock(x, y + 1, z) && (world.getBlock(x, y + 1, z) != this || world.getBlockMetadata(x, y + 1, z) != 8)) {
               InventoryUtils.dropItems(world, x, y, z);
            }
         } else if (metadata == 3) {
            if (world.getBlock(x, y + 1, z) != this || world.getBlockMetadata(x, y + 1, z) != 4) {
               this.dropBlockAsItem(world, x, y, z, metadata, 0);
               world.setBlock(x, y, z, Blocks.air, 0, 3);
            }
         } else if (metadata == 4 && (world.getBlock(x, y - 1, z) != this || world.getBlockMetadata(x, y - 1, z) != 3)) {
            this.dropBlockAsItem(world, x, y, z, metadata, 0);
            world.setBlock(x, y, z, Blocks.air, 0, 3);
         }
      }

   }

   public boolean onBlockActivated(World world, int x, int y, int z, Player player, int side, float par7, float par8, float par9) {
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         return true;
      } else {
         int metadata = world.getBlockMetadata(x, y, z);
         TileEntity tileEntity = world.getTileEntity(x, y, z);
         if (metadata == 0 && tileEntity instanceof TileAlchemyFurnace && !player.isSneaking()) {
            player.openGui(Thaumcraft.instance, 9, world, x, y, z);
            return true;
         } else {
            if (metadata == 1 && tileEntity instanceof TilePedestal) {
               TilePedestal ped = (TilePedestal)tileEntity;
               if (ped.getStackInSlot(0) != null) {
                  InventoryUtils.dropItemsAtEntity(world, x, y, z, player);
                  world.playSoundEffect(x, y, z, "random.pop", 0.2F, ((world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.7F + 1.0F) * 1.5F);
                  return true;
               }

               if (player.getCurrentEquippedItem() != null) {
                  ItemStack i = player.getCurrentEquippedItem().copy();
                  i.stackSize = 1;
                  ped.setInventorySlotContents(0, i);
                  --player.getCurrentEquippedItem().stackSize;
                  if (player.getCurrentEquippedItem().stackSize == 0) {
                     player.setCurrentItemOrArmor(0, null);
                  }

                  player.inventory.markDirty();
                  world.playSoundEffect(x, y, z, "random.pop", 0.2F, ((world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.7F + 1.0F) * 1.6F);
                  return true;
               }
            }

            if (metadata == 8) {
               --y;
               metadata = world.getBlockMetadata(x, y, z);
               tileEntity = world.getTileEntity(x, y, z);
            }

            if (metadata == 5 && tileEntity instanceof TileWandPedestal) {
               if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().isItemEqual(new ItemStack(this, 1, 8))) {
                  return false;
               }

               TileWandPedestal ped = (TileWandPedestal)tileEntity;
               if (ped.getStackInSlot(0) != null) {
                  InventoryUtils.dropItemsAtEntity(world, x, y, z, player);
                  world.markBlockForUpdate(x, y, z);
                  ped.markDirty();
                  world.playSoundEffect(x, y, z, "random.pop", 0.2F, ((world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.7F + 1.0F) * 1.5F);
                  return true;
               }

               if (player.getCurrentEquippedItem() != null && (player.getCurrentEquippedItem().getItem() instanceof WandCastingItem || player.getCurrentEquippedItem().getItem() instanceof ItemAmuletVis)) {
                  ItemStack i = player.getCurrentEquippedItem().copy();
                  i.stackSize = 1;
                  ped.setInventorySlotContents(0, i);
                  --player.getCurrentEquippedItem().stackSize;
                  if (player.getCurrentEquippedItem().stackSize == 0) {
                     player.setCurrentItemOrArmor(0, null);
                  }

                  player.inventory.markDirty();
                  world.markBlockForUpdate(x, y, z);
                  ped.markDirty();
                  world.playSoundEffect(x, y, z, "random.pop", 0.2F, ((world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.7F + 1.0F) * 1.6F);
                  return true;
               }
            }

            if (metadata == 12 && tileEntity instanceof TileSpa && !player.isSneaking()) {
               FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(player.inventory.getCurrentItem());
               if (fs != null) {
                  int volume = fs.amount;
                  TileSpa tile = (TileSpa)tileEntity;
                  if (tile.tank.getFluidAmount() < tile.tank.getCapacity() && (tile.tank.getFluid() == null || tile.tank.getFluid().isFluidEqual(fs))) {
                     tile.fill(Direction.UNKNOWN, FluidContainerRegistry.getFluidForFilledItem(player.inventory.getCurrentItem()), true);
                     ItemStack emptyContainer = null;
                     FluidContainerRegistry.FluidContainerData[] fcs = FluidContainerRegistry.getRegisteredFluidContainerData();

                     for(FluidContainerRegistry.FluidContainerData fcd : fcs) {
                        if (fcd.filledContainer.isItemEqual(player.inventory.getCurrentItem())) {
                           emptyContainer = fcd.emptyContainer.copy();
                        }
                     }

                     player.inventory.decrStackSize(player.inventory.currentItem, 1);
                     if (emptyContainer != null && !player.inventory.addItemStackToInventory(emptyContainer)) {
                        player.dropPlayerItemWithRandomChoice(emptyContainer, false);
                     }

                     player.inventoryContainer.detectAndSendChanges();
                     tile.markDirty();
                     world.markBlockForUpdate(x, y, z);
                     world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "game.neutral.swim", 0.33F, 1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.3F);
                  }
               } else {
                  player.openGui(Thaumcraft.instance, 19, world, x, y, z);
               }

               return true;
            } else if (metadata == 13 && tileEntity instanceof TileFocalManipulator && !player.isSneaking()) {
               if (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "FOCALMANIPULATION")) {
                  player.openGui(Thaumcraft.instance, 20, world, x, y, z);
               } else if (Platform.getEnvironment() != Env.CLIENT) {
                  player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + StatCollector.translateToLocal("tc.researchmissing")));
               }

               return true;
            } else {
               return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
            }
         }
      }
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata == 5) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         this.setBlockBounds(0.25F, 0.5F, 0.25F, 0.75F, 1.0F, 0.75F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         this.setBlockBounds(0.125F, 0.25F, 0.125F, 0.875F, 0.5F, 0.875F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      }

   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata == 1) {
         this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.99F, 0.75F);
      } else if (metadata == 5) {
         this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
      } else if (metadata == 3) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      } else if (metadata == 4) {
         this.setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, -0.5F, 1.0F);
      } else if (metadata == 8) {
         this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.4375F, 0.9375F);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

      super.setBlockBoundsBasedOnState(world, i, j, k);
   }

   public boolean onBlockEventReceived(Level par1World, int par2, int par3, int par4, int par5, int par6) {
      if (par5 == 1) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            ClientFXUtils.blockSparkle(par1World, par2, par3, par4, 11960575, 2);
            par1World.playAuxSFX(2001, par2, par3, par4, Block.getIdFromBlock(Blocks.stonebrick));
         }

         return true;
      } else {
         return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
      }
   }

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, Direction side) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 11 && side == Direction.UP) {
         return true;
      } else {
         return meta == 12 || super.isSideSolid(world, x, y, z, side);
      }
   }
}
