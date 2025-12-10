package thaumcraft.common.lib.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionWarpWard extends Potion {
   public static PotionWarpWard instance = null;
   private int statusIconIndex = -1;
   static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/misc/potions.png");

   public PotionWarpWard(int par1, boolean par2, int par3) {
      super(par1, par2, par3);
      this.setIconIndex(0, 0);
   }

   public static void init() {
      instance.setPotionName("potion.warpward");
      instance.setIconIndex(3, 2);
      instance.setEffectiveness(0.25F);
   }

   public boolean isBadEffect() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public int getStatusIconIndex() {
      Minecraft.getMinecraft().renderEngine.bindTexture(rl);
      return super.getStatusIconIndex();
   }

   public void performEffect(EntityLivingBase target, int par2) {
   }
}
