package thaumcraft.client.fx.migrated.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class FXSonic extends ThaumcraftParticle {
    Entity target = null;
    float yaw = 0.0F;
    float pitch = 0.0F;
    public static final ResourceLocation MODEL = ResourceLocation.tryParse("thaumcraft:models/special/hemis.obj");
    public static BakedModel model = platformUtils.getModel(MODEL);

    public FXSonic(ClientLevel world, double d, double d1, double d2, Entity target, int age) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0F;
        this.lifetime = age + this.random.nextInt(age / 2);
        this.setSize(0.01F, 0.01F);
        this.hasPhysics = false;
        this.quadSize = 1.0F;
        this.target = target;
        this.yaw = target.getYRot();
        this.pitch = target.getXRot();
        this.xo = this.x = target.getX();
        this.yo = this.y = target.getY() + (double) target.getEyeHeight();
        this.zo = this.z = target.getZ();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // 1. 计算实体中心插值位置
        double xx = xo + (x - xo) * partialTicks - camera.getPosition().x();
        double yy = yo + (y - yo) * partialTicks - camera.getPosition().y();
        double zz = zo + (z - zo) * partialTicks - camera.getPosition().z();

        // 2. pushPose
        PoseStack ps = new PoseStack();
        ps.pushPose();
        ps.translate(xx, yy, zz);

        // 3. 旋转 yaw/pitch
        float yawInterpolated = target.yRotO + (target.getYRot() - target.yRotO) * partialTicks;
        float pitchInterpolated = target.xRotO + (target.getXRot() - target.xRotO) * partialTicks;
        ps.mulPose(Axis.YP.rotationDegrees(-yawInterpolated));
        ps.mulPose(Axis.XP.rotationDegrees(pitchInterpolated));

        // 4. 缩放
        double scale = 0.25 * target.getBbHeight();
        ps.scale((float) scale, (float) scale, (float) scale);

        // 6. 渲染模型
        renderBakedModel(ps,buffer,1,model);

        ps.popPose();
    }
//    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
//        tessellator.draw();
//        GL11.glPushMatrix();
//        GL11.glDisable(2884);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(770, 1);
//        if (model == null){
//            model = AdvancedModelLoader.loadModel(MODEL);
//        }
//
//        float fade = ((float) this.age + f) / (float) this.lifetime;
//        float xx = (float)(this.xo + (this.x - this.xo) * (double) f - interpPosX);
//        float yy = (float)(this.yo + (this.y - this.yo) * (double) f - interpPosY);
//        float zz = (float)(this.zo + (this.z - this.zo) * (double) f - interpPosZ);
//        GL11.glTranslated(xx, yy, zz);
//        float b = 1.0F;
//        int frame = Math.min(15, (int)(14.0F * fade) + 1);
//        UtilsFX.bindTexture("textures/models/ripple" + frame + ".png");
//        b = 0.5F;
//        int i = 220;
//        int j = i % 65536;
//        int k = i / 65536;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
//        GL11.glRotatef(-this.yaw, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(this.pitch, 1.0F, 0.0F, 0.0F);
//        GL11.glTranslated(0.0F, 0.0F, 2.0F * this.target.height + this.target.width / 2.0F);
//        GL11.glScaled((double)0.25F * (double) this.target.height, (double)0.25F * (double) this.target.height,
//                -1.0F * this.target.height
//        );
//        GL11.glColor4f(b, b, b, 1.0F);
//        model.renderAll();
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glEnable(2884);
//        GL11.glPopMatrix();
//        Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
//        tessellator.startDrawingQuads();
//    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

        this.x = this.target.getX();
        this.y = this.target.getY() + (double) this.target.getEyeHeight();
        this.z = this.target.getZ();
    }
}
