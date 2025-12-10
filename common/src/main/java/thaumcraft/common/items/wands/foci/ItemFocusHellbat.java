package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.level.Level;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityFireBat;
import thaumcraft.common.items.wands.WandCastingItem;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemFocusHellbat extends ItemFocusBasic {
   public IIcon iconOrnament;
   private static final AspectList costBase;
   private static final AspectList costBomb;
   private static final AspectList costDevil;
   public static FocusUpgradeType batbombs;
   public static FocusUpgradeType devilbats;
   public static FocusUpgradeType vampirebats;

   public ItemFocusHellbat() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "HH" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:focus_hellbat");
      this.iconOrnament = ir.registerIcon("thaumcraft:focus_hellbat_orn");
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

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, Player player, HitResult HitResult) {
      WandCastingItem wand = (WandCastingItem)itemstack.getItem();
      Entity pointedEntity = EntityUtils.getPointedEntity(player.level(), player, 32.0F, EntityFireBat.class);
      double px = player.posX;
      double py = player.posY;
      double pz = player.posZ;
      py = player.boundingBox.minY + (double)(player.height / 2.0F) + (double)0.25F;
      px -= MathHelper.cos(player.rotationYaw / 180.0F * 3.141593F) * 0.16F;
      py -= 0.05000000014901161;
      pz -= MathHelper.sin(player.rotationYaw / 180.0F * 3.141593F) * 0.16F;
      Vec3 vec3d = player.getLook(1.0F);
      px += vec3d.xCoord * (double)0.5F;
      py += vec3d.yCoord * (double)0.5F;
      pz += vec3d.zCoord * (double)0.5F;
      if (pointedEntity instanceof EntityLivingBase) {
         if (Platform.getEnvironment() != Env.CLIENT) {
            if (pointedEntity instanceof Player && !MinecraftServer.getServer().isPVPEnabled()) {
               return itemstack;
            }

            EntityFireBat firebat = new EntityFireBat(world);
            firebat.setLocationAndAngles(px, py + (double)firebat.height, pz, player.rotationYaw, 0.0F);
            firebat.setTarget(pointedEntity);
            firebat.damBonus = wand.getFocusPotency(itemstack);
            firebat.setIsSummoned(true);
            firebat.setIsBatHanging(false);
            if (this.isUpgradedWith(wand.getFocusItem(itemstack), devilbats)) {
               firebat.setIsDevil(true);
            }

            if (this.isUpgradedWith(wand.getFocusItem(itemstack), batbombs)) {
               firebat.setIsExplosive(true);
            }

            if (this.isUpgradedWith(wand.getFocusItem(itemstack), vampirebats)) {
               firebat.owner = player;
               firebat.setIsVampire(true);
            }

            if (wand.consumeAllVis(itemstack, player, this.getVisCost(itemstack), true, false) && world.spawnEntityInWorld(firebat)) {
               world.playAuxSFX(2004, (int)px, (int)py, (int)pz, 0);
               world.playSoundAtEntity(firebat, "thaumcraft:ice", 0.2F, 0.95F + world.getRandom().nextFloat() * 0.1F);
            } else {
               world.playSoundAtEntity(player, "thaumcraft:wandfail", 0.1F, 0.8F + world.getRandom().nextFloat() * 0.1F);
            }
         }

         player.swingItem();
      }

      return itemstack;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 14431746;
   }

   public AspectList getVisCost(ItemStack itemstack) {
      return this.isUpgradedWith(itemstack, batbombs) ? costBomb : (this.isUpgradedWith(itemstack, devilbats) ? costDevil : costBase);
   }

   public int getActivationCooldown(ItemStack focusstack) {
      return 1000;
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, batbombs, devilbats};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.potency, vampirebats};
         default:
            return null;
      }
   }

   public boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
      return !type.equals(vampirebats) || ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "VAMPBAT");
   }

   static {
      costBase = (new AspectList()).add(Aspect.FIRE, 200).add(Aspect.ENTROPY, 100).add(Aspect.AIR, 100);
      costBomb = (new AspectList()).add(Aspect.FIRE, 100).add(Aspect.ENTROPY, 200).add(Aspect.AIR, 100);
      costDevil = (new AspectList()).add(Aspect.FIRE, 100).add(Aspect.ENTROPY, 100).add(Aspect.AIR, 100).add(Aspect.EARTH, 100);
      batbombs = new FocusUpgradeType(13, new ResourceLocation("thaumcraft", "textures/foci/batbombs.png"), "focus.upgrade.batbombs.name", "focus.upgrade.batbombs.text", (new AspectList()).add(Aspect.ENERGY, 1).add(Aspect.TRAP, 1));
      devilbats = new FocusUpgradeType(14, new ResourceLocation("thaumcraft", "textures/foci/devilbats.png"), "focus.upgrade.devilbats.name", "focus.upgrade.devilbats.text", (new AspectList()).add(Aspect.ARMOR, 1));
      vampirebats = new FocusUpgradeType(19, new ResourceLocation("thaumcraft", "textures/foci/vampirebats.png"), "focus.upgrade.vampirebats.name", "focus.upgrade.vampirebats.text", (new AspectList()).add(Aspect.HUNGER, 1).add(Aspect.LIFE, 1));
   }
}
