package thaumcraft.client.fx.bolt;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.WRVector3;
import thaumcraft.client.lib.UtilsFX;

public class FXLightningBolt extends EntityFX {
   private int type = 0;
   private float width = 0.03F;
   private FXLightningBoltCommon main;

   public FXLightningBolt(World world, WRVector3 jammervec, WRVector3 targetvec, long seed) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, jammervec, targetvec, seed);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, Entity detonator, Entity target, long seed) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, detonator, target, seed);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, Entity detonator, Entity target, long seed, int speed) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, detonator, target, seed, speed);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, TileEntity detonator, Entity target, long seed) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, detonator, target, seed);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, 1.0F);
      this.setupFromMain();
   }

   public FXLightningBolt(World world, TileEntity detonator, double x, double y, double z, long seed) {
      super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      this.main = new FXLightningBoltCommon(world, detonator, x, y, z, seed);
      this.setupFromMain();
   }

   private void setupFromMain() {
      this.particleAge = this.main.particleMaxAge;
      this.setPosition(this.main.start.x, this.main.start.y, this.main.start.z);
      this.setVelocity(0.0F, 0.0F, 0.0F);
   }

   public void defaultFractal() {
      this.main.defaultFractal();
   }

   public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle) {
      this.main.fractal(splits, amount, splitchance, splitlength, splitangle);
   }

   public void finalizeBolt() {
      this.main.finalizeBolt();
      FMLClientHandler.instance().getClient().effectRenderer.addEffect(this);
   }

   public void setType(int type) {
      this.type = type;
      this.main.type = type;
   }

   public void setMultiplier(float m) {
      this.main.multiplier = m;
   }

   public void setWidth(float m) {
      this.width = m;
   }

   public void onUpdate() {
      this.main.onUpdate();
      if (this.main.particleAge >= this.main.particleMaxAge) {
         this.setDead();
      }

   }

   private static WRVector3 getRelativeViewVector(WRVector3 pos) {
      EntityPlayer renderentity = FMLClientHandler.instance().getClient().thePlayer;
      return new WRVector3((float)renderentity.posX - pos.x, (float)renderentity.posY - pos.y, (float)renderentity.posZ - pos.z);
   }

   private void renderBolt(Tessellator tessellator, float partialframe, float cosyaw, float cospitch, float sinyaw, float cossinpitch, int pass, float mainalpha) {
      WRVector3 playervec = new WRVector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
      float boltage = this.main.particleAge >= 0 ? (float)this.main.particleAge / (float)this.main.particleMaxAge : 0.0F;
      if (pass == 0) {
         mainalpha = (1.0F - boltage) * 0.4F;
      } else {
         mainalpha = 1.0F - boltage * 0.5F;
      }

      int renderlength = (int)(((float)this.main.particleAge + partialframe + (float)((int)(this.main.length * 3.0F))) / (float)((int)(this.main.length * 3.0F)) * (float)this.main.numsegments0);

      for(FXLightningBoltCommon.Segment rendersegment : this.main.segments) {
         if (rendersegment.segmentno <= renderlength) {
            float width = this.width * (getRelativeViewVector(rendersegment.startpoint.point).length() / 5.0F + 1.0F) * (1.0F + rendersegment.light) * 0.5F;
            WRVector3 diff1 = WRVector3.crossProduct(playervec, rendersegment.prevdiff).scale(width / rendersegment.sinprev);
            WRVector3 diff2 = WRVector3.crossProduct(playervec, rendersegment.nextdiff).scale(width / rendersegment.sinnext);
            WRVector3 startvec = rendersegment.startpoint.point;
            WRVector3 endvec = rendersegment.endpoint.point;
            float rx1 = (float)((double)startvec.x - interpPosX);
            float ry1 = (float)((double)startvec.y - interpPosY);
            float rz1 = (float)((double)startvec.z - interpPosZ);
            float rx2 = (float)((double)endvec.x - interpPosX);
            float ry2 = (float)((double)endvec.y - interpPosY);
            float rz2 = (float)((double)endvec.z - interpPosZ);
            tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, mainalpha * rendersegment.light);
            tessellator.addVertexWithUV(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z, 0.5F, 0.0F);
            tessellator.addVertexWithUV(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z, 0.5F, 0.0F);
            tessellator.addVertexWithUV(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z, 0.5F, 1.0F);
            tessellator.addVertexWithUV(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z, 0.5F, 1.0F);
            if (rendersegment.next == null) {
               WRVector3 roundend = rendersegment.endpoint.point.copy().add(rendersegment.diff.copy().normalize().scale(width));
               float rx3 = (float)((double)roundend.x - interpPosX);
               float ry3 = (float)((double)roundend.y - interpPosY);
               float rz3 = (float)((double)roundend.z - interpPosZ);
               tessellator.addVertexWithUV(rx3 - diff2.x, ry3 - diff2.y, rz3 - diff2.z, 0.0F, 0.0F);
               tessellator.addVertexWithUV(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z, 0.5F, 0.0F);
               tessellator.addVertexWithUV(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z, 0.5F, 1.0F);
               tessellator.addVertexWithUV(rx3 + diff2.x, ry3 + diff2.y, rz3 + diff2.z, 0.0F, 1.0F);
            }

            if (rendersegment.prev == null) {
               WRVector3 roundend = rendersegment.startpoint.point.copy().sub(rendersegment.diff.copy().normalize().scale(width));
               float rx3 = (float)((double)roundend.x - interpPosX);
               float ry3 = (float)((double)roundend.y - interpPosY);
               float rz3 = (float)((double)roundend.z - interpPosZ);
               tessellator.addVertexWithUV(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z, 0.5F, 0.0F);
               tessellator.addVertexWithUV(rx3 - diff1.x, ry3 - diff1.y, rz3 - diff1.z, 0.0F, 0.0F);
               tessellator.addVertexWithUV(rx3 + diff1.x, ry3 + diff1.y, rz3 + diff1.z, 0.0F, 1.0F);
               tessellator.addVertexWithUV(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z, 0.5F, 1.0F);
            }
         }
      }

   }

   public void renderParticle(Tessellator tessellator, float partialframe, float cosyaw, float cospitch, float sinyaw, float sinsinpitch, float cossinpitch) {
      EntityPlayer renderentity = FMLClientHandler.instance().getClient().thePlayer;
      int visibleDistance = 100;
      if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
         visibleDistance = 50;
      }

      if (!(renderentity.getDistance(this.posX, this.posY, this.posZ) > (double)visibleDistance)) {
         tessellator.draw();
         GL11.glPushMatrix();
         GL11.glDepthMask(false);
         GL11.glEnable(GL11.GL_BLEND);
         this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
         float ma = 1.0F;
         switch (this.type) {
            case 0:
               this.particleRed = 0.6F;
               this.particleGreen = 0.3F;
               this.particleBlue = 0.6F;
               GL11.glBlendFunc(770, 1);
               break;
            case 1:
               this.particleRed = 0.6F;
               this.particleGreen = 0.6F;
               this.particleBlue = 0.1F;
               GL11.glBlendFunc(770, 1);
               break;
            case 2:
               this.particleRed = 0.1F;
               this.particleGreen = 0.1F;
               this.particleBlue = 0.6F;
               GL11.glBlendFunc(770, 1);
               break;
            case 3:
               this.particleRed = 0.1F;
               this.particleGreen = 1.0F;
               this.particleBlue = 0.1F;
               GL11.glBlendFunc(770, 1);
               break;
            case 4:
               this.particleRed = 0.6F;
               this.particleGreen = 0.1F;
               this.particleBlue = 0.1F;
               GL11.glBlendFunc(770, 1);
               break;
            case 5:
               this.particleRed = 0.6F;
               this.particleGreen = 0.2F;
               this.particleBlue = 0.6F;
               GL11.glBlendFunc(770, 771);
               break;
            case 6:
               this.particleRed = 0.75F;
               this.particleGreen = 1.0F;
               this.particleBlue = 1.0F;
               ma = 0.2F;
               GL11.glBlendFunc(770, 771);
         }

         UtilsFX.bindTexture("textures/misc/p_large.png");
         tessellator.startDrawingQuads();
         tessellator.setBrightness(15728880);
         this.renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 0, ma);
         tessellator.draw();
         switch (this.type) {
            case 0:
               this.particleRed = 1.0F;
               this.particleGreen = 0.6F;
               this.particleBlue = 1.0F;
               break;
            case 1:
               this.particleRed = 1.0F;
               this.particleGreen = 1.0F;
               this.particleBlue = 0.1F;
               break;
            case 2:
               this.particleRed = 0.1F;
               this.particleGreen = 0.1F;
               this.particleBlue = 1.0F;
               break;
            case 3:
               this.particleRed = 0.1F;
               this.particleGreen = 0.6F;
               this.particleBlue = 0.1F;
               break;
            case 4:
               this.particleRed = 1.0F;
               this.particleGreen = 0.1F;
               this.particleBlue = 0.1F;
               break;
            case 5:
               this.particleRed = 0.0F;
               this.particleGreen = 0.0F;
               this.particleBlue = 0.0F;
               GL11.glBlendFunc(770, 771);
               break;
            case 6:
               this.particleRed = 0.75F;
               this.particleGreen = 1.0F;
               this.particleBlue = 1.0F;
               ma = 0.2F;
               GL11.glBlendFunc(770, 771);
         }

         UtilsFX.bindTexture("textures/misc/p_small.png");
         tessellator.startDrawingQuads();
         tessellator.setBrightness(15728880);
         this.renderBolt(tessellator, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 1, ma);
         tessellator.draw();
         GL11.glDisable(GL11.GL_BLEND);
         GL11.glDepthMask(true);
         GL11.glPopMatrix();
         Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
         tessellator.startDrawingQuads();
      }
   }

   public int getRenderPass() {
      return 2;
   }
}
