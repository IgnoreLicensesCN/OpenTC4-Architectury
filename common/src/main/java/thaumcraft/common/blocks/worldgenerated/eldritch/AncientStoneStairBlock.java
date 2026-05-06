package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;

public class AncientStoneStairBlock extends StairBlock {
    public AncientStoneStairBlock(BlockState blockState, Properties properties) {
        super(blockState, properties);
    }
    public AncientStoneStairBlock() {
        super(ThaumcraftBlocks.ANCIENT_STONE.defaultBlockState(), BlockBehaviour.Properties.copy(ThaumcraftBlocks.ANCIENT_STONE));
    }

    public static final IntegerProperty FACE_STATE = AncientStoneBlock.FACE_STATE;
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACE_STATE);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var coord = blockPlaceContext.getClickedPos();
        var seed = coord.asLong();
        seed = (seed ^ (seed >>> 33)) * 0xff51afd7ed558ccdL;
        seed = (seed ^ (seed >>> 33)) * 0xc4ceb9fe1a85ec53L;
        seed = seed ^ (seed >>> 33);
        return defaultBlockState().setValue(FACE_STATE, (int)(seed&63));
    }
}
