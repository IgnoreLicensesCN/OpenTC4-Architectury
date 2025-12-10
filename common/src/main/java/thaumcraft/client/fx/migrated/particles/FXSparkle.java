package thaumcraft.client.fx.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXSparkle extends ThaumcraftParticle {

    public boolean slowdown;
    public boolean leyLineEffect;
    public int multiplier;
    public boolean shrink;
    public int particle;
    public boolean tinkle;
    public int blendmode;
    public int currentColor;

    public FXSparkle(
            ClientLevel level, double x, double y, double z,
            float scale, float r, float g, float b,
            int multiplier
    ) {
        this(
                level,
                x,
                y,
                z,
                scale,
                r,
                g,
                b,
                multiplier,
                null
        );
    }

    // 颜色 + 缩放 + 生命周期
    public FXSparkle(
            ClientLevel level, double x, double y, double z,
            float scale, float r, float g, float b,
            int multiplier, @Nullable SpriteSet sprites
    ) {
        super(
                level,
                x,
                y,
                z,
                0,
                0,
                0
        );

        this.multiplier = multiplier;
        this.shrink = true;
        this.particle = 16;
        this.blendmode = 1;
        this.slowdown = true;

        if (r == 0.0F) r = 1.0F;

        this.rCol = r;
        this.gCol = g;
        this.bCol = b;

        this.gravity = 0.0F;
        this.xd = this.yd = this.zd = 0.0;

        this.quadSize *= scale;
        this.lifetime = 3 * multiplier;

        this.hasPhysics = false;

        if (sprites != null) {
            this.pickSprite(sprites);
        }
    }

    // 用类型选择颜色
    public FXSparkle(
            ClientLevel level, double x, double y, double z,
            float scale, int type, int multiplier
    ) {
        this(
                level,
                x,
                y,
                z,
                scale,
                1f,
                1f,
                1f,
                multiplier,
                null
        );

        switch (type) {
            case 0 -> {
                rCol = 0.75f + random.nextFloat() * 0.25f;
                gCol = 0.25f + random.nextFloat() * 0.25f;
                bCol = 0.75f + random.nextFloat() * 0.25f;
            }
            case 1 -> {
                rCol = 0.5f + random.nextFloat() * 0.3f;
                gCol = 0.5f + random.nextFloat() * 0.3f;
                bCol = 0.2f;
            }
            case 2 -> {
                rCol = 0.2f;
                gCol = 0.2f;
                bCol = 0.7f + random.nextFloat() * 0.3f;
            }
            case 3 -> {
                rCol = 0.2f;
                gCol = 0.7f + random.nextFloat() * 0.3f;
                bCol = 0.2f;
            }
            case 4 -> {
                rCol = 0.7f + random.nextFloat() * 0.3f;
                gCol = 0.2f;
                bCol = 0.2f;
            }
            case 5 -> {
                rCol = random.nextFloat() * 0.1f;
                gCol = random.nextFloat() * 0.1f;
                bCol = random.nextFloat() * 0.1f;
            }
            case 6 -> {
                rCol = 0.8f + random.nextFloat() * 0.2f;
                gCol = 0.8f + random.nextFloat() * 0.2f;
                bCol = 0.8f + random.nextFloat() * 0.2f;
            }
            case 7 -> {
                rCol = 0.2f;
                gCol = 0.5f + random.nextFloat() * 0.3f;
                bCol = 0.6f + random.nextFloat() * 0.3f;
            }
        }
    }

    // 可以设置目标位置移动
    public FXSparkle(
            ClientLevel level, double x, double y, double z,
            double targetX, double targetY, double targetZ,
            float scale, int type, int multiplier
    ) {
        this(
                level,
                x,
                y,
                z,
                scale,
                type,
                multiplier
        );
        this.xd = (targetX - this.x) / lifetime;
        this.yd = (targetY - this.y) / lifetime;
        this.zd = (targetZ - this.z) / lifetime;
    }

    @Override
    public void tick() {
        // 先保存上一帧位置
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // 声音效果（tinkle）
        if (this.age == 0 && tinkle && this.level.random.nextInt(10) == 0) {
            this.level.playLocalSound(
                    this.x,
                    this.y,
                    this.z,
                    net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                    net.minecraft.sounds.SoundSource.PLAYERS,
                    0.02F,
                    0.7F * ((this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.6F + 2.0F),
                    false
            );
        }

        // 年龄判断
        if (++this.age >= this.lifetime) {
            this.remove();
            return;
        }

        // 重力
        if (hasPhysics) {
            this.yd -= 0.04 * this.gravity;
        }

        // 移动
        move(
                this.xd,
                this.yd,
                this.zd
        );

        // 减速
        if (slowdown) {
            this.xd *= 0.908f;
            this.yd *= 0.908f;
            this.zd *= 0.908f;
        }

        // 缩小
        if (shrink) {
            this.quadSize *= 0.96f;
        }

        if (hasPhysics) {
            pushOutOfBlocksUnified(pos -> !level.getBlockState(pos).isAir());
        }

        // leyLineEffect
        if (leyLineEffect) {
            FXSparkle fx = new FXSparkle(
                    this.level,
                    this.x + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.1,
                    this.y + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.1,
                    this.z + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.1,
                    1.0f,
                    this.currentColor,
                    3 + this.level.random.nextInt(3)
            );
            fx.hasPhysics = false; // 对应原来的 noClip = true
            Minecraft.getInstance().particleEngine.add(fx);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {

        // 位置插值
        float px = (float) (xo + (x - xo) * partialTicks - camera.getPosition().x());
        float py = (float) (yo + (y - yo) * partialTicks - camera.getPosition().y());
        float pz = (float) (zo + (z - zo) * partialTicks - camera.getPosition().z());

        // 尺寸 / alpha
        float size = 0.1F * quadSize;
        if (shrink) {
            size *= (float) (lifetime - age + 1) / (float) lifetime;
        }

        float alpha = 0.75F;

        // camera 基向量
        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();

        float f1 = -left.x();
        float f2 = up.y();
        float f3 = -left.z();
        float f4 = up.x();
        float f5 = up.z();

        // UV
        float u0 = 0f, u1 = 0.0625f;
        float v0 = 0.375f, v1 = 0.4375f;

        // 经典 old-school quad 展开
        buffer.vertex(
                        px - f1 * size - f4 * size,
                        py - f2 * size,
                        pz - f3 * size - f5 * size
                )
                .color(
                        rCol,
                        gCol,
                        bCol,
                        alpha
                ).uv(
                        u1,
                        v1
                ).endVertex();

        buffer.vertex(
                        px - f1 * size + f4 * size,
                        py + f2 * size,
                        pz - f3 * size + f5 * size
                )
                .color(
                        rCol,
                        gCol,
                        bCol,
                        alpha
                ).uv(
                        u1,
                        v0
                ).endVertex();

        buffer.vertex(
                        px + f1 * size + f4 * size,
                        py + f2 * size,
                        pz + f3 * size + f5 * size
                )
                .color(
                        rCol,
                        gCol,
                        bCol,
                        alpha
                ).uv(
                        u0,
                        v0
                ).endVertex();

        buffer.vertex(
                        px + f1 * size - f4 * size,
                        py - f2 * size,
                        pz + f3 * size - f5 * size
                )
                .color(
                        rCol,
                        gCol,
                        bCol,
                        alpha
                ).uv(
                        u0,
                        v1
                ).endVertex();
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


}
