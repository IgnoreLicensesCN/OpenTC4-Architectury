package thaumcraft.client.renderers.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.TileEldritchPortal;

public class TileEldritchPortalRenderer extends TileEntitySpecialRenderer {
   public static final ResourceLocation portaltex = new ResourceLocation("thaumcraft", "textures/misc/eldritch_portal.png");

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      GL11.glPushMatrix();
      if (te.getWorldObj() != null) {
         this.renderPortal((TileEldritchPortal)te, x, y, z, f);
      }

      GL11.glPopMatrix();
   }

   private void renderPortal(TileEldritchPortal te, double x, double y, double z, float f) {
      long nt = System.nanoTime();
      long time = nt / 50000000L;
      int c = (int)Math.min(30.0F, (float)te.opencount + f);
      int e = (int)Math.min(5.0F, (float)te.opencount + f);
      float scale = (float)e / 5.0F;
      float scaley = (float)c / 30.0F;
      UtilsFX.bindTexture(portaltex);
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
      if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
         Tessellator tessellator = Tessellator.instance;
         float arX = ActiveRenderInfo.rotationX;
         float arZ = ActiveRenderInfo.rotationZ;
         float arYZ = ActiveRenderInfo.rotationYZ;
         float arXY = ActiveRenderInfo.rotationXY;
         float arXZ = ActiveRenderInfo.rotationXZ;
         EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
         double var10000 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
         var10000 = player.prevPosY + (player.posY - player.prevPosY) * (double)f;
         var10000 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(220);
         tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
         double px = x + (double)0.5F;
         double py = y + (double)0.5F;
         double pz = z + (double)0.5F;
         Vec3 v1 = Vec3.createVectorHelper(-arX - arYZ, -arXZ, -arZ - arXY);
         Vec3 v2 = Vec3.createVectorHelper(-arX + arYZ, arXZ, -arZ + arXY);
         Vec3 v3 = Vec3.createVectorHelper(arX + arYZ, arXZ, arZ + arXY);
         Vec3 v4 = Vec3.createVectorHelper(arX - arYZ, -arXZ, arZ - arXY);
         int frame = (int)time % 16;
         float f2 = (float)frame / 16.0F;
         float f3 = f2 + 0.0625F;
         float f4 = 0.0F;
         float f5 = 1.0F;
         tessellator.setNormal(0.0F, 0.0F, -1.0F);
         tessellator.addVertexWithUV(px + v1.xCoord * (double)scale, py + v1.yCoord * (double)scaley, pz + v1.zCoord * (double)scale, f2, f5);
         tessellator.addVertexWithUV(px + v2.xCoord * (double)scale, py + v2.yCoord * (double)scaley, pz + v2.zCoord * (double)scale, f3, f5);
         tessellator.addVertexWithUV(px + v3.xCoord * (double)scale, py + v3.yCoord * (double)scaley, pz + v3.zCoord * (double)scale, f3, f4);
         tessellator.addVertexWithUV(px + v4.xCoord * (double)scale, py + v4.yCoord * (double)scaley, pz + v4.zCoord * (double)scale, f2, f4);
         tessellator.draw();
      }

      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }
}
