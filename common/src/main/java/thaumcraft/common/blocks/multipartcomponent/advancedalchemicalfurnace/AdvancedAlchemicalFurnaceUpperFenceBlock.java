package thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.Map;

import static thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceBaseBlock.SELF_POS_1_0_1;

public class AdvancedAlchemicalFurnaceUpperFenceBlock extends AbstractAdvancedAlchemicalFurnaceComponent {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light", 0, 15);

    public AdvancedAlchemicalFurnaceUpperFenceBlock(){
        super(Properties
                .copy(Blocks.IRON_BLOCK)
                .strength(3,17)
                .lightLevel(state -> state.getValue(LIGHT_LEVEL))
        );
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(LIGHT_LEVEL, 0)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    public static final BlockPos SELF_POS_1_1_1 = new BlockPos(1,1,1);
    public static final BlockPos POS_N = SELF_POS_1_1_1.relative(Direction.NORTH);
    public static final BlockPos POS_E = SELF_POS_1_1_1.relative(Direction.EAST);
    public static final BlockPos POS_W = SELF_POS_1_1_1.relative(Direction.WEST);
    public static final BlockPos POS_S = SELF_POS_1_1_1.relative(Direction.SOUTH);
    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        var dir = state.getValue(FACING);
        return switch (dir){
            case NORTH -> POS_N;
            case EAST -> POS_E;
            case WEST -> POS_W;
            case SOUTH -> POS_S;
            default -> throw new IllegalStateException("Direction Axis is Y:" + level + " " + state + " " + pos + " " + dir);
        };
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, ThaumcraftBlocks.ADVANCED_ALCHEMICAL_CONSTRUCT.defaultBlockState(), 3);
        }
    }
}
