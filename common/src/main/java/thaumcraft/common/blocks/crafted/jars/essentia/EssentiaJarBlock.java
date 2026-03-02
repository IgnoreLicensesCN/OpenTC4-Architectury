package thaumcraft.common.blocks.crafted.jars.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.misc.jars.EssentiaJarBlockItem;
import thaumcraft.common.tiles.crafted.jars.EssentiaJarBlockEntity;

public class EssentiaJarBlock extends AbstractEssentiaJarBlock
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public EssentiaJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));//will affect tag dir
    }
    public EssentiaJarBlock() {
        this(JAR_PROPERTIES);
    }

    @Override
    public EssentiaJarBlockItem getEssentiaJarItem() {
        return ThaumcraftItems.ESSENTIA_JAR;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new EssentiaJarBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level != null && !level.isClientSide) {
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EssentiaJarBlockEntity jar) {
                    jar.serverTick();
                }
            };
        }
        return null;
    }

}
