package thaumcraft.common.tiles.crafted.essentiabe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectView;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.abstracts.IAdditionalSuctionProviderBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.EssentiaBufferBlockEntityTagAccessors.*;

public class EssentiaBufferBlockEntity
        extends TileThaumcraft
        implements
        IEssentiaTransportInBlockEntity,
        IEssentiaTransportOutBlockEntity,
        IWandInteractableBlockOrBlockEntity,
        IAspectDisplayBlockEntity<Aspect>,
        IValueContainerBasedComparatorSignalProviderBlockEntity
{
    private byte openSidesMask = ((1<<6) - 1);
    private final byte[] suctionLimits = new byte[Direction.values().length];

    @Override
    public void writeCustomNBT(CompoundTag tag) {
        super.writeCustomNBT(tag);
        OWNING_ASPECTS.writeToCompoundTag(tag,aspects);
        OPEN_SIDES.writeByteToCompoundTag(tag,openSidesMask);
        SUCTION_LIMITS.writeToCompoundTag(tag,suctionLimits);
    }

    @Override
    public void readCustomNBT(CompoundTag tag) {
        super.readCustomNBT(tag);
        aspects.clear();
        OWNING_ASPECTS.readFromCompoundTagInto(tag,aspects);
        openSidesMask = OPEN_SIDES.readByteFromCompoundTag(tag);
        var readSuctionLimits = SUCTION_LIMITS.readFromCompoundTag(tag);
        if (readSuctionLimits.length == suctionLimits.length) {
            System.arraycopy(SUCTION_LIMITS.readFromCompoundTag(tag), 0, suctionLimits, 0, suctionLimits.length);
        }
    }

    public static final byte SUCTION_LIMITS_ZERO = 2;
    public static final byte SUCTION_LIMITS_ONE = 1;
    public static final byte SUCTION_LIMITS_OFF = 0;

    protected final void changeOpenSideState(Direction face) {
        openSidesMask ^= (byte) (1 << face.ordinal());
    }
    protected final boolean getOpenSideState(Direction face) {
        return (openSidesMask & (1 << face.ordinal())) != 0;
    }
    protected final void changeSuctionLimits(Direction face) {
        byte currentLimit = suctionLimits[face.ordinal()];
        if (currentLimit >= 2){
            suctionLimits[face.ordinal()] = SUCTION_LIMITS_OFF;
            return;
        }
        suctionLimits[face.ordinal()] = (byte) (currentLimit + 1);
    }


    public EssentiaBufferBlockEntity(BlockEntityType<? extends EssentiaBufferBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaBufferBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_BUFFER, blockPos, blockState);
    }

    private int tickCount = System.identityHashCode(this) & 31;

    public void serverTick() {
        tickCount += 1;
        if ((tickCount & 31) == 0) {
            calculateBellowSuction();
        }
        if ((tickCount & 3) == 0){
            fillBuffer();
        }
    }
    private int lastFillIndex = 0;
    protected void fillBuffer() {
        if (this.level == null){ return; }
        if (aspects.visSize() >= getAspectCapacity()){return;}
        int dirLength = Direction.values().length;
        var selfPos = getBlockPos();
        int lastFillIndexCached = lastFillIndex;
        for (int indexOffset = 0; indexOffset < dirLength; indexOffset++) {
            int currentConsideringIndex = (lastFillIndexCached + indexOffset)%dirLength;
            var pickDirection = Direction.values()[currentConsideringIndex];
            var pickPos = selfPos.relative(pickDirection);
            if (this.level.getBlockState(pickPos).getBlock() instanceof IEssentiaTransportOutBlockEntity outBE) {
                var outDirection = pickDirection.getOpposite();
                var gotType = outBE.getEssentiaType(outDirection);
                var addAmount = outBE.takeEssentiaWithSuction(
                        getSuctionAmount(pickDirection),
                        gotType,
                        1,
                        outDirection
                );
                this.addEssentia(gotType,addAmount,pickDirection);
                lastFillIndex = currentConsideringIndex;
                if (addAmount != 0){
                    return;
                }
            }
        }
        lastFillIndex = 0;
    }

    private final AspectList<Aspect> aspects = new LinkedHashAspectList<>();
    private final AspectList<Aspect> aspectsView = new UnmodifiableAspectView<>(aspects);

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay() {
        return aspectsView;
    }

    private int bellowSuction = 0;
    private void calculateBellowSuction() {
        if (this.level == null){
            return;
        }
        bellowSuction = 0;
        var selfPos = this.getBlockPos();
        for (var dir: Direction.values()) {
            var pickPos = selfPos.relative(dir);
            var pickState = this.level.getBlockState(pickPos);
            if (pickState.getBlock() instanceof IAdditionalSuctionProviderBlock suctionProvider) {
                bellowSuction += suctionProvider.getAdditionalProvidedSuction(
                        this.level,
                        this,
                        getBlockState(),
                        selfPos,
                        pickState,
                        pickPos
                );
            }
        }
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return getOpenSideState(face);
    }

    @Override
    public boolean canInputFrom(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public boolean canOutputTo(@NotNull Direction face) {
        return isConnectable(face);
    }


    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var shiftClicked = useOnContext.getPlayer() instanceof Player player && player.isShiftKeyDown();
        if (!level.isClientSide()){
            var clickedFace = useOnContext.getClickedFace();
            level.playSound(useOnContext.getPlayer(),useOnContext.getClickedPos(), ThaumcraftSounds.TOOL, SoundSource.BLOCKS, 0.5F, 0.9F + level.random.nextFloat() * 0.2F);
            if (shiftClicked){
                changeSuctionLimits(clickedFace);
            }else {
                changeOpenSideState(clickedFace);
            }
            //facing should rotate if there's facing
            markDirtyAndUpdateSelf();
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        if (!canInputFrom(fromDirection)) {
            return 0;
        }
        int visSizeRemaining = getAspectCapacity() - aspects.visSize();
        if (visSizeRemaining == 0) {
            return 0;
        }
        int canAdd = Math.min(amount, visSizeRemaining);
        aspects.addAll(aspect, canAdd);
        return canAdd;
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        return aspects.getAspectAtIndexEnsureInBound(tickCount);
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        return 0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, @NotNull Direction outputToDirection) {
        if (!canOutputTo(outputToDirection)) {
            return 0;
        }
        int visRemaining = aspects.get(aspect);
        if (visRemaining == 0) {
            return 0;
        }
        int canTake = Math.min(amount, visRemaining);
        aspects.reduceAndRemoveIfNotPositive(aspect,canTake);
        return canTake;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }

    @Override
    public @NotNull Aspect getSuctionType(@NotNull Direction face) {
        return Aspects.EMPTY;
    }

    @Override
    public int getSuctionAmount(@NotNull Direction face) {
        byte dirSuctionLimits = suctionLimits[face.ordinal()];
        if (dirSuctionLimits == SUCTION_LIMITS_OFF){
            return Math.max(1,bellowSuction);
        }
        if (dirSuctionLimits == SUCTION_LIMITS_ONE){
            return 1;
        }
        if (dirSuctionLimits == SUCTION_LIMITS_ZERO){
            return 0;
        }
        throw new IllegalStateException("Unexpected suction limits: " + dirSuctionLimits);
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {

    }

    @Override
    public int currentValueForComparatorSignal() {
        return aspects.visSize();
    }

    @Override
    public int comparatorSignalCapacity() {
        return getAspectCapacity();
    }

    public static final int ASPECT_CAPACITY = 8;
    protected int getAspectCapacity(){
        return ASPECT_CAPACITY;
    }
}
