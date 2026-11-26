package thaumcraft.common.lib.world.structure;

import com.linearity.opentc4.Consts;
import com.mojang.serialization.Codec;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.world.dim.MazeGenerator;
import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.Optional;
import java.util.function.Predicate;

import static thaumcraft.common.lib.world.dim.cell.CellState.cellHasConnectedDirection;
import static thaumcraft.common.lib.world.registries.ThaumcraftStructures.ThaumcraftStructureTypeInstances.MAZE_STRUCTURE_PIECE;
import static thaumcraft.common.lib.world.registries.ThaumcraftStructures.ThaumcraftStructureTypeInstances.MAZE_STRUCTURE_TYPE;

public class MazeStructure extends Structure {
    /*15 + level.random.nextInt(8) * 2*/
    public static final int MAZE_MAX_CHUNK_SIZE = 15 + 14;
    public static final int MAZE_MIN_CHUNK_SIZE = 15;

    public static final long OFFSET_BACK = (1L<<32) / MAZE_MAX_CHUNK_SIZE;

    public static final int MAZE_AT_Y = 50;

    protected MazeStructure(StructureSettings settings) {
        super(settings);
    }

    public static final Codec<MazeStructure> CODEC = simpleCodec(MazeStructure::new);

    @Override
    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        var random = generationContext.random();
        var x = generationContext.chunkPos().x;
        var z = generationContext.chunkPos().z;
        var xLong = ((long)x) + (1L<<32);
        var zLong = ((long)z) + (1L<<32);
        var xScaled = xLong / MAZE_MAX_CHUNK_SIZE;
        var zScaled = zLong / MAZE_MIN_CHUNK_SIZE;
        var xPrepareBack = xScaled - OFFSET_BACK;
        var zPrepareBack = zScaled - OFFSET_BACK;
        var xScaledBack = (int)(xPrepareBack * MAZE_MAX_CHUNK_SIZE);
        var zScaledBack = (int)(zPrepareBack * MAZE_MIN_CHUNK_SIZE);

        var pickedPos = new BlockPos(xScaledBack * 16, MAZE_AT_Y, zScaledBack * 16);

        return Optional.of(new GenerationStub(
                pickedPos, builder -> builder.addPiece(
                new MazeStructurePiece(
                        pickedPos,
                        MAZE_MIN_CHUNK_SIZE + ((random.nextInt(MAZE_MAX_CHUNK_SIZE - MAZE_MIN_CHUNK_SIZE)/2)*2),
                        MAZE_MIN_CHUNK_SIZE + ((random.nextInt(MAZE_MAX_CHUNK_SIZE - MAZE_MIN_CHUNK_SIZE)/2)*2)
                )
        )));
    }

    @Override
    public @NotNull StructureType<?> type() {
        return MAZE_STRUCTURE_TYPE();
    }

    @Override
    public StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long l, ChunkPos chunkPos, int i, LevelHeightAccessor levelHeightAccessor, Predicate<Holder<Biome>> predicate) {
        //TODO
        return super.generate(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, l, chunkPos, i, levelHeightAccessor, predicate);
    }


    public static class MazeStructurePiece extends StructurePiece {
//        public static final StructurePieceType TYPE = register("maze_piece", EldritchRingStructure.EldritchRingStructurePiece::new);

        private final int width;
        private final int height;

        public MazeStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
            super(MAZE_STRUCTURE_PIECE(), tag);
            this.width = Consts.EldritchRingMapToMazeSize.WIDTH_ACCESSOR.readIntFromCompoundTag(tag);
            this.height = Consts.EldritchRingMapToMazeSize.HEIGHT_ACCESSOR.readIntFromCompoundTag(tag);
        }

        public MazeStructurePiece(BlockPos pos,int width,int height) {
            super(
                    MAZE_STRUCTURE_PIECE(), 0, new BoundingBox(
                            pos.getX()-1, pos.getY() - 1, pos.getZ()-1, pos.getX() + width, pos.getY() + height,
                            pos.getZ() + width
                    )
            );
            this.width = width;
            this.height = height;
        }


        public MazeStructurePiece(StructurePieceType type, CompoundTag nbt) {
            super(type, nbt);
            this.width = Consts.EldritchRingMapToMazeSize.WIDTH_ACCESSOR.readIntFromCompoundTag(nbt);
            this.height = Consts.EldritchRingMapToMazeSize.HEIGHT_ACCESSOR.readIntFromCompoundTag(nbt);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
            Consts.EldritchRingMapToMazeSize.WIDTH_ACCESSOR.writeIntToCompoundTag(compoundTag,this.width);
            Consts.EldritchRingMapToMazeSize.HEIGHT_ACCESSOR.writeIntToCompoundTag(compoundTag,this.height);
        }

        @Override
        public void postProcess(
                WorldGenLevel level,
                StructureManager structureManager,
                ChunkGenerator chunkGenerator,
                RandomSource random,
                BoundingBox boundingBox,
                ChunkPos chunkPos,
                BlockPos centerXZMinY
        ) {
            var mazeGen = new MazeGenerator(width,height,random.nextLong());
            while (true){
                if (mazeGen.generate()){
                    break;
                }
            }
            var cellGrid = mazeGen.cellGrid;
            for (int z=0;z<cellGrid.length;z++){
                for (int x=0;x<cellGrid[z].length;x++){
                    placeGrid(cellGrid[z][x],x,z,centerXZMinY,width,height,boundingBox);
                }
            }
        }

        protected void placeGrid(CellState cellState, int gridX, int gridZ, BlockPos centerXZMinY, int width, int height, BoundingBox boundingBox) {
            if (cellState == null){return;}
            if (!cellHasConnectedDirection(cellState)){return;}

        }

    }
}
