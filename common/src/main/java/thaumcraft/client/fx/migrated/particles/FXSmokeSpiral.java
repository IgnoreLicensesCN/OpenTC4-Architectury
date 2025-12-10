package thaumcraft.client.fx.particles.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import static thaumcraft.client.fx.particles.migrated.particles.FXGeneric.PARTICLE_SPRITE;

public class FXSmokeSpiral extends ThaumcraftParticle {
    private final float radius;
    private final int start;
    private final int miny;

    public FXSmokeSpiral(ClientLevel world, double d, double d1, double d2, float radius, int start, int miny) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.gravity = -0.01F;
        this.xd = this.yd = this.zd = 0.0F;
        this.quadSize *= 1.0F;
        this.lifetime = 20 + world.getRandom()
                .nextInt(10);
        this.hasPhysics = true;
        this.setSize(0.01F, 0.01F);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.radius = radius;
        this.start = start;
        this.miny = miny;
        this.setSprite(PARTICLE_SPRITE);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F * this.alpha);
        int particle = (int)(1.0F + (float) this.age / (float) this.lifetime * 4.0F);
        float r1 = (float) this.start + 720.0F * (((float) this.age + partialTicks) / (float) this.lifetime);
        float r2 = 90.0F - 180.0F * (((float) this.age + partialTicks) / (float) this.lifetime);
        float mX = -MathHelper.sin(r1 / 180.0F * (float) Math.PI) * MathHelper.cos(r2 / 180.0F * (float) Math.PI);
        float mZ = MathHelper.cos(r1 / 180.0F * (float) Math.PI) * MathHelper.cos(r2 / 180.0F * (float) Math.PI);
        float mY = -MathHelper.sin(r2 / 180.0F * (float) Math.PI);
        mX *= this.radius;
        mY *= this.radius;
        mZ *= this.radius;
        float u0 = (float)(particle % 16) / 16.0F;
        float u1 = u0 + 0.0624375F;
        float v0 = (float)(particle / 16) / 16.0F;
        float v1 = v0 + 0.0624375F;
        float var12 = 0.15F * this.quadSize;
        float var13 = (float)(this.x + (double) mX - partialTicks);
        float var14 = (float)(Math.max(this.y + (double) mY, (float) this.miny + 0.1F) - partialTicks);
        float var15 = (float)(this.z + (double) mZ - partialTicks);
        float var16 = 1.0F;

        var rightVec = camera.getLeftVector().negate();
        var f1 = rightVec.x;
        var f3 = rightVec.z;
        var upVec = camera.getUpVector();
        var f4 = upVec.x;
        var f2 = upVec.y;
        var f5 = upVec.z;


        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(rCol, gCol, bCol, 0.66F * this.alpha)
                .uv(u0, v1)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, 0.66F * this.alpha)
                .uv(u1, v1)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, 0.66F * this.alpha)
                .uv(u1, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(rCol, gCol, bCol, 0.66F * this.alpha)
                .uv(u0, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }

    public int getFXLayer() {
        return 1;
    }

    @Override
    public void tick() {
        this.setAlphaF((float)(this.lifetime - this.age) / (float) this.lifetime);
        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

    }
}
