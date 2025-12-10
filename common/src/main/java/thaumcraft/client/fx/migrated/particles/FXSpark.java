package thaumcraft.client.fx.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXSpark extends ThaumcraftParticle {

    int particle = 0;
    boolean flip = false;

    public FXSpark(ClientLevel world, double d, double d1, double d2, float f) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0F;
        this.quadSize = f;
        this.lifetime = 5 + world.getRandom().nextInt(5);
        this.hasPhysics = true;
        this.setSize(0.01F, 0.01F);
        this.particle = world.getRandom().nextInt(3) * 8;
        this.flip = world.getRandom().nextBoolean();
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

//        GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
        int part = this.particle + (int)((float) this.age / (float) this.lifetime * 7.0F);
        float var8 = (float)(part % 8) / 8.0F;
        float var9 = var8 + 0.125F;
        float var10 = (float)(part / 8) / 8.0F;
        float var11 = var10 + 0.125F;
        float var12 = this.quadSize;
        if (this.flip) {
            float t = var8;
            var8 = var9;
            var9 = t;
        }

        float var13 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float var14 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float var15 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);


        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(rCol, gCol, bCol, alpha)
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
        }

    }
}
