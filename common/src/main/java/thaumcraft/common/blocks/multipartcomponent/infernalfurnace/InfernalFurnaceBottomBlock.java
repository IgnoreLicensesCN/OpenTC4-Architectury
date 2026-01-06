package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class InfernalFurnaceBottomBlock extends AbstractInfernalFurnaceComponent {

    public InfernalFurnaceBottomBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceBottomBlock() {
        super(Properties
                .copy(Blocks.OBSIDIAN)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
        );
    }


    public static final BlockPos CENTER_POS_RELATED_FROM_1_0_1 = new BlockPos(1,1,0);
    public static final BlockPos SELF_POS_1_0_1 = new BlockPos(1,0,1);
    @Override
    public BlockPos findMultipartCheckerPosRelatedToSelf(Level level, BlockState state, BlockPos pos) {
        return CENTER_POS_RELATED_FROM_1_0_1;
    }

    @Override
    public BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return SELF_POS_1_0_1;
    }

    @Override
    public void recoverToOriginalBlock(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
        }
    }

}
