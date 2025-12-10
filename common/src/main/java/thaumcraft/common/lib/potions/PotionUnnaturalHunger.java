package thaumcraft.common.lib.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;

public class PotionUnnaturalHunger extends Potion {
   public static PotionUnnaturalHunger instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionUnnaturalHunger(int par1, boolean par2, int par3) {
      super(par1, par2, par3);
      this.setIconIndex(0, 0);
   }

   public static void init() {
      instance.setPotionName("potion.unhunger");
      instance.setIconIndex(7, 1);
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
      if (Platform.getEnvironment() != Env.CLIENT && target instanceof Player) {
         ((Player)target).addExhaustion(0.025F * (float)(par2 + 1));
      }

   }

   public boolean isReady(int par1, int par2) {
      return true;
   }
}
