package thaumcraft.client.fx.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;


public class FXBlockRunes extends ThaumcraftParticle {

    private final float ofx, ofy;
    private final int runeIndex;
    private final float rotationY;

    public FXBlockRunes(
            ClientLevel level, double x, double y, double z,
            float r, float g, float b, int multiplier
    ) {
        this(level, x, y, z, r, g, b, multiplier, null, 0.F, 0.F, 0.F);
    }

    public FXBlockRunes(
            ClientLevel level, double x, double y, double z,
            float r, float g, float b, int multiplier,
            SpriteSet sprites
    ) {
        this(level, x, y, z, r, g, b, multiplier, sprites, 0.F, 0.F, 0.F);
    }

    public FXBlockRunes(
            ClientLevel level, double x, double y, double z,
            float r, float g, float b, int multiplier,
            SpriteSet sprites,
            double dx, double dy, double dz
    ) {
        super(level, x, y, z, dx, dy, dz);

        this.rCol = r;
        this.gCol = g;
        this.bCol = b;

        this.setParticleSpeed(0, 0, 0);

        this.rotationY = level.random.nextInt(4) * 90f;
        this.runeIndex = 224 + level.random.nextInt(16);

        this.ofx = level.random.nextFloat() * 0.2f;
        this.ofy = -0.3f + level.random.nextFloat() * 0.6f;

        this.quadSize = 0.3f * (1.0f + (float) level.random.nextGaussian() * 0.1f);
        this.lifetime = 3 * multiplier;
        if (sprites != null) {
            this.pickSprite(sprites);
        }
        this.alpha = 0f;
    }

    @Override
    public void tick() {
        // 保存上一帧位置（用于 lerp）
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // 年龄 + 死亡判断（注意顺序）
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // alpha 渐入 / 渐出
        float threshold = this.lifetime / 5f;
        if (this.age <= threshold) {
            this.alpha = this.age / threshold;
        } else {
            this.alpha = (this.lifetime - this.age) / (float) this.lifetime;
        }

        // 重力（等价 particleGravity）
        this.yd -= 0.04F * this.gravity;

        // 移动
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
    }


    @Override
    protected int getLightColor(float partialTicks) {
        // 等价 tessellator.setBrightness(240)
        return 0xF000F0;
    }


    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        double cx = Mth.lerp(partialTicks, xo, x) - camPos.x + ofx;
        double cy = Mth.lerp(partialTicks, yo, y) - camPos.y + ofy;
        double cz = Mth.lerp(partialTicks, zo, z) - camPos.z - 0.51; // 推出表面

        // UV
        float u1 = (runeIndex % 16) / 16f;
        float u2 = u1 + 0.0624375f;
        float v1 = 0.375f;
        float v2 = v1 + 0.0624375f;

        float half = quadSize * 0.5f;
        float a = alpha * 0.5f;

        // --- 构造“世界空间”旋转 ---
        float ry = Mth.DEG_TO_RAD * rotationY;
        float rz = Mth.DEG_TO_RAD * 90f;

        // 基础平面（Z+）
        Vec3[] quad = new Vec3[]{
                new Vec3(-half, half, 0),
                new Vec3(half, half, 0),
                new Vec3(half, -half, 0),
                new Vec3(-half, -half, 0)
        };

        for (int i = 0; i < quad.length; i++) {
            Vec3 v = quad[i];

            // Z 轴 90°
            v = new Vec3(
                    v.x * Mth.cos(rz) - v.y * Mth.sin(rz),
                    v.x * Mth.sin(rz) + v.y * Mth.cos(rz),
                    v.z
            );

            // Y 轴 rotation
            v = new Vec3(
                    v.x * Mth.cos(ry) + v.z * Mth.sin(ry),
                    v.y,
                    -v.x * Mth.sin(ry) + v.z * Mth.cos(ry)
            );

            quad[i] = v.add(cx, cy, cz);
        }

        buffer.vertex(quad[0].x, quad[0].y, quad[0].z)
                .uv(u2, v2)
                .color(rCol, gCol, bCol, a)
                .endVertex();
        buffer.vertex(quad[1].x, quad[1].y, quad[1].z)
                .uv(u2, v1)
                .color(rCol, gCol, bCol, a)
                .endVertex();
        buffer.vertex(quad[2].x, quad[2].y, quad[2].z)
                .uv(u1, v1)
                .color(rCol, gCol, bCol, a)
                .endVertex();
        buffer.vertex(quad[3].x, quad[3].y, quad[3].z)
                .uv(u1, v2)
                .color(rCol, gCol, bCol, a)
                .endVertex();
    }

}
