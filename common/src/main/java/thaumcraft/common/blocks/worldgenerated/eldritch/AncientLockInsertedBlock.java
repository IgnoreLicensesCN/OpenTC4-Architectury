package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.AncientLockInsertedBlockEntity;

public class AncientLockInsertedBlock extends AncientLockEmptyBlock implements EntityBlock {
    public AncientLockInsertedBlock(Properties properties) {
        super(properties);
    }
    public AncientLockInsertedBlock() {
        super();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() != this){return null;}
        return new AncientLockInsertedBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType) {
        if (blockState0.getBlock() != this){return null;}
        if (blockEntityType != ThaumcraftBlockEntities.ANCIENT_LOCK_INSERTED){return null;}
        return (level, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof AncientLockInsertedBlockEntity lock){
                lock.tick();
            }
        };
    }
}
