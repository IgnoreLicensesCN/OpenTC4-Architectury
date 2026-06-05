package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedHashAspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityEmber;
import thaumcraft.common.entities.projectile.EntityExplosiveOrb;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.items.wands.WandManager;

public class ItemFocusFire extends ItemFocusBasic {
   private static final AspectList<Aspect>costBase;
   private static final AspectList<Aspect>costBeam;
   private static final AspectList<Aspect>costBall;
   long soundDelay = 0L;
   public static FocusUpgradeType fireball;
   public static FocusUpgradeType firebeam;

   public ItemFocusFire() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:focus_fire");
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "AF" + super.getSortingHelper(itemstack);
   }

   public int getFocusColor(ItemStack itemstack) {
      return 15028484;
   }

   public AspectList<Aspect>getVisCost(ItemStack itemstack) {
      return this.isUpgradedWith(itemstack, firebeam) ? costBeam : (this.isUpgradedWith(itemstack, fireball) ? costBall : costBase);
   }

   public int getActivationCooldown(ItemStack focusstack) {
      return this.isUpgradedWith(focusstack, fireball) ? 1000 : 0;
   }

   public boolean isVisCostPerTick(ItemStack itemstack) {
      return true;
   }

   public WandFocusAnimation getAnimation(ItemStack itemstack) {
      return this.isUpgradedWith(itemstack, fireball) ? WandFocusAnimation.WAVE : WandFocusAnimation.CHARGE;
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player p, HitResult HitResult) {
      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
      if (this.isUpgradedWith(wand.getFocusItem(itemstack), fireball)) {
         if (wand.consumeAllCentiVis(itemstack, p, this.getVisCost(itemstack), Platform.getEnvironment() != Env.CLIENT, false)) {
            if (Platform.getEnvironment() != Env.CLIENT) {
               EntityExplosiveOrb orb = new EntityExplosiveOrb(world, p);
               orb.strength += (float)wand.getFocusPotency(itemstack) * 0.4F;
               orb.onFire = this.isUpgradedWith(wand.getFocusItem(itemstack), ThaumcraftFocusUpgradeTypes.ALCHEMISTS_FIRE);
               world.spawnEntityInWorld(orb);
               world.playAuxSFXAtEntity(null, 1009, (int)p.posX, (int)p.posY, (int)p.posZ, 0);
            }

            p.swingItem();
         }
      } else {
         p.setItemInUse(itemstack, Integer.MAX_VALUE);
         WandManager.setCooldown(p, -1);
      }

      return itemstack;
   }

   public void onUsingFocusTick(ItemStack itemstack, Player p, int count) {
      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
      if (!wand.consumeAllCentiVis(itemstack, p, this.getVisCost(itemstack), false, false)) {
         p.stopUsingItem();
      } else {
         int range = 17;
         p.getLook((float)range);
         if (Platform.getEnvironment() != Env.CLIENT && this.soundDelay < System.currentTimeMillis()) {
            p.level().playSoundAtEntity(p, "thaumcraft:fireloop", 0.33F, 2.0F);
            this.soundDelay = System.currentTimeMillis() + 500L;
         }

         if (Platform.getEnvironment() != Env.CLIENT && wand.consumeAllCentiVis(itemstack, p, this.getVisCost(itemstack), true, false)) {
            float scatter = this.isUpgradedWith(wand.getFocusItem(itemstack), firebeam) ? 0.25F : 15.0F;

            for(int a = 0; a < 2 + wand.getFocusPotency(itemstack); ++a) {
               EntityEmber orb = new EntityEmber(p.level(), p, scatter);
               orb.damage = (float)(2 + wand.getFocusPotency(itemstack));
               if (this.isUpgradedWith(wand.getFocusItem(itemstack), firebeam)) {
                  orb.damage += 0.5F;
                  orb.damage *= 1.5F;
                  orb.duration = 30;
               }

               orb.firey = this.getUpgradeLevel(wand.getFocusItem(itemstack), ThaumcraftFocusUpgradeTypes.ALCHEMISTS_FIRE);
               orb.posX += orb.motionX;
               orb.posY += orb.motionY;
               orb.posZ += orb.motionZ;
               p.level().spawnEntityInWorld(orb);
            }
         }

      }
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY};
         case 2:
            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY, ThaumcraftFocusUpgradeTypes.ALCHEMISTS_FIRE};
         case 3:
            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY, fireball, firebeam};
         case 4:
            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY, ThaumcraftFocusUpgradeTypes.ALCHEMISTS_FIRE};
         case 5:
            return new FocusUpgradeType[]{ThaumcraftFocusUpgradeTypes.FRUGAL, ThaumcraftFocusUpgradeTypes.POTENCY};
         default:
            return null;
      }
   }

   public boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
      return !type.equals(ThaumcraftFocusUpgradeTypes.ALCHEMISTS_FIRE) || !this.isUpgradedWith(focusstack, fireball) || !this.isUpgradedWith(focusstack, ThaumcraftFocusUpgradeTypes.ALCHEMISTS_FIRE);
   }

   static {
      costBase = (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 10);
      costBeam = (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 10).addAll(Aspects.ORDER, 3);
      costBall = (new LinkedHashAspectList<>()).addAll(Aspects.FIRE, 66).addAll(Aspects.ENTROPY, 33);
      fireball = new FocusUpgradeType(9, new ResourceLocation("thaumcraft", "textures/foci/fireball.png"), "focus.upgrade.fireball.name", "focus.upgrade.fireball.text", (new LinkedHashAspectList<>()).addAll(
              Aspects.DARKNESS, 1));
      firebeam = new FocusUpgradeType(10, new ResourceLocation("thaumcraft", "textures/foci/firebeam.png"), "focus.upgrade.firebeam.name", "focus.upgrade.firebeam.text", (new LinkedHashAspectList<>()).addAll(
              Aspects.ENERGY, 1).addAll(Aspects.AIR, 1));
   }
}
