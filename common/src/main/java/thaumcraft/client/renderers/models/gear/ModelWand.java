package thaumcraft.client.renderers.models.gear;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ModelWand extends ModelBase {
   ModelRenderer Rod;
   ModelRenderer Focus;
   ModelRenderer Cap;
   ModelRenderer CapBottom;
   private final RenderBlocks renderBlocks = new RenderBlocks();

   public ModelWand() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.Cap = new ModelRenderer(this, 0, 0);
      this.Cap.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
      this.Cap.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Cap.setTextureSize(64, 32);
      this.Cap.mirror = true;
      this.setRotation(this.Cap, 0.0F, 0.0F, 0.0F);
      this.CapBottom = new ModelRenderer(this, 0, 0);
      this.CapBottom.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2);
      this.CapBottom.setRotationPoint(0.0F, 20.0F, 0.0F);
      this.CapBottom.setTextureSize(64, 32);
      this.CapBottom.mirror = true;
      this.setRotation(this.CapBottom, 0.0F, 0.0F, 0.0F);
      this.Rod = new ModelRenderer(this, 0, 8);
      this.Rod.addBox(-1.0F, -1.0F, -1.0F, 2, 18, 2);
      this.Rod.setRotationPoint(0.0F, 2.0F, 0.0F);
      this.Rod.setTextureSize(64, 32);
      this.Rod.mirror = true;
      this.setRotation(this.Rod, 0.0F, 0.0F, 0.0F);
      this.Focus = new ModelRenderer(this, 0, 0);
      this.Focus.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6);
      this.Focus.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Focus.setTextureSize(64, 32);
      this.Focus.mirror = true;
      this.setRotation(this.Focus, 0.0F, 0.0F, 0.0F);
   }

   public void render(ItemStack wandStack) {
      if (RenderManager.instance.renderEngine != null) {
         ItemWandCasting wand = (ItemWandCasting)wandStack.getItem();
         ItemStack focusStack = wand.getFocusItem(wandStack);
         EntityPlayer player = Minecraft.getMinecraft().thePlayer;
         boolean staff = wand.isStaff(wandStack);
         boolean runes = wand.hasRunes(wandStack);
         Minecraft.getMinecraft().renderEngine.bindTexture(wand.getRod(wandStack).getTexture());
         GL11.glPushMatrix();
         if (staff) {
            GL11.glTranslated(0.0F, 0.2, 0.0F);
         }

         GL11.glPushMatrix();
         if (wand.getRod(wandStack).isGlowing()) {
            int j = (int)(200.0F + MathHelper.sin((float)player.ticksExisted) * 5.0F + 5.0F);
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
         }

         if (staff) {
            GL11.glTranslated(0.0F, -0.1, 0.0F);
            GL11.glScaled(1.2, 2.0F, 1.2);
         }

         this.Rod.render(0.0625F);
         if (wand.getRod(wandStack).isGlowing()) {
            int i = player.getBrightnessForRender(0.0F);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
         }

         GL11.glPopMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(wand.getCap(wandStack).getTexture());
         GL11.glPushMatrix();
         if (staff) {
            GL11.glScaled(1.3, 1.1, 1.3);
         } else {
            GL11.glScaled(1.2, 1.0F, 1.2);
         }

         if (wand.isSceptre(wandStack)) {
            GL11.glPushMatrix();
            GL11.glScaled(1.3, 1.3, 1.3);
            this.Cap.render(0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0F, 0.3, 0.0F);
            GL11.glScaled(1.0F, 0.66, 1.0F);
            this.Cap.render(0.0625F);
            GL11.glPopMatrix();
         } else {
            this.Cap.render(0.0625F);
         }

         if (staff) {
            GL11.glTranslated(0.0F, 0.225, 0.0F);
            GL11.glPushMatrix();
            GL11.glScaled(1.0F, 0.66, 1.0F);
            this.Cap.render(0.0625F);
            GL11.glPopMatrix();
            GL11.glTranslated(0.0F, 0.65, 0.0F);
         }

         this.CapBottom.render(0.0625F);
         GL11.glPopMatrix();
         if (wand.getFocus(wandStack) != null) {
            if (wand.getFocus(wandStack).getOrnament(focusStack) != null) {
               GL11.glPushMatrix();
               GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
               Tessellator tessellator = Tessellator.instance;
               IIcon icon = wand.getFocus(wandStack).getOrnament(focusStack);
               float f1 = icon.getMaxU();
               float f2 = icon.getMinV();
               float f3 = icon.getMinU();
               float f4 = icon.getMaxV();
               RenderManager.instance.renderEngine.bindTexture(TextureMap.locationItemsTexture);
               GL11.glPushMatrix();
               GL11.glTranslatef(-0.25F, -0.1F, 0.0275F);
               GL11.glScaled(0.5F, 0.5F, 0.5F);
               ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.1F);
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(-0.25F, -0.1F, 0.0275F);
               GL11.glScaled(0.5F, 0.5F, 0.5F);
               ItemRenderer.renderItemIn2D(tessellator, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.1F);
               GL11.glPopMatrix();
               GL11.glPopMatrix();
            }

            float alpha = 0.95F;
            if (wand.getFocus(wandStack).getFocusDepthLayerIcon(focusStack) != null) {
               GL11.glPushMatrix();
               if (staff) {
                  GL11.glTranslatef(0.0F, -0.15F, 0.0F);
                  GL11.glScaled(0.165, 0.1765, 0.165);
               } else {
                  GL11.glTranslatef(0.0F, -0.09F, 0.0F);
                  GL11.glScaled(0.16, 0.16, 0.16);
               }

               Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
               this.renderBlocks.setRenderBoundsFromBlock(Blocks.stone);
               BlockRenderer.drawFaces(this.renderBlocks, null, wand.getFocus(wandStack).getFocusDepthLayerIcon(focusStack), false);
               GL11.glPopMatrix();
               alpha = 0.6F;
            }

            if (Thaumcraft.isHalloween) {
               UtilsFX.bindTexture("textures/models/spec_h.png");
            } else {
               UtilsFX.bindTexture("textures/models/wand.png");
            }

            GL11.glPushMatrix();
            if (staff) {
               GL11.glTranslatef(0.0F, -0.0475F, 0.0F);
               GL11.glScaled(0.525, 0.5525, 0.525);
            } else {
               GL11.glScaled(0.5F, 0.5F, 0.5F);
            }

            Color c = new Color(wand.getFocus(wandStack).getFocusColor(focusStack));
            GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, alpha);
            int j = (int)(195.0F + MathHelper.sin((float)player.ticksExisted / 3.0F) * 10.0F + 10.0F);
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
            this.Focus.render(0.0625F);
            GL11.glPopMatrix();
         }

         if (wand.isSceptre(wandStack)) {
            GL11.glPushMatrix();
            int j = 200;
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
            GL11.glBlendFunc(770, 1);

            for(int rot = 0; rot < 10; ++rot) {
               GL11.glPushMatrix();
               GL11.glRotated(36 * rot + player.ticksExisted, 0.0F, 1.0F, 0.0F);
               this.drawRune(0.16, -0.01F, -0.125F, rot, player);
               GL11.glPopMatrix();
            }

            GL11.glBlendFunc(770, 771);
            GL11.glPopMatrix();
         }

         if (runes) {
            GL11.glPushMatrix();
            int j = 200;
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
            GL11.glBlendFunc(770, 1);

            for(int rot = 0; rot < 4; ++rot) {
               GL11.glRotated(90.0F, 0.0F, 1.0F, 0.0F);

               for(int a = 0; a < 14; ++a) {
                  int rune = (a + rot * 3) % 16;
                  this.drawRune(0.36 + (double)a * 0.14, -0.01F, -0.08, rune, player);
               }
            }

            GL11.glBlendFunc(770, 771);
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
      }
   }

   private void drawRune(double x, double y, double z, int rune, EntityPlayer player) {
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/misc/script.png");
      float r = MathHelper.sin((float)(player.ticksExisted + rune * 5) / 5.0F) * 0.1F + 0.88F;
      float g = MathHelper.sin((float)(player.ticksExisted + rune * 5) / 7.0F) * 0.1F + 0.63F;
      float alpha = MathHelper.sin((float)(player.ticksExisted + rune * 5) / 10.0F) * 0.2F;
      GL11.glColor4f(r, g, 0.2F, alpha + 0.6F);
      GL11.glRotated(90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslated(x, y, z);
      Tessellator tessellator = Tessellator.instance;
      float var8 = 0.0625F * (float)rune;
      float var9 = var8 + 0.0625F;
      float var10 = 0.0F;
      float var11 = 1.0F;
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA_F(r, g, 0.2F, alpha + 0.6F);
      tessellator.addVertexWithUV(-0.06 - (double)(alpha / 40.0F), 0.06 + (double)(alpha / 40.0F), 0.0F, var9, var11);
      tessellator.addVertexWithUV(0.06 + (double)(alpha / 40.0F), 0.06 + (double)(alpha / 40.0F), 0.0F, var9, var10);
      tessellator.addVertexWithUV(0.06 + (double)(alpha / 40.0F), -0.06 - (double)(alpha / 40.0F), 0.0F, var8, var10);
      tessellator.addVertexWithUV(-0.06 - (double)(alpha / 40.0F), -0.06 - (double)(alpha / 40.0F), 0.0F, var8, var11);
      tessellator.draw();
      GL11.glPopMatrix();
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
   }
}
