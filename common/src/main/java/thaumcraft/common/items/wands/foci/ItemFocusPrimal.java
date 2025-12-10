package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityPrimalOrb;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.Random;

public class ItemFocusPrimal extends ItemFocusBasic {
   IIcon depthIcon = null;
   public static FocusUpgradeType seeker;

   public ItemFocusPrimal() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "FP" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:focus_primal");
      this.depthIcon = ir.registerIcon("thaumcraft:focus_primal_depth");
   }

   public IIcon getFocusDepthLayerIcon(ItemStack itemstack) {
      return this.depthIcon;
   }

   public int getActivationCooldown(ItemStack focusstack) {
      return 500;
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer p, MovingObjectPosition mob) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
      EntityPrimalOrb shard = new EntityPrimalOrb(world, p, this.isUpgradedWith(wand.getFocusItem(itemstack), seeker));
      if (!world.isRemote && wand.consumeAllVis(itemstack, p, this.getVisCost(itemstack), true, false)) {
         world.spawnEntityInWorld(shard);
         world.playSoundAtEntity(shard, "thaumcraft:ice", 0.3F, 0.8F + world.rand.nextFloat() * 0.1F);
      }

      p.swingItem();
      return itemstack;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 10854849;
   }

   public AspectList getVisCost(ItemStack itemstack) {
      Random rand = new Random(System.currentTimeMillis() / 200L);
      AspectList cost = (new AspectList()).add(Aspect.WATER, 50 + rand.nextInt(5) * 50).add(Aspect.AIR, 50 + rand.nextInt(5) * 50).add(Aspect.EARTH, 50 + rand.nextInt(5) * 50).add(Aspect.FIRE, 50 + rand.nextInt(5) * 50).add(Aspect.ORDER, 50 + rand.nextInt(5) * 50).add(Aspect.ENTROPY, 50 + rand.nextInt(5) * 50);
      return cost;
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, seeker};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal};
         default:
            return null;
      }
   }

   static {
      seeker = new FocusUpgradeType(16, new ResourceLocation("thaumcraft", "textures/foci/seeker.png"), "focus.upgrade.seeker.name", "focus.upgrade.seeker.text", (new AspectList()).add(Aspect.SENSES, 1).add(Aspect.MIND, 1));
   }
}
