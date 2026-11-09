package thaumcraft.common.tiles.crafted;

import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import com.linearity.opentc4.utils.collectionlike.coordlookups.CoordLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.blocks.crafted.ArcaneEarBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.ICoordLookupOwnerBlockEntity;

import static com.linearity.opentc4.Consts.ArcaneEarTagAccessors.SHOULD_TICK_MASKS;

public class ArcaneEarBlockEntity extends TileThaumcraft implements ICoordLookupOwnerBlockEntity<ArcaneEarBlockEntity> {

    public static final int TRIGGER_RANGE = 64;
    public static final int TRIGGER_RANGE_SQUARED = TRIGGER_RANGE * TRIGGER_RANGE;
//    public static final Map<Level, CoordLookup<ArcaneEarBlockEntity>> earsForTriggerEvent = new MapMaker().weakKeys()
//            .makeMap();
public static final CoordLookupOwnerLevel.CoordLookupTypeKey<ArcaneEarBlockEntity> COORD_LOOKUP_KEY_FOR_ARCANE_EAR = new CoordLookupOwnerLevel.CoordLookupTypeKey<>();

    public ArcaneEarBlockEntity(BlockEntityType<? extends ArcaneEarBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public @NotNull CoordLookup<ArcaneEarBlockEntity> getSelfLookupInLevel(Level level) {
        for (var lookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(COORD_LOOKUP_KEY_FOR_ARCANE_EAR)){
            return lookup;
        }
        throw new IllegalStateException("Where is my lookup?this should not happen!");
    }

    @Override
    public @NotNull ArcaneEarBlockEntity getStoreItemForLookup() {
        return this;
    }

    public ArcaneEarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ARCANE_EAR(), blockPos, blockState);
    }

    @Override
    public void setLevel(Level level) {
        registerToCubeLookup(level,this.level);
        super.setLevel(level);
    }

    @Override
    public void setRemoved() {
        unregisterFromWeakLookup(this.level);
        super.setRemoved();
    }
    public static final int TICK_DELAY = 10;
    public static final int TICK_DELAY_EVENT_MASK = 1 << TICK_DELAY;
    public static final int TICK_MASK = (1 << (TICK_DELAY + 1)) - 1;
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
