package thaumcraft.client.renderers.entity;

import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.EntitySpecialItem;

public class RenderFollowingItem extends Render {
   private RenderBlocks renderBlocks = new RenderBlocks();
   private Random random = new Random();
   public boolean renderWithColor = true;
   public float zLevel = 0.0F;

   public RenderFollowingItem() {
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRenderItem(EntitySpecialItem par1EntityItem, double par2, double par4, double par6, float par8, float pticks) {
      this.random.setSeed(187L);
      RenderItem ri = new RenderItem();
      ri.setRenderManager(RenderManager.instance);
      ItemStack var10 = par1EntityItem.getEntityItem();
      if (var10 != null) {
         EntityItem ei = new EntityItem(par1EntityItem.worldObj, par1EntityItem.posX, par1EntityItem.posY, par1EntityItem.posZ, var10);
         ei.age = par1EntityItem.age;
         ei.hoverStart = par1EntityItem.hoverStart;
         ri.doRender(ei, par2, par4, par6, par8, pticks);
      }

   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderItem((EntitySpecialItem)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
