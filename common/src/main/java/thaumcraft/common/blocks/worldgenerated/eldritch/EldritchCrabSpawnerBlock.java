package thaumcraft.common.blocks.worldgenerated.eldritch;


import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.EldritchCrabSpawnerBlockEntity;

//TODO:Render(facing model)
public class EldritchCrabSpawnerBlock extends DropExperienceBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntProvider ELDRITCH_CRAB_SPAWNER_EXP_DROP = UniformInt.of(6,10);

    public EldritchCrabSpawnerBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .strength(15,30)
                        .sound(SoundType.STONE)
                        .mapColor(MapColor.COLOR_BLACK)
                        .lightLevel(s -> 4),
                        ELDRITCH_CRAB_SPAWNER_EXP_DROP
        );
    }

    public EldritchCrabSpawnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }

    public EldritchCrabSpawnerBlock(Properties properties, IntProvider intProvider) {
        super(properties, intProvider);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() != this){
            return null;
        }
        return new EldritchCrabSpawnerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ThaumcraftBlockEntities.ELDRITCH_CRAB_SPAWNER){
            return null;
        }
        if (blockState0.getBlock() != this){
            return null;
        }
        if (Platform.getEnvironment() != Env.SERVER){
            return (level, blockPos, blockState, blockEntity) -> {
                if (blockEntity instanceof EldritchCrabSpawnerBlockEntity crabSpawner){
                    crabSpawner.clientTick();
                }
            };
        }
        return (level, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof EldritchCrabSpawnerBlockEntity crabSpawner){
                crabSpawner.serverTick();
            }
        };
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (i == 1 && level.isClientSide()) {
            var bEntity = level.getBlockEntity(blockPos);
            if (bEntity instanceof EldritchCrabSpawnerBlockEntity crabSpawner) {
                crabSpawner.venting = 20;
                return true;
            }
        }
        return super.triggerEvent(blockState, level, blockPos, i, j);
    }
}
