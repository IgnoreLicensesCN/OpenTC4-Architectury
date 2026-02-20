package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class AdvancedAlchemicalFurnaceBlockEntity extends TileThaumcraft {
    public AdvancedAlchemicalFurnaceBlockEntity(BlockEntityType<? extends AdvancedAlchemicalFurnaceBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public AdvancedAlchemicalFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ADVANCED_ALCHEMICAL_FURNACE, blockPos, blockState);
    }
}
