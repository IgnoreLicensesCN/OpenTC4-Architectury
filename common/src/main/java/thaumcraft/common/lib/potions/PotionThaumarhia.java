package thaumcraft.common.lib.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.config.ConfigBlocks;

public class PotionThaumarhia extends Potion {
   public static PotionThaumarhia instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionThaumarhia(int par1, boolean par2, int par3) {
      super(par1, par2, par3);
      this.setIconIndex(0, 0);
   }

   public static void init() {
      instance.setPotionName("potion.thaumarhia");
      instance.setIconIndex(7, 2);
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
      if (Platform.getEnvironment() != Env.CLIENT && target.getRandom().nextInt(15) == 0) {
         int x = MathHelper.floor_double(target.posX);
         int y = MathHelper.floor_double(target.posY);
         int z = MathHelper.floor_double(target.posZ);
         if (target.level().isAirBlock(x, y, z)) {
            target.level().setBlock(x, y, z, ConfigBlocks.blockFluxGoo);
         }
      }

   }

   public boolean isReady(int par1, int par2) {
      return par1 % 20 == 0;
   }
}
