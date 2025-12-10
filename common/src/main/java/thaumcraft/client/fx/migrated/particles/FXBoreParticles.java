package thaumcraft.client.fx.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import thaumcraft.api.rendering.ItemColorOwner;
import thaumcraft.client.fx.migrated.ThaumcraftParticle;

public class FXBoreParticles extends ThaumcraftParticle {
    private final Block blockInstance;
    private final Item itemInstance;
    private final Direction side;
    private final double targetX;
    private final double targetY;
    private final double targetZ;

    public FXBoreParticles(ClientLevel level, double x, double y, double z,
                           double tx, double ty, double tz,
                           Block block, Direction side){

        this(level, x, y, z, tx, ty, tz, block, side, null);
    }
    public FXBoreParticles(ClientLevel level, double x, double y, double z,
                           double tx, double ty, double tz,
                           Block block, Direction side,
                           @Nullable SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.side = side;
        this.blockInstance = block;
        this.itemInstance = null;
//        this.metadata = meta;

        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;

        this.quadSize *= 0.4F + random.nextFloat() * 0.3F;

        // 设置颜色为中灰
        this.rCol = this.gCol = this.bCol = 0.6f;

        // 速度微扰
        float f3 = 0.01F;
        this.xd = random.nextGaussian() * f3;
        this.yd = random.nextGaussian() * f3;
        this.zd = random.nextGaussian() * f3;

        double dx = tx - this.x;
        double dy = ty - this.y;
        double dz = tz - this.z;
        int base = (int)(Math.sqrt(dx * dx + dy * dy + dz * dz) * 3.0);
        if (base < 1) base = 1;
        this.lifetime = base / 2 + random.nextInt(base);

        if (sprites != null) {
            this.pickSprite(sprites);
        }
        removeIfTooFar();
    }

    public FXBoreParticles(ClientLevel level, double x, double y, double z,
                           double tx, double ty, double tz,
                           Item item, Direction side){
        this(level, x, y, z, tx, ty, tz, item, side, null);
    }
    public FXBoreParticles(ClientLevel level, double x, double y, double z,
                           double tx, double ty, double tz,
                           Item item, Direction side,
                           @Nullable SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.blockInstance = null;
        this.itemInstance = item;
        this.side = side;
        this.targetX = tx;
        this.targetY = ty;
        this.targetZ = tz;

        this.quadSize *= 0.4F + random.nextFloat() * 0.3F;

        this.rCol = this.gCol = this.bCol = 0.6f;

        float f3 = 0.01F;
        this.xd = random.nextGaussian() * f3;
        this.yd = random.nextGaussian() * f3;
        this.zd = random.nextGaussian() * f3;

        double dx = tx - this.x;
        double dy = ty - this.y;
        double dz = tz - this.z;
        int base = (int)(Math.sqrt(dx * dx + dy * dy + dz * dz) * 3.0);
        if (base < 1) base = 1;
        this.lifetime = base / 2 + random.nextInt(base);

        if (sprites != null) {
            this.pickSprite(sprites);
        }
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

    public FXBoreParticles applyColourMultiplier(int x, int y, int z) {
        if (blockInstance != null) {
            var pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() == blockInstance) {
                int color = Minecraft.getInstance().getBlockColors().getColor(state,level,pos);
                this.rCol *= (color >> 16 & 255) / 255f;
                this.gCol *= (color >> 8 & 255) / 255f;
                this.bCol *= (color & 255) / 255f;
            }
        } else if (itemInstance != null && itemInstance instanceof ItemColorOwner colorOwner) {
            int color = colorOwner.getColorFromItemStack(itemInstance.getDefaultInstance());
            this.rCol *= (color >> 16 & 255) / 255f;
            this.gCol *= (color >> 8 & 255) / 255f;
            this.bCol *= (color & 255) / 255f;
        }
        return this;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    private final Matrix4f cacheMatrix = new Matrix4f();
    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // 位置插值
        double x = Mth.lerp(partialTicks, xo, this.x) - camera.getPosition().x();
        double y = Mth.lerp(partialTicks, yo, this.y) - camera.getPosition().y();
        double z = Mth.lerp(partialTicks, zo, this.z) - camera.getPosition().z();

        float alpha = 1.0f;
        float size = 0.1f * quadSize;

        // UV
        float u0 = sprite != null ? sprite.getU0() : 0f;
        float u1 = u0 +  (1.f/64.f);
        float v0 = sprite != null ? sprite.getV0() : 0f;
        float v1 = v0 + (1.f/64.f);

        // 使用 JOML Matrix4fStack 做旋转朝向
        Matrix4fStack ms = new Matrix4fStack();
        ms.identity();
        ms.translate((float)x, (float)y, (float)z);

// 四边形顶点
        Vector3f[] corners = {
                new Vector3f(-size, -size, 0f),
                new Vector3f(-size, size, 0f),
                new Vector3f(size, size, 0f),
                new Vector3f(size, -size, 0f)
        };

// 将顶点旋转到摄像机朝向
        Quaternionf rot = camera.rotation();
        for (Vector3f corner : corners) {
            rot.transform(corner);
            buffer.vertex(ms.get(cacheMatrix), corner.x(), corner.y(), corner.z())
                    .color(rCol, gCol, bCol, alpha)
                    .uv(u0, v1) // 按需调整
                    .endVertex();
        }
    }



}
