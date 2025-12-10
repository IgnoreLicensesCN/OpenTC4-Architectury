package thaumcraft.client.fx.migrated.particles;


import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXBubbleAlt extends ThaumcraftParticle {
    public int particle = 25;

    public FXBubbleAlt(
            ClientLevel level, double x, double y, double z,
            double dx, double dy, double dz, int age) {
        super(level, x, y, z, dx, dy, dz);

        this.rCol = 1.0F;
        this.gCol = 0.0F;
        this.bCol = 0.5F;

        this.setSize(0.02F, 0.02F);
        this.hasPhysics = false;

        this.quadSize *= this.random.nextFloat() * 0.3F + 0.2F;

        this.xd = dx * 0.2 + (this.random.nextFloat() * 2 - 1) * 0.02;
        this.yd = dy * 0.2 + this.random.nextFloat() * 0.02;
        this.zd = dz * 0.2 + (this.random.nextFloat() * 2 - 1) * 0.02;

        this.lifetime = (int)((age + 2) + 8.0F / (this.random.nextFloat() * 0.8F + 0.2F));

        removeIfTooFar();
    }
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.xd += (random.nextFloat() - random.nextFloat()) * 0.001F;
        this.zd += (random.nextFloat() - random.nextFloat()) * 0.001F;

        this.x += xd;
        this.y += yd;
        this.z += zd;

        this.xd *= 0.85F;
        this.yd *= 0.85F;
        this.zd *= 0.85F;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        if (this.age == this.lifetime - 2) {
            this.particle = 17;
        } else if (this.age == this.lifetime - 1) {
            this.particle = 18;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        double px = Mth.lerp(partialTicks, xo, x);
        double py = Mth.lerp(partialTicks, yo, y);
        double pz = Mth.lerp(partialTicks, zo, z);

        double cx = camera.getPosition().x();
        double cy = camera.getPosition().y();
        double cz = camera.getPosition().z();

        float x0 = (float)(px - cx);
        float y0 = (float)(py - cy);
        float z0 = (float)(pz - cz);

        // ✅ 核心差异：随生命周期增长
        float lifeScale = (float)this.age / (float)this.lifetime;
        float scale = 0.2F * quadSize * lifeScale;

        float u0 = (particle % 16) / 16.0f;
        float u1 = u0 + 0.0624375f;
        float v0 = (float)(particle / 16) / 16.0f;
        float v1 = v0 + 0.0624375f;

        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();

        buffer.vertex(x0 - left.x() * scale - up.x() * scale,
                        y0 - left.y() * scale - up.y() * scale,
                        z0 - left.z() * scale - up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u1, v1)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        buffer.vertex(x0 - left.x() * scale + up.x() * scale,
                        y0 - left.y() * scale + up.y() * scale,
                        z0 - left.z() * scale + up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u1, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        buffer.vertex(x0 + left.x() * scale + up.x() * scale,
                        y0 + left.y() * scale + up.y() * scale,
                        z0 + left.z() * scale + up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u0, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        buffer.vertex(x0 + left.x() * scale - up.x() * scale,
                        y0 + left.y() * scale - up.y() * scale,
                        z0 + left.z() * scale - up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u0, v1)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }
}
