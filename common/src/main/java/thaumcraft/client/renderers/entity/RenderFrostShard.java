package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.projectile.EntityFrostShard;

@SideOnly(Side.CLIENT)
public class RenderFrostShard extends Render {
   private IModelCustom model;
   private static final ResourceLocation ORB = new ResourceLocation("thaumcraft", "textures/models/orb.obj");
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/blocks/frostshard.png");

   public RenderFrostShard() {
      this.model = AdvancedModelLoader.loadModel(ORB);
   }

   public void renderShard(EntityFrostShard shard, double par2, double par4, double par6, float par8, float par9) {
      this.bindEntityTexture(shard);
      GL11.glPushMatrix();
      GL11.glEnable(32826);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 771);
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      Random rnd = new Random(shard.getEntityId());
      GL11.glRotatef(shard.prevRotationYaw + (shard.rotationYaw - shard.prevRotationYaw) * par9, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(shard.prevRotationPitch + (shard.rotationPitch - shard.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
      GL11.glPushMatrix();
      float base = shard.getDamage() * 0.1F;
      GL11.glScalef(base + rnd.nextFloat() * 0.1F, base + rnd.nextFloat() * 0.1F, base + rnd.nextFloat() * 0.1F);
      this.model.renderAll();
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderShard((EntityFrostShard)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return rl;
   }
}
