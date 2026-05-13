package thaumcraft.common.blocks.crafted.fromtable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.AbstractExtendedMenuProviderContainerBlock;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;

public class ArcaneWorkbenchBlock extends AbstractExtendedMenuProviderContainerBlock {
    //TODO:BER
    public ArcaneWorkbenchBlock(Properties properties) {
        super(properties);
    }
    public ArcaneWorkbenchBlock(){
        this(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcaneWorkbenchBlockEntity(blockPos,blockState);
    }

}
