package thaumcraft.client.renderers.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockRenderer {
   public static float W1 = 0.0625F;
   public static float W2 = 0.125F;
   public static float W3 = 0.1875F;
   public static float W4 = 0.25F;
   public static float W5 = 0.3125F;
   public static float W6 = 0.375F;
   public static float W7 = 0.4375F;
   public static float W8 = 0.5F;
   public static float W9 = 0.5625F;
   public static float W10 = 0.625F;
   public static float W11 = 0.6875F;
   public static float W12 = 0.75F;
   public static float W13 = 0.8125F;
   public static float W14 = 0.875F;
   public static float W15 = 0.9375F;

   public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon icon, boolean st) {
      drawFaces(renderblocks, block, icon, icon, icon, icon, icon, icon, st);
   }

   public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon i1, IIcon i2, IIcon i3, IIcon i4, IIcon i5, IIcon i6, boolean solidtop) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      renderblocks.renderFaceYNeg(block, 0.0F, 0.0F, 0.0F, i1);
      tessellator.draw();
      if (solidtop) {
         GL11.glDisable(3008);
      }

      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      renderblocks.renderFaceYPos(block, 0.0F, 0.0F, 0.0F, i2);
      tessellator.draw();
      if (solidtop) {
         GL11.glEnable(3008);
      }

      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      renderblocks.renderFaceXNeg(block, 0.0F, 0.0F, 0.0F, i3);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      renderblocks.renderFaceXPos(block, 0.0F, 0.0F, 0.0F, i4);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      renderblocks.renderFaceZNeg(block, 0.0F, 0.0F, 0.0F, i5);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      renderblocks.renderFaceZPos(block, 0.0F, 0.0F, 0.0F, i6);
      tessellator.draw();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
   }

   public static int setBrightness(IBlockAccess blockAccess, int i, int j, int k, Block block) {
      Tessellator tessellator = Tessellator.instance;
      int mb = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
      tessellator.setBrightness(mb);
      float f = 1.0F;
      int l = block.colorMultiplier(blockAccess, i, j, k);
      float f1 = (float)(l >> 16 & 255) / 255.0F;
      float f2 = (float)(l >> 8 & 255) / 255.0F;
      float f3 = (float)(l & 255) / 255.0F;
      if (EntityRenderer.anaglyphEnable) {
         float f6 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
         float f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
         float f7 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
         f1 = f6;
         f2 = f4;
         f3 = f7;
      }

      tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
      return mb;
   }

   protected static void renderAllSides(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex) {
      renderAllSides(world, x, y, z, block, renderer, tex, true);
   }

   protected static void renderAllSides(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex, boolean allsides) {
      if (allsides || block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
         renderer.renderFaceXPos(block, x, y, z, tex);
      }

      if (allsides || block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
         renderer.renderFaceXNeg(block, x, y, z, tex);
      }

      if (allsides || block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
         renderer.renderFaceZPos(block, x, y, z, tex);
      }

      if (allsides || block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
         renderer.renderFaceZNeg(block, x, y, z, tex);
      }

      if (allsides || block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
         renderer.renderFaceYPos(block, x, y, z, tex);
      }

      if (allsides || block.shouldSideBeRendered(world, x, y - 1, z, 6)) {
         renderer.renderFaceYNeg(block, x, y, z, tex);
      }

   }

   protected static void renderAllSides(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, boolean allsides) {
      if (allsides || block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
         renderer.renderFaceXPos(block, x, y, z, block.getIcon(world, x, y, z, 5));
      }

      if (allsides || block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
         renderer.renderFaceXNeg(block, x, y, z, block.getIcon(world, x, y, z, 4));
      }

      if (allsides || block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
         renderer.renderFaceZPos(block, x, y, z, block.getIcon(world, x, y, z, 3));
      }

      if (allsides || block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
         renderer.renderFaceZNeg(block, x, y, z, block.getIcon(world, x, y, z, 2));
      }

      if (allsides || block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
         renderer.renderFaceYPos(block, x, y, z, block.getIcon(world, x, y, z, 1));
      }

      if (allsides || block.shouldSideBeRendered(world, x, y - 1, z, 6)) {
         renderer.renderFaceYNeg(block, x, y, z, block.getIcon(world, x, y, z, 0));
      }

   }

   protected static void renderAllSidesInverted(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex, boolean allsides) {
      if (allsides || !block.shouldSideBeRendered(world, x - 1, y, z, 6)) {
         renderer.renderFaceXPos(block, x - 1, y, z, tex);
      }

      if (allsides || !block.shouldSideBeRendered(world, x + 1, y, z, 6)) {
         renderer.renderFaceXNeg(block, x + 1, y, z, tex);
      }

      if (allsides || !block.shouldSideBeRendered(world, x, y, z - 1, 6)) {
         renderer.renderFaceZPos(block, x, y, z - 1, tex);
      }

      if (allsides || !block.shouldSideBeRendered(world, x, y, z + 1, 6)) {
         renderer.renderFaceZNeg(block, x, y, z + 1, tex);
      }

      if (allsides || !block.shouldSideBeRendered(world, x, y - 1, z, 6)) {
         renderer.renderFaceYPos(block, x, y - 1, z, tex);
      }

      if (allsides || !block.shouldSideBeRendered(world, x, y + 1, z, 6)) {
         renderer.renderFaceYNeg(block, x, y + 1, z, tex);
      }

   }

   protected static void renderAllSides(int x, int y, int z, Block block, RenderBlocks renderer, IIcon tex) {
      renderer.renderFaceXPos(block, x - 1, y, z, tex);
      renderer.renderFaceXNeg(block, x + 1, y, z, tex);
      renderer.renderFaceZPos(block, x, y, z - 1, tex);
      renderer.renderFaceZNeg(block, x, y, z + 1, tex);
      renderer.renderFaceYPos(block, x, y - 1, z, tex);
      renderer.renderFaceYNeg(block, x, y + 1, z, tex);
   }
}
