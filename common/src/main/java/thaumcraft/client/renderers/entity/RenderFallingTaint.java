package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.EntityFallingTaint;

@SideOnly(Side.CLIENT)
public class RenderFallingTaint extends Render {
   private RenderBlocks renderBlocks = new RenderBlocks();

   public RenderFallingTaint() {
      this.shadowSize = 0.5F;
   }

   public void doRenderFalling(EntityFallingTaint entity, double par2, double par4, double par6, float par8, float par9) {
      World world = entity.getWorld();
      if (world.getBlock(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)) != entity.block) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2, (float)par4, (float)par6);
         Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         GL11.glDisable(2896);
         if (entity.block != null) {
            this.renderBlocks.setRenderBoundsFromBlock(entity.block);
            this.renderBlocks.renderBlockSandFalling(entity.block, world, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ), entity.metadata);
         }

         GL11.glEnable(2896);
         GL11.glPopMatrix();
      }

   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderFalling((EntityFallingTaint)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
