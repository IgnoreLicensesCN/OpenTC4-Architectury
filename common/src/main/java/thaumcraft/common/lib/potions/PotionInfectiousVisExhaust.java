package thaumcraft.common.lib.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

import java.util.List;

public class PotionInfectiousVisExhaust extends Potion {
   public static PotionInfectiousVisExhaust instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation(Thaumcraft.MOD_ID, "textures/misc/potions.png");

   public PotionInfectiousVisExhaust(int par1, boolean par2, int par3) {
      super(par1, par2, par3);
      this.setIconIndex(0, 0);
   }

   public static void init() {
      instance.setPotionName("potion.infvisexhaust");
      instance.setIconIndex(6, 1);
      instance.setEffectiveness(0.25F);
   }

   public boolean isBadEffect() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public int getStatusIconIndex() {
      Minecraft.getMinecraft().renderEngine.bindTexture(rl);
      return super.getStatusIconIndex();
   }

   public void performEffect(LivingEntity target, int par2) {
      List<LivingEntity> targets = target.level().getEntitiesWithinAABB(LivingEntity.class, target.boundingBox.expand(4.0F, 4.0F, 4.0F));
      if (!targets.isEmpty()) {
         for(LivingEntity e : targets) {
            if (!e.isPotionActive(Config.potionInfVisExhaustID)) {
               if (par2 > 0) {
                  e.addEffect(new MobEffectInstance(Config.potionInfVisExhaustID, 6000, par2 - 1, false));
               } else {
                  e.addEffect(new MobEffectInstance(Config.potionVisExhaustID, 6000, 0, false));
               }
            }
         }
      }

   }

   public boolean isReady(int par1, int par2) {
      return par1 % 40 == 0;
   }
}
