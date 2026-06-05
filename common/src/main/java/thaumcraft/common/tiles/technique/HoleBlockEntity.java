package thaumcraft.common.tiles.technique;

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
        this(ThaumcraftBlockEntities.HOLE,blockPos,blockState);
    }
    private int tickRemaining = 20;
    private @NotNull BlockState storingState = Blocks.AIR.defaultBlockState();
    private CompoundTag storedTag = null;
    private int spreadDistance = 0;
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

    public static final BlockPos[][] offsets = new BlockPos[6][9];
    static {
        for (var dir: Direction.values()) {
            for (int i=0;i<9;i++){
                if (dir.getStepX() != 0){
                    offsets[dir.ordinal()][i] = new BlockPos(dir.getStepX(),(i/3)-1,(i%3)-1);
                }
                else if (dir.getStepY() != 0){
                    offsets[dir.ordinal()][i] = new BlockPos((i/3)-1,dir.getStepY(),(i%3)-1);
                }
                else if (dir.getStepZ() != 0){
                    offsets[dir.ordinal()][i] = new BlockPos((i/3)-1,(i%3)-1,dir.getStepZ());
                }
            }
        }
    }
    public void doSpreadHole(){
        if (this.level == null){return;}
        var offsetArr = offsets[spreadDirection.ordinal()];
        var selfPos = getBlockPos();
        for (var posOffset: offsetArr) {
            if (posOffset.distManhattan(this.spreadDirection.getNormal()) == 0){
                spreadHoleOnBlockPos(selfPos.offset(posOffset),this.spreadDirection,this.spreadDistance-1,this.tickRemaining);
            }
            spreadHoleOnBlockPosNotCenter(selfPos.offset(posOffset),this.spreadDirection,this.tickRemaining);
        }
        this.spreadDistance = 0;
    }
    public void spreadHoleOnBlockPosNotCenter(BlockPos pos,Direction spreadDirection,int tickRemaining){
        spreadHoleOnBlockPos(pos,spreadDirection,0,tickRemaining);
    }
    public void spreadHoleOnBlockPos(BlockPos pos,Direction spreadDirection,int spreadDistance,int tickRemaining){
        if (this.level == null){return;}
        var relatedBE = this.level.getBlockEntity(pos);
        if (relatedBE == null) {
            var stateToStore = this.level.getBlockState(pos);
            this.level.setBlockAndUpdate(pos, ThaumcraftBlocks.HOLE.defaultBlockState());
            if (this.level.getBlockEntity(pos) instanceof HoleBlockEntity hole){
                hole.setSpreadDirection(spreadDirection);
                hole.mergeSpreadDistanceWithMax(spreadDistance-1);
                hole.mergeTickRemainingWithMax(tickRemaining+1);
                hole.setStoringState(stateToStore);
            }

        }else if (relatedBE instanceof HoleBlockEntity hole){
            hole.mergeTickRemainingWithMax(this.tickRemaining+1);
            hole.mergeSpreadDistanceWithMax(this.spreadDistance-1);
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
