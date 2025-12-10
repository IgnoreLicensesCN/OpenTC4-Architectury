package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityEmber;
import thaumcraft.common.entities.projectile.EntityExplosiveOrb;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;

public class ItemFocusFire extends ItemFocusBasic {
   private static final AspectList costBase;
   private static final AspectList costBeam;
   private static final AspectList costBall;
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

   public AspectList getVisCost(ItemStack itemstack) {
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

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer p, MovingObjectPosition movingobjectposition) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
      if (this.isUpgradedWith(wand.getFocusItem(itemstack), fireball)) {
         if (wand.consumeAllVis(itemstack, p, this.getVisCost(itemstack), !p.worldObj.isRemote, false)) {
            if (!world.isRemote) {
               EntityExplosiveOrb orb = new EntityExplosiveOrb(world, p);
               orb.strength += (float)wand.getFocusPotency(itemstack) * 0.4F;
               orb.onFire = this.isUpgradedWith(wand.getFocusItem(itemstack), FocusUpgradeType.alchemistsfire);
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

   public void onUsingFocusTick(ItemStack itemstack, EntityPlayer p, int count) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
      if (!wand.consumeAllVis(itemstack, p, this.getVisCost(itemstack), false, false)) {
         p.stopUsingItem();
      } else {
         int range = 17;
         p.getLook((float)range);
         if (!p.worldObj.isRemote && this.soundDelay < System.currentTimeMillis()) {
            p.worldObj.playSoundAtEntity(p, "thaumcraft:fireloop", 0.33F, 2.0F);
            this.soundDelay = System.currentTimeMillis() + 500L;
         }

         if (!p.worldObj.isRemote && wand.consumeAllVis(itemstack, p, this.getVisCost(itemstack), true, false)) {
            float scatter = this.isUpgradedWith(wand.getFocusItem(itemstack), firebeam) ? 0.25F : 15.0F;

            for(int a = 0; a < 2 + wand.getFocusPotency(itemstack); ++a) {
               EntityEmber orb = new EntityEmber(p.worldObj, p, scatter);
               orb.damage = (float)(2 + wand.getFocusPotency(itemstack));
               if (this.isUpgradedWith(wand.getFocusItem(itemstack), firebeam)) {
                  orb.damage += 0.5F;
                  orb.damage *= 1.5F;
                  orb.duration = 30;
               }

               orb.firey = this.getUpgradeLevel(wand.getFocusItem(itemstack), FocusUpgradeType.alchemistsfire);
               orb.posX += orb.motionX;
               orb.posY += orb.motionY;
               orb.posZ += orb.motionZ;
               p.worldObj.spawnEntityInWorld(orb);
            }
         }

      }
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.alchemistsfire};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, fireball, firebeam};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.alchemistsfire};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         default:
            return null;
      }
   }

   public boolean canApplyUpgrade(ItemStack focusstack, EntityPlayer player, FocusUpgradeType type, int rank) {
      return !type.equals(FocusUpgradeType.alchemistsfire) || !this.isUpgradedWith(focusstack, fireball) || !this.isUpgradedWith(focusstack, FocusUpgradeType.alchemistsfire);
   }

   static {
      costBase = (new AspectList()).add(Aspect.FIRE, 10);
      costBeam = (new AspectList()).add(Aspect.FIRE, 10).add(Aspect.ORDER, 3);
      costBall = (new AspectList()).add(Aspect.FIRE, 66).add(Aspect.ENTROPY, 33);
      fireball = new FocusUpgradeType(9, new ResourceLocation("thaumcraft", "textures/foci/fireball.png"), "focus.upgrade.fireball.name", "focus.upgrade.fireball.text", (new AspectList()).add(Aspect.DARKNESS, 1));
      firebeam = new FocusUpgradeType(10, new ResourceLocation("thaumcraft", "textures/foci/firebeam.png"), "focus.upgrade.firebeam.name", "focus.upgrade.firebeam.text", (new AspectList()).add(Aspect.ENERGY, 1).add(Aspect.AIR, 1));
   }
}
