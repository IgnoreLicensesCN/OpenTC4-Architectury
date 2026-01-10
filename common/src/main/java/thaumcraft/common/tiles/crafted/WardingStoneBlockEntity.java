package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.concurrent.atomic.AtomicInteger;

public class WardingStoneBlockEntity extends BlockEntity {
    public final AtomicInteger tickCounter = new AtomicInteger();
    public WardingStoneBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }
    public WardingStoneBlockEntity(BlockPos blockPos, BlockState blockState){
        this(ThaumcraftBlockEntities.WARDING_STONE, blockPos, blockState);
    }
}
