package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.phys.HitResult;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityPechBlast;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;

public class ItemFocusPech extends ItemFocusBasic {
   IIcon depthIcon = null;
   private static final AspectList cost;
   private static final AspectList costAll;
   public static FocusUpgradeType nightshade;

   public ItemFocusPech() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "PP" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:focus_pech");
      this.depthIcon = ir.registerIcon("thaumcraft:focus_pech_depth");
   }

   public IIcon getFocusDepthLayerIcon(ItemStack itemstack) {
      return this.depthIcon;
   }

   public int getActivationCooldown(ItemStack focusstack) {
      return 250;
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player p, HitResult mob) {
      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
      EntityPechBlast blast = new EntityPechBlast(world, p, wand.getFocusPotency(itemstack), wand.getFocusExtend(itemstack), this.isUpgradedWith(wand.getFocusItem(itemstack), nightshade));
      if (Platform.getEnvironment() != Env.CLIENT && wand.consumeAllCentiVis(itemstack, p, this.getVisCost(itemstack), true, false)) {
         world.spawnEntityInWorld(blast);
         world.playSoundAtEntity(blast, "thaumcraft:ice", 0.4F, 1.0F + world.getRandom().nextFloat() * 0.1F);
      }

      p.swingItem();
      return itemstack;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 2267460;
   }

   public AspectList getVisCost(ItemStack itemstack) {
      return this.isUpgradedWith(itemstack, nightshade) ? costAll : cost;
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.extend};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.extend};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, nightshade};
         default:
            return null;
      }
   }

   static {
      cost = (new AspectList()).addAll(Aspects.EARTH, 10).addAll(Aspects.ENTROPY, 10).addAll(Aspects.WATER, 10);
      costAll = (new AspectList()).addAll(Aspects.AIR, 10).addAll(Aspects.FIRE, 10).addAll(Aspects.EARTH, 10).addAll(
              Aspects.ORDER, 10).addAll(Aspects.ENTROPY, 10).addAll(Aspects.WATER, 10);
      nightshade = new FocusUpgradeType(15, new ResourceLocation("thaumcraft", "textures/foci/nightshade.png"), "focus.upgrade.nightshade.name", "focus.upgrade.nightshade.text", (new AspectList()).addAll(
              Aspects.LIFE, 1).addAll(Aspects.POISON, 1).addAll(Aspects.MAGIC, 1));
   }
}
