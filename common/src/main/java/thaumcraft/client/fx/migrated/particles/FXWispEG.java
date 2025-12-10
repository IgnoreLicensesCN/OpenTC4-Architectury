package thaumcraft.client.fx.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXWispEG extends ThaumcraftParticle {
    Entity target = null;
    double rx = 0.0F;
    double ry = 0.0F;
    double rz = 0.0F;
    public int blendmode = 1;

    public FXWispEG(ClientLevel worldObj, double posX, double posY, double posZ, Entity target2) {
        super(worldObj, posX, posY, posZ, 0.0F, 0.0F, 0.0F);
        this.target = target2;
        this.xd = this.random.nextGaussian() * 0.03;
        this.yd = -0.05;
        this.zd = this.random.nextGaussian() * 0.03;
        this.quadSize *= 0.4F;
        this.lifetime = (int)((double)40.0F / (Math.random() * 0.3 + 0.7));
        this.hasPhysics = true;
        this.setSize(0.01F, 0.01F);
        removeIfTooFar();

        this.xo = posX;
        this.yo = posY;
        this.zo = posZ;
        this.blendmode = 771;
        this.rCol = this.random.nextFloat() * 0.05F;
        this.gCol = this.random.nextFloat() * 0.05F;
        this.bCol = this.random.nextFloat() * 0.05F;
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

        Entity e = Minecraft.getInstance().getCameraEntity();
        if (e == null){return;}
        float agescale = 1.0F - (float)this.age / (float)this.lifetime;
        float d6 = 1024.0F;

        var distSq = (x-e.getX())*(x-e.getX()) + (y-e.getY())*(y-e.getY()) + (z-e.getZ())*(z-e.getZ());

        float base = (float)((double)1.0F - Math.min(d6, distSq) / (double)d6);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F * base);
        float f10 = 0.5F * this.quadSize;
        float f11 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float f12 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float f13 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);

        float var8 = (float)(this.age % 13) / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = 0.1875F;
        float var11 = var10 + 0.0624375F;

        var alpha = 0.2F * agescale * base;


        consumer.vertex(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10
                )
                .color(this.rCol, this.gCol, this.bCol, alpha)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10
                )
                .color(this.rCol, this.gCol, this.bCol, alpha)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10
                )
                .color(this.rCol, this.gCol, this.bCol, alpha)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10)
                .color(this.rCol, this.gCol, this.bCol, alpha)
                .uv(var8, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }

//    public int getFXLayer() {
//        return this.blendmode == 1 ? 0 : 1;
//    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.target != null && !this.onGround) {
            this.x += this.target.getDeltaMovement().x;
            this.z += this.target.getDeltaMovement().z;
        }

        if (hasPhysics){
            this.pushOutOfBlocksUnified(pos -> {
                var state = level.getBlockState(pos);
                boolean airFlag = state.isAir();
                boolean fluidFlag = state.getFluidState()
                        .isEmpty();
                boolean defaultNormalCubeFlag = state.getBlock()
                        .defaultBlockState()
                        .isCollisionShapeFullBlock(level, pos);
                return !airFlag && !fluidFlag && defaultNormalCubeFlag;
            });
        }
        this.moveEntity(this.xd, this.yd, this.zd);
        this.xd *= 0.98F;
        this.yd *= 0.98F;
        this.zd *= 0.98F;
        if (this.onGround) {
            this.xd *= 0.8500000190734863;
            this.zd *= 0.8500000190734863;
        }

        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

    }
}
