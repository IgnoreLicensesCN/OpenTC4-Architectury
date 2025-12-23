package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AncientStoneBlock extends Block {

    public AncientStoneBlock(Properties properties) {
        super(properties);
    }
    public AncientStoneBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F));
    }

    //i dont want to get all face states,i want to save disk space.
    public static final IntegerProperty FACE_STATE = IntegerProperty.create("face_state", 0, 63);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE_STATE);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var coord = blockPlaceContext.getClickedPos();
        var hasher = ""+coord.getX() + coord.getY() + coord.getZ() + blockPlaceContext.getLevel().dimension().location();
        var random = new Random(hasher.hashCode());
        return this.defaultBlockState().setValue(FACE_STATE, random.nextInt(64));
    }
}
