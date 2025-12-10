package thaumcraft.client.fx.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

import java.awt.*;

public class FXEssentiaTrail extends ThaumcraftParticle {
    private final double targetX;
    private final double targetY;
    private final double targetZ;
    private final int count;
    public int particle = 24;

    public FXEssentiaTrail(
            ClientLevel par1World, double par2, double par4, double par6,
            double tx, double ty, double tz, int count, int color, float scale
    ) {
        super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
        this.rCol = this.gCol = this.bCol = 0.6F;
        this.quadSize = (MathHelper.sin((float) count / 2.0F) * 0.1F + 1.0F) * scale;
        this.count = count;
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;
        double dx = tx - this.x;
        double dy = ty - this.y;
        double dz = tz - this.z;
        int base = (int) (MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz) * 30.0F);
        if (base < 1) {
            base = 1;
        }

        this.lifetime = base / 2 + this.random.nextInt(base);
        this.xd = (double) (MathHelper.sin(
                (float) count / 4.0F) * 0.015F) + this.random.nextGaussian() * (double) 0.002F;
        this.yd = 0.1F + MathHelper.sin((float) count / 3.0F) * 0.01F;
        this.zd = (double) (MathHelper.sin(
                (float) count / 2.0F) * 0.015F) + this.random.nextGaussian() * (double) 0.002F;
        Color c = new Color(color);
        float mr = (float) c.getRed() / 255.0F * 0.2F;
        float mg = (float) c.getGreen() / 255.0F * 0.2F;
        float mb = (float) c.getBlue() / 255.0F * 0.2F;
        this.rCol = (float) c.getRed() / 255.0F - mr + this.random.nextFloat() * mr;
        this.gCol = (float) c.getGreen() / 255.0F - mg + this.random.nextFloat() * mg;
        this.bCol = (float) c.getBlue() / 255.0F - mb + this.random.nextFloat() * mb;
        this.gravity = 0.2F;
        this.hasPhysics = true;
        removeIfTooFar();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd += 0.01 * (double) this.gravity;
            if (this.hasPhysics) {
                this.pushOutOfBlocksUnified(pos -> {
                    var state = level.getBlockState(pos);
                    if (state.isAir()) {
                        return false;
                    }
                    if (!state.getBlock().defaultBlockState().isCollisionShapeFullBlock(level, pos)) {
                        return false;
                    }
                    return state.getFluidState().isEmpty();
                });
            }

            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.985;
            this.yd *= 0.985;
            this.zd *= 0.985;
            this.xd = MathHelper.clamp_float((float) this.xd, -0.05F, 0.05F);
            this.yd = MathHelper.clamp_float((float) this.yd, -0.05F, 0.05F);
            this.zd = MathHelper.clamp_float((float) this.zd, -0.05F, 0.05F);
            double dx = this.targetX - this.x;
            double dy = this.targetY - this.y;
            double dz = this.targetZ - this.z;
            double d13 = 0.01;
            double d11 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
            if (d11 < (double) 2.0F) {
                this.quadSize *= 0.98F;
            }

            if (this.quadSize < 0.2F) {
                this.remove();
            } else {
                dx /= d11;
                dy /= d11;
                dz /= d11;
                this.xd += dx * (d13 / Math.min(1.0F, d11));
                this.yd += dy * (d13 / Math.min(1.0F, d11));
                this.zd += dz * (d13 / Math.min(1.0F, d11));
            }
        }
    }

    //   //Right = ( f1,  0, f3 )
//   //Up    = ( f4, f2, f5 )
    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        float t2 = 0.5625F;
        float t3 = 0.625F;
        float t4 = 0.0625F;
        float t5 = 0.125F;
        var rightVec = camera.getLeftVector().negate();
        var f1 = rightVec.x;
        var f3 = rightVec.z;
        var upVec = camera.getUpVector();
        var f4 = upVec.x;
        var f2 = upVec.y;
        var f5 = upVec.z;
        float s = MathHelper.sin((float) (this.age - this.count) / 5.0F) * 0.25F + 1.0F;
        float var12 = 0.1F * this.quadSize * s;
        var cameraPos = camera.getPosition();
        float var13 = (float) (this.xo + (this.x - this.xo) * (double) partialTicks - cameraPos.x);
        float var14 = (float) (this.yo + (this.y - this.yo) * (double) partialTicks - cameraPos.y);
        float var15 = (float) (this.zo + (this.z - this.zo) * (double) partialTicks - cameraPos.z);

        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(rCol, gCol, bCol, 0.5f)
                .uv(t2, t5)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(
                var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, 0.5f)
                .uv(t3, t5)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, 0.5f)
                .uv(t3, t4)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(rCol, gCol, bCol, 0.5f)
                .uv(t2, t4)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }
}
