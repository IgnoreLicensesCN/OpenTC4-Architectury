package thaumcraft.common.tiles.crafted.pipes;

import com.google.common.collect.MapMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Map;

import static com.linearity.opentc4.Consts.EssentiaTubeBlockEntityTagAccessors.*;

public class EssentiaTubeBlockEntity extends TileThaumcraft
        implements
        IEssentiaTransportInBlockEntity,
        IEssentiaTransportOutBlockEntity {
    private @NotNull("is and should be empty if there's no aspect") Aspect owningAspect = Aspect.EMPTY;
    private byte openSidesMask = ((1<<6) - 1);
    private @NotNull Aspect suctionType = Aspect.EMPTY;
    private int suction = 0;
    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.owningAspect = OWNING_ASPECT.readFromCompoundTag(compoundTag);
        this.openSidesMask = OPEN_SIDES.readByteFromCompoundTag(compoundTag);
        this.suctionType = SUCTION_TYPE.readFromCompoundTag(compoundTag);
        this.suction = SUCTION_AMOUNT.readIntFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        OWNING_ASPECT.writeToCompoundTag(compoundTag, this.owningAspect);
        OPEN_SIDES.writeByteToCompoundTag(compoundTag, this.openSidesMask);
        SUCTION_TYPE.writeToCompoundTag(compoundTag, this.suctionType);
        SUCTION_AMOUNT.writeIntToCompoundTag(compoundTag, this.suction);
    }
    public EssentiaTubeBlockEntity(BlockEntityType<? extends EssentiaTubeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaTubeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_TUBE, blockPos, blockState);
    }


    public void serverTick(){
        if (this.level == null){return;}

    }
    public void clientTick(){

    }
    public static class ClientTickContext{

        private static final Map<EssentiaTubeBlockEntity,ClientTickContext> contexts = new MapMaker().weakKeys().makeMap();
        public static void tickBE(EssentiaTubeBlockEntity be){
            var ctx = contexts.computeIfAbsent(be,(_ignored) -> new ClientTickContext());
        }
    }

    @Override
    public boolean isConnectable(Direction face) {
        return getOpenSideState(face);
    }

    @Override
    public boolean canInputFrom(Direction face) {
        return isConnectable(face);
    }

    @Override
    public boolean canOutputTo(Direction face) {
        return isConnectable(face);
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }

    protected final void changeOpenSideState(Direction face) {
        openSidesMask ^= (byte) (1 << face.ordinal());
    }
    protected final boolean getOpenSideState(Direction face) {
        return (openSidesMask & (1 << face.ordinal())) != 0;
    }

    protected void calculateSuction(
            @NotNull("empty -> any") Aspect filter,
            boolean restrict,
            @Nullable("null -> any") Direction limitedFacingToAnotherBE
    ) {
        if (this.level == null){return;}
        this.suction = 0;
        this.suctionType = Aspects.EMPTY;
//        Direction loc = null;
        if (limitedFacingToAnotherBE == null) {
            for (var direction : Direction.values()) {
                calculateSuctionForFacing(filter,restrict,direction);
            }
        }
        else if (this.isConnectable(limitedFacingToAnotherBE)) {
            calculateSuctionForFacing(filter,restrict,limitedFacingToAnotherBE);
        }
    }
    protected void calculateSuctionForFacing(
            @NotNull("empty -> any") Aspect filter,
            boolean restrict,
            Direction toAnotherBEDirection){
        if (this.level == null){return;}
        var facingFromAnotherToSelf = toAnotherBEDirection.getOpposite();
        var anotherBE = this.level.getBlockEntity(getBlockPos().relative(toAnotherBEDirection));
        if (!(anotherBE instanceof IEssentiaTransportInBlockEntity outBE)){
            return;
        }
        var outSuctionType = outBE.getSuctionType(facingFromAnotherToSelf);
        var inSuctionType = getSuctionType(toAnotherBEDirection);
        boolean noFilter = filter.isEmpty();
        boolean noOutType = outSuctionType.isEmpty();
        boolean noInType = inSuctionType.isEmpty();
        boolean outSuctionTypeMatchesFilter = (
                noFilter
                || noOutType
                || outSuctionType == filter
        );
        boolean canAcceptWhenWithoutFilter =noOutType
                || inSuctionType == outSuctionType
//                            || this.getEssentiaAmount(toAnotherBEDirection) <= 0
                        ;
        boolean canAcceptWhenWithFilter =
                        noInType || noOutType
                                || inSuctionType == outSuctionType
//                            || this.getEssentiaAmount(toAnotherBEDirection) <= 0
        ;
        if (
                outSuctionTypeMatchesFilter
                        && (!noFilter || canAcceptWhenWithoutFilter)
                        && (noFilter || canAcceptWhenWithFilter)
        ) {
            int suck = outBE.getSuctionAmount(facingFromAnotherToSelf);
            if (suck > 0 && suck > this.suction + 1) {
                Aspect st = outSuctionType;
                if (st.isEmpty()) {
                    st = filter;
                }

                this.setSuction(st, restrict ? suck / 2 : suck - 1);
            }
        }
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        if (!isConnectable(face)){return 0;}
        return getEssentiaType(face).isEmpty() ? 0 : 1;
    }

    @Override
    public @NotNull Aspect getEssentiaType(Direction face) {
        if (!isConnectable(face)){return Aspects.EMPTY;}
        return this.owningAspect;
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {
        this.suctionType = aspect;
        this.suction = amount;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, Direction outputToDirection) {
        if (this.canOutputTo(outputToDirection) && this.owningAspect == aspect && amount > 0) {
            this.owningAspect = Aspects.EMPTY;
            markDirtyAndUpdateSelf();
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (this.canInputFrom(fromDirection) && this.owningAspect.isEmpty() && amount > 0) {
            this.owningAspect = aspect;
            markDirtyAndUpdateSelf();
            return 1;
        } else {
            return 0;
        }
    }


}
