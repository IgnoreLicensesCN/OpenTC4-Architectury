package thaumcraft.common.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

//TODO:[maybe wont finish]Render still but without blockEntity.
public class EldritchVoidBlockEntity extends BlockEntity {
    public EldritchVoidBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchVoidBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_VOID, blockPos, blockState);
    }
}
