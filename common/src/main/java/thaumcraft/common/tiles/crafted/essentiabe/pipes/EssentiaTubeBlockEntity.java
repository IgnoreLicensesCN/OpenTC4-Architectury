package thaumcraft.common.tiles.crafted.essentiabe.pipes;

import com.google.common.collect.MapMaker;
import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Map;

import static com.linearity.opentc4.Consts.EssentiaTubeBlockEntityTagAccessors.*;
//This part is not efficiency but i have no better way unless we have Thaumic Energistics
public class EssentiaTubeBlockEntity extends TileThaumcraft
        implements
        IEssentiaTransportInBlockEntity,
        IEssentiaTransportOutBlockEntity,
        IWandInteractableBlockOrBlockEntity,
        IValueContainerBasedComparatorSignalProviderBlockEntity {
    protected @NotNull("is and should be empty if there's no aspect") Aspect owningAspect = Aspect.EMPTY;
    private byte openSidesMask = ((1<<6) - 1);
    private @NotNull Aspect suctionType = Aspect.EMPTY;
    private int suction = 0;

    //I got mental illness for performance
    //I don't want to check if init this in #serverTick()
    // since there's if statement and almost always not used.
    private int tickCount = System.identityHashCode(this) & 0x7;
    private int ventingTicksServerSide = 0;//do not store it's just for fun(unless someone went mad and do "open-and-close" save for thousands times)
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
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ESSENTIA_TUBE(), blockPos, blockState);
    }


    public void serverTick(){
        if (this.level == null){return;}
        if (ventingTicksServerSide > 0){
            ventingTicksServerSide -= 1;
        }
        tickCount+=1;
        if (ventingTicksServerSide <= 0) {
            if ((tickCount & 1) == 0) {
                this.calculateSuction(getAspectFilter(), getOrderedFacing());
                this.checkVenting();
            }

            if ((tickCount & 7) == 0 //slower now
                    && this.suction > 0) {
                this.equalizeWithNeighbours(getOrderedFacing());
                //if anyone wants more frequently suction update for a factory based on dynamic suction
                //i will tell them to get a mod called Thaumic Energistics for this version of TC4
            }
        }
    }
    protected void checkVenting() {
        if (this.level == null){return;}
        var pos = getBlockPos();
        for(var dirToAnotherBE:Direction.values()) {
            if (this.isConnectable(dirToAnotherBE)) {
                var dirToSelf = dirToAnotherBE.getOpposite();
                if (this.level.getBlockEntity(pos.relative(dirToAnotherBE)) instanceof IEssentiaTransportInBlockEntity ic) {
                    int inBESuction = ic.getSuctionAmount(dirToSelf);
                    if (this.suction > 0
                            && (inBESuction == this.suction || inBESuction == this.suction - 1)
                            && this.suctionType != ic.getSuctionType(dirToSelf)
                    ) {
                        this.ventingTicksServerSide = 40;
                        this.level.blockEvent(pos,getBlockState().getBlock(),1,this.suctionType.color);
                        break;
                    }
                }
            }
        }
    }

    protected void equalizeWithNeighbours(@Nullable("null -> any") Direction orderedFacing) {
        if (this.level == null){return;}
        var pos = getBlockPos();
        if (this.owningAspect.isEmpty()) {
            for(var dirToAnotherBE:Direction.values()) {
                if ((orderedFacing == null || orderedFacing != dirToAnotherBE.getOpposite())
                        && this.isConnectable(dirToAnotherBE)) {
                    var dirToSelf = dirToAnotherBE.getOpposite();
                    if (this.level.getBlockEntity(pos.relative(dirToAnotherBE)) instanceof IEssentiaTransportOutBlockEntity outBE) {
                        if (outBE.canOutputTo(dirToSelf)
                                &&
                                (this.getSuctionType(dirToAnotherBE).isEmpty()
                                        || this.getSuctionType(dirToAnotherBE) == outBE.getEssentiaType(dirToSelf)
                                        || outBE.getEssentiaType(dirToSelf).isEmpty()) //suctionType condition
                                && !( outBE instanceof IEssentiaTransportInBlockEntity inBE
                                    && this.getSuctionAmount(null) < inBE.getSuctionAmount(dirToSelf)
                                ) //suction amount condition if there's inBE
                                && this.getSuctionAmount(null) >= outBE.getMinimumSuctionToDrainOut()
                            //suction amount condition(outBE)
                        ) {
                            Aspect a = this.getSuctionType(dirToAnotherBE);
                            if (a.isEmpty()) {
                                a = outBE.getEssentiaType(dirToSelf);
                            }

                            int am = this.addEssentia(a, outBE.takeEssentia(a, 1, dirToSelf), dirToAnotherBE);
                            if (am > 0) {
                                if (this.level.random.nextInt(100) == 0) {
                                    this.level.playSound(null,pos, ThaumcraftSounds.CREAK, SoundSource.BLOCKS, 1.0F, 1.3F + this.level.random.nextFloat() * 0.2F);
                                }

                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    public void clientTick(){
        ClientTickContext.tickBE(this);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (!level.isClientSide()){
            var clickedFace = useOnContext.getClickedFace();
            level.playSound(useOnContext.getPlayer(),useOnContext.getClickedPos(), ThaumcraftSounds.TOOL, SoundSource.BLOCKS, 0.5F, 0.9F + level.random.nextFloat() * 0.2F);
            changeOpenSideState(clickedFace);
            //facing should rotate if there's facing
            markDirtyAndUpdateSelf();
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public int currentValueForComparatorSignal() {
        return owningAspect.isEmpty()?0:1;
    }

    @Override
    public int comparatorSignalCapacity() {
        return 1;
    }

    @Override
    public int getComparatorSignal() {
        return owningAspect.isEmpty()?0:15;
    }

    public static class ClientTickContext{
        private int ventingTicks = 0;
        private @RGBColor int ventColor = Aspects.ORDER.color;
        private final double fx;
        private final double fy;
        private final double fz;
        {
            int hash = System.identityHashCode(this);

            fx = ((hash & 0x3F) - 32)*(0.0078125);
            fy = (((hash >> 6) & 0x3F) - 32)*(0.0078125);
            fz = (((hash >> 12) & 0x3F) - 32)*(0.0078125);//i just want faster if there's full of venting pipe
        }
        private static final Map<EssentiaTubeBlockEntity,ClientTickContext> contexts = new MapMaker().weakKeys().makeMap();
        public static void tubeVenting(EssentiaTubeBlockEntity be,@RGBColor int color){
            var ctx = contexts.computeIfAbsent(be,(_ignored) -> new ClientTickContext());
            if (ctx.ventingTicks <= 0 && be.level != null) {
                be.level.playSound(
                        null,
                        be.getBlockPos(),
                        SoundEvents.LAVA_EXTINGUISH,
                        SoundSource.BLOCKS,
                        0.1F,
                        1.0F + be.level.random.nextFloat() * 0.1F
                );
            }
            ctx.ventingTicks = 50;
            ctx.ventColor = color < 0?0xaaaaaa:color;
        }
        public static void tickBE(EssentiaTubeBlockEntity be){
            if (!(be.level instanceof ClientLevel level)){return;}
            var ctx = contexts.computeIfAbsent(be,(_ignored) -> new ClientTickContext());
            if (ctx.ventingTicks > 0){
                ctx.ventingTicks -= 1;
                var pos = be.getBlockPos();
//                i call this part "random rotation".but since we got here(whatever this part is random) why not generate a random coordinate?
//                Random r = new Random(this.hashCode() * 4L);
//                float rp = r.nextFloat() * 360.0F;
//                float ry = r.nextFloat() * 360.0F;
//                double fx = -MathHelper.sin(ry / 180.0F * (float)Math.PI)
//                        * MathHelper.cos(rp / 180.0F * (float)Math.PI) / 5.0;
//                double fz = MathHelper.cos(ry / 180.0F * (float)Math.PI)
//                        * MathHelper.cos(rp / 180.0F * (float)Math.PI) / 5.0;
//                double fy = -MathHelper.sin(rp / 180.0F * (float)Math.PI) / 5.0;
                ClientFXUtils.drawVentParticles(
                        level,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        ctx.fx,
                        ctx.fy,
                        ctx.fz,
                        ctx.ventColor
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
            @Nullable("null -> any") Direction limitedFacingToAnotherBE
    ) {
        if (this.level == null){return;}
        this.suction = 0;//force suction 0,#setSuction may be cancelled since we have EssentiaTubeValve
        this.suctionType = Aspects.EMPTY;
//        Direction loc = null;
        if (limitedFacingToAnotherBE == null) {
            for (var direction : Direction.values()) {
                calculateSuctionForFacing(filter,direction);
            }
        }
        else if (this.isConnectable(limitedFacingToAnotherBE)) {
            calculateSuctionForFacing(filter,limitedFacingToAnotherBE);
        }
    }
    protected void calculateSuctionForFacing(
            @NotNull("empty -> any") Aspect filter,
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
                            || this.getEssentiaAmount(toAnotherBEDirection) <= 0
                        ;
        boolean canAcceptWhenWithFilter =
                        noInType || noOutType
                                || inSuctionType == outSuctionType
                            || this.getEssentiaAmount(toAnotherBEDirection) <= 0
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
                this.setSuctionForCalculating(st,suck);
            }
        }
    }

    public void setSuctionForCalculating(Aspect aspect,int suction){
        this.setSuction(aspect,suction-1);
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        if (!isConnectable(face)){return 0;}
        return getEssentiaType(face).isEmpty() ? 0 : 1;
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        if (!isConnectable(face)){return Aspects.EMPTY;}
        return this.owningAspect;
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {
        this.suctionType = aspect;
        this.suction = amount;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, @NotNull Direction outputToDirection) {
        if (this.canOutputTo(outputToDirection) && this.owningAspect == aspect && amount > 0) {
            this.owningAspect = Aspects.EMPTY;
            markDirtyAndUpdateSelf();
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        if (this.canInputFrom(fromDirection) && this.owningAspect.isEmpty() && amount > 0) {
            this.owningAspect = aspect;
            markDirtyAndUpdateSelf();
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public @NotNull Aspect getSuctionType(@NotNull Direction face) {
        return suctionType;
    }

    @Override
    public int getSuctionAmount(@NotNull Direction face) {
        return suction;
    }

    public Aspect getAspectFilter() {
        return Aspect.EMPTY;
    }

    public @Nullable Direction getOrderedFacing(){
        return null;
    }
}
