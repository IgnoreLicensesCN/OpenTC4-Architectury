package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.tiles.TileJarNode;
import thaumcraft.common.tiles.TileNode;

@SideOnly(Side.CLIENT)
public class TileNodeRenderer extends TileEntitySpecialRenderer {
   public static final ResourceLocation nodetex = new ResourceLocation("thaumcraft", "textures/misc/nodes.png");

   public static void renderFacingStrip_tweaked(double px, double py, double pz, float angle, float scale, float alpha, int frames, int strip, int frame, float partialTicks, int color) {
      UtilsFX.renderFacingStrip(px, py, pz, angle,
              Math.min(scale, ConfigurationHandler.INSTANCE.getNodeVisualSizeLimit()),
              alpha, frames, strip, frame, partialTicks, color);
   }
   public static void renderNode(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
      long nt = System.nanoTime();
      UtilsFX.bindTexture(nodetex);
      int frames = 32;
      if (aspects.size() > 0 && visible) {
         double distance = viewer.getDistance((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F);
         if (distance > viewDistance) {
            return;
         }

         float alpha = (float)((viewDistance - distance) / viewDistance);
         if (mod != null) {
            switch (mod) {
               case BRIGHT:
                  alpha *= 1.5F;
                  break;
               case PALE:
                  alpha *= 0.66F;
                  break;
               case FADING:
                  alpha *= MathHelper.sin((float)viewer.ticksExisted / 3.0F) * 0.25F + 0.33F;
            }
         }

         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glDepthMask(false);
         if (depthIgnore) {
            GL11.glDisable(2929);
         }

         GL11.glDisable(2884);
         long time = nt / 5000000L;
         float bscale = 0.25F;
         GL11.glPushMatrix();
         float rad = ((float)Math.PI * 2F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
         int i = (int)((nt / 40000000L + (long)x) % (long)frames);
         int count = 0;
         float scale = 0.0F;
         float angle = 0.0F;
         float average = 0.0F;

         for(Aspect aspect : aspects.getAspects()) {
            if (aspect.getBlend() == 771) {
               alpha = (float)((double)alpha * (double)1.5F);
            }

            average += (float)aspects.getAmount(aspect);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(770, aspect.getBlend());
            scale = MathHelper.sin((float)viewer.ticksExisted / (14.0F - (float)count)) * bscale + bscale * 2.0F;
            scale = 0.2F + scale * ((float)aspects.getAmount(aspect) / 50.0F);
            scale *= size;
            angle = (float)(time % (5000 + 500L * count)) / (5000.0F + (float)(500 * count)) * rad;
//            UtilsFX.renderFacingStrip
            renderFacingStrip_tweaked
                    ((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, angle, scale, alpha / Math.max(1.0F, (float)aspects.size() / 2.0F), frames, 0, i, partialTicks, aspect.getColor());
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            ++count;
            if (aspect.getBlend() == 771) {
               alpha = (float)((double)alpha / (double)1.5F);
            }
         }

         average /= (float)aspects.size();
         GL11.glPushMatrix();
         GL11.glEnable(GL11.GL_BLEND);
         i = (int)((nt / 40000000L + (long)x) % (long)frames);
         scale = 0.1F + average / 150.0F;
         scale *= size;
         int strip = 1;
         switch (type) {
            case NORMAL:
               GL11.glBlendFunc(770, 1);
               break;
            case UNSTABLE:
               GL11.glBlendFunc(770, 1);
               strip = 6;
               angle = 0.0F;
               break;
            case DARK:
               GL11.glBlendFunc(770, 771);
               strip = 2;
               break;
            case TAINTED:
               GL11.glBlendFunc(770, 771);
               strip = 5;
               break;
            case PURE:
               GL11.glBlendFunc(770, 1);
               strip = 4;
               break;
            case HUNGRY:
               scale *= 0.75F;
               GL11.glBlendFunc(770, 1);
               strip = 3;
         }

         GL11.glColor4f(1.0F, 0.0F, 1.0F, alpha);
//         UtilsFX.renderFacingStrip
         renderFacingStrip_tweaked
                 ((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, angle, scale, alpha, frames, strip, i, partialTicks, 16777215);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glPopMatrix();
         GL11.glPopMatrix();
         GL11.glEnable(2884);
         if (depthIgnore) {
            GL11.glEnable(2929);
         }

         GL11.glDepthMask(true);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      } else {
         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);
         GL11.glDepthMask(false);
         int i = (int)((nt / 40000000L + (long)x) % (long)frames);
         GL11.glColor4f(1.0F, 0.0F, 1.0F, 0.1F);
         //            UtilsFX.renderFacingStrip
         renderFacingStrip_tweaked
                 ((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 0.0F, 0.5F, 0.1F, frames, 1, i, partialTicks, 16777215);
         GL11.glDepthMask(true);
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
      if (tile instanceof INode) {
         float size = 1.0F;
         INode node = (INode)tile;
         double viewDistance = 64.0F;
         EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
         boolean condition = false;
         boolean depthIgnore = false;
         if (viewer instanceof EntityPlayer) {
            if (tile != null && tile instanceof TileJarNode) {
               condition = true;
               size = 0.7F;
            } else if (((EntityPlayer)viewer).inventory.armorItemInSlot(3) != null && ((EntityPlayer)viewer).inventory.armorItemInSlot(3).getItem() instanceof IRevealer && ((IRevealer)((EntityPlayer)viewer).inventory.armorItemInSlot(3).getItem()).showNodes(((EntityPlayer)viewer).inventory.armorItemInSlot(3), viewer)) {
               condition = true;
               depthIgnore = true;
            } else if (((EntityPlayer)viewer).inventory.getCurrentItem() != null && ((EntityPlayer)viewer).inventory.getCurrentItem().getItem() instanceof ItemThaumometer && UtilsFX.isVisibleTo(0.44F, viewer, tile.xCoord, tile.yCoord, tile.zCoord)) {
               condition = true;
               depthIgnore = true;
               viewDistance = 48.0F;
            }
         }

         renderNode(viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode)tile).getAspects(), ((INode)tile).getNodeType(), ((INode)tile).getNodeModifier());
         if (tile instanceof TileNode && ((TileNode)tile).drainEntity != null && ((TileNode)tile).drainCollision != null) {
            Entity drainEntity = ((TileNode)tile).drainEntity;
            if (drainEntity instanceof EntityPlayer && !((EntityPlayer)drainEntity).isUsingItem()) {
               ((TileNode)tile).drainEntity = null;
               ((TileNode)tile).drainCollision = null;
               return;
            }

            MovingObjectPosition drainCollision = ((TileNode)tile).drainCollision;
            GL11.glPushMatrix();
            float f10 = 0.0F;
            int iiud = ((EntityPlayer)drainEntity).getItemInUseDuration();
            if (drainEntity instanceof EntityPlayer) {
               f10 = MathHelper.sin((float)iiud / 10.0F) * 10.0F;
            }

            Vec3 vec3 = Vec3.createVectorHelper(-0.1, -0.1, 0.5F);
            vec3.rotateAroundX(-(drainEntity.prevRotationPitch + (drainEntity.rotationPitch - drainEntity.prevRotationPitch) * partialTicks) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(-(drainEntity.prevRotationYaw + (drainEntity.rotationYaw - drainEntity.prevRotationYaw) * partialTicks) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(-f10 * 0.01F);
            vec3.rotateAroundX(-f10 * 0.015F);
            double d3 = drainEntity.prevPosX + (drainEntity.posX - drainEntity.prevPosX) * (double)partialTicks + vec3.xCoord;
            double d4 = drainEntity.prevPosY + (drainEntity.posY - drainEntity.prevPosY) * (double)partialTicks + vec3.yCoord;
            double d5 = drainEntity.prevPosZ + (drainEntity.posZ - drainEntity.prevPosZ) * (double)partialTicks + vec3.zCoord;
            double d6 = drainEntity == Minecraft.getMinecraft().thePlayer ? (double)0.0F : (double)drainEntity.getEyeHeight();
            UtilsFX.drawFloatyLine(d3, d4 + d6, d5, (double)drainCollision.blockX + (double)0.5F, (double)drainCollision.blockY + (double)0.5F, (double)drainCollision.blockZ + (double)0.5F, partialTicks, ((TileNode)tile).color.getRGB(), "textures/misc/wispy.png", -0.02F, (float)Math.min(iiud, 10) / 10.0F);
            GL11.glPopMatrix();
         }

      }
   }
}
