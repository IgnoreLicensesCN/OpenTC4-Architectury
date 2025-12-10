package thaumcraft.client.fx.particles.migrated.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static net.minecraft.client.particle.ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;


public class FXBreaking extends ThaumcraftParticle{

    public FXBreaking(ClientLevel par1World, double par2, double par4, double par6, Item item) {
        super(par1World, par2, par4, par6, 0.0F, 0.0F, 0.0F);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item.getDefaultInstance());
        TextureAtlasSprite sprite = model.getParticleIcon();
        this.setSprite(sprite);
        this.rCol = this.gCol = this.bCol = 1.0F;
        this.gravity = 1.F;//Blocks.SNOW.blockParticleGravity;
        this.quadSize /= 2.0F;
    }

    public FXBreaking(ClientLevel par1World, double par2, double par4, double par6, double par8, double par10, double par12, Item par14Item) {
        this(par1World, par2, par4, par6, par14Item);
        this.xd *= 0.1F;
        this.yd *= 0.1F;
        this.zd *= 0.1F;
        this.xd += par8;
        this.yd += par10;
        this.zd += par12;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // 1. 插值位置
        double px = Mth.lerp(partialTicks, xo, x);
        double py = Mth.lerp(partialTicks, yo, y);
        double pz = Mth.lerp(partialTicks, zo, z);

        // 相机偏移
        double camX = camera.getPosition().x();
        double camY = camera.getPosition().y();
        double camZ = camera.getPosition().z();
        float x0 = (float)(px - camX);
        float y0 = (float)(py - camY);
        float z0 = (float)(pz - camZ);

        // 2. Fade / 缩放
        float fade = 1.0f - ((float)age / (float)lifetime);
        float scale = 0.1f * quadSize * fade;

        // 3. 获取 sprite UV（保留 jitter 逻辑）
        float u0, u1, v0, v1;
        var particleTextureJitterX = random.nextFloat() * 4.f;
        var particleTextureJitterY = random.nextFloat() * 4.f;
        float jitterX = particleTextureJitterX / 4.0f;
        float jitterY = particleTextureJitterY / 4.0f;
        u0 = sprite.getU(jitterX);
        u1 = sprite.getU(jitterX + 1.0f / 4.0f);
        v0 = sprite.getV(jitterY);
        v1 = sprite.getV(jitterY + 1.0f / 4.0f);

        // 4. 相机左右/上向量
        Vector3f left = camera.getLeftVector();
        Vector3f up = camera.getUpVector();

        // 5. 四边形顶点
        // 顶点顺序保持和原来一致
        buffer.vertex(x0 - left.x() * scale - up.x() * scale,
                        y0 - left.y() * scale - up.y() * scale,
                        z0 - left.z() * scale - up.z() * scale)
                .color(rCol, gCol, bCol, fade)
                .uv(u1, v1)
                .endVertex();

        buffer.vertex(x0 - left.x() * scale + up.x() * scale,
                        y0 - left.y() * scale + up.y() * scale,
                        z0 - left.z() * scale + up.z() * scale)
                .color(rCol, gCol, bCol, fade)
                .uv(u1, v0)
                .endVertex();

        buffer.vertex(x0 + left.x() * scale + up.x() * scale,
                        y0 + left.y() * scale + up.y() * scale,
                        z0 + left.z() * scale + up.z() * scale)
                .color(rCol, gCol, bCol, fade)
                .uv(u0, v0)
                .endVertex();

        buffer.vertex(x0 + left.x() * scale - up.x() * scale,
                        y0 + left.y() * scale - up.y() * scale,
                        z0 + left.z() * scale - up.z() * scale)
                .color(rCol, gCol, bCol, fade)
                .uv(u0, v1)
                .endVertex();
    }


}
