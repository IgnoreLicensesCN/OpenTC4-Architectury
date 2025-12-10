package thaumcraft.client.renderers.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;

@SideOnly(Side.CLIENT)
public class RenderBrainyZombie extends RenderZombie {
   private static final ResourceLocation zombieTextures = new ResourceLocation("thaumcraft", "textures/models/bzombie.png");
   private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("thaumcraft", "textures/models/bzombievil.png");
   private ModelBiped field_82434_o;
   private ModelZombieVillager zombieVillagerModel;
   private int field_82431_q = 1;

   protected ResourceLocation getEntityTexture(EntityZombie par1EntityZombie) {
      return par1EntityZombie.isVillager() ? zombieVillagerTextures : zombieTextures;
   }

   protected void preRenderScale(EntityGiantBrainyZombie z, float par2) {
      GL11.glScalef(z.getAnger(), z.getAnger(), z.getAnger());
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
      if (par1EntityLiving instanceof EntityGiantBrainyZombie) {
         this.preRenderScale((EntityGiantBrainyZombie)par1EntityLiving, par2);
      }

   }

   private void func_82427_a(EntityZombie par1EntityZombie) {
      if (par1EntityZombie.isVillager()) {
         if (this.field_82431_q != this.zombieVillagerModel.func_82897_a()) {
            this.zombieVillagerModel = new ModelZombieVillager();
            this.field_82431_q = this.zombieVillagerModel.func_82897_a();
            this.field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
            this.field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
         }

         this.mainModel = this.zombieVillagerModel;
         this.field_82423_g = this.field_82436_m;
         this.field_82425_h = this.field_82433_n;
      } else {
         this.mainModel = this.field_82434_o;
         this.field_82423_g = this.field_82437_k;
         this.field_82425_h = this.field_82435_l;
      }

      this.modelBipedMain = (ModelBiped)this.mainModel;
   }
}
