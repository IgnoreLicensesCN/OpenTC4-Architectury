package thaumcraft.client.fx.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXBurst extends ThaumcraftParticle {
    public FXBurst(ClientLevel world, double d, double d1, double d2, float f) {
        super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0F;
        this.quadSize *= f;
        this.lifetime = 31;
        this.hasPhysics = true;
        this.setSize(0.01F, 0.01F);
    }


    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }

    }
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // 1️⃣ 粒子位置插值
        double px = Mth.lerp(partialTicks, xo, x);
        double py = Mth.lerp(partialTicks, yo, y);
        double pz = Mth.lerp(partialTicks, zo, z);

        float x0 = (float) (px - camera.getPosition().x());
        float y0 = (float) (py - camera.getPosition().y());
        float z0 = (float) (pz - camera.getPosition().z());

        // 2️⃣ 粒子缩放
        float scale = quadSize;

        // 3️⃣ UV（原 1.7.10 atlas 坐标）
        float u0 = (float)(this.age % 32) / 32.0F;
        float u1 = u0 + 0.03125F;
        float v0 = 0.96875F;
        float v1 = 1.0F;

        // 4️⃣ 相机方向
        Vector3f right = camera.getLeftVector().negate();
        Vector3f up = camera.getUpVector();

        // 5️⃣ 绘制四边形顶点
        float alpha = 1.f; // 原 glColor4f alpha

        buffer.vertex(
                x0 - right.x() * scale - up.x() * scale,
                y0 - right.y() * scale - up.y() * scale,
                z0 - right.z() * scale - up.z() * scale
                )
                .color(rCol, gCol, bCol, alpha)
                .uv(u1, v1)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        buffer.vertex(x0 - right.x() * scale + up.x() * scale,
                        y0 - right.y() * scale + up.y() * scale,
                        z0 - right.z() * scale + up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u1, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        buffer.vertex(x0 + right.x() * scale + up.x() * scale,
                        y0 + right.y() * scale + up.y() * scale,
                        z0 + right.z() * scale + up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u0, v0)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();

        buffer.vertex(x0 + right.x() * scale - up.x() * scale,
                        y0 + right.y() * scale - up.y() * scale,
                        z0 + right.z() * scale - up.z() * scale)
                .color(rCol, gCol, bCol, alpha)
                .uv(u0, v1)
                .uv2(LightTexture.FULL_BRIGHT)
                .endVertex();
    }
}
