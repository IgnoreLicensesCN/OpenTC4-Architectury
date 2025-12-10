package thaumcraft.client.renderers.entity;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.EntitySpecialItem;

public class RenderSpecialItem extends Render {
   private RenderBlocks renderBlocks = new RenderBlocks();
   private Random random = new Random();
   public boolean renderWithColor = true;
   public float zLevel = 0.0F;

   public RenderSpecialItem() {
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRenderItem(EntitySpecialItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
      this.random.setSeed(187L);
      float var11 = MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4 + var11 + 0.15F, (float)par6);
      int q = !FMLClientHandler.instance().getClient().gameSettings.fancyGraphics ? 5 : 10;
      Tessellator tessellator = Tessellator.instance;
      RenderHelper.disableStandardItemLighting();
      float f1 = (float)par1EntityItem.age / 500.0F;
      float f3 = 0.9F;
      float f2 = 0.0F;
      Random random = new Random(245L);
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();

      for(int i = 0; i < q; ++i) {
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
         tessellator.startDrawing(6);
         float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
         float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
         fa /= 30.0F / ((float)Math.min(par1EntityItem.age, 10) / 10.0F);
         f4 /= 30.0F / ((float)Math.min(par1EntityItem.age, 10) / 10.0F);
         tessellator.setColorRGBA_I(16777215, (int)(255.0F * (1.0F - f2)));
         tessellator.addVertex(0.0F, 0.0F, 0.0F);
         tessellator.setColorRGBA_I(16711935, 0);
         tessellator.addVertex(-0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.addVertex(0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.addVertex(0.0F, fa, f4);
         tessellator.addVertex(-0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.draw();
      }

      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.enableStandardItemLighting();
      GL11.glPopMatrix();
      RenderItem ri = new RenderItem();
      ri.setRenderManager(RenderManager.instance);
      ItemStack var10 = par1EntityItem.getEntityItem();
      if (var10 != null) {
         EntityItem ei = new EntityItem(par1EntityItem.worldObj, par1EntityItem.posX, par1EntityItem.posY, par1EntityItem.posZ, var10);
         ei.age = par1EntityItem.age;
         ei.hoverStart = par1EntityItem.hoverStart;
         ri.doRender(ei, par2, par4, par6, par8, par9);
      }

   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderItem((EntitySpecialItem)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
