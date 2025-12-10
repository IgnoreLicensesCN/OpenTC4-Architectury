package thaumcraft.common.blocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.relics.ItemResonator;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.*;

import java.util.LinkedList;
import java.util.List;

public class BlockTube extends BlockContainer {
   public IIcon[] icon = new IIcon[8];
   public IIcon iconValve;
   private RayTracer rayTracer = new RayTracer();

   public BlockTube() {
      super(Material.iron);
      this.setHardness(0.5F);
      this.setResistance(5.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:pipe_1");
      this.icon[1] = ir.registerIcon("thaumcraft:pipe_2");
      this.icon[2] = ir.registerIcon("thaumcraft:pipe_3");
      this.icon[3] = ir.registerIcon("thaumcraft:pipe_filter");
      this.icon[4] = ir.registerIcon("thaumcraft:pipe_filter_core");
      this.icon[5] = ir.registerIcon("thaumcraft:pipe_buffer");
      this.icon[6] = ir.registerIcon("thaumcraft:pipe_restrict");
      this.icon[7] = ir.registerIcon("thaumcraft:pipe_oneway");
      this.iconValve = ir.registerIcon("thaumcraft:pipe_valve");
   }

   public IIcon getIcon(int i, int md) {
      return md == 4 ? this.icon[5] : (md == 5 ? this.icon[6] : this.icon[0]);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
      par3List.add(new ItemStack(par1, 1, 6));
      par3List.add(new ItemStack(par1, 1, 7));
   }

   public int getRenderType() {
      return ConfigBlocks.blockTubeRI;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      boolean noDoodads = Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() == null || !(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemWandCasting) && !(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemResonator);
      if ((metadata == 0 || metadata == 1 || metadata == 3 || metadata == 5 || metadata == 6) && noDoodads) {
         float minx = BlockRenderer.W6;
         float maxx = BlockRenderer.W10;
         float miny = BlockRenderer.W5;
         float maxy = BlockRenderer.W11;
         float minz = BlockRenderer.W5;
         float maxz = BlockRenderer.W11;
         ForgeDirection fd = null;

         for(int side = 0; side < 6; ++side) {
            fd = ForgeDirection.getOrientation(side);
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(world, x, y, z, fd);
            if (te != null) {
               switch (side) {
                  case 0:
                     miny = 0.0F;
                     break;
                  case 1:
                     maxy = 1.0F;
                     break;
                  case 2:
                     minz = 0.0F;
                     break;
                  case 3:
                     maxz = 1.0F;
                     break;
                  case 4:
                     minx = 0.0F;
                     break;
                  case 5:
                     maxx = 1.0F;
               }
            }
         }

         this.setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
      }

      if (metadata == 4 && noDoodads) {
         this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
      }

      if (metadata == 7) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

      return super.getSelectedBoundingBoxFromPool(world, x, y, z);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata == 2) {
         this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
      } else if (metadata == 7) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (metadata != 0 && metadata != 1 && metadata != 3 && metadata != 4 && metadata != 5 && metadata != 6) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      } else {
         float minx = BlockRenderer.W6;
         float maxx = BlockRenderer.W10;
         float miny = BlockRenderer.W6;
         float maxy = BlockRenderer.W10;
         float minz = BlockRenderer.W6;
         float maxz = BlockRenderer.W10;
         ForgeDirection fd = null;

         for(int side = 0; side < 6; ++side) {
            fd = ForgeDirection.getOrientation(side);
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(world, i, j, k, fd);
            if (te != null) {
               switch (side) {
                  case 0:
                     miny = 0.0F;
                     break;
                  case 1:
                     maxy = 1.0F;
                     break;
                  case 2:
                     minz = 0.0F;
                     break;
                  case 3:
                     maxz = 1.0F;
                     break;
                  case 4:
                     minx = 0.0F;
                     break;
                  case 5:
                     maxx = 1.0F;
               }
            }
         }

         this.setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
      }

   }

   public int damageDropped(int metadata) {
      return metadata;
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileTube();
      } else if (metadata == 1) {
         return new TileTubeValve();
      } else if (metadata == 2) {
         return new TileCentrifuge();
      } else if (metadata == 3) {
         return new TileTubeFilter();
      } else if (metadata == 4) {
         return new TileTubeBuffer();
      } else if (metadata == 5) {
         return new TileTubeRestrict();
      } else if (metadata == 6) {
         return new TileTubeOneway();
      } else {
         return metadata == 7 ? new TileEssentiaCrystalizer() : super.createTileEntity(world, metadata);
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileTubeBuffer) {
         float var10000 = (float)((TileTubeBuffer)te).aspects.visSize();
         ((TileTubeBuffer)te).getClass();
         float r = var10000 / 8.0F;
         return MathHelper.floor_float(r * 14.0F) + (((TileTubeBuffer)te).aspects.visSize() > 0 ? 1 : 0);
      } else {
         return 0;
      }
   }

   public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileTubeFilter && ((TileTubeFilter) te).aspectFilter != null && !world.isRemote) {
         world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, new ItemStack(ConfigItems.itemResource, 1, 13)));
      }

      super.breakBlock(world, x, y, z, par5, par6);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 1) {
         if (player.getHeldItem() != null && (player.getHeldItem().getItem() instanceof ItemWandCasting || player.getHeldItem().getItem() instanceof ItemResonator || player.getHeldItem().getItem() == Item.getItemFromBlock(this))) {
            return false;
         }

         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileTubeValve) {
            ((TileTubeValve)te).allowFlow = !((TileTubeValve)te).allowFlow;
            world.markBlockForUpdate(x, y, z);
            if (!world.isRemote) {
               world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:squeek", 0.7F, 0.9F + world.rand.nextFloat() * 0.2F);
            }

            return true;
         }
      }

      if (metadata == 3) {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileTubeFilter && player.isSneaking() && ((TileTubeFilter) te).aspectFilter != null) {
            ((TileTubeFilter)te).aspectFilter = null;
            world.markBlockForUpdate(x, y, z);
            if (world.isRemote) {
               world.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "thaumcraft:page", 1.0F, 1.0F, false);
            } else {
               ForgeDirection fd = ForgeDirection.getOrientation(side);
               world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F + (float)fd.offsetX / 3.0F, (float)y + 0.5F, (float)z + 0.5F + (float)fd.offsetZ / 3.0F, new ItemStack(ConfigItems.itemResource, 1, 13)));
            }

            return true;
         }

         if (te instanceof TileTubeFilter && player.getHeldItem() != null && ((TileTubeFilter) te).aspectFilter == null && player.getHeldItem().getItem() == ConfigItems.itemResource && player.getHeldItem().getItemDamage() == 13) {
            if (((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()) != null) {
               ((TileTubeFilter)te).aspectFilter = ((IEssentiaContainerItem) player.getHeldItem().getItem()).getAspects(player.getHeldItem()).getAspects()[0];
               --player.getHeldItem().stackSize;
               world.markBlockForUpdate(x, y, z);
               if (world.isRemote) {
                  world.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "thaumcraft:page", 1.0F, 1.0F, false);
               }
            }

            return true;
         }
      }

      return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void onBlockHighlight(DrawBlockHighlightEvent event) {
      if (event.target.typeOfHit == MovingObjectType.BLOCK && event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) == this && event.player.worldObj.getBlockMetadata(event.target.blockX, event.target.blockY, event.target.blockZ) != 7 && event.player.getCurrentEquippedItem() != null && (event.player.getCurrentEquippedItem().getItem() instanceof ItemWandCasting || event.player.getCurrentEquippedItem().getItem() instanceof ItemResonator)) {
         RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
      }

   }

   public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if ((tile instanceof TileTube || tile instanceof TileTubeBuffer)) {
         List<IndexedCuboid6> cuboids = new LinkedList<>();
         if (tile instanceof TileTube) {
            ((TileTube)tile).addTraceableCuboids(cuboids);
         } else if (tile instanceof TileTubeBuffer) {
            ((TileTubeBuffer)tile).addTraceableCuboids(cuboids);
         }

         return this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
      } else {
         return super.collisionRayTrace(world, x, y, z, start, end);
      }
   }
}
