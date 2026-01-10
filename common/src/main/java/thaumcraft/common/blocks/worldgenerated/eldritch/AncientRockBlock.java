package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class AncientRockBlock extends Block {

    public AncientRockBlock(Properties properties) {
        super(properties);
    }
    public AncientRockBlock() {
        super(Properties.copy(Blocks.STONE).strength(2.F,10.F));
    }

    //i dont want to get all face states,i want to save disk space.
    public static final IntegerProperty FACE_STATE = IntegerProperty.create("face_state", 0, 7);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE_STATE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var coord = blockPlaceContext.getClickedPos();
        var stateValue = (coord.getX()%2) + (coord.getY()%2)*2 + (coord.getZ()%2)*4;
        return this.defaultBlockState().setValue(FACE_STATE, stateValue);
    }
}
