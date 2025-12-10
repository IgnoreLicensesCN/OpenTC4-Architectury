package thaumcraft.client.fx.particles.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.Level;
import org.lwjgl.opengl.GL11;

public class FXVisSparkle extends ThaumcraftParticle {
    private double targetX;
    private double targetY;
    private double targetZ;
    float sizeMod = 0.0F;

    public FXVisSparkle(ClientLevel par1World, double par2, double par4, double par6, double tx, double ty, double tz) {
        super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
        this.rCol = this.gCol = this.bCol = 0.6F;
        this.quadSize = 0.0F;
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        this.lifetime = 1000;
        float f3 = 0.01F;
        this.xd = (float) this.random.nextGaussian() * f3;
        this.yd = (float) this.random.nextGaussian() * f3;
        this.zd = (float) this.random.nextGaussian() * f3;
        this.sizeMod = (float)(45 + this.random.nextInt(15));
        this.rCol = 0.2F;
        this.gCol = 0.6F + this.random.nextFloat() * 0.3F;
        this.bCol = 0.2F;
        this.gravity = 0.2F;
        this.hasPhysics = false;
        removeIfTooFar();
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

        float bob = MathHelper.sin((float)this.age / 3.0F) * 0.3F + 6.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
        int part = this.age % 16;
        float var8 = (float)part / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = 0.5F;
        float var11 = var10 + 0.0624375F;
        float var12 = 0.1F * this.quadSize * bob;
        float var13 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float var14 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float var15 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);
        float var16 = 1.0F;
        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var8, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.setDead();
        } else {
            this.moveEntity(this.xd, this.yd, this.zd);
            this.xd *= 0.985;
            this.yd *= 0.985;
            this.zd *= 0.985;
            double dx = this.targetX - this.x;
            double dy = this.targetY - this.y;
            double dz = this.targetZ - this.z;
            double d13 = 0.1F;
            double d11 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
            if (d11 < (double)2.0F) {
                this.quadSize *= 0.95F;
            }

            if (d11 < 0.2) {
                this.lifetime = this.age;
            }

            if (this.age < 10) {
                this.quadSize = (float) this.age / this.sizeMod;
            }

            dx /= d11;
            dy /= d11;
            dz /= d11;
            this.xd += dx * d13;
            this.yd += dy * d13;
            this.zd += dz * d13;
            this.xd = MathHelper.clamp_float((float) this.xd, -0.1F, 0.1F);
            this.yd = MathHelper.clamp_float((float) this.yd, -0.1F, 0.1F);
            this.zd = MathHelper.clamp_float((float) this.zd, -0.1F, 0.1F);
        }
    }
}
