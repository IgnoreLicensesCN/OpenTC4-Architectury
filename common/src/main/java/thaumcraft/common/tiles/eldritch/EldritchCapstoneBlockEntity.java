package thaumcraft.common.tiles.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class EldritchCapstoneBlockEntity extends BlockEntity {
    public EldritchCapstoneBlockEntity(BlockEntityType<? extends EldritchCapstoneBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchCapstoneBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_CAPSTONE, blockPos, blockState);
    }
}
