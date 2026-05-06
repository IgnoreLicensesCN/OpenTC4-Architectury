package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class AncientRockBlock extends SuppressedWarningBlock {

    public AncientRockBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACE_STATE, 0)
        );
    }
    public AncientRockBlock() {
        super(Properties.copy(Blocks.STONE).strength(2.F,10.F));
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACE_STATE, 0)
        );
    }

    //i dont want to get all face states,i want to save disk space.
    public static final IntegerProperty FACE_STATE = IntegerProperty.create("face_state", 0, 7);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE_STATE);
    }


    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var coord = blockPlaceContext.getClickedPos();
        var seed = coord.asLong();
        seed = (seed ^ (seed >>> 33)) * 0xff51afd7ed558ccdL;
        seed = (seed ^ (seed >>> 33)) * 0xc4ceb9fe1a85ec53L;
        seed = seed ^ (seed >>> 33);
        return defaultBlockState().setValue(FACE_STATE, (int)(seed&7));
    }
}
