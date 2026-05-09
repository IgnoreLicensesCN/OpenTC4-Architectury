package thaumcraft.common.tiles.crafted.essentiabe.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceNozzleBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class AdvancedAlchemicalFurnaceNozzleBlockEntity extends TileThaumcraft
    implements
        IAspectOutBlockEntity<Aspect>,
        IEssentiaTransportOutBlockEntity,
        IValueContainerBasedComparatorSignalProviderBlockEntity {

    public AdvancedAlchemicalFurnaceNozzleBlockEntity(BlockEntityType<? extends AdvancedAlchemicalFurnaceNozzleBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public AdvancedAlchemicalFurnaceNozzleBlockEntity(BlockPos pos, BlockState state) {
        this(ThaumcraftBlockEntities.ADVANCED_ALCHEMICAL_FURNACE_NOZZLE,pos,state);
    }
    public Direction getFacing() {
        return this.getBlockState().getValue(AdvancedAlchemicalFurnaceNozzleBlock.FACING);
    }

    private @Nullable AdvancedAlchemicalFurnaceBlockEntity baseBECurrentTick = null;
    private long checkedBaseBEAtGameTime = Long.MIN_VALUE;
    public @Nullable AdvancedAlchemicalFurnaceBlockEntity getBaseBE(){
        if (this.level == null) {
            return null;
        }
        var currentGT = this.level.getGameTime();
        if (checkedBaseBEAtGameTime != currentGT) {
            checkedBaseBEAtGameTime = currentGT;
            baseBECurrentTick = null;
            if ((this.level.getBlockEntity(getBlockPos().relative(getFacing().getOpposite()))
                    instanceof AdvancedAlchemicalFurnaceBlockEntity furnace)){
                baseBECurrentTick = furnace;
            }
        }
        return baseBECurrentTick;
    }

    @Override
    public @UnmodifiableView @NotNull AspectList<Aspect> getAspects() {
        var base = getBaseBE();
        if (base != null){
            return base.aspectsView;
        }
        return UnmodifiableAspectList.EMPTY;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        var base = getBaseBE();
        if (base == null) {
            return false;
        }
        if (base.aspects.getAmount(aspect) >= amount) {
            base.aspects.reduceAndRemoveIfNotPositive(aspect, amount);
            base.markDirtyAndUpdateSelf();
            return true;
        }
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        var base = getBaseBE();
        if (base == null) {
            return 0;
        }
        return base.aspectsView.getOrDefault(aspect,0);
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return containerContains(aspect) >= amount;
    }

    @Override
    public int currentValueForComparatorSignal() {
        var base = getBaseBE();
        if (base == null) {
            return 0;
        }
        return base.aspects.visSize();
    }

    @Override
    public int comparatorSignalCapacity() {
        var base = getBaseBE();
        if (base == null) {
            return 0;
        }
        return base.getVisCapacity();
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return face == getFacing();
    }

    @Override
    public boolean canOutputTo(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, @NotNull Direction outputToDirection) {
        return this.canOutputTo(outputToDirection) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }

    @Override
    public @NotNull("null -> empty") Aspect getEssentiaType(@NotNull Direction face) {
        if (canOutputTo(face)) {
            var base = getBaseBE();
            if (base != null) {
                if (!base.aspectsView.isEmpty()){
                    return base.aspectsView.getFirstAspect();
                }
            }
        }
        return Aspects.EMPTY;
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        var base = getBaseBE();
        if (base == null) {
            return 0;
        }
        var type = getEssentiaType(face);
        if (type.isEmpty()) {
            return 0;
        }
        return base.aspectsView.getAmount(getEssentiaType(face));
    }
}
