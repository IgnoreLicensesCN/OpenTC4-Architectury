package thaumcraft.common.tiles.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

//TODO:[maybe wont finish]Render still but without blockEntity.
public class EldritchObeliskBlockEntity extends BlockEntity {
    public EldritchObeliskBlockEntity(BlockEntityType<? extends EldritchObeliskBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EldritchObeliskBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ELDRITCH_OBELISK, blockPos, blockState);
    }
}
