package thaumcraft.client.fx.migrated.bolt;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;
import thaumcraft.client.fx.migrated.WRVector3;
import thaumcraft.client.lib.UtilsFX;

public class FXLightningBolt extends ThaumcraftParticle {

    private int type = 0;
    private float width = 0.03F;
    private FXLightningBoltCommon main;

    public FXLightningBolt(ClientLevel world, WRVector3 jammervec, WRVector3 targetvec, long seed) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, jammervec, targetvec, seed);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, Entity detonator, Entity target, long seed) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, detonator, target, seed);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, Entity detonator, Entity target, long seed, int speed) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, detonator, target, seed, speed);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, BlockEntity detonator, Entity target, long seed) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, detonator, target, seed);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, 1.0F);
        this.setupFromMain();
    }

    public FXLightningBolt(ClientLevel world, BlockEntity detonator, double x, double y, double z, long seed) {
        super(world, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.main = new FXLightningBoltCommon(world, detonator, x, y, z, seed);
        this.setupFromMain();
    }

    private void setupFromMain() {
        this.age = this.main.lifetime;
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
        Minecraft.getInstance().particleEngine.add(this);
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

    @Override
    public void tick() {
        this.main.onUpdate();
        if (this.main.age >= this.main.lifetime) {
            this.setDead();
        }

    }

    private static WRVector3 getRelativeViewVector(WRVector3 pos) {
        Player renderentity = Minecraft.getInstance().player;
        var playerPos = renderentity.position();
        return new WRVector3((float) playerPos.x - pos.x, (float) playerPos.y - pos.y,
                (float) playerPos.z - pos.z
        );
    }

    private void renderBolt(VertexConsumer consumer, float partialframe, float cosyaw, float cospitch, float sinyaw, float cossinpitch, int pass, float mainalpha,Camera camera) {
        var interpPosX = camera.getPosition().x;
        var interpPosY = camera.getPosition().y;
        var interpPosZ = camera.getPosition().z;

        WRVector3 playervec = new WRVector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
        float boltage = this.main.age >= 0 ? (float) this.main.age / (float) this.main.lifetime : 0.0F;
        if (pass == 0) {
            mainalpha = (1.0F - boltage) * 0.4F;
        } else {
            mainalpha = 1.0F - boltage * 0.5F;
        }

        int renderlength = (int)(((float) this.main.age + partialframe + (float)((int)(this.main.length * 3.0F))) / (float)((int)(this.main.length * 3.0F)) * (float) this.main.numsegments0);

        for (FXLightningBoltCommon.Segment rendersegment : this.main.segments) {
            if (rendersegment.segmentno <= renderlength) {
                float width = this.width * (getRelativeViewVector(
                        rendersegment.startpoint.point).length() / 5.0F + 1.0F) * (1.0F + rendersegment.light) * 0.5F;
                WRVector3 diff1 = WRVector3.crossProduct(playervec, rendersegment.prevdiff)
                        .scale(width / rendersegment.sinprev);
                WRVector3 diff2 = WRVector3.crossProduct(playervec, rendersegment.nextdiff)
                        .scale(width / rendersegment.sinnext);
                WRVector3 startvec = rendersegment.startpoint.point;
                WRVector3 endvec = rendersegment.endpoint.point;
                float rx1 = (float) ((double) startvec.x - interpPosX);
                float ry1 = (float) ((double) startvec.y - interpPosY);
                float rz1 = (float) ((double) startvec.z - interpPosZ);
                float rx2 = (float) ((double) endvec.x - interpPosX);
                float ry2 = (float) ((double) endvec.y - interpPosY);
                float rz2 = (float) ((double) endvec.z - interpPosZ);
                consumer.vertex(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z).uv(0.5F, 0.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                consumer.vertex(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z).uv(0.5F, 0.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                consumer.vertex(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z).uv(0.5F, 1.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                consumer.vertex(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z).uv(0.5F, 1.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                if (rendersegment.next == null) {
                    WRVector3 roundend = rendersegment.endpoint.point.copy()
                            .add(rendersegment.diff.copy()
                                    .normalize()
                                    .scale(width));
                    float rx3 = (float) ((double) roundend.x - interpPosX);
                    float ry3 = (float) ((double) roundend.y - interpPosY);
                    float rz3 = (float) ((double) roundend.z - interpPosZ);

                    consumer.vertex(rx3 - diff2.x, ry3 - diff2.y, rz3 - diff2.z).uv(0.5F, 0.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                    consumer.vertex(rx2 - diff2.x, ry2 - diff2.y, rz2 - diff2.z).uv(0.5F, 0.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                    consumer.vertex(rx2 + diff2.x, ry2 + diff2.y, rz2 + diff2.z).uv(0.5F, 1.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                    consumer.vertex(rx3 + diff2.x, ry3 + diff2.y, rz3 + diff2.z).uv(0.5F, 1.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                }

                if (rendersegment.prev == null) {
                    WRVector3 roundend = rendersegment.startpoint.point.copy()
                            .sub(rendersegment.diff.copy()
                                    .normalize()
                                    .scale(width));
                    float rx3 = (float) ((double) roundend.x - interpPosX);
                    float ry3 = (float) ((double) roundend.y - interpPosY);
                    float rz3 = (float) ((double) roundend.z - interpPosZ);
                    consumer.vertex(rx1 - diff1.x, ry1 - diff1.y, rz1 - diff1.z).uv(0.5F, 0.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                    consumer.vertex(rx3 - diff1.x, ry3 - diff1.y, rz3 - diff1.z).uv(0.5F, 0.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                    consumer.vertex(rx3 + diff1.x, ry3 + diff1.y, rz3 + diff1.z).uv(0.5F, 1.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();
                    consumer.vertex(rx1 + diff1.x, ry1 + diff1.y, rz1 + diff1.z).uv(0.5F, 1.0F).uv2(LightTexture.FULL_BRIGHT).color(this.rCol, this.gCol, this.bCol, mainalpha * rendersegment.light).endVertex();

                }
            }
        }

    }
    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        var rightVec = camera.getLeftVector().negate();
        var f1 = rightVec.x;
        var f3 = rightVec.z;
        var upVec = camera.getUpVector();
        var f4 = upVec.x;
        var f2 = upVec.y;
        var f5 = upVec.z;
        var f = partialTicks;
        var cameraPos = camera.getPosition();
        var interpPosX = cameraPos.x;
        var interpPosY = cameraPos.y;
        var interpPosZ = cameraPos.z;
        if (removeIfTooFar()){return;}

//        tessellator.draw();
//        GL11.glPushMatrix();
//        GL11.glDepthMask(false);
//        GL11.glEnable(GL11.GL_BLEND);
        this.rCol = this.gCol = this.bCol = 1.0F;
        float ma = 1.0F;
        switch (this.type) {
            case 0:
                this.rCol = 0.6F;
                this.gCol = 0.3F;
                this.bCol = 0.6F;
                GL11.glBlendFunc(770, 1);
                break;
            case 1:
                this.rCol = 0.6F;
                this.gCol = 0.6F;
                this.bCol = 0.1F;
                GL11.glBlendFunc(770, 1);
                break;
            case 2:
                this.rCol = 0.1F;
                this.gCol = 0.1F;
                this.bCol = 0.6F;
                GL11.glBlendFunc(770, 1);
                break;
            case 3:
                this.rCol = 0.1F;
                this.gCol = 1.0F;
                this.bCol = 0.1F;
                GL11.glBlendFunc(770, 1);
                break;
            case 4:
                this.rCol = 0.6F;
                this.gCol = 0.1F;
                this.bCol = 0.1F;
                GL11.glBlendFunc(770, 1);
                break;
            case 5:
                this.rCol = 0.6F;
                this.gCol = 0.2F;
                this.bCol = 0.6F;
                GL11.glBlendFunc(770, 771);
                break;
            case 6:
                this.rCol = 0.75F;
                this.gCol = 1.0F;
                this.bCol = 1.0F;
                ma = 0.2F;
                GL11.glBlendFunc(770, 771);
        }

        UtilsFX.bindTexture("textures/misc/p_large.png");
//        tessellator.startDrawingQuads();
//        tessellator.setBrightness(15728880);
        this.renderBolt(consumer, f, f1, f2, f3, f5, 0, ma,camera);
//        tessellator.draw();
        switch (this.type) {
            case 0:
                this.rCol = 1.0F;
                this.gCol = 0.6F;
                this.bCol = 1.0F;
                break;
            case 1:
                this.rCol = 1.0F;
                this.gCol = 1.0F;
                this.bCol = 0.1F;
                break;
            case 2:
                this.rCol = 0.1F;
                this.gCol = 0.1F;
                this.bCol = 1.0F;
                break;
            case 3:
                this.rCol = 0.1F;
                this.gCol = 0.6F;
                this.bCol = 0.1F;
                break;
            case 4:
                this.rCol = 1.0F;
                this.gCol = 0.1F;
                this.bCol = 0.1F;
                break;
            case 5:
                this.rCol = 0.0F;
                this.gCol = 0.0F;
                this.bCol = 0.0F;
                GL11.glBlendFunc(770, 771);
                break;
            case 6:
                this.rCol = 0.75F;
                this.gCol = 1.0F;
                this.bCol = 1.0F;
                ma = 0.2F;
                GL11.glBlendFunc(770, 771);
        }

        UtilsFX.bindTexture("textures/misc/p_small.png");
//        tessellator.startDrawingQuads();
//        tessellator.setBrightness(15728880);
        this.renderBolt(consumer, f, f1, f2, f3, f5, 1, ma,camera);
//        tessellator.draw();
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glDepthMask(true);
//        GL11.glPopMatrix();
        UtilsFX.bindTexture("thaumcraft", "textures/particle/particles.png");
//        tessellator.startDrawingQuads();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}
