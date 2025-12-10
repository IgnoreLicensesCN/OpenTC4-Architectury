package thaumcraft.client.fx.migrated;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.migrated.particles.FXDrop;
import thaumcraft.client.fx.migrated.particles.FXGeneric;

import java.io.IOException;
import java.io.InputStream;

public class Particles {
    public static final TextureAtlasSprite PARTICLE_SPRITE = loadSprite("thaumcraft", "particle/particles.png");
    public static final TextureAtlasSprite DROP112;
    public static final TextureAtlasSprite DROP113;
    public static final TextureAtlasSprite DROP114;

    static {
        // ResourceLocation 可以随便写，只是标识用
        DROP112 = loadSprite( "thaumcraft", "particle/drop112.png");
        DROP113 = loadSprite( "thaumcraft", "particle/drop113.png");
        DROP114 = loadSprite("thaumcraft", "particle/drop114.png");
    }

//    public static final DeferredRegister<ParticleType<?>> PARTICLES =
//            DeferredRegister.create("thaumcraft", Registries.PARTICLE_TYPE);
//
//
//
//    public static class ParticleRegistrySuppliers {
//        public static final RegistrySupplier<SimpleParticleType> BLOCK_RUNES =
//                PARTICLES.register("block_runes", () -> new SimpleParticleType(false){});
//        public static final RegistrySupplier<SimpleParticleType> BORE_PARTICLES =
//                PARTICLES.register("bore_particles", () -> new SimpleParticleType(false){});
//        public static final RegistrySupplier<SimpleParticleType> BORE_SPARKLE =
//                PARTICLES.register("bore_sparkle", () -> new SimpleParticleType(false){});
//        public static final RegistrySupplier<SimpleParticleType> BREAKING =
//                PARTICLES.register("breaking", () -> new SimpleParticleType(false){});
//
//        public static final RegistrySupplier<SimpleParticleType> SPARKLE =
//                PARTICLES.register("sparkle", () -> new SimpleParticleType(false){});
//    }


    @Contract("_, _ -> new")
    public static @NotNull TextureAtlasSprite loadSprite(String modid, String path) {
        ResourceLocation rl = new ResourceLocation(modid, path);
        try (InputStream is = FXDrop.class.getClassLoader().getResourceAsStream("assets/"+modid+"/textures/"+path)) {
            if (is == null) throw new IOException("Cannot find resource: "+ modid +":" + path);
            NativeImage image = NativeImage.read(is);
            FrameSize frameSize = new FrameSize(image.getWidth(), image.getHeight());
            SpriteContents spriteContents = new SpriteContents(
                    rl,
                    frameSize,
                    image,
                    null // 如果没有动画元数据
            );
            return new TextureAtlasSprite(rl, spriteContents, image.getWidth(), image.getHeight(), 0, 0){};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void init() {
        //register?go f**k off! we new particles then just add to particleEngine.
//        PARTICLES.register();

    }
}
