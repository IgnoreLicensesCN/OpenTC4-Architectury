package thaumcraft.common.blocks.crafted.essentia.pipes;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.essentiabe.pipes.EssentiaTubeBlockEntity;

public class EssentiaTubeBlock extends AbstractEssentiaTubeBlock
        implements
        EntityBlock {

    public EssentiaTubeBlock(Properties properties) {
        super(properties);
    }
    public EssentiaTubeBlock() {
        super();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaTubeBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EssentiaTubeBlockEntity tube){
                    tube.serverTick();
                }
            });
        }
        return ((level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof EssentiaTubeBlockEntity tube){
                tube.clientTick();
            }
        });
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i,@RGBColor int color) {
        if (level.isClientSide()){
            if (level.getBlockEntity(blockPos) instanceof EssentiaTubeBlockEntity tube){
                EssentiaTubeBlockEntity.ClientTickContext.tubeVenting(tube,color);
                return true;
            }
        }
        return super.triggerEvent(blockState, level, blockPos, i, color);
    }
}
