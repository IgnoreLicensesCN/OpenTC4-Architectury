package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityThaumicSlime;

@SideOnly(Side.CLIENT)
public class RenderThaumicSlime extends RenderLiving {
   private ModelBase scaleAmount;
   private static final ResourceLocation slimeTextures = new ResourceLocation("thaumcraft", "textures/models/tslime.png");

   public RenderThaumicSlime(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
      super(par1ModelBase, par3);
      this.scaleAmount = par2ModelBase;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return slimeTextures;
   }

   protected int shouldSlimeRenderPass(EntityThaumicSlime par1EntitySlime, int par2, float par3) {
      if (par1EntitySlime.isInvisible()) {
         return 0;
      } else if (par2 == 0) {
         this.setRenderPassModel(this.scaleAmount);
         GL11.glEnable(2977);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 771);
         return 1;
      } else {
         if (par2 == 1) {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }

         return -1;
      }
   }

   protected void scaleSlime(EntityThaumicSlime par1EntitySlime, float par2) {
      float f1 = (float)Math.sqrt(par1EntitySlime.getSlimeSize());
      float f2 = (par1EntitySlime.prevSquishFactor + (par1EntitySlime.squishFactor - par1EntitySlime.prevSquishFactor) * par2) / (f1 * 0.25F + 1.0F);
      float f3 = 1.0F / (f2 + 1.0F);
      GL11.glScalef(f3 * f1 + 0.1F, 1.0F / f3 * f1 + 0.1F, f3 * f1 + 0.1F);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      this.scaleSlime((EntityThaumicSlime)par1EntityLiving, par2);
   }

   protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return this.shouldSlimeRenderPass((EntityThaumicSlime)par1EntityLiving, par2, par3);
   }
}
