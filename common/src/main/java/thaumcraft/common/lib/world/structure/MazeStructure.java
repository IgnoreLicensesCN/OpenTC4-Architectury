package thaumcraft.common.lib.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

import static thaumcraft.common.lib.world.registries.ThaumcraftStructures.ThaumcraftStructureTypeInstances.MAZE_STRUCTURE_TYPE;

public class MazeStructure extends Structure {

    protected MazeStructure(StructureSettings settings) {
        super(settings);
    }

    public static final Codec<MazeStructure> CODEC = simpleCodec(MazeStructure::new);

    @Override
    protected @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        generationContext.chunkPos().x
        return ;
    }

    @Override
    public StructureType<?> type() {
        return MAZE_STRUCTURE_TYPE();
    }

    @Override
    public StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long l, ChunkPos chunkPos, int i, LevelHeightAccessor levelHeightAccessor, Predicate<Holder<Biome>> predicate) {
        //TODO
        return super.generate(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, l, chunkPos, i, levelHeightAccessor, predicate);
    }
}
