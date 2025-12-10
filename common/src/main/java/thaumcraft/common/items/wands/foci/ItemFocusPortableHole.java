package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileHole;

public class ItemFocusPortableHole extends ItemFocusBasic {
   IIcon depthIcon = null;
   private static final AspectList cost;

   public ItemFocusPortableHole() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "BPH" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.depthIcon = ir.registerIcon("thaumcraft:focus_portablehole_depth");
      this.icon = ir.registerIcon("thaumcraft:focus_portablehole");
   }

   public IIcon getFocusDepthLayerIcon(ItemStack itemstack) {
      return this.depthIcon;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 594985;
   }

   public AspectList getVisCost(ItemStack itemstack) {
      return cost.copy();
   }

   public static boolean createHole(World world, int ii, int jj, int kk, int side, byte count, int max) {
      Block bi = world.getBlock(ii, jj, kk);
      if (world.getTileEntity(ii, jj, kk) == null && !ThaumcraftApi.portableHoleBlackList.contains(bi) && bi != Blocks.bedrock && bi != ConfigBlocks.blockHole && !bi.isAir(world, ii, jj, kk) && !bi.canPlaceBlockAt(world, ii, jj, kk) && bi.getBlockHardness(world, ii, jj, kk) != -1.0F) {
         TileHole ts = new TileHole(bi, world.getBlockMetadata(ii, jj, kk), (short)max, count, (byte)side, null);
         world.setBlock(ii, jj, kk, Blocks.air, 0, 0);
         if (world.setBlock(ii, jj, kk, ConfigBlocks.blockHole, 0, 0)) {
            world.setTileEntity(ii, jj, kk, ts);
         }

         world.markBlockForUpdate(ii, jj, kk);
         Thaumcraft.proxy.blockSparkle(world, ii, jj, kk, 4194368, 1);
         return true;
      } else {
         return false;
      }
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition mop) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
      if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
         if (world.provider.dimensionId == Config.dimensionOuterId) {
            if (!world.isRemote) {
               world.playSoundEffect((double)mop.blockX + (double)0.5F, (double)mop.blockY + (double)0.5F, (double)mop.blockZ + (double)0.5F, "thaumcraft:wandfail", 1.0F, 1.0F);
            }

            player.swingItem();
            return itemstack;
         }

         int ii = mop.blockX;
         int jj = mop.blockY;
         int kk = mop.blockZ;
         int enlarge = wand.getFocusEnlarge(itemstack);
         int distance = 0;
         int maxdis = 33 + enlarge * 8;

         for(distance = 0; distance < maxdis; ++distance) {
            Block bi = world.getBlock(ii, jj, kk);
            if (ThaumcraftApi.portableHoleBlackList.contains(bi) || bi == Blocks.bedrock || bi == ConfigBlocks.blockHole || bi.isAir(world, ii, jj, kk) || bi.getBlockHardness(world, ii, jj, kk) == -1.0F) {
               break;
            }

            switch (mop.sideHit) {
               case 0:
                  ++jj;
                  break;
               case 1:
                  --jj;
                  break;
               case 2:
                  ++kk;
                  break;
               case 3:
                  --kk;
                  break;
               case 4:
                  ++ii;
                  break;
               case 5:
                  --ii;
            }
         }

         AspectList c = this.getVisCost(itemstack);

         for(Aspect a : c.getAspects()) {
            c.merge(a, c.getAmount(a) * distance);
         }

         if (wand.consumeAllVis(itemstack, player, c, true, false)) {
            int di = this.getUpgradeLevel(wand.getFocusItem(itemstack), FocusUpgradeType.extend);
            short dur = (short)(120 + 60 * di);
            createHole(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, (byte)(distance + 1), dur);
         }

         player.swingItem();
         if (!world.isRemote) {
            world.playSoundEffect((double)mop.blockX + (double)0.5F, (double)mop.blockY + (double)0.5F, (double)mop.blockZ + (double)0.5F, "mob.endermen.portal", 1.0F, 1.0F);
         }
      }

      return itemstack;
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.extend};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.extend};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.extend};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.extend};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.extend};
         default:
            return null;
      }
   }

   static {
      cost = (new AspectList()).add(Aspect.ENTROPY, 10).add(Aspect.AIR, 10);
   }
}
