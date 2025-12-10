package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityCultistCleric;
import thaumcraft.common.entities.monster.boss.EntityCultistLeader;

@SideOnly(Side.CLIENT)
public class RenderCultist extends RenderBiped {
   private static final ResourceLocation skin = new ResourceLocation("thaumcraft", "textures/models/cultist.png");

   public RenderCultist() {
      super(new ModelBiped(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
      return skin;
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      if (par1EntityLiving instanceof EntityCultistLeader) {
         BossStatus.setBossStatus((EntityCultistLeader)par1EntityLiving, false);
         GL11.glScalef(1.25F, 1.25F, 1.25F);
      }

   }

   public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      GL11.glPushMatrix();
      float bob = 0.0F;
      boolean rit = entity instanceof EntityCultistCleric && ((EntityCultistCleric)entity).getIsRitualist();
      if (rit) {
         int val = (new Random(entity.getEntityId())).nextInt(1000);
         float c = (float) entity.ticksExisted + p_76986_9_ + (float)val;
         bob = MathHelper.sin(c / 9.0F) * 0.1F + 0.21F;
         GL11.glTranslated(0.0F, bob, 0.0F);
      }

      super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      if (rit) {
         GL11.glPushMatrix();
         this.drawFloatyLine(entity.posX, entity.posY + (double)(entity.getEyeHeight() * 1.2F), entity.posZ, (double)((EntityCultistCleric)entity).getHomePosition().posX + (double)0.5F, (double)((EntityCultistCleric)entity).getHomePosition().posY + (double)1.5F - (double)bob, (double)((EntityCultistCleric)entity).getHomePosition().posZ + (double)0.5F, p_76986_9_, 1114129, "textures/misc/wispy.png", -0.03F, (float)Math.min(entity.ticksExisted, 10) / 10.0F, 0.25F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
   }

   private void drawFloatyLine(double x, double y, double z, double x2, double y2, double z2, float partialTicks, int color, String texture, float speed, float distance, float width) {
      EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
      double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
      double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
      double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
      GL11.glTranslated(-iPX + x2, -iPY + y2, -iPZ + z2);
      float time = (float)(System.nanoTime() / 30000000L);
      Color co = new Color(color);
      float r = (float)co.getRed() / 255.0F;
      float g = (float)co.getGreen() / 255.0F;
      float b = (float)co.getBlue() / 255.0F;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      Tessellator tessellator = Tessellator.instance;
      double dc1x = (float)(x - x2);
      double dc1y = (float)(y - y2);
      double dc1z = (float)(z - z2);
      UtilsFX.bindTexture(texture);
      tessellator.startDrawing(5);
      double dx2 = 0.0F;
      double dy2 = 0.0F;
      double dz2 = 0.0F;
      double d3 = x - x2;
      double d4 = y - y2;
      double d5 = z - z2;
      float dist = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
      float blocks = (float)Math.round(dist);
      float length = blocks * ((float)Config.golemLinkQuality / 2.0F);
      float f9 = 0.0F;
      float f10 = 1.0F;

      for(int i = 0; (float)i <= length * distance; ++i) {
         float f2 = (float)i / length;
         float f2a = (float)i * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         float f3 = 1.0F - Math.abs((float)i - length / 2.0F) / (length / 2.0F);
         double dx = dc1x + (double)(MathHelper.sin((float)((z % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)4.0F)) * 0.5F * f3);
         double dy = dc1y + (double)(MathHelper.sin((float)((x % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)3.0F)) * 0.5F * f3);
         double dz = dc1z + (double)(MathHelper.sin((float)((y % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)2.0F)) * 0.5F * f3);
         tessellator.setColorRGBA_F(r, g, b, 0.8F);
         float f13 = (1.0F - f2) * dist - time * speed;
         tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 - (double)width, dz * (double)f2, f13, f10);
         tessellator.addVertexWithUV(dx * (double)f2, dy * (double)f2 + (double)width, dz * (double)f2, f13, f9);
      }

      tessellator.draw();
      tessellator.startDrawing(5);

      for(int var85 = 0; (float)var85 <= length * distance; ++var85) {
         float f2 = (float)var85 / length;
         float f2a = (float)var85 * 1.5F / length;
         f2a = Math.min(0.75F, f2a);
         float f3 = 1.0F - Math.abs((float)var85 - length / 2.0F) / (length / 2.0F);
         double dx = dc1x + (double)(MathHelper.sin((float)((z % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)4.0F)) * 0.5F * f3);
         double dy = dc1y + (double)(MathHelper.sin((float)((x % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)3.0F)) * 0.5F * f3);
         double dz = dc1z + (double)(MathHelper.sin((float)((y % (double)16.0F + (double)(dist * (1.0F - f2) * (float)Config.golemLinkQuality / 2.0F) - (double)(time % 32767.0F / 5.0F)) / (double)2.0F)) * 0.5F * f3);
         tessellator.setColorRGBA_F(r, g, b, 0.8F);
         float f13 = (1.0F - f2) * dist - time * speed;
         tessellator.addVertexWithUV(dx * (double)f2 - (double)width, dy * (double)f2, dz * (double)f2, f13, f10);
         tessellator.addVertexWithUV(dx * (double)f2 + (double)width, dy * (double)f2, dz * (double)f2, f13, f9);
      }

      tessellator.draw();
      GL11.glDisable(GL11.GL_BLEND);
   }
}
