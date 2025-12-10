package thaumcraft.client.fx.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXWisp extends ThaumcraftParticle {

    Entity target;
    public boolean shrink;
    float moteParticleScale;
    int moteHalfLife;
    public boolean tinkle;
    public int blendmode;

    public FXWisp(ClientLevel world, double d, double d1, double d2, float f, float f1, float f2) {
        this(world, d, d1, d2, 1.0F, f, f1, f2);
    }

    public FXWisp(ClientLevel world, double d, double d1, double d2, float f, float red, float green, float blue) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.target = null;
        this.shrink = false;
        this.tinkle = false;
        this.blendmode = 1;
        if (red == 0.0F) {
            red = 1.0F;
        }

        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0F;
        this.quadSize *= f;
        this.moteParticleScale = this.quadSize;
        this.lifetime = (int)((double)36.0F / (Math.random() * 0.3 + 0.7));
        this.moteHalfLife = this.lifetime / 2;
        this.hasPhysics = true;
        this.setSize(0.1F, 0.1F);
        removeIfTooFar();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public FXWisp(ClientLevel world, double d, double d1, double d2, float f, int type) {
        this(world, d, d1, d2, f, 0.0F, 0.0F, 0.0F);
        switch (type) {
            case 0:
                this.rCol = 0.75F + world.getRandom().nextFloat() * 0.25F;
                this.gCol = 0.25F + world.getRandom().nextFloat() * 0.25F;
                this.bCol = 0.75F + world.getRandom().nextFloat() * 0.25F;
                break;
            case 1:
                this.rCol = 0.5F + world.getRandom().nextFloat() * 0.3F;
                this.gCol = 0.5F + world.getRandom().nextFloat() * 0.3F;
                this.bCol = 0.2F;
                break;
            case 2:
                this.rCol = 0.2F;
                this.gCol = 0.2F;
                this.bCol = 0.7F + world.getRandom().nextFloat() * 0.3F;
                break;
            case 3:
                this.rCol = 0.2F;
                this.gCol = 0.7F + world.getRandom().nextFloat() * 0.3F;
                this.bCol = 0.2F;
                break;
            case 4:
                this.rCol = 0.7F + world.getRandom().nextFloat() * 0.3F;
                this.gCol = 0.2F;
                this.bCol = 0.2F;
                break;
            case 5:
                this.blendmode = 771;
                this.rCol = world.getRandom().nextFloat() * 0.1F;
                this.gCol = world.getRandom().nextFloat() * 0.1F;
                this.bCol = world.getRandom().nextFloat() * 0.1F;
                break;
            case 6:
                this.rCol = 0.8F + world.getRandom().nextFloat() * 0.2F;
                this.gCol = 0.8F + world.getRandom().nextFloat() * 0.2F;
                this.bCol = 0.8F + world.getRandom().nextFloat() * 0.2F;
                break;
            case 7:
                this.rCol = 0.7F + world.getRandom().nextFloat() * 0.3F;
                this.gCol = 0.5F + world.getRandom().nextFloat() * 0.2F;
                this.bCol = 0.3F + world.getRandom().nextFloat() * 0.1F;
        }

    }

    public FXWisp(ClientLevel world, double d, double d1, double d2, double x, double y, double z, float f, int type) {
        this(world, d, d1, d2, f, type);
        if (this.lifetime > 0) {
            double dx = x - this.x;
            double dy = y - this.y;
            double dz = z - this.z;
            this.xd = dx / (double) this.lifetime;
            this.yd = dy / (double) this.lifetime;
            this.zd = dz / (double) this.lifetime;
        }

    }

    public FXWisp(ClientLevel world, double d, double d1, double d2, Entity tar, int type) {
        this(world, d, d1, d2, 0.4F, type);
        this.target = tar;
    }

    public FXWisp(ClientLevel world, double d, double d1, double d2, double x, double y, double z, float f, float red, float green, float blue) {
        this(world, d, d1, d2, f, red, green, blue);
        if (this.lifetime > 0) {
            double dx = x - this.x;
            double dy = y - this.y;
            double dz = z - this.z;
            this.xd = dx / (double) this.lifetime;
            this.yd = dy / (double) this.lifetime;
            this.zd = dz / (double) this.lifetime;
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
        float agescale = 0.0F;
        if (this.shrink) {
            agescale = ((float) this.lifetime - (float) this.age) / (float) this.lifetime;
        } else {
            agescale = (float) this.age / (float) this.moteHalfLife;
            if (agescale > 1.0F) {
                agescale = 2.0F - agescale;
            }
        }

        this.quadSize = this.moteParticleScale * agescale;
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
        float f10 = 0.5F * this.quadSize;
        float f11 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float f12 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float f13 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);
        float var8 = 0.0F;
        float var9 = 0.125F;
        float var10 = 0.875F;
        float var11 = 1.0F;

        consumer.vertex(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10
                )
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10
                )
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10
                )
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10)
                .color(this.rCol, this.gCol, this.bCol, 0.5F)
                .uv(var8, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }

    public int getFXLayer() {
        return this.blendmode == 1 ? 0 : 1;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age == 0 && this.tinkle && this.level.random.nextInt(3) == 0) {
            this.level.playLocalSound(x,y,z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS,
                    0.02F, 0.5F * ((this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.6F + 2.0F),false);
        }

        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

        this.yd -= 0.04 * (double) this.gravity;
        if (this.hasPhysics) {
            this.pushOutOfBlocksUnified(pos -> {

                var state = level.getBlockState(pos);
                boolean airFlag = state.isAir();
                boolean fluidFlag = state.getFluidState().isEmpty();
                boolean defaultNormalCubeFlag = state.getBlock().defaultBlockState().isCollisionShapeFullBlock(level,pos);
                return !airFlag && !fluidFlag && defaultNormalCubeFlag;
            });
        }

        this.moveEntity(this.xd, this.yd, this.zd);
        if (this.target != null) {
            this.xd *= 0.985;
            this.yd *= 0.985;
            this.zd *= 0.985;
            double dx = this.target.getX() - this.x;
            var boundingBox = this.target.getBoundingBox();
            var entityHeight = boundingBox.maxY - boundingBox.minY;
            double dy = this.target.getY() + (entityHeight / 2.0F) - this.y;
            double dz = this.target.getZ() - this.z;
            double d13 = 0.2;
            double d11 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
            dx /= d11;
            dy /= d11;
            dz /= d11;
            this.xd += dx * d13;
            this.yd += dy * d13;
            this.zd += dz * d13;
            this.xd = MathHelper.clamp_float((float) this.xd, -0.2F, 0.2F);
            this.yd = MathHelper.clamp_float((float) this.yd, -0.2F, 0.2F);
            this.zd = MathHelper.clamp_float((float) this.zd, -0.2F, 0.2F);
        } else {
            this.xd *= 0.98F;
            this.yd *= 0.98F;
            this.zd *= 0.98F;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }

    }
}
