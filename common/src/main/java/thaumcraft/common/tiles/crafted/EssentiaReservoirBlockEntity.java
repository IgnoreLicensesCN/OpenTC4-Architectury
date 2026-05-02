package thaumcraft.common.tiles.crafted;

import com.google.common.collect.MapMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.blocks.crafted.EssentiaReservoirBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Map;

import static com.linearity.opentc4.Consts.EssentiaReservoirBlockEntityTagAccessors.ASPECTS_OWNING;

public class EssentiaReservoirBlockEntity extends TileThaumcraft
        implements
        IEssentiaTransportBlockEntity,
        IValueContainerBasedComparatorSignalProviderBlockEntity,
        IAspectDisplayBlockEntity<Aspect>,
        IRemoteDrainableAspectSourceBlockEntity<Aspect>{

    public EssentiaReservoirBlockEntity(BlockEntityType<? extends EssentiaReservoirBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaReservoirBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_RESERVOIR, blockPos, blockState);
    }

    public Direction getFacing(){
        return getBlockState().getValue(EssentiaReservoirBlock.FACING);
    }

    public static final int ASPECT_CAPACITY = 256;

    public Direction getConnectableDirection() {
        return getFacing();
    }

    public int getAspectCapacity(){
        return ASPECT_CAPACITY;
    }

    public void decreaseAspectAmount(Aspect aspect,int amount){
        this.owningAspects.reduceAndRemoveIfNotPositive(aspect, amount);
    }
    public void increaseAspectAmount(Aspect aspect,int amount){
        this.owningAspects.addAll(aspect, amount);
    }
    public void setAspectAmount(Aspect aspect,int amount){
        this.owningAspects.set(aspect, amount);
    }

    public void clear(){
        owningAspects.clear();
    }

    protected final AspectList<Aspect> owningAspects = new AspectList<>();
    public final AspectList<Aspect> aspOwningView = new UnmodifiableAspectList<>(owningAspects);

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ASPECTS_OWNING.writeToCompoundTag(compoundTag, owningAspects);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.owningAspects.putAllAspects(ASPECTS_OWNING.readFromCompoundTag(compoundTag));
    }

    @Override
    public boolean isConnectable(Direction face) {
        return face == getConnectableDirection();
    }

    @Override
    public boolean canInputFrom(Direction face) {
        return isConnectable(face);
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {}

    @Override
    public int getSuctionAmount(Direction face) {
        if (!capacityFullForAddEssentia()) {
            return getBaseSuction();
        } else {
            return 0;
        }
    }

    @Override
    public @NotNull Aspect getSuctionType(Direction face) {
        return Aspects.EMPTY;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (!canAddEssentia(aspect,amount,fromDirection)) {
            return 0;
        }
        if (capacityFullForAddEssentia()){
            return 0;
        }
        return doAddEssentia(aspect, amount);
    }

    protected boolean canAddEssentia(Aspect aspect,int amount,Direction fromDirection) {
        if (aspect.isEmpty()) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        return isConnectable(fromDirection);
    }

    protected boolean capacityFullForAddEssentia(){
        return this.owningAspects.visSize() >= getAspectCapacity();
    }

    protected int doAddEssentia(Aspect aspect, int amount) {
        int added = Math.min(amount, getAspectCapacity() - this.owningAspects.visSize());
        increaseAspectAmount(aspect,added);
        if (added != 0) {
            markDirtyAndUpdateSelf();
        }
        return added;
    }

    @Override
    public @NotNull Aspect getEssentiaType(Direction face) {
        if (!isConnectable(face)){
            return Aspects.EMPTY;
        }
        return owningAspects.getFirstAspect();
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        return owningAspects.visSize();
    }

    @Override
    public int currentComparatorSignalValue() {
        return owningAspects.visSize();
    }

    @Override
    public int comparatorSignalCapacity() {
        return getAspectCapacity();
    }

    @Override
    public @UnmodifiableView @NotNull AspectList<Aspect> getAspectsToDisplay() {
        return aspOwningView;
    }

    @Override
    public int canProvideAspectAmountForRemoteDrain(Aspect aspect) {
        return owningAspects.get(aspect);
    }

    @Override
    public boolean drainAspectRemote(Aspect aspect, int amount) {
        if (amount <= this.owningAspects.get(aspect)) {
            decreaseAspectAmount(aspect,amount);
        }
        return false;
    }

    protected int tickCount = 0;
    public void serverTick(){
        if (level == null){return;}
        tickCount+=1;
        if (tickCount % 5 == 0 && !capacityFullForAddEssentia()) {
            var facing = getConnectableDirection();
            var drainFromPos = getBlockPos().relative(facing);
            var drainFromBE = level.getBlockEntity(drainFromPos);
            if (drainFromBE instanceof IEssentiaTransportOutBlockEntity outBE){
                var beOutToDir = facing.getOpposite();
                var outAspect = outBE.getEssentiaType(beOutToDir);
                if (!outAspect.isEmpty()) {
                    addEssentia(
                            outAspect,outBE.takeEssentiaWithSuction(
                                    this.getSuctionAmount(facing),
                                    outAspect,
                                    1,
                                    beOutToDir),
                            facing
                    );
                }
            }
        }
    }
    public boolean canFillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            Aspect aspect
    ) {
        return owningAspects.get(aspect) != 0;
    }

    public boolean fillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            int minAmount
    ) {
        if (this.level == null){
            return false;
        }
        return owningAspects.forEachWithBreak(
                (aspect,amountBefore) -> {
                    if (amountBefore >= minAmount) {
                        int remaining = itemToFill.storeAspect(this.level,getBlockPos(),stackToFill, aspect, amountBefore);
                        setAspectAmount(aspect,remaining);

                        if (remaining != amountBefore) {
                            markDirtyAndUpdateSelf();
                            level.playSound(
                                    null,
                                    getBlockPos(),
                                    SoundEvents.PLAYER_SWIM,
                                    SoundSource.BLOCKS,
                                    .5F,
                                    1.F + (level.getRandom()
                                            .nextFloat() - level.getRandom()
                                            .nextFloat()) * 0.3F
                            );
                        }
                        return true;
                    }
                    return false;
                }
        );
    }

    @Override
    public boolean canOutputTo(Direction face) {
        return isConnectable(face);
    }


    @Override
    public int takeEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (aspect.isEmpty()) return 0;
        if (amount <= 0) return 0;
        if (!isConnectable(fromDirection)){
            return 0;
        }
        var amountCurrent = owningAspects.get(aspect);
        if (amountCurrent < amount){
            return 0;
        }
        decreaseAspectAmount(aspect,amount);
        markDirtyAndUpdateSelf();
        return amount;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return getBaseSuction();
    }

    protected int getBaseSuction(){
        return 24;
    }

    public static class ClientTickContext {
        // 目标颜色 (Target Color)
        private float targetR = 1, targetG = 1, targetB = 1;
        // 步长/增量 (Step/Increment)
        private float stepR = 0, stepG = 0, stepB = 0;
        // 当前颜色 (Current Color)
        private float currentR = 0, currentG = 0, currentB = 0;

        private @NotNull Aspect displayAspect = Aspects.EMPTY;


        public @NotNull Aspect getDisplayAspect() {
            return displayAspect;
        }

        public float getCurrentB() {
            return currentB;
        }

        public float getCurrentG() {
            return currentG;
        }

        public float getCurrentR() {
            return currentR;
        }

        public float getStepB() {
            return stepB;
        }

        public float getStepG() {
            return stepG;
        }

        public float getStepR() {
            return stepR;
        }

        public float getTargetB() {
            return targetB;
        }

        public float getTargetG() {
            return targetG;
        }

        public float getTargetR() {
            return targetR;
        }

        // Getter 建议同步更新... (例如 getTargetR, getCurrentR 等)

        private static final Map<EssentiaReservoirBlockEntity, ClientTickContext> contexts =
                new MapMaker().weakKeys().makeMap();

        public static void tickBE(EssentiaReservoirBlockEntity be) {
            var ctx = contexts.computeIfAbsent(be, k -> new ClientTickContext());
            var level = be.level;
            if (level == null) return;

            var aspectsMap = be.owningAspects;
            int totalSize = aspectsMap.size();
            if (totalSize == 0) return;

            int tickCount = (int) (level.getGameTime() & Integer.MAX_VALUE);
            int intervalCount = tickCount >> 4; // 每 16 tick 换一次
            int remainingTicks = tickCount & 15; // 0-15 的周期

            if (ctx.displayAspect.isEmpty()) {
                ctx.targetR = ctx.targetG = ctx.targetB = 1.0F;
                ctx.stepR = ctx.stepG = ctx.stepB = 0.0F;
            } else {
                ctx.currentR -= ctx.stepR;
                ctx.currentG -= ctx.stepG;
                ctx.currentB -= ctx.stepB;
            }

            // 切换显示的 Aspect 逻辑
            if (remainingTicks == 0) {
                int pickIndex = intervalCount % totalSize;

                aspectsMap.acceptForIndex(pickIndex,
                        (aspect,_ignoredAmount) -> {
                            ctx.displayAspect = aspect;
                            int colorInt = ctx.displayAspect.color;

                            // 解码颜色
                            ctx.targetR = ((colorInt >> 16) & 0xFF) / 255F;
                            ctx.targetG = ((colorInt >> 8) & 0xFF) / 255F;
                            ctx.targetB = (colorInt & 0xFF) / 255F;

                            // 计算步长：(当前值 - 目标值) / 周期长度
                            // 假设你想在 20 tick 内完成过渡
                            ctx.stepR = (ctx.currentR - ctx.targetR) / 16F;
                            ctx.stepG = (ctx.currentG - ctx.targetG) / 16F;
                            ctx.stepB = (ctx.currentB - ctx.targetB) / 16F;
                        }
                );
            }
        }
    }
    public void clientTick() {
        ClientTickContext.tickBE(this);
    }

    public int getGooAndGasAmountOnRemove(){
        return owningAspects.visSize() / 16;
    }
}
