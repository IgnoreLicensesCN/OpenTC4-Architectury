package thaumcraft.common.tiles.crafted;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.blocks.crafted.ArcaneEarBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Map;

import static com.linearity.opentc4.Consts.ArcaneEarTagAccessors.SHOULD_TICK_MASKS;

public class ArcaneEarBlockEntity extends TileThaumcraft {

    public static final int TRIGGER_RANGE = 64;
    public static final int TRIGGER_RANGE_SQUARED = TRIGGER_RANGE * TRIGGER_RANGE;
    public static final Map<Level, CubeChunkedWeakLookups<ArcaneEarBlockEntity>> earsForTriggerEvent = new MapMaker().weakKeys()
            .makeMap();

    public ArcaneEarBlockEntity(BlockEntityType<? extends ArcaneEarBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected void storeToLookup(Level level) {
        if (level != this.level) {
            var pos = getBlockPos();
            if (this.level != null) {
                var lookup = earsForTriggerEvent.get(this.level);
                if (lookup != null) {
                    lookup.remove(pos, this);
                }
            }
            if (level != null) {
                earsForTriggerEvent.computeIfAbsent(
                                level,
                                _ignored -> new CubeChunkedWeakLookups<>((byte) 6)
                        )
                        .store(pos, this);
            }
        }
    }

    public ArcaneEarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_EAR, blockPos, blockState);
    }

    @Override
    public void setLevel(Level level) {
        storeToLookup(level);
        super.setLevel(level);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null) {
            var lookup = earsForTriggerEvent.get(this.level);
            if (lookup != null) {
                lookup.remove(getBlockPos(), this);
            }
        }
    }

    public static final int TICK_DELAY = 10;
    public static final int TICK_DELAY_EVENT_MASK = 1 << TICK_DELAY;
    public static final int TICK_MASK = (1 << (TICK_DELAY + 1)) - 1;
    public static final int ARCANE_EAR_LISTEN_DISTANCE = 64;
    private int shouldProvideSignalTick = 0;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        shouldProvideSignalTick = SHOULD_TICK_MASKS.readIntFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        SHOULD_TICK_MASKS.writeIntToCompoundTag(compoundTag, shouldProvideSignalTick);
    }

    public void serverTick() {
        shouldProvideSignalTick = shouldProvideSignalTick >> 1;
        boolean shouldProvideSignal = (shouldProvideSignalTick & 1) == 1;

        shouldProvideSignalTick &= TICK_MASK;

        checkSignalState(shouldProvideSignal);
    }

    protected void checkSignalState(boolean shouldProvideSignal) {
        var currentState = getBlockState();
        if (shouldProvideSignal != currentState.getValue(ArcaneEarBlock.POWERED)) {
            currentState.setValue(ArcaneEarBlock.POWERED, shouldProvideSignal);
            setBlockStateAndUpdate(currentState);
        }
    }

    //args from noteBlock playNote
    public void onNoteBlockEvent(Entity entity, BlockState state, Level level, BlockPos pos) {
        if (level != this.level) {
            return;
        }
        if (pos.distSqr(getBlockPos()) <= TRIGGER_RANGE_SQUARED) {
            var selfState = getBlockState();
            if (state.getValue(NoteBlock.INSTRUMENT) == selfState.getValue(NoteBlock.INSTRUMENT)
                    && state.getValue(NoteBlock.NOTE)
                    .equals(selfState.getValue(NoteBlock.NOTE))
            ) {
                shouldProvideSignalTick |= TICK_DELAY_EVENT_MASK;
            }
        }
    }
}
