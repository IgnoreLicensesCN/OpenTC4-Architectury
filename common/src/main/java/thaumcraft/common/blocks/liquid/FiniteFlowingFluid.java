package thaumcraft.common.blocks.liquid;

import com.linearity.opentc4.simpleutils.ObjectIntPair;
import com.linearity.opentc4.simpleutils.SimplePair;
import com.linearity.opentc4.utils.CollectionsMinecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.linearity.opentc4.utils.DirectionHelper.getDirectionAroundNotOpposite;

public abstract class FiniteFlowingFluid extends FlowingFluid {
    public final IntegerProperty liquidLevel;
    public final int maxLevel;
    public final Direction gravityDirection;
    public static final int MIN_LEVEL = 1;
    public FiniteFlowingFluid(int maxLevel, Direction gravityDirection) {
        this.maxLevel = maxLevel;
        this.liquidLevel = IntegerProperty.create("level", MIN_LEVEL, maxLevel);
        this.gravityDirection = gravityDirection;
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(liquidLevel);
    }

    @Override
    protected @NotNull Map<Direction, FluidState> getSpread(Level level, BlockPos blockPos, BlockState blockState) {
        throw new UnsupportedOperationException("not supported");
    }

    @Override
    protected void spread(Level level, BlockPos blockPos, FluidState fluidState) {

    }

    @Override
    protected void spreadTo(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState) {

    }

    @Override
    protected boolean canSpreadTo(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Direction direction, BlockPos blockPos2, BlockState blockState2, FluidState fluidState, Fluid fluid) {
        throw new UnsupportedOperationException("not supported");
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return this;
    }

    @Override
    public @NotNull Fluid getSource() {
        return this;
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {

    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 0;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return 0;
    }

    @Override
    public @NotNull Item getBucket() {
        return Items.AIR;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected float getExplosionResistance() {
        return 100000.0f;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return fluidState.getValue(liquidLevel);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, FluidState fluidState) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (fluidState.getType() != this){return;}
            var randomSource = level.random;
            if (!tryMoveToGravityDirection(fluidState, serverLevel, blockPos)){
                tryMoveAround(fluidState, serverLevel, blockPos, randomSource);
            }
        }
    }

    protected boolean canStackToFluid(FluidState selfState, ServerLevel serverLevel, FluidState toState){
        return toState.getType() == this
                && selfState.getType() == this;
    }
    protected boolean stackToBlockInGravityDirection(FluidState selfState, BlockPos selfPos, ServerLevel serverLevel, FluidState toState, BlockPos toPos){
        int selfLiquidLevel = selfState.getValue(liquidLevel);
        int toLiquidLevel = toState.getValue(liquidLevel);
        var toMaxLevel = ((FiniteFlowingFluid)toState.getType()).maxLevel;
        if (toLiquidLevel >= toMaxLevel){
            return false;
        }
        var addLevelSpace = toMaxLevel - toLiquidLevel;
        var levelCanAdd = Math.min(selfLiquidLevel, addLevelSpace);
        var remainLevel = selfLiquidLevel - levelCanAdd;
        serverLevel.setBlockAndUpdate(
                toPos,
                toState.setValue(liquidLevel, toLiquidLevel + levelCanAdd)
                .createLegacyBlock()
        );
        if (remainLevel < MIN_LEVEL){
            serverLevel.setBlockAndUpdate(selfPos, Blocks.AIR.defaultBlockState());
        }else {
            serverLevel.setBlockAndUpdate(selfPos,
                    selfState.setValue(liquidLevel, remainLevel)
                            .createLegacyBlock()
            );
        }
        return true;
    }
    protected boolean tryMoveToGravityDirection(FluidState fluidState, ServerLevel serverLevel, BlockPos blockPos) {

        var toPos = blockPos.relative(gravityDirection);
        var toState = serverLevel.getBlockState(toPos);
        var toFluidState = toState.getFluidState();
        if (!toFluidState.isEmpty() && canStackToFluid(fluidState, serverLevel, toFluidState)){
            return stackToBlockInGravityDirection(fluidState, blockPos, serverLevel, toFluidState, toPos);
        }
        if (toState.canBeReplaced(this)) {
            serverLevel.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            serverLevel.setBlockAndUpdate(toPos,fluidState.createLegacyBlock());
            return true;
        }
        return false;
    }
    //assert we have same liquidLevel
    protected void tryMoveAround(FluidState fluidState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource){
        int selfLevel = fluidState.getValue(liquidLevel);
        if (selfLevel == 1){
            return;
        }
        List<ObjectIntPair<SimplePair<Direction,FluidState>>> directionToStack = new ArrayList<>(6);
        int totalLevel = selfLevel;
        for (var dirAround: getDirectionAroundNotOpposite(gravityDirection)){
            var stateAround = serverLevel.getBlockState(blockPos.relative(dirAround));
            var fluidStateAround = stateAround.getFluidState();
            if (!fluidStateAround.isEmpty() && canStackToFluid(fluidState, serverLevel,fluidStateAround)){
                int level = stateAround.getValue(liquidLevel);
                if (level < selfLevel){
                    directionToStack.add(new ObjectIntPair<>(new SimplePair<>(dirAround,fluidStateAround), level));
                    totalLevel += level;
                }
            }else if (stateAround.canBeReplaced(this)){
                directionToStack.add(new ObjectIntPair<>(new SimplePair<>(dirAround,this.defaultFluidState()), 0));
            }
        }

        if (directionToStack.isEmpty()){
            return;
        }

        //so,as our chinese saying,“选择了播放量最高的写法”(in fact i chose the easiest to read way)

        int levelEach = Math.floorDiv(totalLevel, directionToStack.size() + 1);
        int levelRemaining = totalLevel - (levelEach*(directionToStack.size() + 1));
        if (levelRemaining > 0){
            CollectionsMinecraft.shuffle(directionToStack, randomSource);
        }

        setFluidWithCheck(serverLevel,fluidState.setValue(liquidLevel, levelEach),blockPos);
        for (int i=0;i<directionToStack.size();i++){
            var pair = directionToStack.get(i).obj();
            int levelToSet = levelEach;
            if (i < levelRemaining){
                levelToSet += 1;
            }
            setFluidWithCheck(serverLevel,pair.b().setValue(liquidLevel, levelToSet),blockPos.relative(pair.a()));
        }
    }
    protected void setFluidWithCheck(ServerLevel serverLevel,FluidState fluidState,BlockPos blockPos){
        var stateToSet = serverLevel.getBlockState(blockPos);
        if (!stateToSet.isAir()){
            serverLevel.destroyBlock(blockPos,true);
        }
        serverLevel.setBlockAndUpdate(
                blockPos, fluidState.createLegacyBlock()
        );
    }

    protected void decreaseOrRemove(Level level, BlockPos pos, FluidState state) {
        int lvl = state.getValue(liquidLevel);
        if (lvl <= 1)
            level.removeBlock(pos, false);
        else
            level.setBlockAndUpdate(pos, state.setValue(liquidLevel, lvl - 1).createLegacyBlock());
    }


}
