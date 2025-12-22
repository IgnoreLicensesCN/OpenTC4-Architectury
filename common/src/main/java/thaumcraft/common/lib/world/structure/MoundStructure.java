package thaumcraft.common.lib.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Optional;
import java.util.Set;

import static thaumcraft.api.expands.worldgen.node.NodeGenerationManager.createRandomNodeAt;
import static thaumcraft.common.lib.world.registries.ThaumcraftStructures.MOUND_STRUCTURE_TYPE;
import static thaumcraft.common.lib.world.structure.StructureUtils.randomSourceFromChunkPosAndSeed;

public class MoundStructure extends Structure {

    protected MoundStructure(StructureSettings settings) {
        super(settings);
    }

    public static final Codec<MoundStructure> CODEC =
            simpleCodec(MoundStructure::new);

    public static boolean couldGenMountStructureViaCoords(GenerationContext context){
        return randomSourceFromChunkPosAndSeed(context.chunkPos(),context.seed()).nextInt(150) == 0;
    }
    @Override
    public @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        var chunkGen = context.chunkGenerator();
        var heightAccessor = context.heightAccessor();
        var randomState = context.randomState();
        var random = randomSourceFromChunkPosAndSeed(context.chunkPos(),context.seed());
        int x = context.chunkPos()
                .getMinBlockX() + random.nextInt(16);
        int z = context.chunkPos()
                .getMinBlockZ() + random.nextInt(16);
        int y = chunkGen.getFirstOccupiedHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                heightAccessor,
                randomState
        );
        var pos = new BlockPos(x, y, z);
        Holder<Biome> biome = context.biomeSource()
                .getNoiseBiome(
                        QuartPos.fromBlock(pos.getX()),
                        QuartPos.fromBlock(pos.getY()),
                        QuartPos.fromBlock(pos.getZ()),
                        context.randomState()
                                .sampler()
                );

        if (!biome.is(BiomeTags.IS_OVERWORLD)) {
            return Optional.empty();
        }
        if (random.nextInt(150) != 0) {
            return Optional.empty();
        }
        int randPosY = chunkGen.getFirstOccupiedHeight(
                pos.getX(),
                pos.getZ(),
                Heightmap.Types.WORLD_SURFACE_WG,
                heightAccessor,
                randomState
        ) - 9;
        if (randPosY >= context.heightAccessor()
                .getMaxBuildHeight()) {
            return Optional.empty();
        }
        if (!(LocationIsValidSpawn(chunkGen, heightAccessor, randomState, pos.offset(9, 9, 9))
                && LocationIsValidSpawn(chunkGen, heightAccessor, randomState, pos.above(9))//i, j + 9, k)
                && LocationIsValidSpawn(chunkGen, heightAccessor, randomState, pos.offset(18, 9, 0))
                && LocationIsValidSpawn(chunkGen, heightAccessor, randomState, pos.offset(18, 9, 18))
                && LocationIsValidSpawn(chunkGen, heightAccessor, randomState, pos.offset(0, 9, 18)))) {
            return Optional.empty();
        }

        return Optional.of(
                new GenerationStub(
                        pos, builder -> builder.addPiece(
                        new MoundStructurePiece(pos)
                )
                )
        );

    }

    @Override
    public @NotNull StructureType<?> type() {
        return MOUND_STRUCTURE_TYPE;
    }

    private static final Set<Block> VALID_SPAWN_BLOCKS = Set.of(
            Blocks.STONE,
            Blocks.GRASS_BLOCK,
            Blocks.DIRT
    );
    private static final Set<Block> PART_VALID_SPAWN_BLOCKS = Set.of(
            Blocks.SNOW,
            Blocks.TALL_GRASS
    );

    public static boolean LocationIsValidSpawn(ChunkGenerator chunkGen, LevelHeightAccessor heightAccessor, RandomState randomState, BlockPos pos) {
//        int distanceToAir = 0;
//
//
//        for(Block checkID = world.getBlock(i, j, k);
//            checkID != Blocks.air;
//            checkID = world.getBlock(i, j + distanceToAir, k)) {
//            ++distanceToAir;
//        }

        int y = chunkGen.getFirstOccupiedHeight(
                pos.getX(),
                pos.getZ(),
                Heightmap.Types.WORLD_SURFACE_WG,
                heightAccessor,
                randomState
        );

        int distanceToAir = y - pos.getY();

        if (distanceToAir <= 2) {
            pos = pos.above(distanceToAir - 1);
//            j += distanceToAir - 1;
            BlockState bStateAbove = chunkGen.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState)
                    .getBlock(y);
//            Block blockID = world.getBlock(i, j, k);
//            Block blockIDAbove = world.getBlock(i, j + 1, k);
//            Block blockIDBelow = world.getBlock(i, j - 1, k);

            if (!bStateAbove.isAir()) {
                return false;
            }
            BlockState bState = chunkGen.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState)
                    .getBlock(y - 1);
            var block = bState.getBlock();
            if (VALID_SPAWN_BLOCKS.contains(block)) {
                return true;
            }
            BlockState bStateBelow = chunkGen.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState)
                    .getBlock(y - 2);
            return (PART_VALID_SPAWN_BLOCKS.contains(block))
                    && VALID_SPAWN_BLOCKS.contains(bStateBelow.getBlock());
        }
        return false;
    }

    public static class MoundStructurePiece extends StructurePiece {

        public static final StructurePieceType TYPE = register("mound_piece", MoundStructurePiece::new);

        public MoundStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
            super(TYPE, tag);
        }

        private static StructurePieceType register(String id, StructurePieceType type) {
            return Registry.register(
                    BuiltInRegistries.STRUCTURE_PIECE, new ResourceLocation(Thaumcraft.MOD_ID, id), type);
        }

        public MoundStructurePiece(BlockPos pos) {
            super(
                    TYPE, 0, new BoundingBox(
                            pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 18, pos.getY() + 15,
                            pos.getZ() + 18
                    )
            ); // 示例 16x16x16

        }

        public MoundStructurePiece(StructurePieceType type, CompoundTag nbt) {
            super(type, nbt);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {

        }

        // 必须实现
        @Override
        public void postProcess(
                WorldGenLevel level,
                StructureManager structureManager,
                ChunkGenerator chunkGenerator,
                RandomSource randomSource,
                BoundingBox boundingBox,
                ChunkPos chunkPos,
                BlockPos startPos
        ) {
            MoundStructureSetBlocks.setBlocks(level, startPos);

            {
                BlockPos chestPos = startPos.offset(10, 1, 9);

                float rr = randomSource.nextFloat();
                int md = rr < 0.1F ? 2 : (rr < 0.33F ? 1 : 0);
                //TODO:separate md
                level.setBlock(
                        startPos.offset(9, 1, 7), randomSource.nextFloat() < 0.3F
                                ? ConfigBlocks.blockLootCrate : ConfigBlocks.blockLootUrn, md, 3
                );
                rr = randomSource.nextFloat();
                md = rr < 0.1F ? 2 : (rr < 0.33F ? 1 : 0);
                level.setBlock(
                        startPos.offset(9, 1, 11), randomSource.nextFloat() < 0.3F
                                ? ConfigBlocks.blockLootCrate : ConfigBlocks.blockLootUrn, md, 3
                );
                if (randomSource.nextInt(3) == 0) {
                    level.setBlock(chestPos, Blocks.TRAPPED_CHEST.defaultBlockState(), 3);
                    level.setBlock(chestPos.below(2), Blocks.TNT.defaultBlockState(), 3);
                } else {
                    level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);
                }

                level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);
                var blockEntity = level.getBlockEntity(chestPos);
                if (blockEntity instanceof ChestBlockEntity chest) {
                    ResourceLocation lootTable = BuiltInLootTables.SIMPLE_DUNGEON; // 或你自己的
                    chest.setLootTable(lootTable, randomSource.nextLong());
                }

            }

            {
                var skeletonSpawnerPos = startPos.offset(4, 5, 4);
                level.setBlock(skeletonSpawnerPos, Blocks.SPAWNER.defaultBlockState(), 3);
                var skeletonSpawnerBlockEntity = level.getBlockEntity(skeletonSpawnerPos);
                if (skeletonSpawnerBlockEntity instanceof SpawnerBlockEntity spawner) {
                    spawner.setEntityId(EntityType.SKELETON, randomSource);
                }
            }
            {
                var zombieSpawnerPos = startPos.offset(4, 5, 14);
                level.setBlock(zombieSpawnerPos, Blocks.SPAWNER.defaultBlockState(), 3);
                var zombieSpawnerBlockEntity = level.getBlockEntity(zombieSpawnerPos);
                if (zombieSpawnerBlockEntity instanceof SpawnerBlockEntity spawner) {
                    spawner.setEntityId(EntityType.ZOMBIE, randomSource);
                }
            }
            {
                int value = randomSource.nextInt(200) + 400;//anazor forgot something?
                createRandomNodeAt(
                        level,
                        startPos.offset(9, 8, 9),
                        randomSource,
                        false,
                        true,
                        false
                );
            }
        }
    }
}
