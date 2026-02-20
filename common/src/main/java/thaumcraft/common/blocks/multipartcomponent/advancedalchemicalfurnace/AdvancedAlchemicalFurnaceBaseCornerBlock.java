package thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import static thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceBaseBlock.SELF_POS_1_0_1;

public class AdvancedAlchemicalFurnaceBaseCornerBlock extends AbstractAdvancedAlchemicalFurnaceComponent{
    public static final IntegerProperty AT_CORNER = IntegerProperty.create("corner", 0, 3);
    public static final int CORNER_NORTH_WEST = 0;
    public static final int CORNER_NORTH_EAST = 1;
    public static final int CORNER_SOUTH_EAST = 2;
    public static final int CORNER_SOUTH_WEST = 3;
    public static final BlockPos POS_RELATED_NORTH_WEST = SELF_POS_1_0_1.north().west();
    public static final BlockPos POS_RELATED_NORTH_EAST = SELF_POS_1_0_1.north().east();
    public static final BlockPos POS_RELATED_SOUTH_EAST = SELF_POS_1_0_1.south().east();
    public static final BlockPos POS_RELATED_SOUTH_WEST = SELF_POS_1_0_1.south().west();

    public AdvancedAlchemicalFurnaceBaseCornerBlock(){
        super();
        this.registerDefaultState(
                this.stateDefinition.any().setValue(AT_CORNER, CORNER_NORTH_WEST)
        );
    }

    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return switch (state.getValue(AT_CORNER)){
            case CORNER_NORTH_WEST -> POS_RELATED_NORTH_WEST;
            case CORNER_NORTH_EAST -> POS_RELATED_NORTH_EAST;
            case CORNER_SOUTH_EAST -> POS_RELATED_SOUTH_EAST;
            case CORNER_SOUTH_WEST -> POS_RELATED_SOUTH_WEST;
            default -> throw new IllegalStateException("Unexpected corner value: " + state.getValue(AT_CORNER));
        };
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, ThaumcraftBlocks.ADVANCED_ALCHEMICAL_CONSTRUCT.defaultBlockState(), 3);
        }
    }
}
