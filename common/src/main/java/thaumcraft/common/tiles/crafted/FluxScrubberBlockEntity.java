package thaumcraft.common.tiles.crafted;

import com.linearity.opentc4.utils.EasyLCGGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
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
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.FLUX_SCRUBBER(), blockPos, blockState);
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
            this.centiVisAmount += VisNetHandler.drainCentiVis(this.level, getBlockPos(), getCentiVisRequiring(), 10, 8);
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
        return fluid == ThaumcraftFluids.ThaumcraftFluidInstances.FLUX_GOO_FLUID();
    }

    //range added:ball(radius) -> cube(half length)
    protected int getCleanDistance(){
        return 16;
    }
    protected int getMaxToCheckEachRun(){
        return 16;
    }

    protected EasyLCGGenerator LCGGen = new EasyLCGGenerator((getCleanDistance()*2 + 1)*(getCleanDistance()*2 + 1)*(getCleanDistance()*2 + 1));
    protected final BlockPos.MutableBlockPos posToCheck = new BlockPos.MutableBlockPos();
    private void checkFlux() {
        if (this.level == null){
            return;
        }
        int distance = getCleanDistance();
//        int distanceCube = ((distance*2 + 1)*(distance*2 + 1)*(distance*2 + 1));
//        LCGGen.setStep(distanceCube);
        int index = LCGGen.next();
        for (int i = 0; i< getMaxToCheckEachRun(); i++){
            int x = (index/((distance*2 + 1)*(distance*2 + 1))) ;
            int yz = index % ((distance*2 + 1)*(distance*2 + 1));
            int y = yz /((distance*2 + 1));
            int z = yz %((distance*2 + 1));
            var selfPos = getBlockPos();
            posToCheck.set(x + selfPos.getX(), y + selfPos.getY(), z + selfPos.getZ());
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
                    new PacketFXBlockSparkleS2C(x, y, z, 0xdd00ff).sendToAllAround(serverLevel,posToCheck,32*32);
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
