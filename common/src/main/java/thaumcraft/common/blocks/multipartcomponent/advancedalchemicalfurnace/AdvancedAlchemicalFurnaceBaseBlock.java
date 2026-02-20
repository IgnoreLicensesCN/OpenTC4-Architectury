package thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.multiparts.MultipartMatchInfo;
import thaumcraft.common.tiles.crafted.AdvancedAlchemicalFurnaceBlockEntity;

public class AdvancedAlchemicalFurnaceBaseBlock extends AbstractAdvancedAlchemicalFurnaceComponent
        implements EntityBlock {
    public static final BlockPos SELF_POS_1_0_1 = new BlockPos(1,0,1);
    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return SELF_POS_1_0_1;
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, ThaumcraftBlocks.ALCHEMICAL_FURNACE.defaultBlockState(), 3);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new AdvancedAlchemicalFurnaceBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return ;
    }
}
