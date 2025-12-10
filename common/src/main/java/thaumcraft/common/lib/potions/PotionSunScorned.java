package thaumcraft.common.lib.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.resources.ResourceLocation;

public class PotionSunScorned extends Potion {
   public static PotionSunScorned instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionSunScorned(int par1, boolean par2, int par3) {
      super(par1, par2, par3);
      this.setIconIndex(0, 0);
   }

   public static void init() {
      instance.setPotionName("potion.sunscorned");
      instance.setIconIndex(6, 2);
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
      if (Platform.getEnvironment() != Env.CLIENT) {
         float f = target.getBrightness(1.0F);
         if (f > 0.5F && target.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && target.level().canBlockSeeTheSky(MathHelper.floor_double(target.posX), MathHelper.floor_double(target.posY), MathHelper.floor_double(target.posZ))) {
            target.setFire(4);
         } else if (f < 0.25F && target.getRandom().nextFloat() > f * 2.0F) {
            target.heal(1.0F);
         }
      }

   }

   public boolean isReady(int par1, int par2) {
      return par1 % 40 == 0;
   }
}
