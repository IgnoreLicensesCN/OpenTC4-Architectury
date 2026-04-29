package thaumcraft.common.blocks.technique;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.abstracts.ILevitatorBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//i want something like bubble in water(but for air this time.) when you place soul sand.
public class ArcaneLevitatorBubbleBlock extends SuppressedWarningBlock {
    public ArcaneLevitatorBubbleBlock(Properties properties) {
        super(properties);
    }
    public ArcaneLevitatorBubbleBlock(){
        this(Properties.copy(Blocks.AIR));
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);
        //Next best thing to flying
        if (entity.noPhysics){
            return;
        }
        if (entity.isNoGravity()){
            return;
        }
        if (entity.isSpectator()){
            return;
        }
        applyBubbleLift(entity);
    }

    protected double getBubbleLiftSpeedLimit() {
        return 0.35;
    }

    protected void applyBubbleLift(Entity entity){
        boolean isSlowingDown = false;
        if (entity instanceof LivingEntity living) {
            if (living.isShiftKeyDown()) {
                isSlowingDown = true;
            }
        }

        var motion = entity.getDeltaMovement();

        if (isSlowingDown) {
            if (motion.y < -0.05) {
                entity.setDeltaMovement(motion.x, motion.y * 0.9, motion.z);
            }
        } else {
            if (motion.y < getBubbleLiftSpeedLimit()) {
                entity.setDeltaMovement(motion.x, motion.y + 0.1, motion.z);
            }
        }
        entity.fallDistance = 0;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        doClearFrom(serverLevel, blockPos);
    }

    protected boolean shouldClear(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).getBlock() != this) {
            return false;
        }
        var blockPosBelow = blockPos.below();
        var blockStateBelow = level.getBlockState(blockPosBelow);
        if (blockStateBelow.getBlock() == this){
            return false;
        }
        if ((blockStateBelow.getBlock() instanceof ILevitatorBlock levitator
                && blockPosBelow.getY() + levitator.getLevitatorUpliftRange(level,blockPosBelow,blockStateBelow) >= blockPos.getY())//range in bound
        ){
            return false;
        }
        return true;
    }

    protected void doClearFrom(ServerLevel serverLevel, BlockPos blockPos) {
        var clearPos = blockPos;
        while (true){
            //when start clear,this will work as it writes.
            //then goto above,we know that below already set to air,this still works.
            if (shouldClear(serverLevel,clearPos)){
                serverLevel.setBlock(clearPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                if (clearPos.getY() >= serverLevel.getMaxBuildHeight()){
                    break;
                }else {
                    clearPos = clearPos.above();
                }
            }else {
                break;
            }
        }
    }

    @Override
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block neighborBlock,
            BlockPos neighborPos,
            boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        if (neighborPos != pos.below()){
            return;
        }
        level.scheduleTick(pos, this, 1);
    }
}
