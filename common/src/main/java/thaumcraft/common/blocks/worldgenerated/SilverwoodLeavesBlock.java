package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.ClientFXUtils;

public class SilverwoodLeavesBlock extends LeavesBlock {
    public SilverwoodLeavesBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).lightLevel(s -> 7));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(500) == 0) {
            ClientFXUtils.sparkle(
                    pos.getX() + 0.5f + random.nextFloat() - random.nextFloat(),
                    pos.getY() + 0.5f + random.nextFloat() - random.nextFloat(),
                    pos.getZ() + 0.5f + random.nextFloat() - random.nextFloat(),
                    2.0F, 7, 0.0F
            );
        }
    }
}
