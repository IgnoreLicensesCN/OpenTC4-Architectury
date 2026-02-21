package thaumcraft.common.tiles.crafted.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceNozzleBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class AdvancedAlchemicalFurnaceNozzleBlockEntity extends TileThaumcraft
    implements
        IAspectOutBlockEntity<Aspect>,
        IEssentiaTransportOutBlockEntity,
        IEssentiaComparatorSignalProviderBlockEntity {

    public AdvancedAlchemicalFurnaceNozzleBlockEntity(BlockEntityType<? extends AdvancedAlchemicalFurnaceNozzleBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public AdvancedAlchemicalFurnaceNozzleBlockEntity(BlockPos pos, BlockState state) {
        this(ThaumcraftBlockEntities.ADVANCED_ALCHEMICAL_FURNACE_NOZZLE,pos,state);
    }
    public Direction getFacing() {
        return this.getBlockState().getValue(AdvancedAlchemicalFurnaceNozzleBlock.FACING);
    }

    public @Nullable AdvancedAlchemicalFurnaceBlockEntity getBaseBE(){
        if (this.level == null) {
            return null;
        }
        if (!(this.level.getBlockEntity(getBlockPos().relative(getFacing().getOpposite())) 
                instanceof AdvancedAlchemicalFurnaceBlockEntity furnace)) {
            return null;
        }
        return furnace;
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
    public int getRedstoneSignalCalculationVisOwning() {
        var base = getBaseBE();
        if (base == null) {
            return 0;
        }
        return base.aspects.visSize();
    }

    @Override
    public int getRedstoneSignalCalculationVisCapacity() {
        var base = getBaseBE();
        if (base == null) {
            return 0;
        }
        return base.getVisCapacity();
    }

    @Override
    public boolean isConnectable(Direction face) {
        return face == getFacing();
    }

    @Override
    public boolean canOutputTo(Direction face) {
        return isConnectable(face);
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, Direction facing) {
        return this.canOutputTo(facing) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }

    @Override
    public @NotNull("null -> empty") Aspect getEssentiaType(Direction face) {
        if (canOutputTo(face)) {
            var base = getBaseBE();
            if (base != null) {
                if (!base.aspectsView.isEmpty()){
                    return base.aspectsView.keySet().iterator().next();
                }
            }
        }
        return Aspects.EMPTY;
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        if (canOutputTo(face)) {
            var base = getBaseBE();
            if (base != null) {
                if (!base.aspectsView.isEmpty()){
                    return base.aspectsView.entrySet().iterator().next().getValue();
                }
            }
        }
        return 0;
    }
}
