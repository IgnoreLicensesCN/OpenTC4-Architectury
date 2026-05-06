package thaumcraft.common.tiles.crafted.essentiabe.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.crafted.essentia.pipes.EssentiaTubeOnewayBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class EssentiaTubeOnewayBlockEntity extends EssentiaTubeBlockEntity {
    public EssentiaTubeOnewayBlockEntity(BlockEntityType<? extends EssentiaTubeOnewayBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public EssentiaTubeOnewayBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_TUBE_ONEWAY, blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var player = useOnContext.getPlayer();
        if (player != null && player.isShiftKeyDown()){
            var level = useOnContext.getLevel();
            if (!level.isClientSide()){
                setBlockStateAndUpdate(level.getBlockState(getBlockPos()).setValue(EssentiaTubeOnewayBlock.FACING, useOnContext.getClickedFace()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useOnWandInteractable(useOnContext);
    }

    public @Nullable Direction getOrderedFacing(){
        return getBlockState().getValue(EssentiaTubeOnewayBlock.FACING);
    }
}
