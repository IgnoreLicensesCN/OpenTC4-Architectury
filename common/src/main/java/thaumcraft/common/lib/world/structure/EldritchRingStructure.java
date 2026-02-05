package thaumcraft.common.lib.world.structure;

import com.linearity.opentc4.Consts;
import com.mojang.serialization.Codec;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;
import thaumcraft.common.lib.world.registries.ThaumcraftStructures;
import thaumcraft.common.tiles.TileBanner;
import thaumcraft.common.tiles.TileEldritchAltar;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static thaumcraft.api.expands.listeners.worldgen.node.NodeGenerationManager.createRandomNodeAt;
import static thaumcraft.common.lib.world.structure.MoundStructure.couldGenMountStructureViaCoords;
import static thaumcraft.common.lib.world.structure.StructureUtils.randomSourceFromChunkPosAndSeed;

public class EldritchRingStructure extends Structure {
    public static final Codec<EldritchRingStructure> CODEC =
            simpleCodec(EldritchRingStructure::new);
    private final StructureSettings settings;
    protected EldritchRingStructure(StructureSettings settings) {
        super(settings);
        this.settings = settings;
    }

    public static boolean couldGenEldritchRingStructureViaCoords(GenerationContext context){
        if (couldGenMountStructureViaCoords(context)){
            return false;
        }
        return randomSourceFromChunkPosAndSeed(context.chunkPos(),context.seed()).nextInt(66) == 0;
    }
    @Override
    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {

        RandomSource random = randomSourceFromChunkPosAndSeed(context.chunkPos(), context.seed());
        if (couldGenEldritchRingStructureViaCoords(context)) {
            return Optional.empty();
        }


        if (new MoundStructure(settings).findGenerationPoint(context).isPresent()){
            return Optional.empty();
        }

        var chunkGen = context.chunkGenerator();
        var heightAccessor = context.heightAccessor();
        var randomState = context.randomState();
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

        var width = 11 + random.nextInt(6) * 2;
        var height = 11 + random.nextInt(6) * 2;
        if (MazeHandler.mazesInRange(context.chunkPos().x, context.chunkPos().z, width, height)){
            return Optional.empty();
        }
        if (!(
                this.LocationIsValidSpawn(context, pos.offset(-3,0,-3))
                        && this.LocationIsValidSpawn(context, pos)
                        && this.LocationIsValidSpawn(context, pos.offset(3,0,0))
                        && this.LocationIsValidSpawn(context, pos.offset(3,0,3))
                        && this.LocationIsValidSpawn(context, pos.offset(0,0,3))
                )){

            return Optional.empty();
        }

        return Optional.of(new GenerationStub(
                pos, builder -> builder.addPiece(
                new EldritchRingStructure.EldritchRingStructurePiece(pos,width,height)
        )));
    }

    @Override
    public @NotNull StructureType<?> type() {
        return ThaumcraftStructures.ELDRITCH_RING_STRUCTURE_TYPE;
    }

    private static final Set<Block> VALID_SPAWN_BLOCKS = new HashSet<>(){
        {
            addAll(List.of(Blocks.STONE, Blocks.SAND, Blocks.PACKED_ICE, Blocks.GRASS, Blocks.GRAVEL, Blocks.DIRT));
        }
    };
    private static final Set<Block> PART_VALID_SPAWN_BLOCKS = new HashSet<>(){
        {
            addAll(List.of(Blocks.SNOW, Blocks.TALL_GRASS));
        }
    };
    public boolean LocationIsValidSpawn(GenerationContext context, BlockPos pos) {
        var chunkGen = context.chunkGenerator();
        var heightAccessor = context.heightAccessor();
        var randomState = context.randomState();

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
            BlockState bStateAbove = chunkGen.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState)
                    .getBlock(pos.getY()+1);
            if (!bStateAbove.isAir()){
                return false;
            }
            BlockState bState = chunkGen.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState)
                    .getBlock(pos.getY());
            if (VALID_SPAWN_BLOCKS.contains(bState.getBlock())) {
                return true;
            }
            BlockState bStateBelow = chunkGen.getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState)
                    .getBlock(pos.getY()-1);
            if (VALID_SPAWN_BLOCKS.contains(bStateBelow.getBlock()) && PART_VALID_SPAWN_BLOCKS.contains(bState.getBlock())) {
                return true;
            }

        }
        return false;
    }

    public static class EldritchRingStructurePiece extends StructurePiece {
        public static final StructurePieceType TYPE = register("eldritch_ring_piece", EldritchRingStructure.EldritchRingStructurePiece::new);

        private final int width;
        private final int height;

        public EldritchRingStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
            super(TYPE, tag);
            this.width = Consts.EldritchRingMapToMazeSize.WIDTH_ACCESSOR.readFromCompoundTag(tag);
            this.height = Consts.EldritchRingMapToMazeSize.HEIGHT_ACCESSOR.readFromCompoundTag(tag);
        }

        private static StructurePieceType register(String id, StructurePieceType type) {
            return Registry.register(
                    BuiltInRegistries.STRUCTURE_PIECE, new ResourceLocation(Thaumcraft.MOD_ID, id), type);
        }

        public EldritchRingStructurePiece(BlockPos pos,int width,int height) {
            super(
                    TYPE, 0, new BoundingBox(
                            pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 18, pos.getY() + 15,
                            pos.getZ() + 18
                    )
            );
            this.width = width;
            this.height = height;
        }


        public EldritchRingStructurePiece(StructurePieceType type, CompoundTag nbt) {
            super(type, nbt);
            this.width = Consts.EldritchRingMapToMazeSize.WIDTH_ACCESSOR.readFromCompoundTag(nbt);
            this.height = Consts.EldritchRingMapToMazeSize.HEIGHT_ACCESSOR.readFromCompoundTag(nbt);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
            Consts.EldritchRingMapToMazeSize.WIDTH_ACCESSOR.writeToCompoundTag(compoundTag,this.width);
            Consts.EldritchRingMapToMazeSize.HEIGHT_ACCESSOR.writeToCompoundTag(compoundTag,this.height);
        }

        //TODO:impl blockEldritch and blockCosmeticSolid then back here
        @Override
        public void postProcess(WorldGenLevel level,
                                StructureManager structureManager, ChunkGenerator chunkGenerator,
                                RandomSource random,
                                BoundingBox boundingBox,
                                ChunkPos chunkPos,
                                BlockPos blockPos) {
            for (int xOffset = -3; xOffset <= 3; xOffset++) {
                for (int zOffset = -3; zOffset <= 3; zOffset++) {
                    boolean xNotOnEdge = (xOffset!=-3 && xOffset!=3);
                    boolean zNotOnEdge = (zOffset!=-3 && zOffset!=3);

                    if (xNotOnEdge || zNotOnEdge) {
                        for(int yOffset = -4; yOffset < 5; ++yOffset) {
                            var solidPos = blockPos.offset(xOffset,yOffset,zOffset);
                            if (yOffset > 0) {
                                level.setBlock(solidPos,Blocks.AIR.defaultBlockState(),3);
                            }
                            BlockState bState = level.getBlockState(solidPos);
                            Block block = bState.getBlock();

                            if (random.nextInt(4) == 0) {
                                level.setBlock(solidPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                            } else {
                                level.setBlock(solidPos, ThaumcraftBlocks.OBSIDIAN_TILE.defaultBlockState(), 3);
                            }
                        }

                        //center pillar
                        if (xOffset==0 && zOffset==0) {
                            world.setBlock(x, j + 1, z, ConfigBlocks.blockEldritch, 0, 3);//TODO:Meta->Block
                            level.setBlock(blockPos.offset(xOffset,0,zOffset), ThaumcraftBlocks.OBSIDIAN_TILE.defaultBlockState(), 3);
                            int r = random.nextInt(10);
                            BlockEntity te = level.getBlockEntity(blockPos.offset(xOffset,1,zOffset));
                            if (te instanceof TileEldritchAltar) {
                                TileEldritchAltar altar = (TileEldritchAltar) te;
                                switch (r) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                        altar.setSpawner(true);
                                        altar.setSpawnType((byte)0);

                                        for(int a = 2; a < 6; ++a) {
                                            Direction dir = Direction.getOrientation(a);
                                            world.setBlock(x - dir.offsetX * 3, j + 1, z + dir.offsetZ * 3, ConfigBlocks.blockWoodenDevice, 8, 3);
                                            BlockEntity probablyBanner = world.getBlockEntity(x - dir.offsetX * 3, j + 1, z + dir.offsetZ * 3);
                                            if (probablyBanner instanceof TileBanner banner) {
                                                banner.setFacing(bannerFaceFromDirection(a));
                                            }
                                        }
                                    case 5:
                                    case 6:
                                    case 7:
                                        altar.setSpawner(true);
                                        altar.setSpawnType((byte)1);
                                    default:
                                        break;
                                }
                            }

                            world.setBlock(x, j + 3, z, ConfigBlocks.blockEldritch, 1, 3);
                            world.setBlock(x, j + 4, z, ConfigBlocks.blockEldritch, 2, 3);
                            world.setBlock(x, j + 5, z, ConfigBlocks.blockEldritch, 2, 3);
                            world.setBlock(x, j + 6, z, ConfigBlocks.blockEldritch, 2, 3);
                            world.setBlock(x, j + 7, z, ConfigBlocks.blockEldritch, 2, 3);
                        }
                        else {
                            boolean xOnEdge = (xOffset == -3 || xOffset == 3);
                            boolean zOnEdge = (zOffset == -3 || zOffset == 3);

                            boolean zIsOdd = (Math.abs(zOffset % 2) == 1);
                            boolean xIsOdd = (Math.abs(xOffset % 2) == 1);

                            //(-3,-2,-1,0,1,2,3)
                            //if xOnEdge && zOnEdge,will not satisfy "if (xNotOnEdge || zNotOnEdge)" before
                            //so at least one of isn't on edge
                            //however one is onEdge
                            //assume xOnEdge,then x=-3 or 3,z = -1 or 1(z should be odd)
                            //this flag is always satisfied here
//                            boolean notDiagonal = (Math.abs(xOffset) != Math.abs(zOffset));

                            boolean oneEdgeWhileAnotherOdd = (xOnEdge && zIsOdd) || (zOnEdge && xIsOdd);
                            if (oneEdgeWhileAnotherOdd //&& notDiagonal
                            ) {
                                level.setBlock(
                                        blockPos.offset(xOffset, 0, zOffset),
                                        ThaumcraftBlocks.OBSIDIAN_TILE.defaultBlockState(), 3
                                );
                                world.setBlock(x, j + 1, z, ConfigBlocks.blockEldritch, 3, 3);
                            }
                        }
                    }
                }
            }

            createRandomNodeAt(level, blockPos.above(2), random, false, true, false);
            Thread t = new Thread(new MazeThread(chunkPos.x, chunkPos.z, this.width, this.height, random.nextLong()));
            t.start();
        }

    }
}
