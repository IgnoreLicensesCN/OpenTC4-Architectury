package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelEldritchGuardian;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;

@SideOnly(Side.CLIENT)
public class RenderEldritchGuardian extends RenderLiving {
   protected ModelEldritchGuardian modelMain;
   private static final ResourceLocation[] skin = new ResourceLocation[]{new ResourceLocation("thaumcraft", "textures/models/eldritch_guardian.png"), new ResourceLocation("thaumcraft", "textures/models/eldritch_warden.png")};

   public RenderEldritchGuardian(ModelEldritchGuardian par1ModelBiped, float par2) {
      super(par1ModelBiped, par2);
      this.modelMain = par1ModelBiped;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return entity instanceof EntityEldritchWarden ? skin[1] : skin[0];
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      if (par1EntityLiving instanceof EntityEldritchWarden) {
         BossStatus.setBossStatus((EntityEldritchWarden)par1EntityLiving, false);
         GL11.glScalef(1.5F, 1.5F, 1.5F);
      }

   }

   public void doRenderLiving(EntityLiving guardian, double par2, double par4, double par6, float par8, float par9) {
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.003921569F);
      GL11.glBlendFunc(770, 771);
      float base = 1.0F;
      double d3 = par4 - (double)guardian.yOffset;
      if (guardian instanceof EntityEldritchWarden) {
         d3 -= guardian.height * ((float)((EntityEldritchWarden)guardian).getSpawnTimer() / 150.0F);
      } else {
         Entity e = Minecraft.getMinecraft().renderViewEntity;
         float d6 = e.worldObj.difficultySetting == EnumDifficulty.HARD ? 576.0F : 1024.0F;
         float d7 = 256.0F;
         if (guardian.worldObj != null && guardian.worldObj.provider.dimensionId == Config.dimensionOuterId) {
            base = 1.0F;
         } else {
            double d8 = guardian.getDistanceSq(e.posX, e.posY, e.posZ);
            if (d8 < (double)256.0F) {
               base = 0.6F;
            } else {
               base = (float)((double)1.0F - Math.min(d6 - d7, d8 - (double)d7) / (double)(d6 - d7)) * 0.6F;
            }
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, base);
      super.doRender(guardian, par2, d3, par6, par8, par9);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glAlphaFunc(516, 0.1F);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
   }
}
