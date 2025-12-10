package thaumcraft.client.fx.particles.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.NotNull;

import static thaumcraft.client.fx.particles.migrated.particles.FXDrop.loadSprite;

public class FXGeneric extends ThaumcraftParticle {
    public static final TextureAtlasSprite PARTICLE_SPRITE = loadSprite("thaumcraft", "particle/particles.png");

    boolean loop = false;
    int delay = 0;
    int startParticle = 0;
    int numParticles = 1;
    int particleInc = 1;

    public FXGeneric(ClientLevel world, double x, double y, double z, double xx, double yy, double zz) {
        super(world, x, y, z, xx, yy, zz);
        this.setSize(0.1F, 0.1F);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.xd = xx;
        this.yd = yy;
        this.zd = zz;
        this.hasPhysics = false;
        this.setSprite(PARTICLE_SPRITE);
    }

    public void setMaxAge(int max, int delay) {
        setMaxAge(max + delay);
        this.delay = delay;
    }

    protected int particleTextureIndex = 0;

    public void setParticles(int startParticle, int numParticles, int particleInc) {
        this.startParticle = startParticle;
        this.numParticles = numParticles;
        this.particleInc = particleInc;
        this.setParticleTextureIndex(startParticle);
    }

    public void setParticleTextureIndex(int particleTextureIndex) {
        this.particleTextureIndex = particleTextureIndex;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.loop) {
            this.setParticleTextureIndex(this.startParticle + this.age / this.particleInc % this.numParticles);
        } else {
            float fs = (float) this.age / (float) this.lifetime;
            this.setParticleTextureIndex((int) ((float) this.startParticle + Math.min(
                    (float) this.numParticles * fs,
                    (float) (this.numParticles - 1)
            )));
        }

    }

    //Right = ( f1,  0, f3 )
    //Up    = ( f4, f2, f5 )
    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (this.age >= this.delay) {
            float t = this.age;
            if (this.age <= 1 || this.age >= this.lifetime - 1) {
                this.alpha = t / 2.0F;
            }
            int index;
            if (loop) {
                index = startParticle + (age / particleInc) % numParticles;
            } else {
                float fs = (float) age / (float) lifetime;
                index = startParticle + Math.min((int) (numParticles * fs), numParticles - 1);
            }
            this.particleTextureIndex = index;

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
                    .color(1f, 1f, 1f, this.alpha)
                    .endVertex();
            consumer.vertex(px - half, py + half, pz)
                    .uv(u0, v0)
                    .color(1f, 1f, 1f, this.alpha)
                    .endVertex();
            consumer.vertex(px + half, py + half, pz)
                    .uv(u1, v0)
                    .color(1f, 1f, 1f, this.alpha)
                    .endVertex();
            consumer.vertex(px + half, py - half, pz)
                    .uv(u1, v1)
                    .color(1f, 1f, 1f, this.alpha)
                    .endVertex();


//            super.render(consumer, camera, partialTicks);
            this.alpha = t;
        }
    }

}
