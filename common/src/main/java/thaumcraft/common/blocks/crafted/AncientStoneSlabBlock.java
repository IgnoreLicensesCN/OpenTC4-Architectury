package thaumcraft.common.blocks.crafted;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.worldgenerated.AncientStoneStairBlock;

import java.util.Random;

public class AncientStoneSlabBlock extends SlabBlock {
    public AncientStoneSlabBlock(Properties properties) {
        super(properties);
    }

    public AncientStoneSlabBlock() {
        super(BlockBehaviour.Properties.copy(ThaumcraftBlocks.ANCIENT_STONE));
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
