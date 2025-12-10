package thaumcraft.client.fx.particles.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.lwjgl.opengl.GL11;

public class FXWispArcing extends ThaumcraftParticle{
    private double field_70568_aq;
    private double field_70567_ar;
    private double field_70566_as;
    float moteParticleScale;
    int moteHalfLife;
    public boolean tinkle;
    public int blendmode;

    public FXWispArcing(ClientLevel world, double d, double d1, double d2, float f, float red, float green, float blue) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.tinkle = false;
        this.blendmode = 1;
        if (red == 0.0F) {
            red = 1.0F;
        }

        this.field_70568_aq = this.x = d;
        this.field_70567_ar = this.y = d1;
        this.field_70566_as = this.z = d2;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.gravity = 0.0F;
        this.quadSize *= f;
        this.moteParticleScale = this.quadSize;
        this.lifetime = (int)((double)36.0F / (Math.random() * 0.3 + 0.7));
        this.moteHalfLife = this.lifetime / 2;
        this.hasPhysics = true;
        this.setSize(0.01F, 0.01F);
        removeIfTooFar();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public FXWispArcing(ClientLevel world, double d, double d1, double d2, double x, double y, double z, float f, float red, float green, float blue) {
        this(world, d, d1, d2, f, red, green, blue);
        this.xd = x - d;
        this.yd = y - d1;
        this.zd = z - d2;
//        this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);//TODO?
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
        float agescale;
        agescale = (float) this.age / (float) this.moteHalfLife;
        if (agescale > 1.0F) {
            agescale = 2.0F - agescale;
        }

        this.quadSize = this.moteParticleScale * agescale;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
        float f10 = 0.5F * this.quadSize;
        float f11 = (float)(this.xo + (this.x - this.xo) * (double)f - interpPosX);
        float f12 = (float)(this.yo + (this.y - this.yo) * (double)f - interpPosY);
        float f13 = (float)(this.zo + (this.z - this.zo) * (double)f - interpPosZ);
        float var8 = 0.0F;
        float var9 = 0.0F;
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

//    public int getFXLayer() {
//        return this.blendmode == 1 ? 0 : 1;
//    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float var1 = (float) this.age / (float) this.lifetime;
        float var2 = (float) this.age / ((float) this.lifetime / 2.0F);
        var1 = 1.0F - var1;
        var2 = 1.0F - var2;
        var2 *= var2;
        this.x = this.field_70568_aq + this.xd * (double)var1;
        this.y = this.field_70567_ar + this.yd * (double)var1 - (double)var2 + (double)1.0F;
        this.z = this.field_70566_as + this.zd * (double)var1;
        if (this.age++ >= this.lifetime) {
            this.setDead();
        }

    }
}
