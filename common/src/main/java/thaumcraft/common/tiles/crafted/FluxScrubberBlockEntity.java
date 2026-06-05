package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IEssentiaTransportOutBlockEntity;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.common.blocks.crafted.FluxScrubberBlock;
import thaumcraft.common.blocks.liquid.FiniteFlowingFluid;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkleS2C;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.TileThaumcraft;

public class FluxScrubberBlockEntity extends TileThaumcraft
implements IEssentiaTransportOutBlockEntity {
    public FluxScrubberBlockEntity(BlockEntityType<? extends FluxScrubberBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public FluxScrubberBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.FLUX_SCRUBBER, blockPos, blockState);
    }

    protected int essentiaAmount = 0;
    protected int centiVisAmount = 0;
    protected int progressForNextEssentiaGeneration = 0;

    protected int tickCount = System.identityHashCode(this) & 63;
    protected void checkProgressForEssentia(){
        if (this.level == null){
            return;
        }
        int required = progressRequiredForEssentia();
        if (progressForNextEssentiaGeneration > required){
            progressForNextEssentiaGeneration -= required;
            if (this.level.random.nextInt(4) == 0) {
                ++this.essentiaAmount;
                int max = maxEssentiaAmount();
                if (this.essentiaAmount > max) {
                    this.essentiaAmount = max;
                }

                markDirtyAndUpdateSelf();
            }
        }
    }
    protected int progressRequiredForEssentia(){
        return 4;
    }
    protected int maxEssentiaAmount(){
        return 4;
    }

    protected Aspect getCentiVisRequiring(){
        return Aspects.AIR;
    }

    public void serverTick(){
        if (this.level == null){
            return;
        }
        checkProgressForEssentia();
        if (this.centiVisAmount < 5) {
            this.centiVisAmount += VisNetHandler.drainCentiVis(this.level, getBlockPos(), Aspects.AIR, 10);
        }

        if (centiVisAmount >= 5) {
            checkFlux();
        }
    }

    protected boolean isFlux(BlockPos pos) {
        if (this.level == null){
            return false;
        }
        var fluid = this.level.getBlockState(pos).getFluidState().getType();
        return fluid == ThaumcraftFluids.FLUX_GOO_FLUID;
    }

    //range added:ball(radius) -> cube(half length)
    protected int getCleanDistance(){
        return 16;
    }
    protected int getMaxToCheckEachRun(){
        return 16;
    }

    protected int easyLCGState = 0;
    protected static final int[] LCGNeededPrimesExample = {
            3877, 6737, 7237, 62327, 39439, 53791, 53549, 16759,
            1987, 35897, 46589, 59369, 26647, 56629, 26387, 1931,
            43451, 4409, 823, 14947, 22907, 9533, 36343, 46601,
            36833, 26903, 1667, 4519, 53777, 38917, 37181, 56417,
            14923, 42989, 58481, 12577, 54151, 18691, 44927, 47591,
            29569, 54499, 16223, 63997, 12149, 6551, 59341, 30553,
            58909, 34883, 1759, 11093, 13873, 64621, 13367, 16741,
            14221, 28429, 50873, 9127, 54721, 2447, 16057, 28183
    };
    protected int LCGStep = LCGNeededPrimesExample[Math.abs(System.identityHashCode(this)% LCGNeededPrimesExample.length)];
//    {
//        if (LCGStep % ((getCleanDistance()*2 + 1)*(getCleanDistance()*2 + 1)*(getCleanDistance()*2 + 1)) == 0){
//            throw new IllegalArgumentException("illegal range for LCG");
//        }
//    }
    private void checkFlux() {
        if (this.level == null){
            return;
        }
        int distance = getCleanDistance();
        easyLCGState = (easyLCGState + LCGStep) % ((distance*2 + 1)*(distance*2 + 1)*(distance*2 + 1));
        for (int i = 0; i< getMaxToCheckEachRun(); i++){
            int x = (easyLCGState/((distance*2 + 1)*(distance*2 + 1))) ;
            int yz = easyLCGState % ((distance*2 + 1)*(distance*2 + 1));
            int y = yz /((distance*2 + 1));
            int z = yz %((distance*2 + 1));
            var posToCheck = new BlockPos(x,y,z);
            if (this.isFlux(posToCheck)) {
                this.centiVisAmount -= 5;

                var fluidState = this.level.getBlockState(posToCheck).getFluidState();
                int fluidAmount = fluidState.getAmount()-1;
                if (fluidAmount == 0){
                    this.level.setBlockAndUpdate(posToCheck, Blocks.AIR.defaultBlockState());
                }else if (fluidState.getType() instanceof FiniteFlowingFluid finiteFlowingFluid){
                    this.level.setBlockAndUpdate(posToCheck,fluidState.setValue(finiteFlowingFluid.liquidLevel,fluidAmount).createLegacyBlock());
                }else {
                    this.level.setBlockAndUpdate(posToCheck, Blocks.AIR.defaultBlockState());
                }

                if (this.level instanceof ServerLevel serverLevel){
                    new PacketFXBlockSparkleS2C(x, y, z, 14483711).sendToAllAround(serverLevel,posToCheck,32*32);
                }
                ++this.progressForNextEssentiaGeneration;
                markDirtyAndUpdateSelf();
                return;
            }
        }
    }

    @Override
    public boolean canOutputTo(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, @NotNull Direction outputToDirection) {
        int re = Math.min(this.essentiaAmount, amount);
        this.essentiaAmount -= re;
        markDirtyAndUpdateSelf();
        return re;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }

    @Override
    public @NotNull("null -> empty") Aspect getEssentiaType(@NotNull Direction face) {
        return Aspects.MAGIC;
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        return essentiaAmount;
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return face == getBlockState().getValue(FluxScrubberBlock.FACING);
    }
}
