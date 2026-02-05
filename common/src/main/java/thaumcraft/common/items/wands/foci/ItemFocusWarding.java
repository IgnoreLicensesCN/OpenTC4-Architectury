package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.HitResult.MovingObjectType;
import net.minecraft.core.Direction;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.tiles.TileWarded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemFocusWarding extends ItemFocusBasic implements IArchitect {
   public IIcon iconOrnament;
   IIcon depthIcon = null;
   private static final AspectList<Aspect>cost;
   public static HashMap<String,Long> delay;
   ArrayList<BlockCoordinates> checked = new ArrayList<>();

   public ItemFocusWarding() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "BWA" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.depthIcon = ir.registerIcon("thaumcraft:focus_warding_depth");
      this.icon = ir.registerIcon("thaumcraft:focus_warding");
      this.iconOrnament = ir.registerIcon("thaumcraft:focus_warding_orn");
   }

   public IIcon getFocusDepthLayerIcon(ItemStack itemstack) {
      return this.depthIcon;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
      return renderPass == 1 ? this.icon : this.iconOrnament;
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public IIcon getOrnament(ItemStack itemstack) {
      return this.iconOrnament;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 16771535;
   }

   public AspectList<Aspect>getVisCost(ItemStack itemstack) {
      return cost.copy();
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player player, HitResult mop) {
      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
      player.swingItem();
      if (Platform.getEnvironment() != Env.CLIENT && mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
         String key = mop.blockX + ":" + mop.blockY + ":" + mop.blockZ + ":" + world.dimension();
         if (delay.containsKey(key) && delay.get(key) > System.currentTimeMillis()) {
            return itemstack;
         }

         delay.put(key, System.currentTimeMillis() + 500L);
         TileEntity tt = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
         boolean solid = world.isBlockNormalCubeDefault(mop.blockX, mop.blockY, mop.blockZ, true);
         if (tt == null && solid) {
            for(BlockCoordinates c : this.getArchitectBlocks(itemstack, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player)) {
               if (!wand.consumeAllCentiVis(itemstack, player, this.getVisCost(itemstack), true, false)) {
                  break;
               }

               if (world.getTileEntity(c.x, c.y, c.z) == null && world.isBlockNormalCubeDefault(c.x, c.y, c.z, true)) {
                  Block bi = world.getBlock(c.x, c.y, c.z);
                  int md = world.getBlockMetadata(c.x, c.y, c.z);
                  int ll = bi.getLightValue(world, c.x, c.y, c.z);
                  world.setBlock(c.x, c.y, c.z, ConfigBlocks.blockWarded, md, 3);
                  TileEntity tile = world.getTileEntity(c.x, c.y, c.z);
                  if (tile instanceof TileWarded) {
                     TileWarded tw = (TileWarded)tile;
                     tw.block = bi;
                     tw.blockMd = (byte)md;
                     tw.light = (byte)ll;
                     tw.owner = player.getCommandSenderName().hashCode();
                     world.markBlockForUpdate(c.x, c.y, c.z);
                     PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkleS2C(c.x, c.y, c.z, 16556032), new NetworkRegistry.TargetPoint(world.dimension(), c.x, c.y, c.z, 32.0F));
                  }
               }
            }

            world.playSoundEffect((double)mop.blockX + (double)0.5F, (double)mop.blockY + (double)0.5F, (double)mop.blockZ + (double)0.5F, "thaumcraft:zap", 0.25F, 1.0F);
         } else if (tt instanceof TileWarded) {
            TileWarded tw = (TileWarded)tt;
            if (tw.owner == player.getCommandSenderName().hashCode()) {
               for(BlockCoordinates c : this.getArchitectBlocks(itemstack, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, player)) {
                  TileEntity tile = world.getTileEntity(c.x, c.y, c.z);
                  if (tile instanceof TileWarded) {
                     TileWarded tw2 = (TileWarded)tile;
                     if (tw2.owner == player.getCommandSenderName().hashCode()) {
                        world.setBlock(c.x, c.y, c.z, tw2.block, tw2.blockMd, 3);
                        world.markBlockForUpdate(c.x, c.y, c.z);
                        PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkleS2C(c.x, c.y, c.z, 16556032), new NetworkRegistry.TargetPoint(world.dimension(), c.x, c.y, c.z, 32.0F));
                     }
                  }
               }

               world.playSoundEffect((double)mop.blockX + (double)0.5F, (double)mop.blockY + (double)0.5F, (double)mop.blockZ + (double)0.5F, "thaumcraft:zap", 0.25F, 1.0F);
            }
         }
      }

      return itemstack;
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.architect};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
         default:
            return null;
      }
   }

   public boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
      return !type.equals(FocusUpgradeType.enlarge) || this.isUpgradedWith(focusstack, FocusUpgradeType.architect);
   }

   public int getMaxAreaSize(ItemStack focusstack) {
      return 3 + this.getUpgradeLevel(focusstack, FocusUpgradeType.enlarge);
   }

   public List<BlockPos> getArchitectBlocks(ItemStack stack, World world, int x, int y, int z, int side, Player player) {
      List<BlockPos> out = new ArrayList<>();
      WandCastingItem wand = (WandCastingItem)stack.getItem();
      wand.getFocus(stack);
      this.checked.clear();
      boolean tiles = false;
      TileEntity tt = world.getTileEntity(x, y, z);
      boolean solid = world.isBlockNormalCubeDefault(x, y, z, true);
      if (tt instanceof TileWarded) {
         tiles = true;
      }

      int sizeX = 0;
      int sizeY = 0;
      int sizeZ = 0;
      if (this.isUpgradedWith(wand.getFocusItem(stack), FocusUpgradeType.architect)) {
         sizeX = WandManager.getAreaX(stack);
         sizeY = WandManager.getAreaY(stack);
         sizeZ = WandManager.getAreaZ(stack);
      }

      if (side != 2 && side != 3) {
         this.checkNeighbours(world, x, y, z, new BlockCoordinates(x, y, z), side, sizeX, sizeY, sizeZ, out, player, tiles);
      } else {
         this.checkNeighbours(world, x, y, z, new BlockCoordinates(x, y, z), side, sizeZ, sizeY, sizeX, out, player, tiles);
      }

      return out;
   }

   public void checkNeighbours(World world, int x, int y, int z, BlockCoordinates pos, int side, int sizeX, int sizeY, int sizeZ, ArrayList list, Player player, boolean tiles) {
      if (!this.checked.contains(pos)) {
         this.checked.add(pos);
         switch (side) {
            case 0:
            case 1:
               if (Math.abs(pos.x - x) > sizeX) {
                  return;
               }

               if (Math.abs(pos.z - z) > sizeZ) {
                  return;
               }

               if (Math.abs(pos.y - y) > sizeY) {
                  return;
               }
               break;
            case 2:
            case 3:
               if (Math.abs(pos.x - x) > sizeX) {
                  return;
               }

               if (Math.abs(pos.y - y) > sizeZ) {
                  return;
               }

               if (Math.abs(pos.z - z) > sizeY) {
                  return;
               }
               break;
            case 4:
            case 5:
               if (Math.abs(pos.y - y) > sizeX) {
                  return;
               }

               if (Math.abs(pos.z - z) > sizeZ) {
                  return;
               }

               if (Math.abs(pos.x - x) > sizeY) {
                  return;
               }
         }

         TileEntity tt = world.getTileEntity(pos.x, pos.y, pos.z);
         boolean solid = world.isBlockNormalCubeDefault(pos.x, pos.y, pos.z, true);
         if (!tiles || tt instanceof TileWarded) {
            if (tiles || tt == null && solid) {
               if (tiles && tt != null && tt instanceof TileWarded) {
                  TileWarded tw2 = (TileWarded)tt;
                  if (tw2.owner != player.getCommandSenderName().hashCode()) {
                     return;
                  }
               }

               if (!world.isAirBlock(pos.x, pos.y, pos.z)) {
                  list.add(pos);

                  for(Direction dir : Direction.VALID_DIRECTIONS) {
                     BlockCoordinates cc = new BlockCoordinates(pos.x + dir.offsetX, pos.y + dir.offsetY, pos.z + dir.offsetZ);
                     this.checkNeighbours(world, x, y, z, cc, side, sizeX, sizeY, sizeZ, list, player, tiles);
                  }

               }
            }
         }
      }
   }

   public boolean showAxis(ItemStack stack, World world, Player player, int side, EnumAxis axis) {
      int dim = WandManager.getAreaDim(stack);
      if (dim == 0) {
         return true;
      } else {
         switch (side) {
            case 0:
            case 1:
               if (axis == EnumAxis.X && dim == 1 || axis == EnumAxis.Z && dim == 2 || axis == EnumAxis.Y && dim == 3) {
                  return true;
               }
               break;
            case 2:
            case 3:
               if (axis == EnumAxis.Y && dim == 1 || axis == EnumAxis.X && dim == 2 || axis == EnumAxis.Z && dim == 3) {
                  return true;
               }
               break;
            case 4:
            case 5:
               if (axis == EnumAxis.Y && dim == 1 || axis == EnumAxis.Z && dim == 2 || axis == EnumAxis.X && dim == 3) {
                  return true;
               }
         }

         return false;
      }
   }

   static {
      cost = (new AspectList()).addAll(Aspects.EARTH, 25).addAll(Aspects.ORDER, 25).addAll(Aspects.WATER, 10);
      delay = new HashMap<>();
   }
}
