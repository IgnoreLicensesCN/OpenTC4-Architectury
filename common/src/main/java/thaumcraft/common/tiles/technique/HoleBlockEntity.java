package thaumcraft.common.tiles.technique;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.HoleBlockEntityTagAccessors.*;

public class HoleBlockEntity extends TileThaumcraft {
    public HoleBlockEntity(BlockEntityType<? extends HoleBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public HoleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.HOLE(),blockPos,blockState);
    }
    private int tickRemaining = 33;
    private @NotNull BlockState storingState = Blocks.AIR.defaultBlockState();
    private CompoundTag storedTag = null;
    private int spreadDistance = 3;
    private Direction spreadDirection = Direction.DOWN;

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);

        if (level != null) {
            storingState = STORING_BLOCK_STATE.readFromCompoundTag(level.holderLookup(Registries.BLOCK),compoundTag);
        }else {
            storedTag = compoundTag;
        }
        tickRemaining = TICK_REMAINING.readIntFromCompoundTag(compoundTag);
        spreadDirection = SPREAD_DIRECTION.readFromCompoundTag(compoundTag);
        spreadDistance = SPREAD_DISTANCE.readIntFromCompoundTag(compoundTag);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (this.level != null && storedTag != null) {
            storingState = STORING_BLOCK_STATE.readFromCompoundTag(level.holderLookup(Registries.BLOCK),storedTag);
            storedTag = null;
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        STORING_BLOCK_STATE.writeToCompoundTag(compoundTag, storingState);
        TICK_REMAINING.writeIntToCompoundTag(compoundTag, tickRemaining);
        SPREAD_DIRECTION.writeToCompoundTag(compoundTag, spreadDirection);
        SPREAD_DISTANCE.writeIntToCompoundTag(compoundTag, spreadDistance);
    }

    public void serverTick(){
        if (spreadDistance > 0){
            doSpreadHole();
        }
        tickRemaining--;
        if (tickRemaining <= 0) {
            if (this.level != null) {
                this.level.setBlockAndUpdate(getBlockPos(), storingState);
            }
        }
    }

    static {
        for (var dir: Direction.values()) {
            for (int i=0;i<9;i++){
                if (dir.getStepX() != 0){
                    HoleSpreader.offsets[dir.ordinal()][i] = new BlockPos(dir.getStepX(),(i/3)-1,(i%3)-1);
                }
                else if (dir.getStepY() != 0){
                    HoleSpreader.offsets[dir.ordinal()][i] = new BlockPos((i/3)-1,dir.getStepY(),(i%3)-1);
                }
                else if (dir.getStepZ() != 0){
                    HoleSpreader.offsets[dir.ordinal()][i] = new BlockPos((i/3)-1,(i%3)-1,dir.getStepZ());
                }
            }
        }
    }
    public void doSpreadHole(){
        if (this.level == null){return;}
        var selfPos = getBlockPos();
        HoleSpreader.spreadForward(this.level, spreadDirection, this.spreadDistance - 1, this.tickRemaining, selfPos);
        this.spreadDistance = 0;
    }

    public static class HoleSpreader{

        public static final BlockPos[][] offsets = new BlockPos[6][9];

        public static void doSpreadInitialHoleAt(Level level, BlockPos pos, Direction spreadDirection, int spreadDistance, int tickRemaining){
            var posBase = pos.relative(spreadDirection.getOpposite());
            spreadForward(level, spreadDirection, spreadDistance, tickRemaining, posBase);
        }

        private static void spreadForward(Level level, Direction spreadDirection, int spreadDistance, int tickRemaining, BlockPos posBase) {
            var offsetArr = offsets[spreadDirection.ordinal()];
            for (var posOffset: offsetArr) {
                if (posOffset.distManhattan(spreadDirection.getNormal()) == 0){
                    spreadHoleOnBlockPos(level, posBase.offset(posOffset), spreadDirection, spreadDistance, tickRemaining);
                }
                spreadHoleOnBlockPosNotCenter(level, posBase.offset(posOffset), spreadDirection, tickRemaining);
            }
        }

        public static void spreadHoleOnBlockPosNotCenter(Level level, BlockPos pos, Direction spreadDirection, int tickRemaining){
            spreadHoleOnBlockPos(level,pos,spreadDirection,0,tickRemaining);
        }

        public static void spreadHoleOnBlockPos(Level level, BlockPos pos, Direction spreadDirection, int spreadDistance, int tickRemaining){
            if (!canSpreadHoleAtPos(level,pos)){return;}
            var relatedBE = LevelBlockEntityAccessing.getExistingBlockEntity(level, pos);
            if (relatedBE == null) {
                var stateToStore = level.getBlockState(pos);
                level.setBlockAndUpdate(pos, ThaumcraftBlocks.ThaumcraftBlockInstances.HOLE().defaultBlockState());
                if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof HoleBlockEntity hole){
                    hole.setSpreadDirection(spreadDirection);
                    hole.mergeSpreadDistanceWithMax(spreadDistance-1);
                    hole.mergeTickRemainingWithMax(tickRemaining+1);
                    hole.setStoringState(stateToStore);
                }
            } else if (relatedBE instanceof HoleBlockEntity hole){
                hole.mergeTickRemainingWithMax(tickRemaining+1);
                hole.mergeSpreadDistanceWithMax(spreadDistance-1);
                if (hole.spreadDistance == spreadDistance-1){
                    hole.setSpreadDirection(spreadDirection);
                }
            }
        }

        public static boolean canSpreadHoleAtPos(Level level, BlockPos pos){
            var blockState = level.getBlockState(pos);
            if (blockState.getDestroySpeed(level, pos) < 0){
                return false;
            }
            var relatedBE = LevelBlockEntityAccessing.getExistingBlockEntity(level, pos);
            if (relatedBE != null && !(relatedBE instanceof HoleBlockEntity)){
                return false;
            }
            if (blockState.isAir()){
                return false;
            }
            return true;
        }
    }


    public void setTickRemaining(int tickRemaining) {
        this.tickRemaining = tickRemaining;
    }
    public void mergeTickRemainingWithMax(int tickRemaining) {
        this.tickRemaining = Math.max(this.tickRemaining, tickRemaining);
    }

    public void setSpreadDirection(Direction spreadDirection) {
        this.spreadDirection = spreadDirection;
    }

    public void setSpreadDistance(int spreadDistance) {
        this.spreadDistance = spreadDistance;
    }
    public void mergeSpreadDistanceWithMax(int spreadDistance) {
        this.spreadDistance = Math.max(this.spreadDistance, spreadDistance);
    }

    public void setStoringState(@NotNull BlockState storingState) {
        this.storingState = storingState;
    }
}
