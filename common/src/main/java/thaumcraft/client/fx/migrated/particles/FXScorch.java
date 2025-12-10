package thaumcraft.client.fx.particles.migrated.particles;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static thaumcraft.client.fx.particles.migrated.particles.FXGeneric.PARTICLE_SPRITE;

public class FXScorch extends ThaumcraftParticle {
    public boolean pvp = true;
    public boolean mobs = true;
    public boolean animals = true;
    private double px;
    private double py;
    private double pz;
    private float transferParticleScale;
    Entity partDestEnt;
    public boolean lance = false;

    public FXScorch(ClientLevel world, double x, double y, double z, Vec3 v, float spread, boolean lance) {
        super(world, x, y, z, 0.0F, 0.0F, 0.0F);
        this.x = x;
        this.y = y;
        this.z = z;
        this.lance = lance;
        this.px = x + v.x * (double)100.0F;
        this.py = y + v.y * (double)100.0F;
        this.pz = z + v.z * (double)100.0F;
        if (!lance) {
            this.px += (this.random.nextFloat() - this.random.nextFloat()) * spread;
            this.py += (this.random.nextFloat() - this.random.nextFloat()) * spread;
            this.pz += (this.random.nextFloat() - this.random.nextFloat()) * spread;
        } else {
            this.px += (double)(this.random.nextFloat() - this.random.nextFloat()) * (double)0.5F;
            this.py += (double)(this.random.nextFloat() - this.random.nextFloat()) * (double)0.5F;
            this.pz += (double)(this.random.nextFloat() - this.random.nextFloat()) * (double)0.5F;
        }

        this.transferParticleScale = this.quadSize = this.random.nextFloat() * 0.5F + 2.0F;
        if (!lance) {
            this.transferParticleScale = this.quadSize = this.random.nextFloat() + 3.0F;
        }

        this.lifetime = 50;
        this.setSize(0.1F, 0.1F);
        this.setParticleTextureIndex(151);
        this.hasPhysics = true;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.alpha = 0.66F;
        this.setSprite(PARTICLE_SPRITE);
    }

    public int getBrightnessForRender(float par1) {
        return 210;
    }

    public float getBrightness(float par1) {
        return 1.0F;
    }

    @Override
    public void tick() {
        double dx = this.px - this.x;
        double dy = this.py - this.y;
        double dz = this.pz - this.z;
        double distance = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
        this.xd = dx / (distance * (double)1.25F);
        this.yd = dy / (distance * (double)1.25F);
        this.zd = dz / (distance * (double)1.25F);
        this.xd *= (float)(this.lifetime - this.age) / (float)this.lifetime;
        this.yd *= (float)(this.lifetime - this.age) / (float)this.lifetime;
        this.zd *= (float)(this.lifetime - this.age) / (float)this.lifetime;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.xd += this.random.nextFloat() * 0.07F - 0.035F;
        this.yd += this.random.nextFloat() * 0.07F - 0.035F;
        this.zd += this.random.nextFloat() * 0.07F - 0.035F;
        int var7 = MathHelper.floor_double(this.x);
        int var8 = MathHelper.floor_double(this.y);
        int var9 = MathHelper.floor_double(this.z);
        var bpos = new BlockPos(var7, var8, var9);
        if (this.age > 1 && this.level.getBlockState(bpos).isCollisionShapeFullBlock(level,bpos)) {
            this.xd = 0.0F;
            this.yd = 0.0F;
            this.zd = 0.0F;
            this.age += 10;
        }

        pushOutOfBlocksUnified(pos -> {
            BlockState state = level.getBlockState(pos);
            return state.isCollisionShapeFullBlock(level, pos);
        });
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        ++this.age;
        if (this.age >= this.lifetime) {
            this.remove();
        }

        float fs = (float)this.age / (float)(this.lifetime - 9);
        if (fs <= 1.0F) {
            this.setParticleTextureIndex((int)(151.0F + fs * 6.0F));
        } else {
            this.setParticleTextureIndex(159 - (this.lifetime - this.age) / 3);
        }

    }

    //Right = ( f1,  0, f3 )
    //Up    = ( f4, f2, f5 )
    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        float fs = (float)this.age / (float)this.lifetime;
        this.quadSize = this.transferParticleScale * (fs + 0.25F) * 2.0F;
        float fc = (float)this.age * 9.0F / (float)this.lifetime;
        if (fc > 1.0F) {
            fc = 1.0F;
        }

        this.rCol = this.gCol = fc;
        this.bCol = 1.0F;

        var index = this.particleTextureIndex;
        int spriteSize = 8; // 单个子粒子大小
        int atlasSize = 128; // 整张大图大小
        int cols = atlasSize / spriteSize;


        int row = index / cols;
        int col = index % cols;

        float u0 = (float) col * spriteSize / atlasSize;
        float u1 = (float) (col + 1) * spriteSize / atlasSize;
        float v0 = (float) row * spriteSize / atlasSize;
        float v1 = (float) (row + 1) * spriteSize / atlasSize;
        // 渲染 quad（billboard）
        float px = (float) (this.x + (this.xo - this.x) * (1.0F - partialTicks));
        float py = (float) (this.y + (this.yo - this.y) * (1.0F - partialTicks));
        float pz = (float) (this.z + (this.zo - this.z) * (1.0F - partialTicks));
        float scale = this.quadSize;

        // 顶点偏移量
        float half = 0.5F * scale;
        // 简单 billboard，顶点顺序可按需要调整
        consumer.vertex(px - half, py - half, pz)
                .uv(u0, v1)
                .color(rCol, gCol, bCol, this.alpha)
                .endVertex();
        consumer.vertex(px - half, py + half, pz)
                .uv(u0, v0)
                .color(rCol, gCol, bCol, this.alpha)
                .endVertex();
        consumer.vertex(px + half, py + half, pz)
                .uv(u1, v0)
                .color(rCol, gCol, bCol, this.alpha)
                .endVertex();
        consumer.vertex(px + half, py - half, pz)
                .uv(u1, v1)
                .color(rCol, gCol, bCol, this.alpha)
                .endVertex();
//        super.render(consumer, camera, partialTicks);
    }


    public void setParticleTextureIndex(int particleTextureIndex) {
        this.particleTextureIndex = particleTextureIndex;
    }
    protected int particleTextureIndex = 0;
}
