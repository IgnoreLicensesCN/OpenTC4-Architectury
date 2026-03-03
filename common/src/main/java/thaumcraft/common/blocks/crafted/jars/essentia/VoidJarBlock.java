package thaumcraft.common.blocks.crafted.jars.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.misc.jars.EssentiaJarBlockItem;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.jars.VoidJarBlockEntity;

public class VoidJarBlock extends AbstractEssentiaJarBlock {

    public VoidJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));//will affect tag dir
    }

    @Override
    public EssentiaJarBlockItem getEssentiaJarItem() {
        return ThaumcraftItems.VOID_JAR;
    }

    public VoidJarBlock() {
        this(JAR_PROPERTIES);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new VoidJarBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level != null && !level.isClientSide && blockEntityType == ThaumcraftBlockEntities.VOID_JAR) {
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof VoidJarBlockEntity jar) {
                    jar.serverTick();
                }
            };
        }
        return null;
    }
}
