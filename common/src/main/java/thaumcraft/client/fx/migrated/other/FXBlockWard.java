package thaumcraft.client.fx.migrated.other;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXBlockWard extends ThaumcraftParticle {
    Direction side;
    int rotation = 0;
    float sx = 0.0F;
    float sy = 0.0F;
    float sz = 0.0F;

    public FXBlockWard(ClientLevel world, double d, double d1, double d2, Direction side, float f, float f1, float f2) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.side = side;
        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0F;
        this.lifetime = 12 + this.random.nextInt(5);
        this.setSize(0.01F, 0.01F);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.hasPhysics = false;
        this.quadSize = (float)(1.4 + this.random.nextGaussian() * (double)0.3F);
        this.rotation = this.random.nextInt(360);
        this.sx = MathHelper.clamp_float(f - 0.6F + this.random.nextFloat() * 0.2F, -0.4F, 0.4F);
        this.sy = MathHelper.clamp_float(f1 - 0.6F + this.random.nextFloat() * 0.2F, -0.4F, 0.4F);
        this.sz = MathHelper.clamp_float(f2 - 0.6F + this.random.nextFloat() * 0.2F, -0.4F, 0.4F);
        if (side.getStepX() != 0) {
            this.sx = 0.0F;
        }

        if (side.getStepY() != 0) {
            this.sy = 0.0F;
        }

        if (side.getStepZ() != 0) {
            this.sz = 0.0F;
        }

    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vc, Camera camera, float partialTicks) {
        int frame = Math.min(15, (int)(15f * ((age + partialTicks) / lifetime)));
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("thaumcraft", "textures/models/hemis" + frame + ".png"));

        PoseStack pose = new PoseStack();

        Vec3 cam = camera.getPosition();
        float px = (float)(Mth.lerp(partialTicks, xo, x) - cam.x + sx);
        float py = (float)(Mth.lerp(partialTicks, yo, y) - cam.y + sy);
        float pz = (float)(Mth.lerp(partialTicks, zo, z) - cam.z + sz);

        pose.pushPose();
        pose.translate(px, py, pz);

        // 面向 block face
        pose.mulPose(Axis.of(
                new Vector3f(side.getStepY(), -side.getStepX(), side.getStepZ())
        ).rotationDegrees(90));

        pose.mulPose(Axis.ZP.rotationDegrees(rotation));

        if (side.getStepZ() > 0) {
            pose.translate(0, 0, 0.505);
            pose.mulPose(Axis.YN.rotationDegrees(180));
        } else {
            pose.translate(0, 0, -0.505);
        }

        float size = quadSize / 2f;
        float a = alpha / 2f;

        Matrix4f mat = pose.last().pose();

        vc.vertex(mat, -size,  size, 0).uv(0, 1)
                .color(rCol, gCol, bCol, a)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        vc.vertex(mat,  size,  size, 0).uv(1, 1)
                .color(rCol, gCol, bCol, a)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        vc.vertex(mat,  size, -size, 0).uv(1, 0)
                .color(rCol, gCol, bCol, a)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        vc.vertex(mat, -size, -size, 0).uv(0, 0)
                .color(rCol, gCol, bCol, a)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        pose.popPose();
    }
//    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
//        tessellator.draw();
//        GL11.glPushMatrix();
//        float fade = ((float) this.age + f) / (float) this.lifetime;
//        int frame = Math.min(15, (int)(15.0F * fade));
//        UtilsFX.bindTexture("textures/models/hemis" + frame + ".png");
//        GL11.glDepthMask(false);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(770, 1);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha / 2.0F);
//        float var13 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
//        float var14 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
//        float var15 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);
//        GL11.glTranslated(var13 + this.sx, var14 + this.sy, var15 + this.sz);
//        GL11.glRotatef(90.0F, (float) this.side.offsetY, (float)(-this.side.offsetX), (float) this.side.offsetZ);
//        GL11.glRotatef((float) this.rotation, 0.0F, 0.0F, 1.0F);
//        if (this.side.offsetZ > 0) {
//            GL11.glTranslated(0.0F, 0.0F, 0.505F);
//            GL11.glRotatef(180.0F, 0.0F, -1.0F, 0.0F);
//        } else {
//            GL11.glTranslated(0.0F, 0.0F, -0.505F);
//        }
//
//        float var12 = this.quadSize;
//        float var16 = 1.0F;
//        tessellator.startDrawingQuads();
//        tessellator.setBrightness(240);
//        tessellator.setColorRGBA_F(this.rCol * var16, this.gCol * var16, this.bCol * var16, this.alpha / 2.0F);
//        tessellator.addVertexWithUV((double)-0.5F * (double)var12, (double)0.5F * (double)var12, 0.0F, 0.0F, 1.0F);
//        tessellator.addVertexWithUV((double)0.5F * (double)var12, (double)0.5F * (double)var12, 0.0F, 1.0F, 1.0F);
//        tessellator.addVertexWithUV((double)0.5F * (double)var12, (double)-0.5F * (double)var12, 0.0F, 1.0F, 0.0F);
//        tessellator.addVertexWithUV((double)-0.5F * (double)var12, (double)-0.5F * (double)var12, 0.0F, 0.0F, 0.0F);
//        tessellator.draw();
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glDepthMask(true);
//        GL11.glPopMatrix();
//        Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
//        tessellator.startDrawingQuads();
//    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float threshold = (float) this.lifetime / 5.0F;
        if ((float) this.age <= threshold) {
            this.alpha = (float) this.age / threshold;
        } else {
            this.alpha = (float)(this.lifetime - this.age) / (float) this.lifetime;
        }

        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

        this.yd -= 0.04 * (double) this.gravity;
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
    }
}
