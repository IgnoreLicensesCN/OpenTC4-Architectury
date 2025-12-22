package thaumcraft.common.blocks.technique;

import com.linearity.opentc4.clientrenderapi.IClientRandomTickableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.crafted.PavingStoneWardingBlock;
import thaumcraft.common.tiles.TileWardingStone;

public class WardingAuraBlock extends Block{
    public WardingAuraBlock(Properties properties) {
        super(properties);
    }
    public WardingAuraBlock() {
        super(Properties.of().randomTicks().sound(SoundType.WOOL).replaceable());
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {

        for(int a = 1; a < 3; ++a) {
            var bState = blockGetter.getBlockState(blockPos.below(a));
            if (bState.getBlock() instanceof PavingStoneWardingBlock pavingStoneWardingBlock) {
                return pavingStoneWardingBlock.isCharged(blockState) ? Shapes.block():Shapes.empty();
            }
        }

        return Shapes.empty();
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return !getCollisionShape(blockState, blockGetter, blockPos, null).isEmpty();
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        for(int a = 1; a < 3; ++a) {
            var bState = serverLevel.getBlockState(blockPos.below(a));
            if (bState.getBlock() instanceof PavingStoneWardingBlock pavingStoneWardingBlock) {
                if (pavingStoneWardingBlock.isCharged(blockState)){
                    serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(),3);
                };
            }
        }
    }
}
