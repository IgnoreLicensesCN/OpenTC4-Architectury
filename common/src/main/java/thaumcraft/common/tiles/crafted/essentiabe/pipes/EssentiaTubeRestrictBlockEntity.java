package thaumcraft.common.tiles.crafted.essentiabe.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class EssentiaTubeRestrictBlockEntity extends EssentiaTubeBlockEntity{
    public EssentiaTubeRestrictBlockEntity(BlockEntityType<? extends EssentiaTubeRestrictBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public EssentiaTubeRestrictBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_TUBE_RESTRICT, blockPos, blockState);
    }

    @Override
    public void setSuctionForCalculating(Aspect aspect, int suction) {
        this.setSuction(aspect, suction/2);
    }
}
