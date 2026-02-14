package thaumcraft.client.fx.migrated.particles;

import com.linearity.opentc4.Color;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

import static thaumcraft.client.fx.migrated.Particles.PARTICLE_SPRITE;

public class FXVent extends ThaumcraftParticle {
    float psm = 1.0F;
    public FXVent(ClientLevel par1World, double par2, double par4, double par6, double par8, double par10, double par12, int color) {
        super(par1World, par2, par4, par6, par8, par10, par12);
        this.setSize(0.02F, 0.02F);
        this.quadSize = this.random.nextFloat() * 0.1F + 0.05F;
        this.xd = par8;
        this.yd = par10;
        this.zd = par12;
        this.hasPhysics = false;
        Color c = new Color(color);
        this.rCol = (float)c.getRed() / 255.0F;
        this.bCol = (float)c.getBlue() / 255.0F;
        this.gCol = (float)c.getGreen() / 255.0F;
        this.setHeading(this.xd, this.yd, this.zd, 0.125F, 5.0F);

        removeIfTooFar();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.setSprite(PARTICLE_SPRITE);
    }

    public void setScale(float f) {
        this.quadSize *= f;
        this.psm *= f;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        ++this.age;
        if (this.quadSize > this.psm) {
            this.setDead();
        }

        this.yd += 0.0025;
        this.moveEntity(this.xd, this.yd, this.zd);
        this.xd *= 0.8500000190734863;
        this.yd *= 0.8500000190734863;
        this.zd *= 0.8500000190734863;
        if (this.quadSize < this.psm) {
            this.quadSize = (float)((double) this.quadSize * 1.15);
        }

        if (this.onGround) {
            this.xd *= 0.7F;
            this.zd *= 0.7F;
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

//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.33F);
        int part = (int)(1.0F + this.quadSize / this.psm * 4.0F);
        float var8 = (float)(part % 16) / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = (float)(part / 16) / 16.0F;
        float var11 = var10 + 0.0624375F;
        float var12 = 0.3F * this.quadSize;
        float var13 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float var14 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float var15 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);

        consumer.vertex(
                        var13 - f1 * var12 - f4 * var12,
                        var14 - f2 * var12,
                        var15 - f3 * var12 - f5 * var12
                )
                .color(this.rCol,this.gCol,this.bCol, alpha)
                .uv(var9, var11)
                .uv2(LightTexture.FULL_BRIGHT)//TODO:Bright
                .endVertex();
        consumer.vertex(
                        var13 - f1 * var12 + f4 * var12,
                        var14 + f2 * var12,
                        var15 - f3 * var12 + f5 * var12
                )
                .color(this.rCol,this.gCol,this.bCol, alpha)
                .uv(var9, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12
                )
                .color(this.rCol,this.gCol,this.bCol, alpha)
                .uv(var8, var10)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
        consumer.vertex(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12)
                .color(this.rCol,this.gCol,this.bCol, alpha)
                .uv(var8, var11)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }
}
