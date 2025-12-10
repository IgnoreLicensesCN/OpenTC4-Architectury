package thaumcraft.client.fx.migrated.other;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.entityrender.ShieldRunesFXGetter;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class FXShieldRunes extends ThaumcraftParticle {

    Entity target = null;
    float yaw = 0.0F;
    float pitch = 0.0F;
    private final BakedModel model = platformUtils.getModel(new ResourceLocation("thaumcraft", "models/special/hemis.obj"));

    public FXShieldRunes(ClientLevel world, double d, double d1, double d2, Entity target, int age, float yaw, float pitch) {
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
        this.yaw = yaw;
        this.pitch = pitch;

        var boundingBox = target.getBoundingBox();
        this.xo = this.x = target.getX();
        this.yo = this.y = (boundingBox.minY + boundingBox.maxY) / (double) 2.0F;
        this.zo = this.z = target.getZ();
    }


    private static ResourceLocation getTexture(int frame, Entity target) {
        if (target instanceof ShieldRunesFXGetter getter) {
            return getter.getShieldRunesFXTexture(frame);
        }
        return new ResourceLocation("thaumcraft", "textures/models/hemis" + frame + ".png");
//        if (target instanceof Mob && !(target instanceof EntityCultist)) {//anazor may get something wrong
//            return new ResourceLocation(
//                    "thaumcraft",
//                    "textures/models/ripple" + frame + ".png"
//            );
//        } else {
//            return new ResourceLocation(
//                    "thaumcraft",
//                    "textures/models/hemis" + frame + ".png"
//            );
//        }
    }
    @Override
    public void render(VertexConsumer ignored, Camera camera, float partialTicks) {
        PoseStack pose = new PoseStack();

        Vec3 cam = camera.getPosition();
        pose.pushPose();

        pose.translate(
                Mth.lerp(partialTicks, xo, x) - cam.x,
                Mth.lerp(partialTicks, yo, y) - cam.y,
                Mth.lerp(partialTicks, zo, z) - cam.z
        );

        pose.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        pose.mulPose(Axis.XP.rotationDegrees(-pitch));

        float scale = 0.4f * target.getBbHeight();
        pose.scale(scale, scale, scale);

        float fade = (age + partialTicks) / lifetime;
        float alpha = Math.min(1.0F, (1.0F - fade) * 3.0F);

        int frame = Math.min(15, (int)(14.0F * fade) + 1);
        ResourceLocation tex = getTexture(frame,target);

        RenderSystem.setShaderTexture(0, tex);

        RenderType type = RenderType.entityTranslucent(tex);
        VertexConsumer vc = Minecraft.getInstance()
                .renderBuffers()
                .bufferSource()
                .getBuffer(type);

        renderBakedModel(pose, vc, alpha,model);

        pose.popPose();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
//    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
//        tessellator.draw();
//        GL11.glPushMatrix();
//        GL11.glDisable(2884);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(770, 1);
//        if (this.model == null) {
//            this.model = AdvancedModelLoader.loadModel(MODEL);
//        }
//
//        float fade = ((float) this.age + f) / (float) this.lifetime;
//        float xx = (float)(this.xo + (this.x - this.xo) * (double) f - interpPosX);
//        float yy = (float)(this.yo + (this.y - this.yo) * (double) f - interpPosY);
//        float zz = (float)(this.zo + (this.z - this.zo) * (double) f - interpPosZ);
//        GL11.glTranslated(xx, yy, zz);
//        float b = 1.0F;
//        int frame = Math.min(15, (int)(14.0F * fade) + 1);
//        if (this.target instanceof EntityMob && !(this.target instanceof EntityCultist)) {
//            UtilsFX.bindTexture("textures/models/ripple" + frame + ".png");
//            b = 0.5F;
//        } else {
//            UtilsFX.bindTexture("textures/models/hemis" + frame + ".png");
//        }
//
//        int i = 220;
//        int j = i % 65536;
//        int k = i / 65536;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
//        GL11.glRotatef(180.0F - this.yaw, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(-this.pitch, 1.0F, 0.0F, 0.0F);
//        GL11.glScaled(0.4 * (double) this.target.height, 0.4 * (double) this.target.height, 0.4 * (double) this.target.height);
//        GL11.glColor4f(b, b, b, Math.min(1.0F, (1.0F - fade) * 3.0F));
//        this.model.renderAll();
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

        var boundingBox = this.target.getBoundingBox();
        this.x = this.target.getX();
        this.y = (boundingBox.minY + boundingBox.maxY) / (double) 2.0F;
        this.z = this.target.getZ();
    }


}
