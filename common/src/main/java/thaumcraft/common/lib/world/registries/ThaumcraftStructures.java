package thaumcraft.common.lib.world.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.world.structure.EldritchRingStructure;
import thaumcraft.common.lib.world.structure.MoundStructure;

public class ThaumcraftStructures {
    public static class ThaumcraftStructureTypeInstances {

        public static StructureType<MoundStructure> MOUND_STRUCTURE_TYPE() {
            return RegistryStructure.SUPPLIER_MOUND.get();
        }

        public static StructureType<EldritchRingStructure> ELDRITCH_RING_STRUCTURE_TYPE() {
            return RegistryStructure.SUPPLIER_ELDRITCH_RING.get();
        }
    }

    public static class RegistryStructure{
        public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Thaumcraft.MOD_ID,Registries.STRUCTURE_TYPE);
        public static final RegistrySupplier<StructureType<MoundStructure>> SUPPLIER_MOUND = STRUCTURE_TYPES.register("mound", () ->() -> MoundStructure.CODEC);
        public static final RegistrySupplier<StructureType<EldritchRingStructure>> SUPPLIER_ELDRITCH_RING = STRUCTURE_TYPES.register("eldritch_ring", () ->() -> EldritchRingStructure.CODEC);

    }

    public static void init() {
        RegistryStructure.STRUCTURE_TYPES.register();
    }
}
