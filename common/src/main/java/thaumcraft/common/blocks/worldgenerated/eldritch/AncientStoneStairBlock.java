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

import java.util.Random;

public class AncientStoneStairBlock extends StairBlock {
    public AncientStoneStairBlock(BlockState blockState, Properties properties) {
        super(blockState, properties);
    }
    public AncientStoneStairBlock() {
        super(ThaumcraftBlocks.ANCIENT_STONE.defaultBlockState(), BlockBehaviour.Properties.copy(ThaumcraftBlocks.ANCIENT_STONE));
    }

    public static final IntegerProperty FACE_STATE = AncientStoneStairBlock.FACE_STATE;
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACE_STATE);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var coord = blockPlaceContext.getClickedPos();
        var hasher = ""+coord.getX() + coord.getY() + coord.getZ() + blockPlaceContext.getLevel().dimension().location();
        var random = new Random(hasher.hashCode());
        return super.getStateForPlacement(blockPlaceContext).setValue(FACE_STATE, random.nextInt(64));
    }

}
