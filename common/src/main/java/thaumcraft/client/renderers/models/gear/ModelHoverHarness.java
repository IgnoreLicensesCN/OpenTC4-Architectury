package thaumcraft.client.renderers.models.gear;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.items.armor.ItemHoverHarness;
import thaumcraft.common.lib.utils.BlockUtils;

@SideOnly(Side.CLIENT)
public class ModelHoverHarness extends ModelBiped {
   HashMap<Integer,Long> timingShock = new HashMap<>();
   private static final ResourceLocation HARNESS = new ResourceLocation("thaumcraft", "textures/models/hoverharness.obj");
   private IModelCustom modelBack;

   public ModelHoverHarness() {
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.6F);
      this.modelBack = AdvancedModelLoader.loadModel(HARNESS);
   }

   public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      GL11.glPushMatrix();
      GL11.glPushMatrix();
      if (entity != null && entity.isSneaking()) {
         GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
      }

      this.bipedBody.render(par7);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glDisable(2896);
      GL11.glScalef(0.1F, 0.1F, 0.1F);
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      if (entity != null && entity.isSneaking()) {
         GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, 0.33F, -3.7F);
      FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/models/hoverharness2.png"));
      this.modelBack.renderAll();
      GL11.glEnable(2896);
      GL11.glPopMatrix();
      if (entity instanceof EntityPlayer && !GL11.glIsEnabled(GL11.GL_BLEND) && GL11.glGetInteger(2976) == 5888 && ((EntityPlayer) entity).inventory.armorItemInSlot(2).hasTagCompound() && ((EntityPlayer) entity).inventory.armorItemInSlot(2).stackTagCompound.hasKey("hover") && ((EntityPlayer) entity).inventory.armorItemInSlot(2).stackTagCompound.getByte("hover") == 1) {
         long currenttime = System.currentTimeMillis();
         long timeShock = 0L;
         if (this.timingShock.get(entity.getEntityId()) != null) {
            timeShock = this.timingShock.get(entity.getEntityId());
         }

         GL11.glPushMatrix();
         float mod = 0.0F;
         if (entity.isSneaking()) {
            GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.075F, -0.05F);
            mod = 0.075F;
         }

         GL11.glTranslatef(0.0F, 0.2F, 0.55F);
         GL11.glPushMatrix();
         UtilsFX.renderQuadCenteredFromIcon(false, ((ItemHoverHarness)((EntityPlayer)entity).inventory.armorItemInSlot(2).getItem()).iconLightningRing, 2.5F, 1.0F, 1.0F, 1.0F, 230, 1, 1.0F);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(0.0F, 0.0F, 0.03F);
         UtilsFX.renderQuadCenteredFromIcon(false, ((ItemHoverHarness)((EntityPlayer)entity).inventory.armorItemInSlot(2).getItem()).iconLightningRing, 1.5F, 1.0F, 0.5F, 1.0F, 230, 1, 1.0F);
         GL11.glPopMatrix();
         GL11.glPopMatrix();
         if (timeShock < currenttime) {
            timeShock = currenttime + 50L + (long)entity.worldObj.rand.nextInt(50);
            this.timingShock.put(entity.getEntityId(), timeShock);
            MovingObjectPosition mop = BlockUtils.getTargetBlock(entity.worldObj, entity.posX, entity.posY - (double)0.45F - (double)mod, entity.posZ, ((EntityPlayer)entity).renderYawOffset - 90.0F - (float)entity.worldObj.rand.nextInt(180), (float)(-80 + entity.worldObj.rand.nextInt(160)), false, 6.0F);
            if (mop != null) {
               double px = mop.hitVec.xCoord;
               double py = mop.hitVec.yCoord;
               double pz = mop.hitVec.zCoord;
               FXLightningBolt bolt = new FXLightningBolt(entity.worldObj, entity.posX - (double)(MathHelper.cos((((EntityPlayer)entity).renderYawOffset + 90.0F) / 180.0F * 3.141593F) * 0.5F), entity.posY - (double)0.45F - (double)mod, entity.posZ - (double)(MathHelper.sin((((EntityPlayer)entity).renderYawOffset + 90.0F) / 180.0F * 3.141593F) * 0.5F), px, py, pz, entity.worldObj.rand.nextLong(), 1, 2.0F, 3);
               bolt.defaultFractal();
               bolt.setType(6);
               bolt.setWidth(0.015F);
               bolt.finalizeBolt();
            }
         }
      }

      GL11.glPopMatrix();
   }
}
