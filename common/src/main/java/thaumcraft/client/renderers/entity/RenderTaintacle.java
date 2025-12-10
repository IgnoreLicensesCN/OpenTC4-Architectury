package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelTaintacle;
import thaumcraft.common.entities.monster.boss.EntityTaintacleGiant;

@SideOnly(Side.CLIENT)
public class RenderTaintacle extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/taintacle.png");

   public RenderTaintacle(float shadow, int length) {
      super(new ModelTaintacle(length), shadow);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      if (par1EntityLiving instanceof EntityTaintacleGiant) {
         BossStatus.setBossStatus((EntityTaintacleGiant)par1EntityLiving, false);
         GL11.glScalef(1.33F, 1.33F, 1.33F);
      }

      super.preRenderCallback(par1EntityLiving, par2);
   }
}
