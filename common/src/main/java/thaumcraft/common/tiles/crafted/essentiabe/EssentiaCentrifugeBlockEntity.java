package thaumcraft.common.tiles.crafted.essentiabe;

import com.linearity.opentc4.annotations.forvalue.DegreeValue;
import com.linearity.opentc4.mixinaccessors.clientbe.EssentiaCentrifugeBlockEntityClientAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableSingleAspectListFromSupplier;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.EssentiaCentrifugeBlockEntityTagAccessors.ASPECT_IN;
import static com.linearity.opentc4.Consts.EssentiaCentrifugeBlockEntityTagAccessors.ASPECT_OUT;

public class EssentiaCentrifugeBlockEntity extends TileThaumcraft 
        implements 
        IEssentiaTransportInBlockEntity,
        IEssentiaTransportOutBlockEntity,
        IAspectDisplayBlockEntity<Aspect>,
        IValueContainerBasedComparatorSignalProviderBlockEntity,
        UnmodifiableSingleAspectListFromSupplier.SingleAspectAndAmountSupplier<Aspect>
{
    public EssentiaCentrifugeBlockEntity(BlockEntityType<? extends EssentiaCentrifugeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaCentrifugeBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_CENTRIFUGE, blockPos, blockState);
    }
    private final UnmodifiableSingleAspectListFromSupplier<Aspect> aspToDisplay = new UnmodifiableSingleAspectListFromSupplier<>(this);

    @Override
    public Aspect getAspectAsSupplier() {
        return this.aspectOut;
    }

    @Override
    public int getAmountAsSupplier() {
        return this.aspectOut.isEmpty()?0:1;
    }
    private @NotNull("null -> empty") CompoundAspect aspectIn = Aspects.EMPTY_COMPOUND;
    private @NotNull("null -> empty") Aspect aspectOut = Aspects.EMPTY;
    private int tickCount = System.identityHashCode(this) & 3;
    private int processingTicks = 0;

    @Override
    public void readCustomNBT(CompoundTag tag) {
        super.readCustomNBT(tag);
        aspectIn = ASPECT_IN.readFromCompoundTag(tag);
        aspectOut = ASPECT_OUT.readFromCompoundTag(tag);
    }

    @Override
    public void writeCustomNBT(CompoundTag tag) {
        super.writeCustomNBT(tag);
        ASPECT_IN.writeToCompoundTag(tag, aspectIn);
        ASPECT_OUT.writeToCompoundTag(tag, aspectOut);
    }

    public void serverTick(){
        if (!this.hasNeighbourSignal()) {
            tickCount += 1;
            boolean hasNoAspectOut = this.aspectOut.isEmpty();
            boolean hasNoAspectIn = this.aspectIn.isEmpty();
            if (hasNoAspectOut && hasNoAspectIn && (tickCount & 3) == 0) {
                this.drawEssentia();
            }

            if (this.processingTicks > 0) {
                this.processingTicks -= 1;
            }

            if (hasNoAspectOut && !hasNoAspectIn && this.processingTicks == 0) {
                this.processEssentia();
            }
        }
    }


    void processEssentia() {
        if (this.level == null || this.aspectIn.isEmpty()) {
            return;
        }
        var comps = this.aspectIn.components;
        if (this.level.random.nextBoolean()) {
            this.aspectOut = comps.aspectA();
        }else {
            this.aspectOut = comps.aspectB();
        }
        this.aspectIn = Aspects.EMPTY_COMPOUND;
        markDirtyAndUpdateSelf();
    }
    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return canOutputTo(face) || canInputFrom(face);
    }

    @Override
    public boolean canOutputTo(@NotNull Direction face) {
        return face == OUTPUT_DIRECTION;
    }

    @Override
    public boolean canInputFrom(@NotNull Direction face) {
        return face == INPUT_DIRECTION;
    }
    public static final int PROCESSING_TICKS_PER_ASPECT = 39;
    protected void drawEssentia() {
        if (this.level == null) {
            return;
        }
        if (!this.aspectIn.isEmpty()) {
            return;
        }
        var selfPos = getBlockPos();
        var inPos = selfPos.relative(INPUT_DIRECTION);
        var outDirForOutBE = INPUT_DIRECTION.getOpposite();
        if (this.level.getBlockEntity(inPos) instanceof IEssentiaTransportOutBlockEntity outBE 
                && outBE.canOutputTo(outDirForOutBE)
        ) {
            Aspect ta = outBE.getEssentiaType(outDirForOutBE);

            if (ta instanceof CompoundAspect compoundAspect 
                    && outBE.takeEssentiaWithSuction(
                            this.getSuctionAmount(INPUT_DIRECTION),
                    ta, 1, Direction.UP) > 0
            ) {
                this.aspectIn = compoundAspect;
                this.processingTicks = PROCESSING_TICKS_PER_ASPECT;
                markDirtyAndUpdateSelf();
            }
        }

    }

    //TODO:Use blockstate
    private boolean hasNeighbourSignal(){
        if (this.level == null){
            return false;
        }
        return this.level.hasNeighborSignal(this.getBlockPos());
    }
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }
    public int getSuctionAmount(@NotNull Direction face) {
        return face == INPUT_DIRECTION ? (this.hasNeighbourSignal() ? 0 : (this.aspectIn.isEmpty() ? 128 : 64)) : 0;
    }
    @Override
    public @NotNull Aspect getSuctionType(@NotNull Direction face) {
        return Aspects.EMPTY;
    }

    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        if (!this.aspectIn.isEmpty() 
                || amount <= 0 
                || !canOutputTo(fromDirection) 
                || !(aspect instanceof CompoundAspect compoundAspect)) {
            return 0;
        }
        this.processingTicks = PROCESSING_TICKS_PER_ASPECT;
        this.aspectIn = compoundAspect;
        markDirtyAndUpdateSelf();
        return 1;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, @NotNull Direction outputToDirection) {
        if (aspect.isEmpty() || amount <= 0 || !canOutputTo(outputToDirection) || aspect != this.aspectOut) {
            return 0;
        }
        this.aspectOut = Aspects.EMPTY;
        markDirtyAndUpdateSelf();
        return 1;
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        if (!canInputFrom(face)) {
            return 0;
        }
        return this.aspectOut.isEmpty() ? 0 : 1;
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        if (!canInputFrom(face)) {
            return Aspects.EMPTY;
        }
        return this.aspectOut;
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {
        
    }
    

    public static final Direction INPUT_DIRECTION = Direction.DOWN;
    public static final Direction OUTPUT_DIRECTION = Direction.UP;
    public void clientTick(){
        ClientTickContext.tickBE(this);
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay() {
        return aspToDisplay;
    }

    @Override
    public int currentValueForComparatorSignal() {
        return (aspectOut.isEmpty() ? 0 : 1) + (aspectIn.isEmpty()?0:1);
    }

    @Override
    public int comparatorSignalCapacity() {
        return 2;
    }


    public static class ClientTickContext {
        private @DegreeValue float rotationSpeed;
        private @DegreeValue float rotation;

        @DegreeValue
        public float getRotation() {
            return rotation;
        }

        @DegreeValue
        public float getRotationSpeed() {
            return rotationSpeed;
        }

        public static void tickBE(EssentiaCentrifugeBlockEntity centrifuge){
            if (centrifuge.level == null) {
                return;
            }
            var ctx = ((EssentiaCentrifugeBlockEntityClientAccessor)centrifuge).opentc4$getClientTickContext();
            var gotPower = centrifuge.hasNeighbourSignal();
            var aspIn = centrifuge.aspectIn;
            boolean working = !aspIn.isEmpty() && !gotPower;
            if (working && ctx.rotationSpeed < 20) {
                ctx.rotationSpeed += 2;
            } else if(ctx.rotationSpeed > 0){
                ctx.rotationSpeed -= 0.5F;
            }

            float previousRotation = ctx.rotation;
            ctx.rotation += ctx.rotationSpeed;
            if (ctx.rotation % 180 <= 20 && previousRotation % 180 >= 160 && ctx.rotationSpeed > 0) {
                centrifuge.level.playSound(
                        null,
                        centrifuge.getBlockPos(),
                        ThaumcraftSounds.PUMP, SoundSource.BLOCKS, 1.0F, 1.0F
                );
            }
        }
    }
}
