package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockContainer;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.misc.ItemShard;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.*;

import java.util.List;
import java.util.Random;

//  "tile.blockMetalDevice.0.name": "坩埚",
//  "tile.blockMetalDevice.1.name": "奥术蒸馏器",
//  "tile.blockMetalDevice.2.name": "魔力充能中继器",--done
//  "tile.blockMetalDevice.3.name": "高级炼金构材",
// 4?anazor forgot that.
//  "tile.blockMetalDevice.5.name": "物品格栅",
//  "tile.blockMetalDevice.6.name": "物品格栅(关闭)",
//  "tile.blockMetalDevice.7.name": "奥术灯",
//  "tile.blockMetalDevice.8.name": "催生灯",
//  "tile.blockMetalDevice.9.name": "炼金构材",
//  "tile.blockMetalDevice.10.name": "神秘炼金塔",
//  "tile.blockMetalDevice.11.name": "神秘炼金塔",
//  "tile.blockMetalDevice.12.name": "记忆矩阵",
//  "tile.blockMetalDevice.13.name": "育种灯",
//  "tile.blockMetalDevice.14.name": "魔力中继器",--done
public class BlockMetalDevice extends BlockContainer {
   public IIcon[] icon = new IIcon[23];
   public IIcon iconGlow;
   private int delay = 0;

   public BlockMetalDevice() {
      super(Material.iron);
      this.setHardness(3.0F);
      this.setResistance(17.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public static boolean addToPlayerInventoryBiased(InventoryPlayer inv, ItemStack s) {
      if (s == null || s.stackSize == 0 || s.getItem() == null) return false;
      // logic: first try to stack, then try to place in original slot, last fallback to vanilla logic
      for (ItemStack stack : inv.mainInventory) {
         if (stack != null && stack.isStackable() && stack.getItem() == s.getItem() &&
                 stack.stackSize < Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit())
                 && (!stack.getHasSubtypes() || stack.getItemDamage() == s.getItemDamage()) &&
                 ItemStack.areItemStackTagsEqual(stack, s)) {
            int toAdd = Math.min(s.stackSize, Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()) - stack.stackSize);
            s.stackSize -= toAdd;
            stack.stackSize += toAdd;
            stack.animationsToGo = 5;
            if (s.stackSize == 0) return true;
         }
      }
      if (inv.currentItem >= 0 && inv.currentItem < InventoryPlayer.getHotbarSize() && inv.getCurrentItem() == null) {
         inv.setInventorySlotContents(inv.currentItem, s);
         return true;
      } else {
         return inv.addItemStackToInventory(s);
      }
   }

   public IIcon getIcon(int i, int md) {
      if (md == 3) {
         return this.icon[22];
      } else if (md == 7) {
         return this.icon[10];
      } else if (md == 8) {
         return this.icon[12];
      } else if (md != 10 && md != 9 && md != 11) {
         if (md == 12) {
            return this.icon[17];
         } else if (md == 13) {
            return this.icon[18];
         } else if (md != 14 && md != 2) {
            return md != 0 && md != 1 && md != 5 && md != 6 ? this.icon[7] : this.icon[0];
         } else {
            return this.icon[0];//== 14 || == 2
         }
      } else {
         return this.icon[16];
      }
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:metalbase");

      for (int a = 1; a <= 6; ++a) {
         this.icon[a] = ir.registerIcon("thaumcraft:crucible" + a);
      }

      this.icon[7] = ir.registerIcon("thaumcraft:goldbase");
      this.icon[8] = ir.registerIcon("thaumcraft:grate");
      this.icon[9] = ir.registerIcon("thaumcraft:grate_hatch");
      this.icon[10] = ir.registerIcon("thaumcraft:lamp_side");
      this.icon[11] = ir.registerIcon("thaumcraft:lamp_top");
      this.icon[12] = ir.registerIcon("thaumcraft:lamp_grow_side");
      this.icon[13] = ir.registerIcon("thaumcraft:lamp_grow_top");
      this.icon[14] = ir.registerIcon("thaumcraft:lamp_grow_side_off");
      this.icon[15] = ir.registerIcon("thaumcraft:lamp_grow_top_off");
      this.icon[16] = ir.registerIcon("thaumcraft:alchemyblock");
      this.icon[17] = ir.registerIcon("thaumcraft:brainbox");
      this.icon[18] = ir.registerIcon("thaumcraft:lamp_fert_side");
      this.icon[19] = ir.registerIcon("thaumcraft:lamp_fert_top");
      this.icon[20] = ir.registerIcon("thaumcraft:lamp_fert_side_off");
      this.icon[21] = ir.registerIcon("thaumcraft:lamp_fert_top_off");
      this.icon[22] = ir.registerIcon("thaumcraft:alchemyblockadv");
      this.iconGlow = ir.registerIcon("thaumcraft:animatedglow");
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));

      par3List.add(new ItemStack(par1, 1, 5));
      par3List.add(new ItemStack(par1, 1, 7));
      par3List.add(new ItemStack(par1, 1, 8));
      par3List.add(new ItemStack(par1, 1, 9));

      par3List.add(new ItemStack(par1, 1, 12));
      par3List.add(new ItemStack(par1, 1, 13));
      par3List.add(new ItemStack(par1, 1, 14));
   }

   public int getRenderType() {
      return ConfigBlocks.blockMetalDeviceRI;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int side) {
      int metadata = iblockaccess.getBlockMetadata(i, j, k);
      if (metadata != 5 && metadata != 6) {
         if (metadata == 7) {
            return side <= 1 ? this.icon[11] : this.icon[10];
         } else {
            if (metadata == 8) {
               TileEntity te = iblockaccess.getTileEntity(i, j, k);
               if (te instanceof TileArcaneLampGrowth) {
                  if (((TileArcaneLampGrowth) te).charges > 0) {
                     if (side <= 1) {
                        return this.icon[13];
                     }

                     return this.icon[12];
                  }

                  if (side <= 1) {
                     return this.icon[15];
                  }

                  return this.icon[14];
               }
            } else if (metadata == 13) {
               TileEntity te = iblockaccess.getTileEntity(i, j, k);
               if (te instanceof TileArcaneLampFertility) {
                  if (((TileArcaneLampFertility) te).charges > 0) {
                     if (side <= 1) {
                        return this.icon[19];
                     }

                     return this.icon[18];
                  }

                  if (side <= 1) {
                     return this.icon[21];
                  }

                  return this.icon[20];
               }
            } else {
               if (metadata == 10 || metadata == 9 || metadata == 11) {
                  return this.icon[16];
               }

               if (metadata == 12) {
                  return this.icon[17];
               }

               if (metadata == 3) {
                  return this.icon[22];
               }
            }

            if (side == 1) {
               return this.icon[1];
            } else if (side == 0) {
               return this.icon[2];
            } else {
               return this.icon[3];
            }
         }
      } else {
         return this.icon[8];
      }
   }

   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         int metadata = world.getBlockMetadata(i, j, k);
         if (metadata == 0) {
            TileCrucible tile = (TileCrucible) world.getTileEntity(i, j, k);
            if (tile != null && entity instanceof EntityItem && !(entity instanceof EntitySpecialItem) && tile.heat > 150 && tile.tank.getFluidAmount() > 0) {
               tile.attemptSmelt((EntityItem) entity);
            } else {
               ++this.delay;
               if (this.delay < 10) {
                  return;
               }

               this.delay = 0;
               if (entity instanceof EntityLivingBase && tile != null && tile.heat > 150 && tile.tank.getFluidAmount() > 0) {
                  entity.attackEntityFrom(DamageSource.inFire, 1.0F);
                  world.playSoundEffect(i, j, k, "random.fizz", 0.4F, 2.0F + world.getRandom().nextFloat() * 0.4F);
               }
            }
         }
      }

   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata != 5 && metadata != 6) {
         if (metadata != 7 && metadata != 8 && metadata != 13) {
            if (metadata == 10) {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
            }
            else if (metadata == 11) {
               this.setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
            else if (metadata == 12) {
               this.setBlockBounds(BlockRenderer.W3, BlockRenderer.W3, BlockRenderer.W3, BlockRenderer.W13, BlockRenderer.W13, BlockRenderer.W13);
            }
            else if (metadata == 2) {
               this.setBlockBounds(BlockRenderer.W5, 0.5F, BlockRenderer.W5, BlockRenderer.W11, 1.0F, BlockRenderer.W11);
            }
            else if (metadata == 14) {
               TileEntity te = world.getTileEntity(i, j, k);
               if (te instanceof TileVisRelay) {
                  switch (Direction.getOrientation(((TileVisRelay) te).orientation).getOpposite()) {
                     case UP:
                        this.setBlockBounds(BlockRenderer.W5, 0.5F, BlockRenderer.W5, BlockRenderer.W11, 1.0F, BlockRenderer.W11);
                        break;
                     case DOWN:
                        this.setBlockBounds(BlockRenderer.W5, 0.0F, BlockRenderer.W5, BlockRenderer.W11, 0.5F, BlockRenderer.W11);
                        break;
                     case EAST:
                        this.setBlockBounds(0.5F, BlockRenderer.W5, BlockRenderer.W5, 1.0F, BlockRenderer.W11, BlockRenderer.W11);
                        break;
                     case WEST:
                        this.setBlockBounds(0.0F, BlockRenderer.W5, BlockRenderer.W5, 0.5F, BlockRenderer.W11, BlockRenderer.W11);
                        break;
                     case SOUTH:
                        this.setBlockBounds(BlockRenderer.W5, BlockRenderer.W5, 0.5F, BlockRenderer.W11, BlockRenderer.W11, 1.0F);
                        break;
                     case NORTH:
                        this.setBlockBounds(BlockRenderer.W5, BlockRenderer.W5, 0.0F, BlockRenderer.W11, BlockRenderer.W11, 0.5F);
                  }
               }
            }
            else {
               this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);//== 1,== 3,== 4,== 9
            }
         } else {
            this.setBlockBounds(BlockRenderer.W4, BlockRenderer.W2, BlockRenderer.W4, BlockRenderer.W12, BlockRenderer.W14, BlockRenderer.W12);
            //== 7,== 8, == 13
         }
      } else {
         this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);// == 5,== 6
      }

      super.setBlockBoundsBasedOnState(world, i, j, k);
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World w, int i, int j, int k, Random r) {
      if (r.nextInt(10) == 0) {
         TileEntity te = w.getTileEntity(i, j, k);
         if (te instanceof TileCrucible && ((TileCrucible) te).tank.getFluidAmount() > 0 && ((TileCrucible) te).heat > 150) {
            w.playSound(i, j, k, "liquid.lavapop", 0.1F + r.nextFloat() * 0.1F, 1.2F + r.nextFloat() * 0.2F, false);
         }
      }

   }

   public int damageDropped(int metadata) {
      if (metadata == 6) {
         return 5;
      } else {
         return metadata != 10 && metadata != 11 ? metadata : 9;
      }
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileCrucible();
      } else if (metadata == 5) {
         return new TileGrate();
      } else if (metadata == 6) {
         return new TileGrate();
      } else if (metadata == 1) {
         return new TileAlembic();
      } else if (metadata == 7) {
         return new TileArcaneLamp();
      } else if (metadata == 8) {
         return new TileArcaneLampGrowth();
      } else if (metadata == 10) {
         return new TileThaumatorium();
      } else if (metadata == 11) {
         return new TileThaumatoriumTop();
      } else if (metadata == 12) {
         return new TileBrainbox();
      } else if (metadata == 13) {
         return new TileArcaneLampFertility();
      } else if (metadata == 14) {
         return new TileVisRelay();
      } else {
         return metadata == 2 ? new TileMagicWorkbenchCharger() : super.createTileEntity(world, metadata);
      }
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata == 0) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         float f = 0.125F;
         this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 0.85F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.85F, f);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 0.85F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 0.85F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else if (metadata == 2) {
         this.setBlockBounds(BlockRenderer.W5, 0.5F, BlockRenderer.W5, BlockRenderer.W11, 1.0F, BlockRenderer.W11);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else if (metadata == 5) {
         if (par7Entity != null && !(par7Entity instanceof EntityItem)) {
            this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         }
      } else if (metadata == 6) {
         this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else if (metadata != 7 && metadata != 8 && metadata != 13) {
         if (metadata == 12) {
            this.setBlockBounds(BlockRenderer.W3, BlockRenderer.W3, BlockRenderer.W3, BlockRenderer.W13, BlockRenderer.W13, BlockRenderer.W13);
            super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         } else if (metadata == 14) {
            TileEntity te = world.getTileEntity(i, j, k);
            if (te instanceof TileVisRelay) {
               switch (Direction.getOrientation(((TileVisRelay) te).orientation).getOpposite()) {
                  case UP:
                     this.setBlockBounds(BlockRenderer.W5, 0.5F, BlockRenderer.W5, BlockRenderer.W11, 1.0F, BlockRenderer.W11);
                     break;
                  case DOWN:
                     this.setBlockBounds(BlockRenderer.W5, 0.0F, BlockRenderer.W5, BlockRenderer.W11, 0.5F, BlockRenderer.W11);
                     break;
                  case EAST:
                     this.setBlockBounds(0.5F, BlockRenderer.W5, BlockRenderer.W5, 1.0F, BlockRenderer.W11, BlockRenderer.W11);
                     break;
                  case WEST:
                     this.setBlockBounds(0.0F, BlockRenderer.W5, BlockRenderer.W5, 0.5F, BlockRenderer.W11, BlockRenderer.W11);
                     break;
                  case SOUTH:
                     this.setBlockBounds(BlockRenderer.W5, BlockRenderer.W5, 0.5F, BlockRenderer.W11, BlockRenderer.W11, 1.0F);
                     break;
                  case NORTH:
                     this.setBlockBounds(BlockRenderer.W5, BlockRenderer.W5, 0.0F, BlockRenderer.W11, BlockRenderer.W11, 0.5F);
               }

               super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
            }
         } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
         }
      } else {
         this.setBlockBounds(BlockRenderer.W4, BlockRenderer.W2, BlockRenderer.W4, BlockRenderer.W12, BlockRenderer.W14, BlockRenderer.W12);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      }

   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileThaumatorium) {
         return Container.calcRedstoneFromInventory((IInventory) te);
      } else if (te instanceof TileAlembic) {
         float r = (float) ((TileAlembic) te).amount / (float) ((TileAlembic) te).maxAmount;
         return MathHelper.floor_float(r * 14.0F) + (((TileAlembic) te).amount > 0 ? 1 : 0);
      } else if (te instanceof TileCrucible) {
         float var10000 = (float) ((TileCrucible) te).aspects.visSize();
         ((TileCrucible) te).getClass();
         float r = var10000 / 100.0F;
         return MathHelper.floor_float(r * 14.0F) + (((TileCrucible) te).aspects.visSize() > 0 ? 1 : 0);
      } else {
         return 0;
      }
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
      TileEntity te = world.getTileEntity(x, y, z);
      int md = world.getBlockMetadata(x, y, z);
      if (te instanceof TileCrucible) {
         ((TileCrucible) te).getBellows();
      }

      if (Platform.getEnvironment() != Env.CLIENT) {
         if (te instanceof TileAlembic) {
            world.markBlockForUpdate(x, y, z);
         } else if (te instanceof TileArcaneLamp) {
            TileArcaneLamp telb = (TileArcaneLamp) te;
            if (world.isAirBlock(x + telb.facing.offsetX, y + telb.facing.offsetY, z + telb.facing.offsetZ)) {
               this.dropBlockAsItem(world, x, y, z, 7, 0);
               world.setBlockToAir(x, y, z);
            }
         } else if (te instanceof TileArcaneLampGrowth) {
            TileArcaneLampGrowth telb = (TileArcaneLampGrowth) te;
            if (world.isAirBlock(x + telb.facing.offsetX, y + telb.facing.offsetY, z + telb.facing.offsetZ)) {
               this.dropBlockAsItem(world, x, y, z, 8, 0);
               world.setBlockToAir(x, y, z);
            }
         } else if (te instanceof TileBrainbox) {
            TileBrainbox telb = (TileBrainbox) te;
            if (world.isAirBlock(x + telb.facing.offsetX, y + telb.facing.offsetY, z + telb.facing.offsetZ)) {
               this.dropBlockAsItem(world, x, y, z, 12, 0);
               world.setBlockToAir(x, y, z);
            }
         } else if (te instanceof TileVisRelay && md == 14) {
            TileVisRelay telb = (TileVisRelay) te;
            if (world.isAirBlock(x + Direction.getOrientation(telb.orientation).getOpposite().offsetX, y + Direction.getOrientation(telb.orientation).getOpposite().offsetY, z + Direction.getOrientation(telb.orientation).getOpposite().offsetZ)) {
               this.dropBlockAsItem(world, x, y, z, 14, 0);
               world.setBlockToAir(x, y, z);
            }
         } else if (md == 10) {
            if (world.getBlock(x, y + 1, z) != this || world.getBlockMetadata(x, y + 1, z) != 11 || world.getBlock(x, y - 1, z) != this || world.getBlockMetadata(x, y - 1, z) != 0) {
               InventoryUtils.dropItems(world, x, y, z);
               world.setBlockToAir(x, y, z);
               world.setBlock(x, y, z, this, 9, 3);
               return;
            }

            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileThaumatorium) {
               ((TileThaumatorium) tile).getUpgrades();
            }
         } else if (md == 11) {
            if (world.getBlock(x, y - 1, z) != this || world.getBlockMetadata(x, y - 1, z) != 10) {
               world.setBlockToAir(x, y, z);
               world.setBlock(x, y, z, this, 9, 3);
               return;
            }

            TileEntity tile = world.getTileEntity(x, y - 1, z);
            if (tile instanceof TileThaumatorium) {
               ((TileThaumatorium) tile).getUpgrades();
            }
         }

         boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
         if (flag || nbid.canProvidePower()) {
            this.onPoweredBlockChange(world, x, y, z, flag);
         }
      }

      super.onNeighborBlockChange(world, x, y, z, nbid);
   }

   public void breakBlock(Level par1World, int par2, int par3, int par4, Block par5, int par6) {
      InventoryUtils.dropItems(par1World, par2, par3, par4);
      TileEntity te = par1World.getTileEntity(par2, par3, par4);
      if (te instanceof TileCrucible) {
         ((TileCrucible) te).spillRemnants();
      } else if (te instanceof TileAlembic && ((TileAlembic) te).aspectFilter != null) {
         par1World.spawnEntityInWorld(new EntityItem(par1World, (float) par2 + 0.5F, (float) par3 + 0.5F, (float) par4 + 0.5F, new ItemStack(ThaumcraftItems.JAR_LABEL, 1)));
      } else if (te instanceof TileArcaneLamp) {
         ((TileArcaneLamp) te).removeLights();
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, Player player, int side, float par7, float par8, float par9) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 0 && Platform.getEnvironment() != Env.CLIENT) {
         FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(player.inventory.getCurrentItem());
         if (fs != null && fs.isFluidEqual(new FluidStack(FluidRegistry.WATER, 1000))) {
            int volume = fs.amount;
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileCrucible) {
               TileCrucible tile = (TileCrucible) te;
               if (tile.tank.getFluidAmount() >= tile.tank.getCapacity()) {
                  return true;
               }

               tile.fill(Direction.UNKNOWN, FluidContainerRegistry.getFluidForFilledItem(player.inventory.getCurrentItem()), true);
               ItemStack emptyContainer = null;
               FluidContainerRegistry.FluidContainerData[] fcs = FluidContainerRegistry.getRegisteredFluidContainerData();

               for (FluidContainerRegistry.FluidContainerData fcd : fcs) {
                  if (fcd.filledContainer.isItemEqual(player.inventory.getCurrentItem())) {
                     emptyContainer = fcd.emptyContainer.copy();
                  }
               }

               player.inventory.decrStackSize(player.inventory.currentItem, 1);
               if (emptyContainer != null && !addToPlayerInventoryBiased(player.inventory, emptyContainer)) {
                  player.dropPlayerItemWithRandomChoice(emptyContainer, false);
               }

               player.inventoryContainer.detectAndSendChanges();
               te.markDirty();
               world.markBlockForUpdate(x, y, z);
               world.playSoundEffect((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "game.neutral.swim", 0.33F, 1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.3F);
            }
         }
      }

      if (metadata == 1 && Platform.getEnvironment() != Env.CLIENT && !player.isSneaking() && player.getHeldItem() == null) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileAlembic) {
            TileAlembic tile = (TileAlembic) te;
            String msg = "";
            if (tile.aspect != null && tile.amount != 0) {
               if ((double) tile.amount < (double) tile.maxAmount * 0.4) {
                  msg = StatCollector.translateToLocal("tile.alembic.msg.2");
               } else if ((double) tile.amount < (double) tile.maxAmount * 0.8) {
                  msg = StatCollector.translateToLocal("tile.alembic.msg.3");
               } else if (tile.amount < tile.maxAmount) {
                  msg = StatCollector.translateToLocal("tile.alembic.msg.4");
               } else if (tile.amount == tile.maxAmount) {
                  msg = StatCollector.translateToLocal("tile.alembic.msg.5");
               }
            } else {
               msg = StatCollector.translateToLocal("tile.alembic.msg.1");
            }

            player.addChatMessage(new ChatComponentTranslation("§3" + msg));
            world.playSoundEffect(x, y, z, "thaumcraft:alembicknock", 0.2F, 1.0F);
         }
      }

      if (metadata == 1) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileAlembic) {
            if (player.isSneaking() && ((TileAlembic) te).aspectFilter != null) {
               ((TileAlembic) te).aspectFilter = null;
               world.markBlockForUpdate(x, y, z);
               te.markDirty();
               if ((Platform.getEnvironment() == Env.CLIENT)) {
                  world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "thaumcraft:page", 1.0F, 1.1F, false);
               } else {
                  Direction fd = Direction.getOrientation(side);
                  world.spawnEntityInWorld(new EntityItem(world, (float) x + 0.5F + (float) fd.offsetX / 3.0F, (float) y + 0.5F, (float) z + 0.5F + (float) fd.offsetZ / 3.0F, new ItemStack(ThaumcraftItems.JAR_LABEL, 1)));
               }

               return true;
            }

            if (player.isSneaking() && player.getHeldItem() == null) {
               ((TileAlembic) te).amount = 0;
               ((TileAlembic) te).aspect = null;
               if ((Platform.getEnvironment() == Env.CLIENT)) {
                  world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "thaumcraft:alembicknock", 0.2F, 1.0F, false);
                  world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "game.neutral.swim", 0.5F, 1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.3F, false);
               }
            } else {
               if (player.getHeldItem() != null && ((TileAlembic) te).aspectFilter == null && player.getHeldItem().getItem() == ConfigItems.itemResource && player.getHeldItem().getItemDamage() == 13) {
                  if (((TileAlembic) te).amount == 0 && ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()) == null) {
                     return true;
                  }

                  if (((TileAlembic) te).amount == 0 && ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()) != null) {
                     ((TileAlembic) te).aspect = ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()).getAspects()[0];
                  }

                  --player.getHeldItem().stackSize;
                  ((TileAlembic) te).aspectFilter = ((TileAlembic) te).aspect;
                  world.markBlockForUpdate(x, y, z);
                  te.markDirty();
                  if ((Platform.getEnvironment() == Env.CLIENT)) {
                     world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "thaumcraft:page", 1.0F, 0.9F, false);
                  }

                  return true;
               }

               if (player.getHeldItem() != null && ((TileAlembic) te).amount > 0 && (player.getHeldItem().getItem() == ConfigItems.itemJarFilled || player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 0)) || player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 3)))) {
                  boolean doit = false;
                  ItemStack drop = null;
                  if (!player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 0)) && !player.getHeldItem().isItemEqual(new ItemStack(ConfigBlocks.blockJar, 1, 3))) {
                     drop = player.getHeldItem();
                     if ((((ItemJarFilled) drop.getItem()).getAspects(drop) == null || ((ItemJarFilled) drop.getItem()).getAspects(drop).visSize() == 0 || ((ItemJarFilled) drop.getItem()).getAspects(drop).getAmount(((TileAlembic) te).aspect) > 0) && (((ItemJarFilled) drop.getItem()).getFilter(drop) == null || ((ItemJarFilled) drop.getItem()).getFilter(drop) == ((TileAlembic) te).aspect)) {
                        int amount = Math.min(((ItemJarFilled) drop.getItem()).getAspects(drop) == null ? 64 : 64 - ((ItemJarFilled) drop.getItem()).getAspects(drop).visSize(), ((TileAlembic) te).amount);
                        if (drop.getItemDamage() == 3) {
                           amount = ((TileAlembic) te).amount;
                        }

                        if (amount > 0) {
                           ((TileAlembic) te).amount -= amount;
                           AspectList<Aspect>as = ((ItemJarFilled) drop.getItem()).getAspects(drop);
                           if (as == null) {
                              as = new AspectList<>();
                           }

                           as.addAll(((TileAlembic) te).aspect, amount);
                           if (as.getAmount(((TileAlembic) te).aspect) > 64) {
                              int q = as.getAmount(((TileAlembic) te).aspect) - 64;
                              as.tryReduce(((TileAlembic) te).aspect, q);
                           }

                           ((ItemJarFilled) drop.getItem()).setAspects(drop, as);
                           if (((TileAlembic) te).amount <= 0) {
                              ((TileAlembic) te).aspect = null;
                           }

                           doit = true;
                           player.setCurrentItemOrArmor(0, drop);
                        }
                     }
                  } else {
                     drop = new ItemStack(ConfigItems.itemJarFilled, 1, player.getHeldItem().getItemDamage());
                     doit = true;
                     ((ItemJarFilled) drop.getItem()).setAspects(drop, (new AspectList<>()).addAll(((TileAlembic) te).aspect, ((TileAlembic) te).amount));
                     ((TileAlembic) te).amount = 0;
                     ((TileAlembic) te).aspect = null;
                     --player.getHeldItem().stackSize;
                     if (!addToPlayerInventoryBiased(player.inventory, drop) && Platform.getEnvironment() != Env.CLIENT) {
                        world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, drop));
                     }
                  }

                  if (doit) {
                     te.markDirty();
                     world.markBlockForUpdate(x, y, z);
                     if ((Platform.getEnvironment() == Env.CLIENT)) {
                        world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "game.neutral.swim", 0.5F, 1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.3F, false);
                     }
                  }

                  return true;
               }
            }
         }
      }

      if (metadata == 5) {
         world.setBlockMetadataWithNotify(x, y, z, 6, 2);
         world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
         return true;
      } else if (metadata == 6) {
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);
         world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
         return true;
      } else if ((Platform.getEnvironment() == Env.CLIENT)) {
         return true;
      } else {
         if (metadata == 10) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileThaumatorium && !player.isSneaking()) {
               player.openGui(Thaumcraft.instance, 3, world, x, y, z);
               return true;
            }
         }

         if (metadata == 11) {
            TileEntity te = world.getTileEntity(x, y - 1, z);
            if (te instanceof TileThaumatorium && !player.isSneaking()) {
               player.openGui(Thaumcraft.instance, 3, world, x, y - 1, z);
               return true;
            }
         }

         if ((metadata == 14 || metadata == 2)
                 && !player.isSneaking()
                 && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemShard) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileVisRelay) {
               TileVisRelay tile = (TileVisRelay) te;
               byte c = (byte) player.getHeldItem().getItemDamage();
               if (c != tile.color && c != 6) {
                  tile.color = c;
               } else {
                  tile.color = -1;
               }

               tile.removeThisNode();
               tile.nodeRefresh = true;
               tile.markDirty();
               world.markBlockForUpdate(x, y, z);
               world.playSoundEffect(x, y, z, "thaumcraft:crystal", 0.2F, 1.0F);
            }
         }

         return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
      }
   }

   public void onPoweredBlockChange(Level par1World, int par2, int par3, int par4, boolean flag) {
      int l = par1World.getBlockMetadata(par2, par3, par4);
      if (l == 5 && flag) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 6, 2);
         par1World.playAuxSFXAtEntity(null, 1003, par2, par3, par4, 0);
      } else if (l == 6 && !flag) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
         par1World.playAuxSFXAtEntity(null, 1003, par2, par3, par4, 0);
      }

   }

   public void onBlockPlacedBy(World world, int par2, int par3, int par4, EntityLivingBase ent, ItemStack stack) {
      int l = MathHelper.floor_double((double) (ent.rotationYaw * 4.0F / 360.0F) + (double) 0.5F) & 3;
      if (stack.getItemDamage() == 1) {
         TileEntity tile = world.getTileEntity(par2, par3, par4);
         if (tile instanceof TileAlembic) {
            if (l == 0) {
               ((TileAlembic) tile).facing = 2;
            }

            if (l == 1) {
               ((TileAlembic) tile).facing = 5;
            }

            if (l == 2) {
               ((TileAlembic) tile).facing = 3;
            }

            if (l == 3) {
               ((TileAlembic) tile).facing = 4;
            }
         }
      }

   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int md = world.getBlockMetadata(x, y, z);
      if (md == 3) {
         return 11;
      } else if (md == 7) {
         return 15;
      } else {
         if (md == 8) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileArcaneLampGrowth) {
               if (((TileArcaneLampGrowth) te).charges > 0) {
                  return 15;
               }

               return 8;
            }
         } else if (md == 13) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileArcaneLampFertility) {
               if (((TileArcaneLampFertility) te).charges > 0) {
                  return 15;
               }

               return 8;
            }
         } else if (md == 14) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileVisRelay) {
               if (VisNetHandler.isNodeValid(((TileVisRelay) te).getParent())) {
                  return 10;
               }

               return 2;
            }
         }

         return super.getLightValue(world, x, y, z);
      }
   }
}
