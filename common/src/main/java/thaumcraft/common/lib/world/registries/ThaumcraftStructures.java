package thaumcraft.common.lib.world.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.world.structure.EldritchRingStructure;
import thaumcraft.common.lib.world.structure.MazeStructure;
import thaumcraft.common.lib.world.structure.MoundStructure;

import static thaumcraft.common.lib.world.registries.ThaumcraftStructures.RegistryStructure.STRUCTURE_PIECES;

public class ThaumcraftStructures {
    public static class ThaumcraftStructureTypeInstances {
        public static StructureType<MazeStructure> MAZE_STRUCTURE_TYPE() {
            return RegistryStructure.SUPPLIER_MAZE.get();
        }
        public static StructurePieceType MAZE_STRUCTURE_PIECE() {
            return RegistryStructure.SUPPLIER_MAZE_PIECE.get();
        }

        public static StructureType<MoundStructure> MOUND_STRUCTURE_TYPE() {
            return RegistryStructure.SUPPLIER_MOUND.get();
        }

        public static StructurePieceType MOUND_STRUCTURE_PIECE() {
            return RegistryStructure.SUPPLIER_MOUND_PIECE.get();
        }

        public static StructureType<EldritchRingStructure> ELDRITCH_RING_STRUCTURE_TYPE() {
            return RegistryStructure.SUPPLIER_ELDRITCH_RING.get();
        }
        public static StructurePieceType ELDRITCH_RING_STRUCTURE_PIECE() {
            return RegistryStructure.SUPPLIER_ELDRITCH_RING_PIECE.get();
        }

    }

    public static class RegistryStructure{
        public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Thaumcraft.MOD_ID,Registries.STRUCTURE_TYPE);
        public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Thaumcraft.MOD_ID,Registries.STRUCTURE_PIECE);
        public static final RegistrySupplier<StructureType<MoundStructure>> SUPPLIER_MOUND =
                STRUCTURE_TYPES.register(
                "mound",
                () ->() -> MoundStructure.CODEC
        );
        public static final RegistrySupplier<StructurePieceType> SUPPLIER_MOUND_PIECE =
                STRUCTURE_PIECES.register(
                        "mound_piece",
                        () -> MoundStructure.MoundStructurePiece::new
                );
        public static final RegistrySupplier<StructureType<EldritchRingStructure>> SUPPLIER_ELDRITCH_RING =
                STRUCTURE_TYPES.register(
                        "eldritch_ring",
                        () ->() -> EldritchRingStructure.CODEC
                );
        public static final RegistrySupplier<StructurePieceType> SUPPLIER_ELDRITCH_RING_PIECE =
                STRUCTURE_PIECES.register(
                        "eldritch_ring_piece",
                        () -> EldritchRingStructure.EldritchRingStructurePiece::new
                );
        public static final RegistrySupplier<StructureType<MazeStructure>> SUPPLIER_MAZE =
                STRUCTURE_TYPES.register(
                        "maze",
                        () ->() -> MazeStructure.CODEC
                );
        public static final RegistrySupplier<StructurePieceType> SUPPLIER_MAZE_PIECE =
                STRUCTURE_PIECES.register(
                        "maze_piece",
                        () -> MazeStructure.MazeStructurePiece::new
                );

    }

    public static void init() {
        RegistryStructure.STRUCTURE_TYPES.register();
        STRUCTURE_PIECES.register();
    }
}
