package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelEldritchGolem;
import thaumcraft.common.entities.monster.boss.EntityEldritchGolem;

@SideOnly(Side.CLIENT)
public class RenderEldritchGolem extends RenderLiving {
   protected ModelEldritchGolem modelMain;
   private static final ResourceLocation skin = new ResourceLocation("thaumcraft", "textures/models/eldritch_golem.png");

   public RenderEldritchGolem(ModelEldritchGolem par1ModelBiped, float par2) {
      super(par1ModelBiped, par2);
      this.modelMain = par1ModelBiped;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return skin;
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      BossStatus.setBossStatus((EntityEldritchGolem)par1EntityLiving, false);
      GL11.glScalef(2.15F, 2.15F, 2.15F);
   }

   public void doRenderLiving(EntityLiving golem, double par2, double par4, double par6, float par8, float par9) {
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glBlendFunc(770, 771);
      double d3 = par4 - (double)golem.yOffset;
      super.doRender(golem, par2, d3, par6, par8, par9);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.1F);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
   }
}
