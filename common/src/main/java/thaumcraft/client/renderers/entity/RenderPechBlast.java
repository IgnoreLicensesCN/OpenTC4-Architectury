package thaumcraft.client.renderers.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.projectile.EntityPechBlast;

public class RenderPechBlast extends Render {
   public RenderPechBlast() {
      this.shadowSize = 0.1F;
   }

   public void renderEntityAt(EntityPechBlast tg, double x, double y, double z, float fq) {
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt((EntityPechBlast)entity, d, d1, d2, f);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
