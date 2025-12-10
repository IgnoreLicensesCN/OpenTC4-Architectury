package thaumcraft.client.renderers.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityTaintCreeper;

public class RenderTaintCreeper extends RenderLiving {
   private ModelBase field_27008_a = new ModelCreeper(2.0F);
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/creeper.png");
   private static final ResourceLocation armoredCreeperTextures = new ResourceLocation("thaumcraft", "textures/entity/creeper/creeper_armor.png");

   public RenderTaintCreeper() {
      super(new ModelCreeper(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }

   protected void updateCreeperScale(EntityTaintCreeper par1EntityCreeper, float par2) {
      float var4 = par1EntityCreeper.getCreeperFlashIntensity(par2);
      float var5 = 1.0F + MathHelper.sin(var4 * 100.0F) * var4 * 0.01F;
      if (var4 < 0.0F) {
         var4 = 0.0F;
      }

      if (var4 > 1.0F) {
         var4 = 1.0F;
      }

      var4 *= var4;
      var4 *= var4;
      float var6 = (1.0F + var4 * 0.4F) * var5;
      float var7 = (1.0F + var4 * 0.1F) / var5;
      GL11.glScalef(var6, var7, var6);
   }

   protected int updateCreeperColorMultiplier(EntityTaintCreeper par1EntityCreeper, float par2, float par3) {
      float var5 = par1EntityCreeper.getCreeperFlashIntensity(par3);
      if ((int)(var5 * 10.0F) % 2 == 0) {
         return 0;
      } else {
         int var6 = (int)(var5 * 0.2F * 255.0F);
         if (var6 < 0) {
            var6 = 0;
         }

         if (var6 > 255) {
            var6 = 255;
         }

         short var7 = 255;
         short var8 = 255;
         short var9 = 255;
         return var6 << 24 | var7 << 16 | var8 << 8 | var9;
      }
   }

   protected int func_27007_b(EntityTaintCreeper par1EntityCreeper, int par2, float par3) {
      return -1;
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      this.updateCreeperScale((EntityTaintCreeper)par1EntityLiving, par2);
   }

   protected int getColorMultiplier(EntityLivingBase par1EntityLiving, float par2, float par3) {
      return this.updateCreeperColorMultiplier((EntityTaintCreeper)par1EntityLiving, par2, par3);
   }

   protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return this.func_27007_b((EntityTaintCreeper)par1EntityLiving, par2, par3);
   }
}
