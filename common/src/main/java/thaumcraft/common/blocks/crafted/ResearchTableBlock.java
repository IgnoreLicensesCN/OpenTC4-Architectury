package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ResearchTableBlock extends Block implements EntityBlock {
    public ResearchTableBlock(Properties properties) {
        super(properties);
    }
    public ResearchTableBlock() {
        super(Properties.copy(Blocks.CRAFTING_TABLE));
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResearchTableBlockEntity(blockPos,blockState);
    }
}
