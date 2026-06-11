package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.ILevitatorBlock;
import thaumcraft.common.blocks.abstracts.ILevitatorRangeAmplifierBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class ArcaneLevitatorBlock extends SuppressedWarningBlock
        implements
        ILevitatorRangeAmplifierBlock,
        ILevitatorBlock {
    public ArcaneLevitatorBlock(Properties properties) {
        super(properties);
    }
    public ArcaneLevitatorBlock() {
        this(
                Properties.of().strength(2.5F, 15F)
                        .sound(SoundType.WOOD)
        );
    }

    //air
    //some block not air <- stop here
    //air
    //..
    //air
    //lifter <- tick this.
    //lifter
    //lifter
    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        var posAbove = blockPos.above();
        var stateAbove = serverLevel.getBlockState(posAbove);
        if (stateAbove.is(this)) {
            serverLevel.scheduleTick(posAbove,this,1);
            return;
        }
        int rangeAbove = getLevitatorUpliftRange(serverLevel,blockPos,blockState);
        int upperBound = rangeAbove + blockPos.getY();
        upperBound = Math.min(upperBound, serverLevel.getMaxBuildHeight());
        int yOffset = 1;
        for (; yOffset + blockPos.getY() <= upperBound;yOffset++){
            var pickPos = blockPos.above(yOffset);
            if (!serverLevel.getBlockState(pickPos).isAir()){
                break;
            }
            serverLevel.setBlockAndUpdate(pickPos, ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_LEVITATOR_BUBBLE.defaultBlockState());
        }
        yOffset += 1;
        var posToClear = blockPos.above(yOffset);
        if (yOffset + blockPos.getY() <= serverLevel.getMaxBuildHeight()
                && serverLevel.getBlockState(posToClear).getBlock() == ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_LEVITATOR_BUBBLE
        ){
            serverLevel.setBlockAndUpdate(posToClear, Blocks.AIR.defaultBlockState());
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        if (!level.isClientSide) {
            level.scheduleTick(blockPos, this, 20);
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
        if (!level.isClientSide) {
            level.scheduleTick(blockPos, this, 20);
        }
    }

    @Override
    public int getBaseLevitatorUpliftRange(Level atLevel, BlockPos levitatorPos, BlockState levitatorState) {
        return getLevitatorUpliftRangeAddition(atLevel,levitatorPos,levitatorState,levitatorPos,levitatorState);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource r) {
        super.animateTick(blockState, level, blockPos, r);
        if (this.getLevitatorUpliftRange(level,blockPos,blockState) > 0){
            int i = blockPos.getX();
            int j = blockPos.getY();
            int k = blockPos.getZ();
            ClientFXUtils.sparkle(
                    (float)i + 0.2F + r.nextFloat() * 0.6F,
                    (float)(j + 1),
                    (float)k + 0.2F + r.nextFloat() * 0.6F,
                    1.0F,
                    3,
                    -0.3F
            );
        }
    }
}
