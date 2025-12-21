package thaumcraft.common.lib.world.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.world.structure.MoundStructure;

public class ThaumcraftStructures {
    public static final StructureType<MoundStructure> MOUND_STRUCTURE_TYPE = RegistryStructure.SUPPLIER_MOUND.get();
    public static class RegistryStructure{
        public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Thaumcraft.MOD_ID,Registries.STRUCTURE_TYPE);
        public static final RegistrySupplier<StructureType<MoundStructure>> SUPPLIER_MOUND = STRUCTURE_TYPES.register("mound", () ->() -> MoundStructure.CODEC);
        static {
            STRUCTURE_TYPES.register();
        }
    }

    public static void init() {
    }
}
