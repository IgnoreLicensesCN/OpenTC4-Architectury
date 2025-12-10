package thaumcraft.client.fx.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

import static thaumcraft.client.fx.migrated.Particles.PARTICLE_SPRITE;

public class FXSmokeTrail extends ThaumcraftParticle {

    private Entity target;
    public int particle = 24;

    public FXSmokeTrail(ClientLevel par1World, double x, double y, double z, Entity target, float r, float g, float b) {
        super(par1World, x, y, z, 0.0F, 0.0F, 0.0F);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.quadSize = this.random.nextFloat() * 0.5F + 0.5F;
        this.target = target;
        double dx = target.getX() - this.x;
        double dy = target.getY() - this.y;
        double dz = target.getZ() - this.z;
        int base = (int)(MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz) * 3.0F);
        if (base < 1) {
            base = 1;
        }

        this.lifetime = base / 2 + this.random.nextInt(base);
        float f3 = 0.1F;
        this.xd = (this.random.nextFloat() - this.random.nextFloat()) * f3;
        this.yd = (this.random.nextFloat() - this.random.nextFloat()) * f3;
        this.zd = (this.random.nextFloat() - this.random.nextFloat()) * f3;
        this.gravity = 0.2F;
        this.hasPhysics = true;
        removeIfTooFar();
        this.setSprite(PARTICLE_SPRITE);
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

        float bob = MathHelper.sin((float) this.age / 3.0F) * 0.33F + 0.66F;
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.33F);
        int part = (int)(1.0F + (float) this.age / (float) this.lifetime * 4.0F);
        float var8 = (float)(part % 16) / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = (float)(part / 16) / 16.0F;
        float var11 = var10 + 0.0624375F;
        float var12 = 0.3F * this.quadSize * bob;
        float var13 = (float)(this.xo + (this.x - this.xo) * (double) f - interpPosX);
        float var14 = (float)(this.yo + (this.y - this.yo) * (double) f - interpPosY);
        float var15 = (float)(this.zo + (this.z - this.zo) * (double) f - interpPosZ);

        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(rCol, gCol, bCol, 0.33F)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, 0.33F)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, 0.33F)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(rCol, gCol, bCol, 0.33F)
                .uv(var8, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }

    public int getFXLayer() {
        return 1;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;


        if (this.age++ < this.lifetime && !(this.getDistanceSqToEntity(this.target) < (double)1.0F)) {
            if (this.hasPhysics) {
                this.pushOutOfBlocksUnified(pos -> {
                    var state = level.getBlockState(pos);
                    boolean airFlag = state.isAir();
                    boolean fluidFlag = state.getFluidState().isEmpty();
                    return !airFlag && !fluidFlag;
                });
            }

            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.985;
            this.yd *= 0.985;
            this.zd *= 0.985;
            double dx = this.target.getX() - this.x;
            double dy = this.target.getY() - this.y;
            double dz = this.target.getZ() - this.z;
            double d13 = 0.3;
            double d11 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
            if (d11 < (double)4.0F) {
                this.quadSize *= 0.9F;
                d13 = 0.6;
            }

            dx /= d11;
            dy /= d11;
            dz /= d11;
            this.xd += dx * d13;
            this.yd += dy * d13;
            this.zd += dz * d13;
            this.xd = MathHelper.clamp_float((float) this.xd, -0.35F, 0.35F);
            this.yd = MathHelper.clamp_float((float) this.yd, -0.35F, 0.35F);
            this.zd = MathHelper.clamp_float((float) this.zd, -0.35F, 0.35F);
        } else {
            this.setDead();
        }
    }

}
