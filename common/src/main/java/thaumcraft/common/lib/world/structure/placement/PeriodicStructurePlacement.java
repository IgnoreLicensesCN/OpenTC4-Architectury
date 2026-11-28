package thaumcraft.common.lib.world.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static thaumcraft.common.lib.world.registries.ThaumcraftStructures.RegistryStructurePlacementTypes.SUPPLIER_PERIODIC;

@Getter
public class PeriodicStructurePlacement extends StructurePlacement {

    public static final Codec<PeriodicStructurePlacement> CODEC = ExtraCodecs.validate(
                    RecordCodecBuilder.mapCodec(
                            instance -> placementCodec(instance)
                                    .and(
                                            instance.group(
                                                    Codec.intRange(0, 4096).fieldOf("period_chunk_x").forGetter(PeriodicStructurePlacement::getPeriodChunkX),
                                                    Codec.intRange(0, 4096).fieldOf("period_chunk_z").forGetter(PeriodicStructurePlacement::getPeriodChunkZ)
                                            )
                                    )
                                    .apply(instance, PeriodicStructurePlacement::new)
                    ),
                    PeriodicStructurePlacement::validate
            )
            .codec();
    private static DataResult<PeriodicStructurePlacement> validate(PeriodicStructurePlacement arg) {
        return (arg.getPeriodChunkX() <= 0 || arg.getPeriodChunkZ() <= 0) ? DataResult.error(() -> "period should greater than zero") : DataResult.success(arg);
    }
    public final int periodChunkX;
    public final int periodChunkZ;
    public PeriodicStructurePlacement(Vec3i vec3i, FrequencyReductionMethod frequencyReductionMethod, float f, int i, Optional<ExclusionZone> optional,int periodChunkX,int periodChunkZ) {
        super(vec3i, frequencyReductionMethod, f, i, optional);
        this.periodChunkX = periodChunkX;
        this.periodChunkZ = periodChunkZ;
    }
    public PeriodicStructurePlacement(){
        this(16);
    }
    public PeriodicStructurePlacement(int i) {
        this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, i , Optional.empty());
    }
    public PeriodicStructurePlacement(Vec3i vec3i, FrequencyReductionMethod frequencyReductionMethod, float f, int i, Optional<ExclusionZone> optional){
        this(vec3i,frequencyReductionMethod,f,i,optional,16,16);
    }


    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int i, int j) {
        return ((i + (1L<<32)) % periodChunkX == 0)
                && ((j + (1L<<32)) % periodChunkZ == 0);
    }

    @Override
    public @NotNull StructurePlacementType<PeriodicStructurePlacement> type() {
        return SUPPLIER_PERIODIC.get();
    }
}
