package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityFrostShard;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;

public class ItemFocusFrost extends ItemFocusBasic {
   private static final AspectList costBase;
   private static final AspectList costScatter;
   private static final AspectList costBoulder;
   public static FocusUpgradeType scattershot;
   public static FocusUpgradeType iceboulder;

   public ItemFocusFrost() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "BF" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:focus_frost");
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player p, HitResult mob) {
      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
      if (Platform.getEnvironment() != Env.CLIENT && wand.consumeAllVis(itemstack, p, this.getVisCost(itemstack), true, false)) {
         int frosty = this.getUpgradeLevel(wand.getFocusItem(itemstack), FocusUpgradeType.alchemistsfrost);
         EntityFrostShard shard = null;
         if (this.isUpgradedWith(wand.getFocusItem(itemstack), scattershot)) {
            for(int a = 0; a < 5 + wand.getFocusPotency(itemstack) * 2; ++a) {
               shard = new EntityFrostShard(world, p, 8.0F);
               shard.setDamage(1.0F);
               shard.fragile = true;
               shard.setFrosty(frosty);
               world.spawnEntityInWorld(shard);
            }
         } else if (this.isUpgradedWith(wand.getFocusItem(itemstack), iceboulder)) {
            shard = new EntityFrostShard(world, p, 1.0F);
            shard.setDamage((float)(4 + wand.getFocusPotency(itemstack) * 2));
            shard.bounce = 0.8;
            shard.bounceLimit = 6;
            shard.setFrosty(frosty);
            world.spawnEntityInWorld(shard);
         } else {
            shard = new EntityFrostShard(world, p, 1.0F);
            shard.setDamage((float)((double)3.0F + (double)wand.getFocusPotency(itemstack) * (double)1.5F));
            shard.setFrosty(frosty);
            world.spawnEntityInWorld(shard);
         }

         world.playSoundAtEntity(shard, "thaumcraft:ice", 0.4F, 1.0F + world.getRandom().nextFloat() * 0.1F);
      }

      p.swingItem();
      return itemstack;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 5204428;
   }

   public AspectList getVisCost(ItemStack itemstack) {
      return this.isUpgradedWith(itemstack, scattershot) ? costScatter : (this.isUpgradedWith(itemstack, iceboulder) ? costBoulder : costBase);
   }

   public int getActivationCooldown(ItemStack focusstack) {
      return this.getUpgradeLevel(focusstack, scattershot) <= 0 && this.getUpgradeLevel(focusstack, iceboulder) <= 0 ? 200 : 500;
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.alchemistsfrost};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, scattershot, iceboulder, FocusUpgradeType.alchemistsfrost};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.alchemistsfrost};
         default:
            return null;
      }
   }

   static {
      costBase = (new AspectList()).addAll(Aspects.WATER, 5).addAll(Aspects.FIRE, 2).addAll(Aspects.ENTROPY, 2);
      costScatter = (new AspectList()).addAll(Aspects.WATER, 20).addAll(Aspects.FIRE, 2).addAll(Aspects.ENTROPY, 2).addAll(
              Aspects.AIR, 5);
      costBoulder = (new AspectList()).addAll(Aspects.WATER, 20).addAll(Aspects.FIRE, 2).addAll(Aspects.ENTROPY, 2).addAll(
              Aspects.EARTH, 5);
      scattershot = new FocusUpgradeType(11, new ResourceLocation("thaumcraft", "textures/foci/scattershot.png"), "focus.upgrade.scattershot.name", "focus.upgrade.scattershot.text", (new AspectList()).addAll(
              Aspects.COLD, 1).addAll(Aspects.WEAPON, 1));
      iceboulder = new FocusUpgradeType(12, new ResourceLocation("thaumcraft", "textures/foci/iceboulder.png"), "focus.upgrade.iceboulder.name", "focus.upgrade.iceboulder.text", (new AspectList()).addAll(
              Aspects.COLD, 1).addAll(Aspects.CRYSTAL, 1));
   }
}
