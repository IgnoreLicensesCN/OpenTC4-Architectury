package thaumcraft.client.renderers.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class ModelRendererTaintacle extends ModelRenderer {
   private int textureOffsetX;
   private int textureOffsetY;
   private boolean compiled;
   private int displayList;
   private ModelBase baseModel;

   public ModelRendererTaintacle(ModelBase par1ModelBase) {
      super(par1ModelBase);
   }

   public ModelRendererTaintacle(ModelBase par1ModelBase, int par2, int par3) {
      this(par1ModelBase);
      this.setTextureOffset(par2, par3);
   }

   @SideOnly(Side.CLIENT)
   public void render(float par1, float scale) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(par1);
         }

         GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
               if (this.childModels == null) {
                  int j = 15728880;
                  int k = j % 65536;
                  int l = j / 65536;
                  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
               }

               GL11.glCallList(this.displayList);
               if (this.childModels != null) {
                   for (Object childModel : this.childModels) {
                       GL11.glPushMatrix();
                       GL11.glScalef(scale, scale, scale);
                       ((ModelRendererTaintacle) childModel).render(par1, scale);
                       GL11.glPopMatrix();
                   }
               }
            } else {
               GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
               if (this.childModels == null) {
                  int j = 15728880;
                  int k = j % 65536;
                  int l = j / 65536;
                  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
               }

               GL11.glCallList(this.displayList);
               if (this.childModels != null) {
                   for (Object childModel : this.childModels) {
                       GL11.glPushMatrix();
                       GL11.glScalef(scale, scale, scale);
                       ((ModelRendererTaintacle) childModel).render(par1, scale);
                       GL11.glPopMatrix();
                   }
               }

               GL11.glTranslatef(-this.rotationPointX * par1, -this.rotationPointY * par1, -this.rotationPointZ * par1);
            }
         } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
            if (this.rotateAngleZ != 0.0F) {
               GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
               GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            }

            if (this.childModels == null) {
               int j = 15728880;
               int k = j % 65536;
               int l = j / 65536;
               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
            }

            GL11.glCallList(this.displayList);
            if (this.childModels != null) {
                for (Object childModel : this.childModels) {
                    GL11.glPushMatrix();
                    GL11.glScalef(scale, scale, scale);
                    ((ModelRendererTaintacle) childModel).render(par1, scale);
                    GL11.glPopMatrix();
                }
            }

            GL11.glPopMatrix();
         }

         GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
      }

   }

   @SideOnly(Side.CLIENT)
   private void compileDisplayList(float par1) {
      this.displayList = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.displayList, 4864);
      Tessellator tessellator = Tessellator.instance;

       for (Object o : this.cubeList) {
           ((ModelBox) o).render(tessellator, par1);
       }

      GL11.glEndList();
      this.compiled = true;
   }
}
