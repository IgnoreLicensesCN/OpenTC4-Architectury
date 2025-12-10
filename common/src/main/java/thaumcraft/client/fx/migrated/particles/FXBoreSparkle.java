package thaumcraft.client.fx.particles.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FXBoreSparkle extends ThaumcraftParticle {

    private final double targetX, targetY, targetZ;
    public int particle = 24;

    public FXBoreSparkle(ClientLevel level, double x, double y, double z,
                         double tx, double ty, double tz){
        this(level,x,y,z,tx,ty,tz,null);
    }
    public FXBoreSparkle(ClientLevel level, double x, double y, double z,
                         double tx, double ty, double tz,
                         @Nullable SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);

        // Sprite
        if (sprites != null) {
            this.pickSprite(sprites);
        }

        // 目标位置
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;

        // 随机初始颜色
        this.rCol = 0.2f;
        this.gCol = 0.6f + random.nextFloat() * 0.3f;
        this.bCol = 0.2f;

        // 粒子大小
        this.quadSize = 0.1f * (0.5f + random.nextFloat() * 0.5f);

        // 计算寿命
        double dx = tx - x;
        double dy = ty - y;
        double dz = tz - z;
        int base = (int) (Math.sqrt(dx * dx + dy * dy + dz * dz) * 3.0);
        if (base < 1) {
            base = 1;
        }
        this.lifetime = base / 2 + random.nextInt(base);

        // 初始运动
        this.xd = random.nextGaussian() * 0.01;
        this.yd = random.nextGaussian() * 0.01;
        this.zd = random.nextGaussian() * 0.01;

        // 重力
        this.gravity = 0.2f;

        // 默认物理属性
        this.hasPhysics = true; // 对应旧版 noClip = false
        removeIfTooFar();
    }

    @Override
    public void tick() {
        // 保存上一帧位置
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // 年龄判断
        if (++this.age >= this.lifetime
                || (Math.floor(this.x) == Math.floor(this.targetX)
                && Math.floor(this.y) == Math.floor(this.targetY)
                && Math.floor(this.z) == Math.floor(this.targetZ))) {
            this.remove();
            return;
        }

        // 推出方块（noClip = false）
        if (!this.hasPhysics) {
            // 这里可以调用你实现的 pushOutOfBlocksUnified 或类似方法
            pushOutOfBlocksUnified(pos -> {
                // pos 是 BlockPos
                boolean notAir = !level.getBlockState(pos).isAir();
                boolean noLiquid = level.getFluidState(pos).isEmpty();
                return notAir && noLiquid;
            });
        }

        // 移动
        move(this.xd, this.yd, this.zd);

        // 阻力
        this.xd *= 0.985;
        this.yd *= 0.985;
        this.zd *= 0.985;

        // 朝目标加速
        double dx = this.targetX - this.x;
        double dy = this.targetY - this.y;
        double dz = this.targetZ - this.z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double speed = 0.3;

        if (distance < 4.0) {
            this.quadSize *= 0.9f; // 对应 particleScale *= 0.9F
            speed = 0.6;
        }

        dx /= distance;
        dy /= distance;
        dz /= distance;

        this.xd += dx * speed;
        this.yd += dy * speed;
        this.zd += dz * speed;

        // 限制最大速度
        this.xd = Mth.clamp((float) this.xd, -0.35f, 0.35f);
        this.yd = Mth.clamp((float) this.yd, -0.35f, 0.35f);
        this.zd = Mth.clamp((float) this.zd, -0.35f, 0.35f);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // 位置插值
        double x = Mth.lerp(partialTicks, xo, this.x) - camera.getPosition().x();
        double y = Mth.lerp(partialTicks, yo, this.y) - camera.getPosition().y();
        double z = Mth.lerp(partialTicks, zo, this.z) - camera.getPosition().z();

        // bob 缩放
        float bob = (float)Math.sin((float)age / 3.0f) * 0.5f + 1.0f;
        float size = 0.1f * quadSize * bob;

        // UV
        int part = age % 4;
        float u0 = (float)part / 16f;
        float u1 = u0 + 0.0624375f;
        float v0 = 0.25f;
        float v1 = v0 + 0.0624375f;

        // alpha
        float alpha = 0.75f;

        // 四个角顶点
        buffer.vertex(x - size, y + size, z - size).color(rCol, gCol, bCol, alpha).uv(u1, v1).endVertex();
        buffer.vertex(x - size, y - size, z - size).color(rCol, gCol, bCol, alpha).uv(u1, v0).endVertex();
        buffer.vertex(x + size, y - size, z + size).color(rCol, gCol, bCol, alpha).uv(u0, v0).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(rCol, gCol, bCol, alpha).uv(u0, v1).endVertex();
    }


}
