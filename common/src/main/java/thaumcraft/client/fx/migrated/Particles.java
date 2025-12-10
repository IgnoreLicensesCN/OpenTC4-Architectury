package thaumcraft.client.fx.particles.migrated;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import thaumcraft.client.fx.particles.migrated.particles.*;

public class Particles {

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

    public static void init() {
        //register?go f**k off! we new particles then just add to particleEngine.
//        PARTICLES.register();
//

        try {
            Class.forName(FXDrop.class.getName());//see if it would crash
            Class.forName(FXGeneric.class.getName());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
