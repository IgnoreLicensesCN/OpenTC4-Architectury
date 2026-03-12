package thaumcraft.common.blocks.crafted.lamps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.lamp.FertilityArcaneLampBlockEntity;
import thaumcraft.common.tiles.crafted.lamp.GrowthArcaneLampBlockEntity;

public class FertilityArcaneLampBlock extends ArcaneLampBlock implements EntityBlock {

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    public FertilityArcaneLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.DOWN)
                        .setValue(LIT, false)
        );
    }

    public FertilityArcaneLampBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(3, 17)
                .lightLevel(s -> s.getValue(LIT)?15:8)
                .randomTicks()
        );
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var result = super.getStateForPlacement(context);
        if (result == null){
            return null;
        }
        return result.setValue(LIT,false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new FertilityArcaneLampBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            if (blockState.getBlock() == this && blockEntityType == ThaumcraftBlockEntities.FERTILITY_ARCANE_LAMP){
                return ((level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof FertilityArcaneLampBlockEntity lamp){
                        lamp.serverTick();
                    }
                });
            }
        }
        return null;
    }
}
