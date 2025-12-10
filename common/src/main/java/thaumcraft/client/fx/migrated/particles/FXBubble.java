package thaumcraft.client.fx.particles.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static net.minecraft.client.particle.ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;

/**
 * mapping from chatGPT(part)
 * | 1.7.10           | 1.20.x     |
 * | ---------------- | ---------- |
 * | `posX`           | `x`        |
 * | `prevPosX`       | `xo`       |
 * | `motionX`        | `xd`       |
 * | `particleAge`    | `age`      |
 * | `particleMaxAge` | `lifetime` |
 * | `setDead()`      | `remove()` |
 * | 1.7.10 (`EntityFX`) | 1.20.x (`Particle`) | 含义    |
 * | ------------------- | ------------------- | ----- |
 * | `motionX`           | `xd`                | X 轴速度 |
 * | `motionY`           | `yd`                | Y 轴速度 |
 * | `motionZ`           | `zd`                | Z 轴速度 |
 * | `posX`              | `x`                 | 当前坐标  |
 * | `prevPosX`          | `xo`                | 上一帧坐标 |
 * | `particleGravity`   | `gravity`           | 重力系数  |
 * | `particleMaxAge`    | `lifetime`          | 生命周期  |
 */
public class FXBubble extends ThaumcraftParticle {
    public int particle = 16;
    public double bubblespeed = 0.002;

    public FXBubble(ClientLevel par1World, double x, double y, double z, double dx, double dy, double dz, int age) {
        super(par1World, x, y, z, dx, dy, dz);
        this.rCol = 1.0F;
        this.gCol = 0.0F;
        this.bCol = 0.5F;

        this.setSize(0.02F, 0.02F);
        this.hasPhysics = false;
        this.quadSize *= this.random.nextFloat() * 0.3F + 0.2F;
        this.xd = dx * (double)0.2F + (double)((float)(Math.random() * (double)2.0F - (double)1.0F) * 0.02F);
        this.yd = dy * (double)0.2F + (double)((float)Math.random() * 0.02F);
        this.zd = dz * (double)0.2F + (double)((float)(Math.random() * (double)2.0F - (double)1.0F) * 0.02F);
        this.lifetime = (int)((double)(age + 2) + (double)8.0F / (Math.random() * 0.8 + 0.2));
        removeIfTooFar();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public void setFroth() {
        this.quadSize *= 0.75F;
        this.lifetime = 4 + this.random.nextInt(3);
        this.bubblespeed = -0.001;
        this.xd /= 5.0F;
        this.yd /= 10.0F;
        this.zd /= 5.0F;
    }

    public void setFroth2() {
        this.quadSize *= 0.75F;
        this.lifetime = 12 + this.random.nextInt(12);
        this.bubblespeed = -0.005;
        this.xd /= 5.0F;
        this.yd /= 10.0F;
        this.zd /= 5.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.yd += this.bubblespeed;
        if (this.bubblespeed > (double)0.0F) {
            this.xd += (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.01F;
            this.zd += (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.01F;
        }

        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        this.xd *= 0.85F;
        this.yd *= 0.85F;
        this.zd *= 0.85F;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else if (this.lifetime <= 2) {
            ++this.particle;
        }

    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // 1️⃣ 粒子位置插值
        double px = Mth.lerp(partialTicks, xo, x);
        double py = Mth.lerp(partialTicks, yo, y);
        double pz = Mth.lerp(partialTicks, zo, z);

        // 相机位置偏移
        double camX = camera.getPosition().x();
        double camY = camera.getPosition().y();
        double camZ = camera.getPosition().z();
        float x0 = (float)(px - camX);
        float y0 = (float)(py - camY);
        float z0 = (float)(pz - camZ);

        // 2️⃣ 缩放
        float scale = 0.1f * quadSize;

        // 3️⃣ 计算 atlas UV
        float u0 = (float)(particle % 16) / 16.0f;
        float u1 = u0 + 1.0f / 16.0f;   // 0.0625f, 原 0.0624375f 可以直接用 1/16
        float v0 = (float)(particle / 16) / 16.0f;
        float v1 = v0 + 1.0f / 16.0f;

        // 4️⃣ 相机左右/上向量
        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();

        // 5️⃣ 顶点顺序
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
