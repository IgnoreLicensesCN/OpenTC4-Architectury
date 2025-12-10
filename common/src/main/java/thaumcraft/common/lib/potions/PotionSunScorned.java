package thaumcraft.common.lib.potions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

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

   public void performEffect(EntityLivingBase target, int par2) {
      if (!target.worldObj.isRemote) {
         float f = target.getBrightness(1.0F);
         if (f > 0.5F && target.worldObj.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && target.worldObj.canBlockSeeTheSky(MathHelper.floor_double(target.posX), MathHelper.floor_double(target.posY), MathHelper.floor_double(target.posZ))) {
            target.setFire(4);
         } else if (f < 0.25F && target.worldObj.rand.nextFloat() > f * 2.0F) {
            target.heal(1.0F);
         }
      }

   }

   public boolean isReady(int par1, int par2) {
      return par1 % 40 == 0;
   }
}
