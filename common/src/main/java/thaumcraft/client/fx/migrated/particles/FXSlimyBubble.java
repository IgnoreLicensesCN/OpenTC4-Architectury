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

public class FXSlimyBubble extends ThaumcraftParticle {
    int particle = 144;

    public FXSlimyBubble(ClientLevel world, double d, double d1, double d2, float f) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0F;
        this.quadSize = f;
        this.lifetime = 15 + world.getRandom()
                .nextInt(5);
        this.hasPhysics = true;
        this.setSize(0.01F, 0.01F);
        this.setSprite(PARTICLE_SPRITE);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
//        float var8 = (float)(this.particle % 16) / 16.0F;
//        float var9 = var8 + 0.0625F;
//        float var10 = (float)(this.particle / 16) / 16.0F;
//        float var11 = var10 + 0.0625F;
        float var12 = this.quadSize;

        var rightVec = camera.getLeftVector().negate();
        var f1 = rightVec.x;
        var f3 = rightVec.z;
        var upVec = camera.getUpVector();
        var f4 = upVec.x;
        var f2 = upVec.y;
        var f5 = upVec.z;
        var cameraPos = camera.getPosition();
        float var13 = (float) (this.xo + (this.x - this.xo) * (double) partialTicks - cameraPos.x);
        float var14 = (float) (this.yo + (this.y - this.yo) * (double) partialTicks - cameraPos.y);
        float var15 = (float) (this.zo + (this.z - this.zo) * (double) partialTicks - cameraPos.z);

        var index = this.particle;
        int spriteSize = 8; // 单个子粒子大小
        int atlasSize = 128; // 整张大图大小
        int cols = atlasSize / spriteSize;


        int row = index / cols;
        int col = index % cols;

        float u0 = (float) col * spriteSize / atlasSize;
        float u1 = (float) (col + 1) * spriteSize / atlasSize;
        float v0 = (float) row * spriteSize / atlasSize;
        float v1 = (float) (row + 1) * spriteSize / atlasSize;

//        tessellator.setBrightness(this.getBrightnessForRender(f));


        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(u0, v1)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(u1, v1)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(u1, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(rCol, gCol, bCol, alpha)
                .uv(u0, v0)
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
        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

        if (this.age - 1 < 6) {
            this.particle = 144 + this.age / 2;
            if (this.age == 5) {
                this.y += 0.1;
            }
        } else if (this.age < this.lifetime - 4) {
            this.yd += 0.005;
            this.particle = 147 + this.age % 4 / 2;
        } else {
            this.yd /= 2.0F;
            this.particle = 150 - (this.lifetime - this.age) / 2;
        }

        this.y += this.yd;
    }

}
